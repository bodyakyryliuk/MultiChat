package com.example.javafx;

import com.example.javafx.Controllers.MainWindowController;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private final String connectionURL = "jdbc:mysql://localhost:3306/chatsql";
    private Connection connection = null;
    private Statement statement = null;

    public User(){
        makeConnection();
    }

    private void makeConnection(){
        try(Connection connection = DriverManager.getConnection(connectionURL, "root", "root")) {
            statement = connection.createStatement();

        } catch (SQLException a) {
            a.printStackTrace();
        }
    }

    public void addUsersToList(ObservableList<String> users){
        try (Connection connection = DriverManager.getConnection(connectionURL, "root", "root")){
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()){
                users.add(resultSet.getString("username"));
            }

            connection.close();
            statement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void addUser(String name, String surname, String email,
                        String password, String username){
        try(Connection connection = DriverManager.getConnection(connectionURL, "root", "root")) {
            statement = connection.createStatement();

            String data = "INSERT INTO user (name, surname, email, password, username) Values(" +
                    "'" + name + "', '" + surname + "', '" + email + "', '" + password + "', '"
                    + username + "')";

            statement.execute(data);

            connection.close();
            statement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkIfEmailExists(String enteredEmail){
        boolean exists = false;
        try(Connection connection = DriverManager.getConnection(connectionURL, "root", "root")) {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");

            while (resultSet.next()){
                String email = resultSet.getString("email");
                if(email.equals(enteredEmail))
                    exists = true;
            }

            connection.close();
            statement.close();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return exists;
    }

    public boolean checkPassword(String email, String enteredPassword){
        boolean correct = false;
        try(Connection connection = DriverManager.getConnection(connectionURL, "root", "root")) {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT password FROM user WHERE email = \"" + email + "\"");

            if(resultSet.next()) {
                if (enteredPassword.equals(resultSet.getString("password")))
                    correct = true;
            }
            else {
                ResultSet resultSet1 = statement.executeQuery("SELECT password FROM user WHERE username = \"" + email + "\"");
                if(resultSet1.next()) {
                    if (enteredPassword.equals(resultSet1.getString("password")))
                        correct = true;
                }
            }


            connection.close();
            statement.close();

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return correct;
    }

    public boolean checkIfUsernameExists(String enteredUsername){
        boolean exists = false;
        try(Connection connection = DriverManager.getConnection(connectionURL, "root", "root")) {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM user");

            while (resultSet.next()){
                String username = resultSet.getString("username");
                if(username.equals(enteredUsername))
                    exists = true;
            }

            connection.close();
            statement.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return exists;
    }


}
