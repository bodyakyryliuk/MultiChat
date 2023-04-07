package com.example.javafx.Controllers;

import com.example.javafx.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationController {

    @FXML
    public Button sendCodeBtn;

    @FXML
    public Button confirmRegistrationBtn;

    @FXML
    public Label codeLabel;

    @FXML
    public TextField codeField;

    @FXML
    public Label codeInfoLabel;

    @FXML
    public TextField nameField, surnameField, usernameField, emailField1, passwordField1;


    @FXML
    public void sendCode(){
        if(checkAllFields()) {
            sendCodeBtn.setDisable(true);
            sendCodeBtn.setVisible(false);
            confirmRegistrationBtn.setDisable(false);
            confirmRegistrationBtn.setVisible(true);
            codeLabel.setVisible(true);
            codeField.setVisible(true);
            codeInfoLabel.setVisible(true);
        }
    }

    private boolean checkAllFields(){
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField1.getText();
        String password = passwordField1.getText();
        String username = usernameField.getText();

        boolean checkName = checkName(name);
        boolean checkSurname = checkSurname(surname);
        boolean checkUsername = checkUsername(username);
        boolean checkEmail = checkEmail(email);
        boolean checkPassword = checkPassword(password);

        if(checkName && checkSurname && checkUsername && checkEmail && checkPassword)
            return true;
        else return false;
    }


    private boolean checkName(String name){
        Pattern pattern = Pattern.compile("[A-Z]{1}[a-z]+");
        Matcher matcher = pattern.matcher(name);

        if(!matcher.matches())
            nameField.setStyle("-fx-background-color: tomato;");
        else
            nameField.setStyle("-fx-background-color: white;");
        return matcher.matches();
    }

    private boolean checkSurname(String name){
        Pattern pattern = Pattern.compile("[A-Z]{1}[a-z]+");
        Matcher matcher = pattern.matcher(name);

        if(!matcher.matches())
            surnameField.setStyle("-fx-background-color: tomato;");
        else
            surnameField.setStyle("-fx-background-color: white;");
        return matcher.matches();
    }

    private boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
        Matcher matcher = pattern.matcher(email);
        User user = new User();

        if(!matcher.matches() || user.checkIfEmailExists(email) ) {
            emailField1.setStyle("-fx-background-color: tomato;");
            return false;
        }
        else {
            emailField1.setStyle("-fx-background-color: white;");
            return true;
        }
    }

    private boolean checkPassword(String password){
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
        Matcher matcher = pattern.matcher(password);

        if(!matcher.matches())
            passwordField1.setStyle("-fx-background-color: tomato;");
        else
            passwordField1.setStyle("-fx-background-color: white;");

        return matcher.matches();
    }

    private boolean checkUsername(String username){
        Pattern pattern = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{5,15}$");
        Matcher matcher = pattern.matcher(username);
        User user = new User();

        if(!matcher.matches() || user.checkIfUsernameExists(username) )
            usernameField.setStyle("-fx-background-color: tomato;");
        else
            usernameField.setStyle("-fx-background-color: white;");

        return matcher.matches();
    }


    @FXML
    public void register(ActionEvent event){
        new User().addUser(nameField.getText(), surnameField.getText(),
                           emailField1.getText(), passwordField1.getText(), usernameField.getText());
        openLoginWindow(event);
    }

    private void openLoginWindow(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registration");
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelRegistration(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Registration");
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
