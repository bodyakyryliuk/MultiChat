package com.example.javafx.Controllers;

import com.example.javafx.ChatService.Client;
import com.example.javafx.User;
import com.example.javafx.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {


    public ObservableList<String> allUsers = FXCollections.observableArrayList();
    public ObservableList<String> addedUsers = FXCollections.observableArrayList();

    @FXML
    public ToggleButton searchButton;

    @FXML
    public ToggleButton addButton;

    @FXML
    public TextField inputField;

    @FXML
    public ListView<String> userList;

    @FXML
    public AnchorPane pane;

    @FXML
    public Label companion;

    @FXML
    public Button deleteChatBtn;

    @FXML
    public TextField searchField;

    @FXML
    public ListView<HBox> chatList;

    @FXML
    public Label usernameLabel;

    public static Socket socket;
    public static DataInputStream din;
    public static DataOutputStream dout;

    private String receiver;
    private static MainWindowController instance;

    public MainWindowController(){
        instance = this;
    }

    public static MainWindowController getInstance(){
        return instance;
    }

    public void addUser(){
        User user = new User();

        FilteredList<String> filteredList = new FilteredList<>(allUsers, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> {
                if(newValue == null || newValue.isEmpty())
                    return true;

                return item.toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<String> sortedList = new SortedList<>(filteredList);

        userList.setItems(sortedList);

        searchField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                String username = searchField.getText();
                if(!addedUsers.contains(username) && user.checkIfUsernameExists(username)) {
                    addedUsers.add(username);
                    searchField.clear();
                    searchUser();
                }
            }
        });

        userList.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                String selectedItem = userList.getSelectionModel().getSelectedItem();
                if(!addedUsers.contains(selectedItem) && user.checkIfUsernameExists(selectedItem)) {
                    addedUsers.add(selectedItem);
                    searchField.clear();
                    searchUser();
                }
            }
        });

    }

    public void setUsernameLabel(UserModel user){
        usernameLabel.setText(user.getUsername());
    }

    public String getUsername(){
        return usernameLabel.getText();
    }

    public String getReceiver(){
        return receiver;
    }

    public String getMessage(){
        return inputField.getText();
    }

    public ListView<String> getUserList(){
        return userList;
    }
    public TextField getInputField(){
        return inputField;
    }

    public void displayInput(String msg, boolean isServer){
        Label text = new Label(msg);

        text.setWrapText(true);
        text.setFont(new Font(16));
        text.setStyle("-fx-background-color: linear-gradient(to top, #c471f5 0%, #fa71cd 100%); -fx-padding: 5px; -fx-background-radius: 20px;");
        text.setMaxWidth(500);
        HBox container = new HBox();

        if(isServer)
            container.setAlignment(Pos.CENTER_LEFT);
        else
            container.setAlignment(Pos.CENTER_RIGHT);

        container.setSpacing(10);
        container.getChildren().add(text);

        chatList.getItems().add(container);
    }

    public void displayInput(){
        inputField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                Label text = new Label(inputField.getText());

                text.setWrapText(true);
                text.setFont(new Font(16));
                text.setStyle("-fx-background-color: linear-gradient(to top, #c471f5 0%, #fa71cd 100%); -fx-padding: 5px; -fx-background-radius: 20px;");
                text.setMaxWidth(500);
                HBox container = new HBox();
                container.setAlignment(Pos.CENTER_LEFT);
                container.setSpacing(10);
                container.getChildren().add(text);

                chatList.getItems().add(container);
            }
        });

    }

    private void setAddSearchButtons(){
        ToggleGroup toggleGroup = new ToggleGroup();
        searchButton.setToggleGroup(toggleGroup);
        addButton.setToggleGroup(toggleGroup);

        searchButton.setSelected(true);
        searchButton.setStyle("-fx-background-color: #2b2727;");

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == searchButton) {
                deleteChatBtn.setVisible(true);
                searchButton.setStyle("-fx-background-color: #2b2727;");
                addButton.setStyle("-fx-background-color: #46474A;");
                searchUser();
            } else if (newValue == addButton) {
                deleteChatBtn.setVisible(false);
                addButton.setStyle("-fx-background-color: #2b2727;");
                searchButton.setStyle("-fx-background-color: #46474A;");
                addUser();
            }
        });
    }

    private void searchUser(){
        FilteredList<String> filteredList = new FilteredList<>(addedUsers, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(item -> {
                if(newValue == null || newValue.isEmpty())
                    return true;

                return item.toLowerCase().contains(newValue.toLowerCase());
            });
        });

        SortedList<String> sortedList = new SortedList<>(filteredList);

        userList.setItems(sortedList);

        userList.setOnMouseClicked(event ->{
            if(event.getButton() == MouseButton.PRIMARY){
                companion.setText(userList.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void deleteChat(){
        deleteChatBtn.getStyleClass().add("button");
        addedUsers.remove(userList.getSelectionModel().getSelectedItem());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = new User();
        user.addUsersToList(allUsers);
        setAddSearchButtons();
        receiver = userList.getSelectionModel().getSelectedItem();


    }

}







