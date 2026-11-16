# Student Skill & Internship Management System

A Java 17 desktop PBL built with **Swing/AWT + JDBC + MySQL 8**. There is no browser frontend and no JavaScript/Python application layer.

## Implemented modules

### Student
- Student account registration and BCrypt password authentication.
- Role-based login to the student or administrator workspace.
- Live dashboard metrics queried from MySQL.
- Add/update a skill with category, proficiency and experience; remove a skill.
- Browse open, non-expired internships.
- Submit a cover letter; the database prevents duplicate applications for the same internship.
- Track application status and administrator feedback.

### Administrator
- View all internships and their current status.
- Post an internship with company, description, skills, location, duration, stipend and deadline.
- Open, close or archive an internship.
- Review applications and set UNDER_REVIEW, ACCEPTED or REJECTED with feedback.

## Architecture
`Swing UI -> Service -> DAO -> JDBC PreparedStatement -> MySQL -> model -> Swing UI`

- UI database work runs in `SwingWorker`, keeping the Swing event thread responsive.
- SQL uses prepared statements.
- Passwords are stored as BCrypt hashes.
- Database credentials live in an ignored local `application.properties` file.
- No sample internship records or hardcoded dashboard counts are inserted.

## Requirements
- JDK 17+
- Maven 3.9+
- MySQL 8+

## Run locally (Windows PowerShell)
1. Clone:
   ```powershell
   git clone https://github.com/maharshi-ai/student-skill.git
   cd student-skill
   ```
2. Run `database/schema.sql` in MySQL Workbench, or:
   ```powershell
   mysql -u root -p < database/schema.sql
   ```
3. Configure the local database:
   ```powershell
   Copy-Item application.properties.example application.properties
   notepad application.properties
   ```
   Set `db.username` and `db.password` to your MySQL credentials.
4. Build and test:
   ```powershell
   mvn clean verify
   ```
5. Launch:
   ```powershell
   java -jar target/student-skill-1.0.0.jar
   ```

## Create the first administrator
1. Register through the application.
2. In MySQL Workbench, promote that account (replace the email):
   ```sql
   USE student_internship;
   UPDATE users SET role='ADMIN' WHERE email='your-admin-email@example.com';
   ```
3. Sign in again. That account now opens the administrator workspace.

Do not expose MySQL directly to the public internet. For a college demonstration, run the desktop app and MySQL on the same laptop or trusted LAN.

## Build and distribution
GitHub Actions runs `mvn clean verify` on pushes and pull requests to `main`, then uploads the executable shaded JAR as a workflow artifact.

**This is a Swing desktop application.** Vercel, Render static hosting and GitHub Pages cannot run its desktop UI. The distributable is the executable JAR. Download the latest successful build from the repository's Actions page under the run's Artifacts section.

## Current limitations
- Student profile fields (phone, department, academic year and CGPA) are in the schema but do not yet have a dedicated edit screen.
- Internship search filters, CV/file upload, email notifications and password reset are not included.
- No hosted MySQL service or public multi-user backend is provisioned; each user must configure a database.
- The application has not been tested against your personal MySQL instance. Complete the local setup and run a student/admin end-to-end demo before presenting it.
