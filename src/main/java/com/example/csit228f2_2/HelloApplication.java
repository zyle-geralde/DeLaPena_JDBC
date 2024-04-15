package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    public static List<User> users;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try(Connection c = SqlConnect.getConnection(); Statement statement = c.createStatement()){
            String createTableQuery ="CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "password VARCHAR(50) NOT NULL)";
            statement.execute(createTableQuery);
            System.out.println("Table Created successfully");
        }catch(SQLException e){
            e.printStackTrace();
        }
        users = new ArrayList<>();
        // LOAD USERS
        users.add(new User("tsgtest", "123456"));
        users.add(new User("jayvince", "secret"));
        users.add(new User("russselll", "palma"));

        AnchorPane pnMain = new AnchorPane();
        GridPane grid = new GridPane();
        pnMain.getChildren().add(grid);
        grid.setAlignment(Pos.CENTER);
        Text sceneTitle = new Text("Welcome to CSIT228");
        sceneTitle.setStrokeType(StrokeType.CENTERED);
        sceneTitle.setStrokeWidth(100);
        sceneTitle.setFill(Paint.valueOf("#325622"));
        sceneTitle.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 69));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label lblUsername = new Label("Username: ");
        lblUsername.setTextFill(Paint.valueOf("#c251d5"));
        lblUsername.setFont(Font.font(40));
        grid.add(lblUsername, 0, 1);

        TextField tfUsername = new TextField();
        tfUsername.setFont(Font.font(35));
        grid.add(tfUsername, 1, 1);

        Label lblPassword = new Label("Password: ");
        lblPassword.setTextFill(Paint.valueOf("#c251d5"));
        lblPassword.setFont(Font.font(40));
        grid.add(lblPassword, 0, 2);

        TextField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(35));
        grid.add(pfPassword, 1, 2);

        Button btnShow = new Button("<*>");
        HBox hbShow = new HBox();
        hbShow.getChildren().add(btnShow);
        hbShow.setAlignment(Pos.CENTER);
        hbShow.setMaxWidth(150);
        TextField tfPassword = new TextField();
        tfPassword.setFont(Font.font(35));
        grid.add(tfPassword, 1, 2);
        tfPassword.setVisible(false);
        grid.add(hbShow, 2, 2);

        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent actionEvent) {
                tfPassword.setText(pfPassword.getText());
                tfPassword.setVisible(true);
                pfPassword.setVisible(false);
                grid.add(new Button("Hello"), 4,4);
            }
        });

        btnShow.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tfPassword.setVisible(false);
                pfPassword.setVisible(true);
            }
        });

        Button btnSignIn = new Button("Sign In");
        btnSignIn.setFont(Font.font(45));
        HBox hbSignIn = new HBox();
        hbSignIn.getChildren().add(btnSignIn);
        hbSignIn.setAlignment(Pos.CENTER);
        grid.add(hbSignIn, 0, 3, 2, 1);
        final Text actionTarget = new Text("Hi");
        actionTarget.setFont(Font.font(30));
        grid.add(actionTarget, 1, 6);

        btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = tfUsername.getText();
                String password = pfPassword.getText();

                try(Connection c = SqlConnect.getConnection(); Statement statement = c.createStatement()){

                    String selectQuery = "SELECT * FROM users";
                    ResultSet resultSet = statement.executeQuery(selectQuery);

                    while(resultSet.next()){
                        int id  = resultSet.getInt("id");
                        String name = resultSet.getString("name");
                        String pass = resultSet.getString("password");
                        //System.out.printf("\nID: %d\nName: %s\nEmail:%s\n",id,name,email);

                        for (User user : users) {
                            if (username.equals(name) && password.equals(pass)) {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                                try {
                                    Scene scene = new Scene(loader.load());
                                    stage.setScene(scene);
                                    stage.show();
                                    break;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        actionTarget.setText("Invalid username/password");
                        actionTarget.setOpacity(1);
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }

                /*for (User user : users) {
                    if (username.equals(user.username) && password.equals(user.password)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                        try {
                            Scene scene = new Scene(loader.load());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                actionTarget.setText("Invalid username/password");
                actionTarget.setOpacity(1);*/
            }
        });

        Button btnRegister =  new Button("Register");
        btnRegister.setFont(Font.font(45));
        hbSignIn.getChildren().add(btnRegister);


        btnRegister.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String username = tfUsername.getText();
                String password = pfPassword.getText();
                if(username.trim().equals("") || password.equals("")){
                    actionTarget.setText("Invalid username/password");
                    actionTarget.setOpacity(1);
                }
                else{
                    try(Connection c = SqlConnect.getConnection(); PreparedStatement statement = c.prepareStatement("INSERT INTO users (name,password) VALUES (?,?)")){

                        String name = username;
                        String pass = password;
                        statement.setString(1,name);
                        statement.setString(2,pass);
                        int rowsInserted = statement.executeUpdate();

                        if(rowsInserted > 0){
                            actionTarget.setText("Data Inserted successfully");
                            actionTarget.setOpacity(1);
                            tfUsername.setText("");
                            pfPassword.setText("");
                        }
                    }catch(SQLException e){
                        e.printStackTrace();
                    }
                }

                ///////////////////////////////////////
                /*String username = tfUsername.getText();
                String password = pfPassword.getText();
                for (User user : users) {
                    if (username.equals(user.username) && password.equals(user.password)) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                        try {
                            Scene scene = new Scene(loader.load());
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                actionTarget.setText("Invalid username/password");
                actionTarget.setOpacity(1);*/
            }
        });


        EventHandler<KeyEvent> fieldChange = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent actionEvent) {
                actionTarget.setOpacity(0);
            }
        };
        tfUsername.setOnKeyTyped(fieldChange);
        pfPassword.setOnKeyTyped(fieldChange);


        Scene scene = new Scene(pnMain, 700, 560);
        stage.setScene(scene);
        stage.show();
    }
}