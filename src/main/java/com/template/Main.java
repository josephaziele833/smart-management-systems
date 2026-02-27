package com.template;

import com.template.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class git initMain extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        DatabaseUtil.createTableIfNotExists();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/main.fxml"));
        Scene scene = new Scene(loader.load(),600,400);

        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}
