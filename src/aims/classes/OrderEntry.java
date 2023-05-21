/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.classes;

/**
 *
 * @author Vienji
 */
public class OrderEntry {

    public String getOID() {
        return OID;
    }

    public String getDate() {
        return date;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getNumOfItems() {
        return numOfItems;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public String getStatus() {
        return status;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setOID(String OID) {
        this.OID = OID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setNumOfItems(String numOfItems) {
        this.numOfItems = numOfItems;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }
    private String OID;
    private String date;
    private String supplier;
    private String numOfItems;
    private String orderTotal;
    private String status;
    private String orderedBy;
    private String paymentMethod;
    private String dateReceived;
    private String receivedBy;
}
