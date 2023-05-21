/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.images;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Vienji
 */
public class ImageManipulator {   
    public void setIcons(String filePath, JLabel label){
          ImageIcon icon = new ImageIcon(filePath);
          Image image = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
          label.setIcon(new ImageIcon(image));
    }
}
