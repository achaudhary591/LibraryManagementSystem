# LibTrack Deployment Guide

This document provides comprehensive instructions for deploying the LibTrack Library Management System in various environments.

## Table of Contents

- [Local Development Environment](#local-development-environment)
- [Production Deployment](#production-deployment)
  - [Traditional Deployment](#traditional-deployment)
  - [Docker Deployment](#docker-deployment)
  - [Cloud Deployment](#cloud-deployment)
- [Database Setup](#database-setup)
- [Configuration](#configuration)
- [Security Considerations](#security-considerations)
- [Monitoring and Maintenance](#monitoring-and-maintenance)
- [Backup and Recovery](#backup-and-recovery)
- [Troubleshooting](#troubleshooting)

## Local Development Environment

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 10+
- Git

### Setup Steps

1. **Clone the repository**

```bash
git clone https://github.com/yourusername/libtrack.git
cd libtrack
```

2. **Configure the database**

Create a PostgreSQL database:

```sql
CREATE DATABASE libtrack;
CREATE USER libtrackuser WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE libtrack TO libtrackuser;
```

3. **Configure application properties**

Create a `src/main/resources/application-dev.properties` file:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/libtrack
spring.datasource.username=libtrackuser
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Logging
logging.level.org.springframework=INFO
logging.level.com.spark.lms=DEBUG

# Server
server.port=8080
```

4. **Run the application**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

5. **Access the application**

Open a browser and navigate to `http://localhost:8080`

Default credentials:
- Admin: username `admin`, password `admin`
- Librarian: username `librarian`, password `librarian`

## Production Deployment

### Traditional Deployment

#### Prerequisites

- JDK 17 or higher
- PostgreSQL 10+
- Nginx (recommended for reverse proxy)
- Systemd (for service management)

#### Setup Steps

1. **Build the application**

```bash
mvn clean package -DskipTests
```

2. **Create a service user**

```bash
sudo useradd -r -m -U -d /opt/libtrack -s /bin/false libtrack
```

3. **Create directory structure**

```bash
sudo mkdir -p /opt/libtrack/app
sudo mkdir -p /opt/libtrack/logs
sudo mkdir -p /opt/libtrack/config
```

4. **Copy the JAR file**

```bash
sudo cp target/libtrack-0.0.1-SNAPSHOT.jar /opt/libtrack/app/
```

5. **Create application.properties**

```bash
sudo nano /opt/libtrack/config/application.properties
```

Add the following content:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/libtrack
spring.datasource.username=libtrackuser
spring.datasource.password=your_secure_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Logging
logging.file.name=/opt/libtrack/logs/libtrack.log
logging.level.org.springframework=WARN
logging.level.com.spark.lms=INFO

# Server
server.port=8080

# Security
spring.security.user.name=admin
spring.security.user.password=your_secure_admin_password

# Set production profile
spring.profiles.active=prod
```

6. **Create a systemd service**

```bash
sudo nano /etc/systemd/system/libtrack.service
```

Add the following content:

```
[Unit]
Description=LibTrack Library Management System
After=syslog.target network.target postgresql.service

[Service]
User=libtrack
Group=libtrack
WorkingDirectory=/opt/libtrack
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/libtrack/app/libtrack-0.0.1-SNAPSHOT.jar --spring.config.location=file:/opt/libtrack/config/application.properties
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

7. **Set permissions**

```bash
sudo chown -R libtrack:libtrack /opt/libtrack
```

8. **Start the service**

```bash
sudo systemctl daemon-reload
sudo systemctl enable libtrack
sudo systemctl start libtrack
```

9. **Configure Nginx as a reverse proxy**

```bash
sudo nano /etc/nginx/sites-available/libtrack
```

Add the following content:

```nginx
server {
    listen 80;
    server_name library.yourdomain.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable the site:

```bash
sudo ln -s /etc/nginx/sites-available/libtrack /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

10. **Set up SSL with Let's Encrypt**

```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d library.yourdomain.com
```

### Docker Deployment

#### Prerequisites

- Docker
- Docker Compose
- PostgreSQL (can be containerized)

#### Setup Steps

1. **Create a Dockerfile**

```bash
nano Dockerfile
```

Add the following content:

```dockerfile
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.spark.lms.LibTrackApplication"]
```

2. **Create docker-compose.yml**

```bash
nano docker-compose.yml
```

Add the following content:

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/libtrack
      - SPRING_DATASOURCE_USERNAME=libtrackuser
      - SPRING_DATASOURCE_PASSWORD=your_secure_password
      - SPRING_JPA_HIBERNATE_DDL_AUTO=update
    depends_on:
      - db
    restart: always
    
  db:
    image: postgres:14-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=libtrack
      - POSTGRES_USER=libtrackuser
      - POSTGRES_PASSWORD=your_secure_password
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: always

volumes:
  postgres_data:
```

3. **Build and run with Docker Compose**

```bash
docker-compose up -d
```

4. **Access the application**

Open a browser and navigate to `http://localhost:8080`

### Cloud Deployment

#### AWS Elastic Beanstalk

1. **Install the EB CLI**

```bash
pip install awsebcli
```

2. **Initialize EB application**

```bash
eb init
```

3. **Create an environment**

```bash
eb create libtrack-production
```

4. **Configure environment variables**

```bash
eb setenv SPRING_DATASOURCE_URL=jdbc:postgresql://your-rds-instance:5432/libtrack \
          SPRING_DATASOURCE_USERNAME=libtrackuser \
          SPRING_DATASOURCE_PASSWORD=your_secure_password \
          SPRING_JPA_HIBERNATE_DDL_AUTO=validate \
          SPRING_PROFILES_ACTIVE=prod
```

5. **Deploy the application**

```bash
eb deploy
```

#### Heroku

1. **Install the Heroku CLI**

```bash
curl https://cli-assets.heroku.com/install.sh | sh
```

2. **Login to Heroku**

```bash
heroku login
```

3. **Create a Heroku app**

```bash
heroku create libtrack
```

4. **Provision a PostgreSQL database**

```bash
heroku addons:create heroku-postgresql:hobby-dev
```

5. **Configure environment variables**

```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

6. **Deploy the application**

```bash
git push heroku main
```

## Database Setup

### PostgreSQL Setup

1. **Install PostgreSQL**

```bash
# Ubuntu/Debian
sudo apt update
sudo apt install postgresql postgresql-contrib

# RHEL/CentOS
sudo yum install postgresql-server postgresql-contrib
sudo postgresql-setup initdb
sudo systemctl start postgresql
```

2. **Create database and user**

```sql
CREATE DATABASE libtrack;
CREATE USER libtrackuser WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE libtrack TO libtrackuser;
```

3. **Configure PostgreSQL for remote access (if needed)**

Edit `postgresql.conf`:

```bash
sudo nano /etc/postgresql/14/main/postgresql.conf
```

Set:
```
listen_addresses = '*'
```

Edit `pg_hba.conf`:

```bash
sudo nano /etc/postgresql/14/main/pg_hba.conf
```

Add:
```
host    libtrack        libtrackuser    0.0.0.0/0               md5
```

Restart PostgreSQL:

```bash
sudo systemctl restart postgresql
```

## Configuration

### Application Properties

Key configuration properties:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/libtrack
spring.datasource.username=libtrackuser
spring.datasource.password=your_secure_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update  # Use 'validate' in production
spring.jpa.show-sql=false

# Server
server.port=8080
server.servlet.context-path=/  # Change if deploying to a subdirectory

# Security
spring.security.user.name=admin
spring.security.user.password=your_secure_admin_password

# Logging
logging.file.name=/path/to/logs/libtrack.log
logging.level.org.springframework=WARN
logging.level.com.spark.lms=INFO

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### Environment-Specific Configuration

Use Spring profiles for environment-specific configuration:

- `application-dev.properties`: Development environment
- `application-test.properties`: Testing environment
- `application-prod.properties`: Production environment

Activate a profile:

```bash
java -jar libtrack.jar --spring.profiles.active=prod
```

## Security Considerations

### Secure Database Credentials

- Use environment variables for sensitive information
- Never commit credentials to version control
- Use a secrets management solution in production

### HTTPS Configuration

Configure SSL in `application.properties`:

```properties
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=your_keystore_password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=libtrack
server.port=443
```

### Password Storage

- Passwords are stored using BCrypt hashing
- Configure password strength in `SecurityConfiguration.java`

### Regular Updates

- Keep the application and dependencies updated
- Monitor security advisories for Spring Boot and related libraries

## Monitoring and Maintenance

### Spring Boot Actuator

Enable and secure Spring Boot Actuator:

```properties
# Enable specific endpoints
management.endpoints.web.exposure.include=health,info,metrics

# Secure actuator endpoints
management.endpoints.web.base-path=/management
management.endpoint.health.show-details=when_authorized
```

### Logging

Configure logging rotation:

```properties
logging.file.max-size=10MB
logging.file.max-history=10
```

### Performance Monitoring

Use tools like:
- Prometheus for metrics collection
- Grafana for visualization
- ELK stack for log analysis

## Backup and Recovery

### Database Backup

Schedule regular PostgreSQL backups:

```bash
pg_dump -U libtrackuser -d libtrack -F c -f /backup/libtrack_$(date +%Y%m%d).dump
```

Create a cron job for daily backups:

```bash
0 2 * * * pg_dump -U libtrackuser -d libtrack -F c -f /backup/libtrack_$(date +%Y%m%d).dump
```

### Application Backup

Backup application configuration and data:

```bash
tar -czf /backup/libtrack_config_$(date +%Y%m%d).tar.gz /opt/libtrack/config
```

### Recovery Procedure

1. **Restore database**

```bash
pg_restore -U libtrackuser -d libtrack -c /backup/libtrack_20250416.dump
```

2. **Restore application configuration**

```bash
tar -xzf /backup/libtrack_config_20250416.tar.gz -C /tmp
cp -r /tmp/opt/libtrack/config/* /opt/libtrack/config/
```

3. **Restart the application**

```bash
sudo systemctl restart libtrack
```

## Troubleshooting

### Common Issues

#### Application Won't Start

Check logs:
```bash
sudo journalctl -u libtrack.service -f
```

Verify database connection:
```bash
psql -U libtrackuser -h localhost -d libtrack
```

#### Database Connection Issues

Verify PostgreSQL is running:
```bash
sudo systemctl status postgresql
```

Check connection properties:
```bash
sudo -u libtrack psql -h localhost -U libtrackuser -d libtrack
```

#### Out of Memory Errors

Increase JVM heap size:
```bash
sudo nano /etc/systemd/system/libtrack.service
```

Modify the ExecStart line:
```
ExecStart=/usr/bin/java -Xms1024m -Xmx2048m -jar /opt/libtrack/app/libtrack-0.0.1-SNAPSHOT.jar
```

Reload and restart:
```bash
sudo systemctl daemon-reload
sudo systemctl restart libtrack
```

### Support Resources

- GitHub Issues: https://github.com/yourusername/libtrack/issues
- Documentation: https://libtrack.com/docs
- Email Support: support@libtrack.com
