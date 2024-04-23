package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogInSignUpView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("LogApp");
        stage.setScene(scene);
        stage.show();

        Label ul = (Label) scene.lookup("#userlabel");
        ul.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");

        Label pl = (Label) scene.lookup("#passlabel");
        pl.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");


        Pane paneb = (Pane) scene.lookup("#paneback");
        paneb.setStyle("-fx-background-color: pink; -fx-opacity: 0.8;");

        Image img = new Image("file:src/main/java/com/example/csit228f2_2/_09b362f4-dad0-4599-946f-0d5d12eafe20.jpg");
        Circle circ= (Circle) scene.lookup("#logohold");
        circ.setFill(new ImagePattern(img));

        circ.setStyle("-fx-stroke: transparent;");

        Label ntf= (Label) scene.lookup("#notetify");
        ntf.setStyle("-fx-font-family: Arial; -fx-font-size: 40; -fx-text-fill: #874CCC; -fx-font-weight: bold;" +
                "-fx-background-color: #F27BBD; -fx-padding: 5; -fx-opacity: 0.9; -fx-background-radius: 10");


        Button logdesign= (Button) scene.lookup("#LogBut");

        logdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");


        Button SignButdesign= (Button) scene.lookup("#SignBut");

        SignButdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");





        try(Connection c = SqlConnect.getConnection(); Statement statement = c.createStatement()){
            String createTableQuery ="CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL)";
            statement.execute(createTableQuery);

            String createTableQuery2 ="CREATE TABLE IF NOT EXISTS notes (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "post VARCHAR(5000) NOT NULL," +
                    "uid INT NOT NULL)";
            statement.execute(createTableQuery2);


        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
