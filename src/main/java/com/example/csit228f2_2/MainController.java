package com.example.csit228f2_2;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.Set;

public class MainController {
    @FXML
    public Button LogBut;

    @FXML
    public Button SignBut;

    @FXML
    public AnchorPane AllParent;
    @FXML
    public TextField usertext;
    @FXML
    public PasswordField passtext;
    @FXML
    public Label labelcheck;
    @FXML
    public Label idcont;
    @FXML
    public Label namecont;

    @FXML
    public TextArea notetext;

    @FXML
    public VBox notepane;
    @FXML
    public ScrollPane notepane2;

    @FXML
    public Label userlabel;
    @FXML
    public Label passlabel;

    @FXML
    public Button shownote;

    public ScrollPane forAll;




    @FXML
    public void LogIn() throws IOException {

        try (Connection connection = SqlConnect.getConnection();
             Statement statement = connection.createStatement()) {

            String selectQuery = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String pass = resultSet.getString("password");

                if(usertext.getText().equals(name) && passtext.getText().equals(pass)){
                    Parent root = FXMLLoader.load(getClass().getResource("tryme.fxml"));
                    AnchorPane jj = (AnchorPane) usertext.getParent();
                    jj.getChildren().setAll(root.getChildrenUnmodifiable());

                    Label idl = (Label)jj.lookup("#idcont");
                    Label nl = (Label)jj.lookup("#namecont");
                    ScrollPane npp = (ScrollPane)jj.lookup("#notepane2");
                    Pane covhead = (Pane) jj.lookup("#coverhead");
                    covhead.setStyle("-fx-background-color: #874CCC; -fx-border-style: solid; -fx-border-width: 0 0 4 0; -fx-border-color: pink;");

                    Button dlme = (Button) jj.lookup("#delacc");
                    Button logme = (Button) jj.lookup("#Logout");
                    Button edt = (Button) jj.lookup("#editacc");
                    Button chpass = (Button) jj.lookup("#changepass");

                    dlme.setStyle("-fx-background-color: pink; -fx-text-fill:#10439F;");
                    logme.setStyle("-fx-background-color: pink; -fx-text-fill:#10439F;");
                    edt.setStyle("-fx-background-color: pink; -fx-text-fill:#10439F;");
                    chpass.setStyle("-fx-background-color: pink; -fx-text-fill:#10439F;");

                    Label idcontme = (Label) jj.lookup("#idcont");
                    Label ncont = (Label) jj.lookup("#namecont");
                    idcontme.setStyle("-fx-text-fill:white;");
                    ncont.setStyle("-fx-text-fill:white;");

                    Label idlabel = (Label) jj.lookup("#idlabel");
                    Label ulabel = (Label) jj.lookup("#ulabel");
                    idlabel.setStyle("-fx-text-fill:white;");
                    ulabel.setStyle("-fx-text-fill:white;");
                    idl.setText(id+"");
                    nl.setText(name);

                    Pane backcolor= (Pane)jj.lookup("#backcolor");
                    backcolor.setStyle("-fx-background-color: #FFCBCB");

                    Button notedd = (Button) jj.lookup("#noted");
                    Button shownoteme=(Button) jj.lookup("#shownote");
                    notedd.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");
                    shownoteme.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");

                    ScrollPane np2 = (ScrollPane) jj.lookup("#notepane2");

                    return;
                }
            }
            labelcheck.setText("Invalid Username/Password");
            labelcheck.setOpacity(1.0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void shownotesbut(){
        if(shownote.getText().toString().equals("Show Notes")){
            VBox addme = new VBox();
            addme.setId("notepane");
            addme.setSpacing(10);

            notepane2.setContent(null);
            notepane2.setContent(addme);


            getAllNotes(idcont,addme);

            shownote.setText("Hide Notes");
        }
        else{
            notepane2.setContent(null);
            shownote.setText("Show Notes");
        }
    }

    @FXML
    public void SignUp(){
        if(usertext.getText().toString().trim().equals("") || usertext.getText().toString().trim().equals("")) {
            labelcheck.setText("Invalid Username/Password");
            labelcheck.setOpacity(1.0);
        }
        else{
            try (Connection connection = SqlConnect.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, password) VALUES (?, ?)")) {

                String name = usertext.getText().trim();
                String password = passtext.getText().trim();

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, password);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {

                }
                usertext.setText("");
                passtext.setText("");

                labelcheck.setOpacity(0);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void delaccbut(){
        try (Connection connection = SqlConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            int userIdToDelete = Integer.parseInt(idcont.getText());
            preparedStatement.setInt(1, userIdToDelete);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {

            }
            AnchorPane al = (AnchorPane) idcont.getParent();
            Parent root = FXMLLoader.load(getClass().getResource("LogInSignUpView.fxml"));
            al.getChildren().setAll(root.getChildrenUnmodifiable());

            Label ul = (Label) al.lookup("#userlabel");
            ul.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");

            Label pl = (Label) al.lookup("#passlabel");
            pl.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");


            Pane paneb = (Pane) al.lookup("#paneback");
            paneb.setStyle("-fx-background-color: pink; -fx-opacity: 0.8;");

            Image img = new Image("file:src/main/java/com/example/csit228f2_2/_09b362f4-dad0-4599-946f-0d5d12eafe20.jpg");
            Circle circ= (Circle) al.lookup("#logohold");
            circ.setFill(new ImagePattern(img));

            circ.setStyle("-fx-stroke: transparent;");

            Label ntf= (Label) al.lookup("#notetify");
            ntf.setStyle("-fx-font-family: Arial; -fx-font-size: 40; -fx-text-fill: #874CCC; -fx-font-weight: bold;" +
                    "-fx-background-color: #F27BBD; -fx-padding: 5; -fx-opacity: 0.9; -fx-background-radius: 10");


            Button logdesign= (Button) al.lookup("#LogBut");

            logdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");


            Button SignButdesign= (Button) al.lookup("#SignBut");

            SignButdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void Logoutbut() throws IOException {
        AnchorPane al = (AnchorPane) idcont.getParent();
        Parent root = FXMLLoader.load(getClass().getResource("LogInSignUpView.fxml"));
        al.getChildren().setAll(root.getChildrenUnmodifiable());

        Label ul = (Label) al.lookup("#userlabel");
        ul.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");

        Label pl = (Label) al.lookup("#passlabel");
        pl.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");


        Pane paneb = (Pane) al.lookup("#paneback");
        paneb.setStyle("-fx-background-color: pink; -fx-opacity: 0.8;");

        Image img = new Image("file:src/main/java/com/example/csit228f2_2/_09b362f4-dad0-4599-946f-0d5d12eafe20.jpg");
        Circle circ= (Circle) al.lookup("#logohold");
        circ.setFill(new ImagePattern(img));

        circ.setStyle("-fx-stroke: transparent;");

        Label ntf= (Label) al.lookup("#notetify");
        ntf.setStyle("-fx-font-family: Arial; -fx-font-size: 40; -fx-text-fill: #874CCC; -fx-font-weight: bold;" +
                "-fx-background-color: #F27BBD; -fx-padding: 5; -fx-opacity: 0.9; -fx-background-radius: 10");


        Button logdesign= (Button) al.lookup("#LogBut");

        logdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");


        Button SignButdesign= (Button) al.lookup("#SignBut");

        SignButdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");
    }
    @FXML
    public void editaccbut(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input");
        dialog.setHeaderText(null);
        dialog.setContentText("Change Username");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name->{
            if(name.trim().equals("")){

            }
            else{
                try (Connection connection = SqlConnect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?")) {

                    String newName = name;
                    int userIdToUpdate = Integer.parseInt(idcont.getText());
                    preparedStatement.setString(1, newName);
                    preparedStatement.setInt(2, userIdToUpdate);

                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {

                    }
                    namecont.setText(name);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void changepassbut(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input");
        dialog.setHeaderText(null);
        dialog.setContentText("Change Password");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name->{
            if(name.trim().equals("")){

            }
            else{
                try (Connection connection = SqlConnect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET password = ? WHERE id = ?")) {

                    String pass = name;
                    int userIdToUpdate = Integer.parseInt(idcont.getText());
                    preparedStatement.setString(1, pass);
                    preparedStatement.setInt(2, userIdToUpdate);

                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {

                    }

                    AnchorPane al = (AnchorPane) idcont.getParent();
                    Parent root = FXMLLoader.load(getClass().getResource("LogInSignUpView.fxml"));
                    al.getChildren().setAll(root.getChildrenUnmodifiable());


                    Label ul = (Label) al.lookup("#userlabel");
                    ul.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");

                    Label pl = (Label) al.lookup("#passlabel");
                    pl.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 10;");


                    Pane paneb = (Pane) al.lookup("#paneback");
                    paneb.setStyle("-fx-background-color: pink; -fx-opacity: 0.8;");

                    Image img = new Image("file:src/main/java/com/example/csit228f2_2/_09b362f4-dad0-4599-946f-0d5d12eafe20.jpg");
                    Circle circ= (Circle) al.lookup("#logohold");
                    circ.setFill(new ImagePattern(img));

                    circ.setStyle("-fx-stroke: transparent;");

                    Label ntf= (Label) al.lookup("#notetify");
                    ntf.setStyle("-fx-font-family: Arial; -fx-font-size: 40; -fx-text-fill: #874CCC; -fx-font-weight: bold;" +
                            "-fx-background-color: #F27BBD; -fx-padding: 5; -fx-opacity: 0.9; -fx-background-radius: 10");


                    Button logdesign= (Button) al.lookup("#LogBut");

                    logdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");


                    Button SignButdesign= (Button) al.lookup("#SignBut");

                    SignButdesign.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    public void notebut(){
        if(notetext.getText().trim().equals("")){

        }
        else{
            try (Connection connection = SqlConnect.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO notes (post,uid) VALUES (?,?)")) {

                String post = notetext.getText().trim();
                int userid = Integer.parseInt(idcont.getText());

                preparedStatement.setString(1, post);
                preparedStatement.setInt(2, userid);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {

                }
                notetext.setText("");


                VBox addme = new VBox();
                addme.setId("notepane");
                addme.setSpacing(10);

                notepane2.setContent(null);
                notepane2.setContent(addme);


                getAllNotes(idcont,addme);

                shownote.setText("Hide Notes");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void getAllNotes(Label idcc,VBox np ){
        try (Connection connection = SqlConnect.getConnection();
             Statement statement = connection.createStatement()) {

            String selectQuery = "SELECT * FROM notes";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String post = resultSet.getString("post");
                int uid = resultSet.getInt("uid");

                if(uid == Integer.parseInt(idcc.getText())){


                    ScrollPane npp = notepane2;


                    VBox store = new VBox();
                    store.setMinWidth(705);
                    store.setMaxWidth(705);
                    store.setSpacing(10);
                    Label lab = new Label(post);
                    lab.setWrapText(true);
                    store.getChildren().add(new Label("#"+id));
                    store.getChildren().add(lab);
                    store.setStyle("-fx-background-color: lightpink;");

                    HBox forbut = new HBox();
                    Button delPost = new Button("Delete");
                    Button editPost= new Button("Edit");

                    delPost.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");
                    editPost.setStyle("-fx-background-color: #874CCC; -fx-text-fill:white;");

                    forbut.getChildren().add(delPost);
                    forbut.getChildren().add(editPost);
                    forbut.setAlignment(Pos.CENTER);

                    delPost.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            try (Connection connection = SqlConnect.getConnection();
                                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM notes WHERE id = ?")) {
                                int userIdToDelete = id;
                                preparedStatement.setInt(1, userIdToDelete);

                                int rowsDeleted = preparedStatement.executeUpdate();
                                if (rowsDeleted > 0) {

                                }

                                VBox addme = new VBox();
                                addme.setId("notepane");
                                addme.setSpacing(10);

                                npp.setContent(null);
                                npp.setContent(addme);

                                getAllNotes(idcont,addme);

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    editPost.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            int hid = id;
                            ScrollPane hnpp = npp;
                            Label hidcont = idcont;
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Input");
                            dialog.setHeaderText(null);
                            dialog.setContentText("Edit Post");
                            Optional<String> result = dialog.showAndWait();
                            result.ifPresent(name->{
                                if(name.trim().equals("")){

                                }
                                else{
                                    try (Connection connection = SqlConnect.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement("UPDATE notes SET post = ? WHERE id = ?")) {

                                        String postme = name;
                                        int userIdToUpdate = hid;
                                        preparedStatement.setString(1, postme);
                                        preparedStatement.setInt(2, userIdToUpdate);

                                        int rowsUpdated = preparedStatement.executeUpdate();
                                        if (rowsUpdated > 0) {

                                        }


                                        VBox addme = new VBox();
                                        addme.setId("notepane");
                                        addme.setSpacing(10);

                                        hnpp.setContent(null);
                                        hnpp.setContent(addme);

                                        getAllNotes(hidcont,addme);

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });
                        }
                    });

                    store.getChildren().add(forbut);

                    np.getChildren().add(store);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
