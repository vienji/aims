/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.classes;

import java.sql.Blob;

/**
 *
 * @author Vienji
 */
public class Item {

    public String getImagePath() {
        return imagePath;
    }

    public Blob getImage() {
        return image;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getSID() {
        return SID;
    }

    public String getItemName() {
        return itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getEncoder() {
        return encoder;
    }

    public String getStatus() {
        return status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
    
    private String SID;
    private String itemName;
    private String quantity;
    private String supplier;
    private String cost;
    private String description;
    private String encoder;
    private String status;
    private String deleted;
    private String imagePath;
    private Blob image;
}
