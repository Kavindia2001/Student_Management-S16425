import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Mainproject {
    public static void main(String[] args) {
        String url ="jdbc:mysql://localhost:3306/studentdb";
        String user = "root";
        String password= "11684";
        boolean running = true;

        try (Connection conn = DriverManager.getConnection(url, user, password);
           Scanner scanner = new Scanner(System.in)){

            System.out.println("connected to the database successfully");

           
          while(running){
            System.out.println("\n ----Student Management Menu----");
            System.out.println("1. Add Student");
            System.out.println("2. View Studnets");
            System.out.println("3. Update Student GPA");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.println("--------------------------------");
            System.out.println("Enter your choice ?");

            int choice;
            try {
                choice = scanner.nextInt();
                
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            

          switch(choice) {
            case 1 -> addStudent(conn, scanner);
            case 2 -> viewStudents(conn);
            case 3 -> updateStudent(conn, scanner);
            case 4 -> deleteStudent (conn, scanner);
            case 5 -> {
                System.out.println("Exiting the program. GoodBye!");
                running = false; 
            }
            default -> System.out.println("Invalid choice,try again");
          }  
         } 
        } catch (SQLException e) {
            System.out.println("Database connection failed: "+ e.getMessage());
            
        }  
        
    }
    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        scanner.nextLine();

        System.out.print("Enter name :");
        String name = scanner.nextLine();
        int age ;
        double gpa;
        try {
            System.out.print("Enter age:");
            age = scanner.nextInt();
            if (age <= 0) {
                System.out.println("Invalid age. Age must be a positive number.");
                return;
            }

            System.out.print("Enter GPA:");
            gpa = scanner.nextDouble();

            if (gpa < 0.0 || gpa> 4.0){
                System.out.println("GPA value should range within 0.0 to 4.0");
                return;
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Invlaid input.Please enter numbers.");
            scanner.next();
            return;
        }
        
        String sql = "INSERT INTO students (name,age,gpa) VALUES (?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2,age);
            stmt.setDouble(3,gpa);
            stmt.executeUpdate();
            System.out.println("Student added successfully!");

        }
    }       
    private static void viewStudents(Connection conn) throws SQLException {
        String sql = "SELECT * FROM students";
        try (Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n---Student List---");
            boolean hasStudents = false;

            while (rs.next()) {
                hasStudents = true;
                System.out.printf("ID: %d| Name: %s| Age: %d | GPA: %.2f%n",
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getDouble("gpa"));
            }
            if(!hasStudents){
                System.out.println("No students found in the database.");
            }
        }
    }       
    //Update student
    private static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        String listSql = "SELECT id, name FROM students";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(listSql)){

            System.out.println("\n--Available Students--");
            while (rs.next()){
                System.out.printf("ID: %d | Name : %s%n",
                rs.getInt("id"),
                rs.getString("name"));
            }    
        }  
        System.out.print("\nEnter Student ID to update:");
        int id;

        try {
            id = scanner.nextInt();
        } catch (InputMismatchException e) {
        
        System.out.println("Invalid input. Please enter a valid number.");
        scanner.next();
        return;
        }
        System.out.print("Enter new GPA: ");
        double gpa;
        try {
            gpa = scanner.nextDouble();
            if (gpa<0.0 || gpa >4.0) {
                System.out.println("GPA value should range within 0.0 to 4.0");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.Please enter a valid number.");
            scanner.next();
            return;

        }

        String sql = " UPDATE students SET gpa =? WHERE id =?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, gpa);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            if (rows >0) {
                System.out.println("Student updated successfully!");

            }else {
                System.out.println("Student not found");
            }
        }
    }
    //Delete Student
    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        
        
        String listSql = "SELECT id, name FROM students";
        try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(listSql)) {
               
            System.out.println("\n--Available Students--");
            while (rs.next()){
                System.out.printf("ID: %d | Name : %s%n",
                rs.getInt("id"),
                rs.getString("name"));
            }
        }
        System.out.print("\nEnter Student ID to delete:");
        int id;
        try {
            id = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.Please enter a valid number");
            scanner.next();
            return;
        }

        System.out.print("Are you sure want to delete this student? (y/n):");
        String confirm = scanner.next();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Deletion cancelled");
            return;
        }

        String sql = " DELETE FROM students WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows >0) {
                System.out.println("Student deleted successfully");
             }else {
                System.out.println("Student not found");
             }

        }
    }

}