Student Management Project - Database requirements

1.MySQL Database :
-Database name: student Database

2.Table: students
-SQL to create the table:

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    age INT,
    gpa DOUBLE
);