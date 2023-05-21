/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.databasemanager;

import java.sql.*;
import aims.classes.User;
import java.util.*;

/**
 *
 * @author Vienji
 */
public class UserDBController {
    Connection connection = Driver.getConnection();
    private String query, col1 = "user_ID", col2 = "name", col3 = "username", col4 = "password", col5 = "access_level", col6 = "status"; 
    
    public void add(String name, String username, String password, String accessLevel){
        try {
            Statement statement = connection.createStatement();
            query = "INSERT INTO users_account ( " + col2 + ", " + col3 + ", " + col4 + ", " + col5 + ", " + col6 + " ) "
                    + "VALUES ('" + name + "', '" + username +
                    "', '" + password + "', '" + accessLevel +"', 'active'" + ") ";
            statement.executeUpdate(query);
        } catch (SQLException e){}
    } 
    
    public void delete(String uID){
        try{
            Statement statement = connection.createStatement();
            query = "UPDATE users_account SET status = 'inactive' WHERE " + col1 + " = " + uID;
            statement.executeUpdate(query);
        } catch (SQLException e) {}
    }
    
    public ArrayList<User> getAllAccounts(){
         ArrayList<User> userList = new ArrayList<>();
                
         try {
             Statement statement = connection.createStatement();
             query = "SELECT * FROM users_account";
             ResultSet result = statement.executeQuery(query);
         
             while (result.next()){
                 User user = new User();
                 user.setId(String.valueOf(result.getInt(col1)));
                 user.setName(result.getString(col2));
                 user.setUsername(result.getString(col3));
                 user.setAccessLevel(result.getString(col5));
                 user.setStatus(result.getString(col6));
                 
                 userList.add(user);
             } 
         }
         catch (SQLException e){}
         
         return userList;
    }
    
    public ArrayList<User> searchUsers(String value, String by){
         ArrayList<User> userList = new ArrayList<>();
                
         try {
             Statement statement = connection.createStatement();
             query = "SELECT * FROM users_account WHERE " + by + " LIKE " + value;
             ResultSet result = statement.executeQuery(query);
         
             while (result.next()){
                 User user = new User();
                 user.setId(String.valueOf(result.getInt(col1)));
                 user.setName(result.getString(col2));
                 user.setUsername(result.getString(col3));
                 user.setAccessLevel(result.getString(col5));
                 user.setStatus(col6);
                 
                 userList.add(user);
             } 
         }
         catch (SQLException e){}
         
         return userList;
    }
    
    public ArrayList<User> sortUsers(String by){
         ArrayList<User> userList = new ArrayList<>();
                
         try {
             Statement statement = connection.createStatement();
             query = "SELECT * FROM users_account ORDER BY " + by ;
             ResultSet result = statement.executeQuery(query);
         
             while (result.next()){
                 User user = new User();
                 user.setId(String.valueOf(result.getInt(col1)));
                 user.setName(result.getString(col2));
                 user.setUsername(result.getString(col3));
                 user.setAccessLevel(result.getString(col5));
                 user.setStatus(col6);
                 
                 userList.add(user);
             } 
         }
         catch (SQLException e){}
         
         return userList;
    }
    
    public String[] getCredentials(String username, String password, String accessLevel){
        String[] credentials = new String[5];
        
        try {
            Statement statement = connection.createStatement();
            query = "SELECT " + col2 + ", " + col3 + ", " + col4 + ", " + col5 + ", " + col6 + " FROM users_account WHERE " + col3 + " = '" + username
                + "' AND " + col4 + " = '" + password + "' AND " + col5 + " = '" + accessLevel + "'";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                credentials[0] = result.getString(col2);
                credentials[1] = result.getString(col3);
                credentials[2] = result.getString(col4);
                credentials[3] = result.getString(col5);
                credentials[4] = result.getString(col6);
            }
        }
        catch (SQLException e) {}
        
        return credentials;
    }
    
    public String getUserID(String name){
        String id = "";
        
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT user_ID FROM users_account WHERE name = '" + name + "'";
            
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                id = String.valueOf(result.getInt("user_ID"));   
            }
        } catch (SQLException e) {}
        
        return id;
    }
    
    public String getUserName(int id){
        String name = "";
        
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT name FROM users_account WHERE user_ID = " + id;
            
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                name = result.getString("name");   
            }
        } catch (SQLException e) {}
        
        return name;
    }
}
