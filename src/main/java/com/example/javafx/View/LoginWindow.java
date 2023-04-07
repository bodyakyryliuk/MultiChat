package com.example.javafx.View;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginWindow extends Application {

    public LoginWindow(){
        Stage stage = new Stage();
        stage.setResizable(false);
        start(stage);
    }
    @Override
    public void start(Stage stage)  {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(loader.load());
        }catch (Exception e){
            e.printStackTrace();
        }

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
