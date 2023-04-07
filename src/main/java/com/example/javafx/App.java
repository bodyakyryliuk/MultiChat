package com.example.javafx;

import com.example.javafx.ChatService.Server;
import com.example.javafx.Controllers.MainWindowController;
import com.example.javafx.View.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage){
        new ViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}
