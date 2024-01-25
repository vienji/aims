/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author Vienji
 */
public class AIMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       new LoginUI().setVisible(true);
       File file = new File("src\\aims\\path\\to\\config.properties");
       if(!file.exists()){
           try(OutputStream output = new FileOutputStream("src\\aims\\path\\to\\config.properties")){
            Properties network = new Properties();
            
            network.setProperty("username", "");
            network.setProperty("password", "");
            network.setProperty("server", "");
            network.setProperty("port", "");
            network.setProperty("database", "");
            
            network.store(output, null);
            } catch (IOException io){
                io.printStackTrace();
            }
       }
    }
}
