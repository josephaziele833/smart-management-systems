package com.template.service;

import com.template.domain.Student;
import com.template.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class StudentService {



    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;


    }

    // =========================
    // ADD STUDENT
    // =========================
    public void addStudent(Student student) {

        validateStudent(student);

        // Check duplicate ID
        Optional<Student> existing = repository.getStudentById(student.getStudentId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Student ID already exists.");
        }

        repository.addStudent(student);
    }

    // =========================
    // GET ALL
    // =========================
    public List<Student> getAllStudents() {
        return repository.getAllStudents();
    }

    // =========================
    // UPDATE
    // =========================
    public void updateStudent(Student student) {

        validateStudent(student);

        repository.updateStudent(student);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteStudent(String studentId) {
        repository.deleteStudent(studentId);
    }

    // =========================
    // VALIDATION LOGIC
    // =========================
    private void validateStudent(Student student) {

        // Student ID
        if (student.getStudentId() == null ||
                !student.getStudentId().matches("^[A-Za-z0-9]{4,20}$")) {
            throw new IllegalArgumentException("Invalid Student ID (4-20 letters or digits).");
        }

        // Full Name
        if (student.getFullName() == null ||
                !student.getFullName().matches("^[A-Za-z ]{2,60}$")) {
            throw new IllegalArgumentException("Invalid Full Name (2-60 letters only).");
        }

        // Programme
        if (student.getProgramme() == null ||
                student.getProgramme().isBlank()) {
            throw new IllegalArgumentException("Programme is required.");
        }

        // Level
        int level = student.getLevel();
        if (!(level == 100 || level == 200 || level == 300 ||
                level == 400)) {
            throw new IllegalArgumentException("Invalid level. Must be 100-400.");
        }

        // GPA
        if (student.getGpa() < 0.0 || student.getGpa() > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0.");
        }

        // Email
        if (student.getEmail() == null ||
                !student.getEmail().contains("@") ||
                !student.getEmail().contains(".")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        // Phone
        if (student.getPhoneNumber() == null ||
                !student.getPhoneNumber().matches("^\\d{10,15}$")) {
            throw new IllegalArgumentException("Phone must be 10-15 digits.");
        }

        // Date Added
        if (student.getDateAdded() == null) {
            student.setDateAdded(LocalDate.now());
        }

        // Status
        if (!(student.getStatus().equalsIgnoreCase("Active") ||
                student.getStatus().equalsIgnoreCase("Inactive"))) {
            throw new IllegalArgumentException("Status must be Active or Inactive.");
        }
    }
}