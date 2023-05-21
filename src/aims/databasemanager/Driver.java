/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.databasemanager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 *
 * @author Vienji
 */
public class Driver {
    private static String USER;
    private static String PASSWORD;
    private static String SERVER;
    private static String PORT;
    private static String DATABASE;
    
    public static Connection getConnection(){   
        try(InputStream input = new FileInputStream("src\\aims\\path\\to\\config.properties")){
            Properties network = new Properties();
            
            network.load(input);
            
            USER = network.getProperty("username");
            PASSWORD = network.getProperty("password");
            SERVER = network.getProperty("server");
            PORT = network.getProperty("port");
            DATABASE = network.getProperty("database");
                   
        } catch (IOException io){
            io.printStackTrace();
        }
        
        try{ 
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://" + 
                    SERVER.concat(":" + PORT + "/" + DATABASE), USER, PASSWORD);
            return connect;
        } 
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null; }             
    }    
}
