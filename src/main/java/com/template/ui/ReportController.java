package com.template.ui;

import com.template.domain.Student;
import com.template.service.StudentService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReportController {

    @FXML private TableView<Student> reportTable;
    @FXML private TableColumn<Student, String> reportIdColumn;
    @FXML private TableColumn<Student, String> reportNameColumn;
    @FXML private TableColumn<Student, String> reportProgrammeColumn;
    @FXML private TableColumn<Student, Integer> reportLevelColumn;
    @FXML private TableColumn<Student, Double> reportGpaColumn;
    @FXML private TableColumn<Student, String> reportStatusColumn;

    private StudentService studentService;

    public void setStudentService(StudentService service) {
        this.studentService = service;
    }

    public void generateReport(double gpaThreshold, String sortBy) {
        List<Student> filtered = studentService.getAllStudents().stream()
                .filter(s -> s.getGpa() <= gpaThreshold)
                .sorted(sortBy.equals("GPA") ?
                        Comparator.comparing(Student::getGpa).reversed() :
                        Comparator.comparing(Student::getFullName))
                .collect(Collectors.toList());

        reportTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void initialize() {
        reportIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentId()));
        reportNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));
        reportProgrammeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProgramme()));
        reportLevelColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLevel()));
        reportGpaColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGpa()));
        reportStatusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
    }

    @FXML
    private void exportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(reportTable.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Name,Programme,Level,GPA,Status");
                for (Student s : reportTable.getItems()) {
                    writer.printf("%s,%s,%s,%d,%.2f,%s%n",
                            s.getStudentId(),
                            s.getFullName(),
                            s.getProgramme(),
                            s.getLevel(),
                            s.getGpa(),
                            s.getStatus());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ------------------ CSV IMPORT ------------------
    @FXML
    private void importCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(reportTable.getScene().getWindow());
        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                br.readLine(); // skip header
                List<Student> imported = new ArrayList<>();
                while ((line = br.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length < 6) continue; // skip invalid lines

                    // Use only the fields your Student constructor needs
                    Student s = new Student(
                            fields[0].trim(), // ID
                            fields[1].trim(), // Name
                            fields[2].trim(), // Programme
                            Integer.parseInt(fields[3].trim()), // Level
                            Double.parseDouble(fields[4].trim()), // GPA
                            fields[5].trim()  // Status
                    );

                    try {
                        studentService.addStudent(s);
                        imported.add(s); // might throw exception
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Skipping invalid student: " + s.getStudentId() + " -> " + ex.getMessage());
                        continue; // skip this student, keep importing the rest
                    }
                }
                reportTable.setItems(FXCollections.observableArrayList(imported));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}