package com.template.repository;

import com.template.domain.Student;
import com.template.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteStudentRepository implements StudentRepository {

    @Override
    public void addStudent(Student student) {

        String sql = """
                INSERT INTO students 
                (student_id, full_name, programme, level, gpa, email, phone_number, date_added, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getProgramme());
            pstmt.setInt(4, student.getLevel());
            pstmt.setDouble(5, student.getGpa());
            pstmt.setString(6, student.getEmail());
            pstmt.setString(7, student.getPhoneNumber());
            pstmt.setString(8, student.getDateAdded().toString());
            pstmt.setString(9, student.getStatus());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getString("student_id"),
                        rs.getString("full_name"),
                        rs.getString("programme"),
                        rs.getInt("level"),
                        rs.getDouble("gpa"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        LocalDate.parse(rs.getString("date_added")),
                        rs.getString("status")
                );
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public Optional<Student> getStudentById(String studentId) {

        String sql = "SELECT * FROM students WHERE student_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Student student = new Student(
                        rs.getString("student_id"),
                        rs.getString("full_name"),
                        rs.getString("programme"),
                        rs.getInt("level"),
                        rs.getDouble("gpa"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        LocalDate.parse(rs.getString("date_added")),
                        rs.getString("status")
                );
                return Optional.of(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public void updateStudent(Student student) {

        String sql = """
                UPDATE students
                SET full_name = ?, programme = ?, level = ?, gpa = ?, 
                    email = ?, phone_number = ?, status = ?
                WHERE student_id = ?
                """;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getProgramme());
            pstmt.setInt(3, student.getLevel());
            pstmt.setDouble(4, student.getGpa());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhoneNumber());
            pstmt.setString(7, student.getStatus());
            pstmt.setString(8, student.getStudentId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteStudent(String studentId) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}