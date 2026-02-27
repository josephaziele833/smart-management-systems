package com.template.repository;

import com.template.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    void addStudent(Student student);

    List<Student> getAllStudents();

    Optional<Student> getStudentById(String studentId);

    void updateStudent(Student student);

    void deleteStudent(String studentId);
}