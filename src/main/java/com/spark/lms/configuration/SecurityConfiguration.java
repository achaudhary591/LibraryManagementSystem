package com.spark.lms.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.spark.lms.common.Constants;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private SecurityAuditLogger securityAuditLogger;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String roleQuery;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(roleQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login").permitAll()
                .requestMatchers("/resources/**", "/static/**", "/custom/**", "/vendors/**", "/images/**").permitAll()
                .requestMatchers("/css/**", "/jquery/**", "/bootstrap/**", "/js/**", "/fonts/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority(Constants.ROLE_ADMIN)
                .requestMatchers("/librarian/**").hasAuthority(Constants.ROLE_LIBRARIAN)
                .requestMatchers("/student/**").hasAuthority(Constants.ROLE_STUDENT)
                .requestMatchers("/").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN, Constants.ROLE_STUDENT)
                .requestMatchers("/home").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN, Constants.ROLE_STUDENT)
                .requestMatchers("/book/**", "/category/**", "/member/**", "/issue/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN)
                .requestMatchers("/student/books").hasAuthority(Constants.ROLE_STUDENT)
                .anyRequest().authenticated()
            )
            // Enable CSRF protection with secure cookie
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/rest/**")
            )
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    securityAuditLogger.logLoginSuccess(authentication);
                    response.sendRedirect("/home");
                })
                .failureHandler((request, response, exception) -> {
                    securityAuditLogger.logLoginFailure(request.getParameter("username"), exception);
                    response.sendRedirect("/login?error=true");
                })
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .logoutSuccessHandler((request, response, authentication) -> {
                    securityAuditLogger.logLogout(authentication);
                    response.sendRedirect("/login?logout=true");
                })
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            )
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline'; " +
                    "style-src 'self' 'unsafe-inline'; " +
                    "img-src 'self' data:; " +
                    "font-src 'self'; " +
                    "frame-ancestors 'self'; " +
                    "form-action 'self'"
                ))
                .frameOptions(frame -> frame.sameOrigin())
            )
            // Session management
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            );
            
        return http.build();
    }
}
