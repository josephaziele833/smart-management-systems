package com.template.ui;

import com.template.domain.Student;
import com.template.repository.SQLiteStudentRepository;
import com.template.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class mainController {

    private StudentService studentService;

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    // ===== FORM FIELDS =====
    @FXML private TextField studentIdField;
    @FXML private TextField nameField;
    @FXML private TextField programmeField;
    @FXML private TextField levelField;
    @FXML private TextField gpaField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> statusBox;

    // ===== TABLE =====
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colProgramme;
    @FXML private TableColumn<Student, Integer> colLevel;
    @FXML private TableColumn<Student, Double> colGpa;
    @FXML private TableColumn<Student, String> colStatus;

    @FXML private TextField gpaThresholdField;
    @FXML private ComboBox<String> sortByComboBox;

    @FXML
    public void initialize() {

        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldSelection, selectedStudent) -> {

                    if (selectedStudent != null) {

                        studentIdField.setText(selectedStudent.getStudentId());
                        nameField.setText(selectedStudent.getFullName());
                        programmeField.setText(selectedStudent.getProgramme());
                        levelField.setText(String.valueOf(selectedStudent.getLevel()));
                        gpaField.setText(String.valueOf(selectedStudent.getGpa()));
                        emailField.setText(selectedStudent.getEmail());
                        phoneField.setText(selectedStudent.getPhoneNumber());
                        statusBox.setValue(selectedStudent.getStatus());
                    }
                }
        );
        studentService = new StudentService(new SQLiteStudentRepository());

        statusBox.getItems().addAll("Active", "Inactive");

        // Bind table columns properly
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentId()));
        colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        colProgramme.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getProgramme()));
        colLevel.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getLevel()));
        colGpa.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getGpa()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        // Populate table
        loadStudents();

        // Fill form when selecting a row
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                studentIdField.setText(newSelection.getStudentId());
                studentIdField.setDisable(true); // ID is not editable
                nameField.setText(newSelection.getFullName());
                programmeField.setText(newSelection.getProgramme());
                levelField.setText(String.valueOf(newSelection.getLevel()));
                gpaField.setText(String.valueOf(newSelection.getGpa()));
                emailField.setText(newSelection.getEmail());
                phoneField.setText(newSelection.getPhoneNumber());
                statusBox.setValue(newSelection.getStatus());
            }
        });
    }

    // =========================
    // LOAD STUDENTS
    // =========================
    private void loadStudents() {
        studentList.clear();
        studentList.addAll(studentService.getAllStudents());
        studentTable.setItems(studentList);
    }

    // =========================
    // ADD STUDENT
    // =========================
    @FXML
    private void handleAddStudent() {
        try {
            Student student = new Student(
                    studentIdField.getText(),
                    nameField.getText(),
                    programmeField.getText(),
                    Integer.parseInt(levelField.getText()),
                    Double.parseDouble(gpaField.getText()),
                    emailField.getText(),
                    phoneField.getText(),
                    LocalDate.now(),
                    statusBox.getValue()
            );

            studentService.addStudent(student);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
            clearFields();
            loadStudents();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Level and GPA must be numbers.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unexpected error occurred.");
        }
    }

    // =========================
    // DELETE STUDENT
    // =========================
    @FXML
    private void handleDeleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a student.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Delete selected student?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                studentService.deleteStudent(selected.getStudentId());
                loadStudents();
            }
        });
    }

    // =========================
    // EDIT STUDENT
    // =========================
    @FXML
    private void handleEditStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a student to edit.");
            return;
        }
        try {
            selected.setFullName(nameField.getText());
            selected.setProgramme(programmeField.getText());
            selected.setLevel(Integer.parseInt(levelField.getText()));
            selected.setGpa(Double.parseDouble(gpaField.getText()));
            selected.setEmail(emailField.getText());
            selected.setPhoneNumber(phoneField.getText());
            selected.setStatus(statusBox.getValue());

            studentService.updateStudent(selected);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully.");
            clearFields();
            loadStudents();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Level and GPA must be numbers.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Unexpected error occurred.");
        }
    }

    // =========================
    // REFRESH TABLE
    // =========================
    @FXML
    private void handleRefresh() {
        loadStudents();
    }

    // =========================
    // HELPERS
    // =========================
    private void clearFields() {
        studentIdField.clear();
        studentIdField.setDisable(false);
        nameField.clear();
        programmeField.clear();
        levelField.clear();
        gpaField.clear();
        emailField.clear();
        phoneField.clear();
        statusBox.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleOpenReportWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Report.fxml"));
            Parent root = loader.load();

            ReportController controller = loader.getController();
            controller.setStudentService(studentService);

            double threshold = 0;
            try {
                threshold = Double.parseDouble(gpaThresholdField.getText());
            } catch (NumberFormatException e) {
                threshold = 0; // default if empty
            }

            String sortBy = sortByComboBox.getValue();
            if (sortBy == null) sortBy = "Name";

            controller.generateReport(threshold, sortBy);

            Stage stage = new Stage();
            stage.setTitle("Student Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}