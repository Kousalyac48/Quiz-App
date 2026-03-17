**Interactive Quiz Application**
   A lightweight Java Swing desktop application that connects to a MySQL database to deliver a timed, randomized quiz.

**🚀 Features**
Login System: Secure user authentication.
Timed Questions: 15-second countdown per question.
Randomized Content: Fetches 5 random questions from the database each session.
Score Tracking: Automatically saves results to the SQL database.
Dark Theme: Custom modern UI using a dark color palette.

**🛠️ Files**
DBConfig.java: Handles the connection to the MySQL database.
QuizApp.java: Contains the entire GUI, Login logic, and Quiz engine.

**📋 Prerequisites**
Java JDK 8 or higher.
MySQL Server (and MySQL Workbench).
MySQL Connector JAR (Add to your project build path or use Maven).

**⚙️ Setup**
1) Database: Create a schema named quiz_system and run the following:
        CREATE TABLE users (user_id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50), password VARCHAR(50));
        CREATE TABLE questions (id INT PRIMARY KEY AUTO_INCREMENT, question_text TEXT, option_a VARCHAR(100), option_b VARCHAR(100), option_c VARCHAR(100), option_d VARCHAR(100), correct_answer CHAR(1));
        CREATE TABLE results (result_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT, score INT, total_questions INT);
2) Credentials: Update DBConfig.java with your MySQL root password.
3) Run: Execute the main method in QuizApp.java

**🏁 Conclusion**
The Interactive Quiz Application successfully demonstrates the practical application of Java Swing for front-end development and MySQL for robust data management. By bridging these two technologies via JDBC, the project achieves a seamless flow from user authentication to real-time performance tracking.

**Key Takeaways:**
Database Integration: Gained hands-on experience with CRUD operations and SQL randomization.
Event-Driven Programming: Mastered the use of javax.swing.Timer and ActionListeners to create a dynamic user experience.
Modular Design: Separated database configuration from UI logic to ensure code maintainability and scalability.

This project serves as a foundational template for more complex assessment tools. Future updates will focus on implementing password hashing for enhanced security and a dedicated administrator dashboard for real-time question management.
