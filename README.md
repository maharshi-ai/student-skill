# Student Skill & Internship Management System

A Java 17 Swing desktop application using JDBC and MySQL 8.

## Stack
- Java 17, Swing/AWT
- JDBC + MySQL 8
- Maven
- BCrypt password hashing
- JUnit 5

## Data flow
Swing UI -> Service -> DAO -> JDBC PreparedStatement -> MySQL -> model -> Swing UI.
Dashboard figures are read from SQL; there are no seeded/demo records or hardcoded dashboard totals.

## Setup
1. Install JDK 17+, Maven 3.9+, and MySQL 8.
2. Execute `database/schema.sql` in MySQL.
3. Copy `application.properties.example` to `application.properties` and configure credentials.
4. Run `mvn clean package`.
5. Run `java -jar target/student-skill-1.0.0.jar`.

The properties file is intentionally ignored by Git. Never commit database credentials.

## Deployment decision
This is a desktop Swing application, not a browser app. Vercel and GitHub Pages cannot host/run the Swing UI. Use GitHub Actions to build/test and GitHub Releases to distribute the JAR. GitHub Pages is optional for static documentation only.

## Scope status
The current baseline implements student registration/login and live dashboard counts. Skills CRUD, internship browsing/management, application workflows and admin review are not yet implemented; they must not be presented as complete.
