package com.example.javafx.ChatService;

import com.example.javafx.Controllers.LoginController;
import com.example.javafx.Controllers.MainWindowController;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private ServerSocket serverSocket;
    private Map<String, ClientHandler> users = new HashMap<String, ClientHandler>();
    private static String username1;
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }


    public static void setUsername(String username){
        username1 = username;
    }

    public void runServer(){
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("New client connected" );

                String username = getUsername(socket);


                break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private String getUsername(Socket socket){
        InputStream inputStream;
        int bytesRead;
        byte[] buffer = new byte[1024];
        String username = "";

        try {
            inputStream = socket.getInputStream();
            bytesRead = inputStream.read(buffer);
            username = new String(buffer, 0, bytesRead);
        }catch (Exception e){
            e.printStackTrace();
        }
        return username;
    }

    public void addUser(String username, ClientHandler clientHandler){
        users.put(username, clientHandler);
    }

    public ClientHandler getUser(String username){
        return users.get(username);
    }

    public void removeUser(String username){
        users.remove(username);
    }

    public Map<String, ClientHandler> getUsersMap(){
        return users;
    }

    public static void startServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            Server server = new Server(serverSocket);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startServer();
    }

}
