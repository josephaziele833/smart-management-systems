package com.template.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    private static final String DB_NAME = "students.db";

    public static Connection getConnection() throws SQLException {

        // Ensure database is created inside project folder
        String dbPath = System.getProperty("user.dir") + File.separator + DB_NAME;

        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public static void createTableIfNotExists() {

        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    student_id TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    programme TEXT NOT NULL,
                    level INTEGER NOT NULL CHECK(level IN (100,200,300,400,500,600,700)),
                    gpa REAL NOT NULL CHECK(gpa BETWEEN 0.0 AND 4.0),
                    email TEXT NOT NULL,
                    phone_number TEXT NOT NULL,
                    date_added TEXT NOT NULL,
                    status TEXT NOT NULL
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}