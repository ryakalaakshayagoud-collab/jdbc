package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Main {

    private static final String URL =
            "jdbc:mysql://localhost:3306/akshaya";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Connect to MySQL database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/akshaya", "root", "root");
    }

    // CREATE: Insert a student
    public static int insertStudent(
            String name, int age, String gender, String addhar)
            throws SQLException {

        String sql = "INSERT INTO student(name, age, gender, addhar) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, addhar);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        System.out.println(
                                "SUCCESS: Student added! ID: " + id);
                        return id;
                    }
                }
            }
        }

        throw new SQLException("Student could not be added.");
    }

    // READ: Display all students
    public static void displayStudents() throws SQLException {

        String sql = "SELECT * FROM student";

        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            boolean found = false;

            System.out.println("\n------ STUDENT RECORDS ------");

            while (rs.next()) {
                found = true;

                System.out.println(
                        "ID: " + rs.getInt("id")
                                + " | Name: " + rs.getString("name")
                                + " | Age: " + rs.getInt("age")
                                + " | Gender: " + rs.getString("gender")
                                + " | Aadhaar: " + rs.getString("addhar"));
            }

            if (!found) {
                System.out.println("No student records available.");
            }

            System.out.println("-----------------------------");
        }
    }

    // UPDATE: Update student age
    public static void updateStudent(int id, int age)
            throws SQLException {

        String sql = "UPDATE student SET age = ? WHERE id = ?";

        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(sql)) {

            stmt.setInt(1, age);
            stmt.setInt(2, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("SUCCESS: Student details updated.");
            } else {
                System.out.println("ERROR: Student ID " + id + " not found.");
            }
        }
    }

    // DELETE: Delete student by ID
    public static void deleteStudent(int id) throws SQLException {

        String sql = "DELETE FROM student WHERE id = ?";

        try (Connection cn = getConnection();
             PreparedStatement stmt = cn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("SUCCESS: Student record deleted.");
            } else {
                System.out.println("ERROR: Student ID " + id + " not found.");
            }
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        try {
            // Test database connection
            try (Connection cn = getConnection()) {
                System.out.println(
                        "SUCCESS: Connected to MySQL database 'chandana'.");
            }

            // Insert a test student
            int studentId = insertStudent(
                    "Student Test", 21, "Female", "TEST000011");

            // Display student records
            displayStudents();

            // Update the newly inserted student's age
            updateStudent(studentId, 22);

            // Display updated records
            System.out.println("\nRecords after update:");
            displayStudents();

            // Uncomment to delete the test student
            // deleteStudent(studentId);

        } catch (SQLException e) {
            System.err.println("\nDATABASE ERROR: " + e.getMessage());
            System.err.println(
                    "Check the MySQL server, credentials, driver, "
                            + "database, and table.");
        }
    }
}