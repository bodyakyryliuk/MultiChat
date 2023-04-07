package com.example.javafx.ChatService;


import com.example.javafx.Controllers.LoginController;
import com.example.javafx.Controllers.MainWindowController;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String username;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String receiver;
    private MainWindowController mainWindowController;
    private LoginController loginController;


    public ClientHandler(Socket socket, LoginController loginController, MainWindowController mainWindowController, String username){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.mainWindowController = mainWindowController;
            this.loginController = loginController;



        }catch (Exception ex){
            closeEverything(bufferedReader, bufferedWriter, socket);
        }
    }

    private void forwardMessage(String sender, String receiver, String message){
        ClientHandler receiverClient = loginController.getUser(receiver);
        if(receiverClient!=null) {
            receiverClient.sendMessage(sender, message);
            System.out.println("Message sent");
        }else
            System.out.println("Error in message sending");
    }

    private void sendMessage(String senderId, String message) {
        try {
            this.bufferedWriter.write(senderId + ": " + message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
            mainWindowController.displayInput(message, false);

        } catch (IOException e) {
            e.printStackTrace();
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



    @Override
    public void run() {
        String message;
        String receiver = mainWindowController.getReceiver();

        while (socket.isConnected()){
            mainWindowController.inputField.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER) {
                    forwardMessage(this.username, receiver, MainWindowController.getInstance().getMessage());
                }
            });

        }
    }
}
