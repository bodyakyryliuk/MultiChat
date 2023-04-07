package com.example.javafx.ChatService;

import com.example.javafx.Controllers.MainWindowController;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private MainWindowController mainWindowController;

    public Client(Socket socket, MainWindowController mainWindowController){
        try {
            this.mainWindowController = mainWindowController;
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (Exception ex){
            closeEverything(bufferedReader, bufferedWriter, socket);
        }
    }

    public void closeEverything(BufferedReader bufferedReader, BufferedWriter bufferedWriter, Socket socket){
        try {
            if (bufferedReader != null)
                bufferedReader.close();
            if (bufferedWriter != null)
                bufferedWriter.close();
            if (socket != null)
                socket.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void readMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                while (socket.isConnected()){
                    try {
                        message = bufferedReader.readLine();
                        mainWindowController.displayInput(message, true);
                    }catch (IOException e){
                        closeEverything(bufferedReader,bufferedWriter,socket);
                    }
                }
            }
        }).start();
    }

    private void sendMessage(){
        if (mainWindowController.inputField.getText() == null || mainWindowController.inputField.getText().trim().isEmpty()) {
            return;
        }

        try {
            bufferedWriter.write(mainWindowController.inputField.getText());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        mainWindowController.displayInput(mainWindowController.inputField.getText().trim(), false);
        mainWindowController.inputField.setText(null);
    }

    public void runClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                readMessage();
            }
        }).start();

        // Start a new thread to send messages to the server
        new Thread(new Runnable() {
            @Override
            public void run() {
                mainWindowController.inputField.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode() == KeyCode.ENTER)
                        sendMessage();
                });
            }
        }).start();
    }





}
