📝 Interactive Quiz System (Java & MySQL)
A streamlined, high-performance desktop quiz application featuring a Dark Mode UI. This project demonstrates core Java competencies including GUI design, event-driven programming, and JDBC database integration.

📂 Project Structure
The project is optimized into two primary components:

DBConfig.java: Manages the JDBC connection lifecycle and database credentials.

QuizApp.java: A unified file containing the Main Dashboard, Login Authentication (JDialog), and custom UI Styling.

🛠 Features
User Authentication: Secure login dialog verified against a MySQL users table.

Live Countdown: 15-second timer per question using javax.swing.Timer.

Dynamic Content: Randomly selects 5 questions per session from the database.

Persistent Results: Automatically saves user scores and timestamps to a results table.

Responsive UI: Built with GridBagLayout and BorderLayout for clean component scaling.

⚙️ Setup & Installation
1. Database Setup (MySQL Workbench)
Run the following SQL script to initialize the system:

SQL
CREATE DATABASE quiz_system;
USE quiz_system;

CREATE TABLE users (user_id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), password VARCHAR(50));
CREATE TABLE questions (id INT PRIMARY KEY AUTO_INCREMENT, question_text TEXT, option_a VARCHAR(100), option_b VARCHAR(100), option_c VARCHAR(100), option_d VARCHAR(100), correct_answer CHAR(1));
CREATE TABLE results (result_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT, score INT, total_questions INT, FOREIGN KEY (user_id) REFERENCES users(user_id));

-- Add test user
INSERT INTO users (username, password) VALUES ('admin', 'root');
2. Dependency Management (Maven)
Include the following in your pom.xml to enable the SQL bridge:

XML
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
3. Execution
Update the credentials in DBConfig.java to match your MySQL setup.

Compile and run QuizApp.java.

🚀 Key Technical Highlights
SQL Randomization: Uses ORDER BY RAND() LIMIT 5 to ensure unique quiz experiences without overhead in Java memory.

Thread-Safe UI: Implements timer logic on the Event Dispatch Thread (EDT) for smooth performance.

HTML Rendering: Utilizes HTML tags within JLabel components to support automatic text wrapping for long questions.
