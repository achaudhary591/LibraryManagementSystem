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
                .requestMatchers("/css/**", "/jquery/**", "/bootstrap/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority(Constants.ROLE_ADMIN)
                .requestMatchers("/librarian/**").hasAuthority(Constants.ROLE_LIBRARIAN)
                .requestMatchers("/student/**").hasAuthority(Constants.ROLE_STUDENT)
                .requestMatchers("/").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN, Constants.ROLE_STUDENT)
                .requestMatchers("/home").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN, Constants.ROLE_STUDENT)
                .requestMatchers("/book/**", "/category/**", "/member/**", "/issue/**").hasAnyAuthority(Constants.ROLE_ADMIN, Constants.ROLE_LIBRARIAN)
                .requestMatchers("/student/books").hasAuthority(Constants.ROLE_STUDENT)
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")
                .usernameParameter("username")
                .passwordParameter("password")
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            );
            
        return http.build();
    }
}
