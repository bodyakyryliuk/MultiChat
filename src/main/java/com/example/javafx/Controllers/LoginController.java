package com.example.javafx.Controllers;

import com.example.javafx.ChatService.Client;
import com.example.javafx.ChatService.ClientHandler;
import com.example.javafx.ChatService.Server;
import com.example.javafx.User;
import com.example.javafx.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class LoginController implements Serializable {

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField emailField;

    private UserModel user;

    private Map<String, ClientHandler> users = new HashMap<String, ClientHandler>();


    public void addUser(String username, ClientHandler clientHandler){
        users.put(username, clientHandler);
    }

    @FXML
    public void signIn(ActionEvent event){
      if(user.checkPassword(emailField.getText(), passwordField.getText())){
        user = new UserModel(emailField.getText(), passwordField.getText());
        Node node = (Node) event.getSource();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.close();
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
                Parent root = loader.load();

                stage.setUserData(user);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Multichat");
                stage.setResizable(false);
                stage.show();

            }catch (IOException e){
                e.printStackTrace();
            }
      }
    }

    public String getUsername(){
        return user.getUsername();
    }

    public ClientHandler getUser(String username){
        return users.get(username);
    }

    private TextField getTextField(MainWindowController mainWindowController){
        return mainWindowController.getInputField();
    }

    private ListView<String> getListView(MainWindowController mainWindowController){
        return mainWindowController.getUserList();
    }



    private void runClient(MainWindowController mainWindowController){
        try {
            Socket socket = new Socket("localhost", 8080);
            socket.getOutputStream().write(getUsername().getBytes());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(mainWindowController);

            String selectedUser = mainWindowController.getReceiver();
            ClientHandler clientHandler = new ClientHandler(socket, this, mainWindowController, getUsername());
            Thread thread = new Thread(clientHandler);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void signUp(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/FXML/Registration.fxml"));
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
