/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.databasemanager;


import aims.classes.Item;
import aims.classes.OrderEntry;
import aims.classes.OrderItem;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import aims.classes.Supplier;
import java.time.LocalTime;


/**
 *
 * @author Vienji
 */
public class InventoryDBController {
    Connection connection = Driver.getConnection();
    private String query;
    
    //Orders
    public void addOrder(String supplier, Date date, ArrayList<OrderItem> orders, String numOfItems, 
            String orderTotal, String modeOfPayment, String encoder){
        
        LocalTime time = LocalTime.now();
        String timeS = String.valueOf(time);
        
        try {
            Statement statement = connection.createStatement();
            
            query = "INSERT INTO orders (date, supplier, no_of_items, order_total, status, "
                    + "ordered_by, payment_method, time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement prepStatement = connection.prepareStatement(query);
            prepStatement.setDate(1, date); 
            prepStatement.setInt(2, Integer.parseInt(getSupplierID(supplier))); 
            prepStatement.setInt(3, Integer.parseInt(numOfItems)); 
            prepStatement.setFloat(4, Float.parseFloat(orderTotal)); 
            prepStatement.setString(5, "To Receive"); 
            prepStatement.setInt(6, Integer.parseInt(new UserDBController().getUserID(encoder))); 
            prepStatement.setInt(7, Integer.parseInt(new AccountingDBController().getAccountID(modeOfPayment))); 
            prepStatement.setString(8, timeS); 
            prepStatement.execute();
            
            query = "SELECT OID FROM orders WHERE date = '" + date + "' AND time = '" + timeS + "' ";
            
            ResultSet result = statement.executeQuery(query);
            String id = "0";
            
            while(result.next()){ 
                id = String.valueOf(result.getInt("OID")); 
                System.out.println("ID = " + id);
            }
            
            query = "INSERT INTO order_items (OID, name, quantity, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement prepStatement2 = connection.prepareStatement(query);
            
            for(OrderItem o : orders){
                prepStatement2.setInt(1, Integer.parseInt(id)); 
                prepStatement2.setString(2, o.getItemName()); 
                prepStatement2.setInt(3, Integer.parseInt(o.getQuantity())); 
                prepStatement2.setFloat(4, Float.parseFloat(o.getAmount())); 
                prepStatement2.execute();
            }

        } catch (SQLException e) {}
    }
    
    public ArrayList<OrderEntry> getOrders(){
        ArrayList<OrderEntry> ordersList = new ArrayList<>();
        
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT orders.OID, orders.date, suppliers.name, orders.no_of_items, orders.order_total, orders.status,"
                + " users_account.name, accounts.account_name, orders.date_received, orders.receiver"
                + " FROM orders"
                + " INNER JOIN suppliers ON orders.supplier = suppliers.ID"
                + " INNER JOIN users_account ON orders.ordered_by = users_account.user_ID"
                + " INNER JOIN accounts ON orders.payment_method = accounts.AID";
                
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                OrderEntry order = new OrderEntry();
                
                order.setOID(String.valueOf(result.getInt("OID"))); 
                order.setDate(String.valueOf(result.getDate("date"))); 
                order.setSupplier(result.getString("suppliers.name")); 
                order.setNumOfItems(String.valueOf(result.getInt("no_of_items"))); 
                order.setOrderTotal(String.valueOf(result.getFloat("order_total"))); 
                order.setStatus(result.getString("status")); 
                order.setOrderedBy(result.getString("users_account.name")); 
                order.setPaymentMethod(result.getString("account_name")); 
                
                if(result.getDate("date_received") != null){
                    order.setDateReceived(String.valueOf(result.getDate("date_received"))); 
                } else {
                    order.setDateReceived("To Receive");
                }              
                
                order.setReceivedBy(new UserDBController().getUserName(result.getInt("receiver"))); 
                
                order.getOID();
                order.getDate();
                order.getSupplier();
                order.getNumOfItems();
                order.getOrderTotal();
                order.getStatus();
                order.getOrderedBy();
                order.getPaymentMethod();
                order.getPaymentMethod();
                order.getDateReceived();
                
                ordersList.add(order);
            }
            
        } catch (SQLException e) {}
        
        
        return ordersList;
    }
    
    public ArrayList<OrderEntry> getSortedOrders(String what, String value, String type){
        ArrayList<OrderEntry> ordersList = new ArrayList<>();
        String newValue = type.equalsIgnoreCase("String") ? "'" + value + "'" : value;
   
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT orders.OID, orders.date, suppliers.name, orders.no_of_items, orders.order_total, orders.status,"
                + " users_account.name, accounts.account_name, orders.date_received, orders.receiver"
                + " FROM orders"
                + " INNER JOIN suppliers ON orders.supplier = suppliers.ID"
                + " INNER JOIN users_account ON orders.ordered_by = users_account.user_ID"
                + " INNER JOIN accounts ON orders.payment_method = accounts.AID"
                + " WHERE " + what + " = " + newValue;
                 
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                OrderEntry order = new OrderEntry();
                
                order.setOID(String.valueOf(result.getInt("OID"))); 
                order.setDate(String.valueOf(result.getDate("date"))); 
                order.setSupplier(result.getString("suppliers.name")); 
                order.setNumOfItems(String.valueOf(result.getInt("no_of_items"))); 
                order.setOrderTotal(String.valueOf(result.getFloat("order_total"))); 
                order.setStatus(result.getString("status")); 
                order.setOrderedBy(result.getString("users_account.name")); 
                order.setPaymentMethod(result.getString("account_name")); 
                
                if(result.getDate("date_received") != null){
                    order.setDateReceived(String.valueOf(result.getDate("date_received"))); 
                } else {
                    order.setDateReceived("To Receive");
                }              
                
                order.setReceivedBy(new UserDBController().getUserName(result.getInt("receiver"))); 
                
                order.getOID();
                order.getDate();
                order.getSupplier();
                order.getNumOfItems();
                order.getOrderTotal();
                order.getStatus();
                order.getOrderedBy();
                order.getPaymentMethod();
                order.getPaymentMethod();
                order.getDateReceived();
                
                ordersList.add(order);
            }
            
        } catch (SQLException e) {}
        
        
        return ordersList;
    }
    
    public OrderEntry getSelectedOrder(String id){  
        OrderEntry order = new OrderEntry();
        
        try {
            Statement statement = connection.createStatement();
            query = "SELECT orders.OID, orders.date, suppliers.name, orders.no_of_items, orders.order_total, orders.status,"
                + " users_account.name, accounts.account_name, orders.date_received, orders.receiver"
                + " FROM orders"
                + " INNER JOIN suppliers ON orders.supplier = suppliers.ID"
                + " INNER JOIN users_account ON orders.ordered_by = users_account.user_ID"
                + " INNER JOIN accounts ON orders.payment_method = accounts.AID"
                + " WHERE OID = " + id  ;
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){                            
                order.setOID(String.valueOf(result.getInt("OID"))); 
                order.setDate(String.valueOf(result.getDate("date"))); 
                order.setSupplier(result.getString("suppliers.name")); 
                order.setNumOfItems(String.valueOf(result.getInt("no_of_items"))); 
                order.setOrderTotal(String.valueOf(result.getFloat("order_total"))); 
                order.setStatus(result.getString("status")); 
                order.setOrderedBy(result.getString("users_account.name")); 
                order.setPaymentMethod(result.getString("account_name")); 
                
                if(result.getDate("date_received") != null){
                    order.setDateReceived(String.valueOf(result.getDate("date_received"))); 
                    order.setReceivedBy(new UserDBController().getUserName(result.getInt("receiver")));
                } else {
                    order.setDateReceived("To Receive");
                    order.setReceivedBy("None");
                } 
            }
            
        } catch (SQLException e) {}
        
        return order;    
    }
    
    public ArrayList<OrderItem> getOrderItems(String id){
        ArrayList<OrderItem> orders = new ArrayList<>();
        
        try {
            Statement statement = connection.createStatement();
            
            query = "SELECT * FROM order_items WHERE OID =" + id; 
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                OrderItem item = new OrderItem();
                
                item.setItemName(result.getString("name"));
                item.setQuantity(String.valueOf(result.getInt("quantity")));
                item.setAmount(String.valueOf(result.getFloat("amount")));
                
                orders.add(item);
            }
        } catch (SQLException e) {}
        
        return orders;
    }
    
    public void changeStatus(String id, String status, String modifiedBy, String date){
        try{
            Statement statement = connection.createStatement();
            query = "UPDATE orders SET status = '" + status + "', receiver = '" + 
                    modifiedBy + "', date_received = '" + date + "' WHERE OID = " + id;   
            statement.execute(query);
        } catch (SQLException e){}
    }
    
    //Search and Sort Order
    
    public ArrayList<OrderEntry> searchOrder(String value, String by){
        ArrayList<OrderEntry> ordersList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT orders.OID, orders.date, suppliers.name, orders.no_of_items, orders.order_total, orders.status,"
                + " users_account.name, accounts.account_name, orders.date_received, orders.receiver"
                + " FROM orders"
                + " INNER JOIN suppliers ON orders.supplier = suppliers.ID"
                + " INNER JOIN users_account ON orders.ordered_by = users_account.user_ID"
                + " INNER JOIN accounts ON orders.payment_method = accounts.AID"
                + " WHERE " + by + " LIKE " + value;
            
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                OrderEntry order = new OrderEntry();
                
                order.setOID(String.valueOf(result.getInt("OID"))); 
                order.setDate(String.valueOf(result.getDate("date"))); 
                order.setSupplier(result.getString("suppliers.name")); 
                order.setNumOfItems(String.valueOf(result.getInt("no_of_items"))); 
                order.setOrderTotal(String.valueOf(result.getFloat("order_total"))); 
                order.setStatus(result.getString("status")); 
                order.setOrderedBy(result.getString("users_account.name")); 
                order.setPaymentMethod(result.getString("account_name")); 
                
                if(result.getDate("date_received") != null){
                    order.setDateReceived(String.valueOf(result.getDate("date_received"))); 
                } else {
                    order.setDateReceived("To Receive");
                }              
                
                order.setReceivedBy(new UserDBController().getUserName(result.getInt("receiver"))); 
                
                order.getOID();
                order.getDate();
                order.getSupplier();
                order.getNumOfItems();
                order.getOrderTotal();
                order.getStatus();
                order.getOrderedBy();
                order.getPaymentMethod();
                order.getPaymentMethod();
                order.getDateReceived();
                
                ordersList.add(order);
            }
            
        } catch (SQLException e) {e.printStackTrace();}
        
        return ordersList;
    }
    
    public ArrayList<OrderEntry> sortOrder(String sortBy){
        ArrayList<OrderEntry> ordersList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT orders.OID, orders.date, suppliers.name, orders.no_of_items, orders.order_total, orders.status,"
                + " users_account.name, accounts.account_name, orders.date_received, orders.receiver"
                + " FROM orders"
                + " INNER JOIN suppliers ON orders.supplier = suppliers.ID"
                + " INNER JOIN users_account ON orders.ordered_by = users_account.user_ID"
                + " INNER JOIN accounts ON orders.payment_method = accounts.AID"
                + " ORDER BY " + sortBy ;
            
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                OrderEntry order = new OrderEntry();
                
                order.setOID(String.valueOf(result.getInt("OID"))); 
                order.setDate(String.valueOf(result.getDate("date"))); 
                order.setSupplier(result.getString("suppliers.name")); 
                order.setNumOfItems(String.valueOf(result.getInt("no_of_items"))); 
                order.setOrderTotal(String.valueOf(result.getFloat("order_total"))); 
                order.setStatus(result.getString("status")); 
                order.setOrderedBy(result.getString("users_account.name")); 
                order.setPaymentMethod(result.getString("account_name")); 
                
                if(result.getDate("date_received") != null){
                    order.setDateReceived(String.valueOf(result.getDate("date_received"))); 
                } else {
                    order.setDateReceived("To Receive");
                }              
                
                order.setReceivedBy(new UserDBController().getUserName(result.getInt("receiver"))); 
                
                order.getOID();
                order.getDate();
                order.getSupplier();
                order.getNumOfItems();
                order.getOrderTotal();
                order.getStatus();
                order.getOrderedBy();
                order.getPaymentMethod();
                order.getPaymentMethod();
                order.getDateReceived();
                
                ordersList.add(order);
            }
            
        } catch (SQLException e) {e.printStackTrace();}
        
        return ordersList;
    }
    
    //Stocks
    public void addStock(String itemName, String supplier, String quantity, String cost, String description, String encoder, String filePath, String status){
        
        try {
            InputStream file = null;
                 
            try {
                file = new FileInputStream(filePath);
            } catch (FileNotFoundException e) { System.out.println("File not Found!");}
            
            query = "INSERT INTO stocks (item_name, supplier, quantity, cost_per_item, item_desc, encoder, status, deleted, image_filepath, image_file) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement statement = connection.prepareStatement(query);
            
            statement.setString(1, itemName); 
            statement.setInt(2, Integer.parseInt(getSupplierID(supplier))); 
            statement.setInt(3, Integer.parseInt(quantity)); 
            statement.setFloat(4, Float.parseFloat(cost)); 
            statement.setString(5, description); 
            statement.setInt(6, Integer.parseInt(new UserDBController().getUserID(encoder))); 
            statement.setString(7, status); 
            statement.setString(8, "No"); 
            statement.setString(9, filePath);  
            statement.setBlob(10, file); 
            statement.execute();            
            
        } catch (SQLException e) {}
    }
    
    public ArrayList<Item> getItems(){
        ArrayList<Item> itemList = new ArrayList<>();
        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM stocks";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                Item item = new Item();
                
                item.setSID(String.valueOf(result.getInt("SID")));
                item.setItemName(result.getString("item_name"));
                item.setSupplier(result.getString("supplier"));
                item.setQuantity(String.valueOf(result.getInt("quantity")));
                item.setCost(String.valueOf(result.getFloat("cost_per_item")));
                item.setDescription(result.getString("item_desc"));
                item.setEncoder(String.valueOf(result.getInt("encoder")));
                item.setStatus(result.getString("status"));
                item.setDeleted(result.getString("deleted"));
                item.setImagePath(result.getString("image_filepath"));
                item.setImage(result.getBlob("image_file"));
                
                itemList.add(item);
            }
                        
        } catch (SQLException e) {}
        
        return itemList;
    }
    
    public ArrayList<Item> searchItem(String value, String by){
        ArrayList<Item> itemList = new ArrayList<>();
        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM stocks "
                    + " INNER JOIN suppliers ON stocks.supplier = suppliers.ID"
                    + " INNER JOIN users_account ON stocks.encoder = users_account.user_ID"
                    + " WHERE " + by + " LIKE" + value;
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                Item item = new Item();
                
                item.setSID(String.valueOf(result.getInt("SID")));
                item.setItemName(result.getString("item_name"));
                item.setSupplier(result.getString("supplier"));
                item.setQuantity(String.valueOf(result.getInt("quantity")));
                item.setCost(String.valueOf(result.getFloat("cost_per_item")));
                item.setDescription(result.getString("item_desc"));
                item.setEncoder(String.valueOf(result.getInt("encoder")));
                item.setStatus(result.getString("status"));
                item.setDeleted(result.getString("deleted"));
                item.setImagePath(result.getString("image_filepath"));
                item.setImage(result.getBlob("image_file"));
                
                itemList.add(item);
            }
                        
        } catch (SQLException e) {}
        
        return itemList;
    } 
    
    public ArrayList<Item> sortItem(String sortBy){
        ArrayList<Item> itemList = new ArrayList<>();
        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM stocks "
                    + " INNER JOIN suppliers ON stocks.supplier = suppliers.ID"
                    + " INNER JOIN users_account ON stocks.encoder = users_account.user_ID"
                    + " ORDER BY " + sortBy;
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                Item item = new Item();
                
                item.setSID(String.valueOf(result.getInt("SID")));
                item.setItemName(result.getString("item_name"));
                item.setSupplier(result.getString("supplier"));
                item.setQuantity(String.valueOf(result.getInt("quantity")));
                item.setCost(String.valueOf(result.getFloat("cost_per_item")));
                item.setDescription(result.getString("item_desc"));
                item.setEncoder(String.valueOf(result.getInt("encoder")));
                item.setStatus(result.getString("status"));
                item.setDeleted(result.getString("deleted"));
                item.setImagePath(result.getString("image_filepath"));
                item.setImage(result.getBlob("image_file"));
                
                itemList.add(item);
            }
                        
        } catch (SQLException e) {e.printStackTrace();}
        
        return itemList;
    } 
    
    public Item getSelectedItem(String id){
        Item item = new Item();
    
        try {
            Statement statement = connection.createStatement();
            query = "SELECT * FROM stocks WHERE SID = " + id;
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){              
                
                item.setSID(String.valueOf(result.getInt("SID")));
                item.setItemName(result.getString("item_name"));
                item.setSupplier(result.getString("supplier"));
                item.setQuantity(String.valueOf(result.getInt("quantity")));
                item.setCost(String.valueOf(result.getFloat("cost_per_item")));
                item.setDescription(result.getString("item_desc"));
                item.setEncoder(String.valueOf(result.getInt("encoder")));
                item.setStatus(result.getString("status"));
                item.setDeleted(result.getString("deleted"));
                item.setImagePath(result.getString("image_filepath"));
                item.setImage(result.getBlob("image_file"));
                            
            }
            
        } catch (SQLException e) {}
        
        return item;
    }
    
    public void setRemaining(String id, String remaining, String what){
        try{
            Statement statement = connection.createStatement();           
            
            if(what.equals("Decrease")){
                query = "UPDATE stocks SET quantity = " + remaining + " WHERE SID = " + id;           

                statement.execute(query); 
            } else {
               
                query = "UPDATE stocks SET quantity = " + getRemaining(id, remaining) + " WHERE SID = " + id;           
           
                statement.execute(query);   
            }         
                    
        } catch (SQLException e) {}   
    }
    
    public int getRemaining(String id, String remaining){
        int rem = 0;
        
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT quantity FROM stocks WHERE SID = " + id;
            ResultSet result = statement.executeQuery(query);
                
            while(result.next()){
                rem = Integer.parseInt(remaining) + result.getInt("quantity");
            }
            
        } catch (SQLException e) {}
        
        return rem;
    }
    
    public void setItemStatus(String id, String status){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE stocks SET status = '"+ status +"' WHERE SID = " + id;
            
            statement.execute(query);
        } catch (SQLException e){}
    }
    
    public void deleteItem(String id, String encoder, String deleted){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE stocks SET encoder = " + new UserDBController().getUserID(encoder) + ", deleted = '"
                    + deleted + "' WHERE SID = " + id;
            
            statement.execute(query);
        } catch (SQLException e) {}
    }
    
    public void updateItem(String id, String quantity, String cost, String encoder){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE stocks SET quantity = " + quantity + ", cost_per_item = "+ cost + ", encoder = " + encoder +" WHERE SID = " + id;
            
            statement.execute(query);          
        } catch (SQLException e) {}
    }

    //Supplier
    public void addSupplier(String name, String contactNum, String email, String description){
        try {          
            query = "INSERT INTO suppliers (name, contact_no, email, description) VALUES (?, ?, ?, ?)";
            
            PreparedStatement prepStatement = connection.prepareStatement(query);
            
            prepStatement.setString(1, name);
            prepStatement.setString(2, contactNum);
            prepStatement.setString(3, email);
            prepStatement.setString(4, description);
            prepStatement.execute();            
        } catch (SQLException e) {}
    }
    
    public ArrayList<Supplier> getSuppliersList(){
        ArrayList<Supplier> supplierList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM suppliers";
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                Supplier supplier = new Supplier();
                
                supplier.setID(String.valueOf(result.getInt("ID")));
                supplier.setName(result.getString("name"));
                supplier.setContactNum(result.getString("contact_no"));
                supplier.setEmail(result.getString("email"));
                supplier.setDescription(result.getString("description"));
                
                supplierList.add(supplier);
            }
        } catch(SQLException e) {}     
        return supplierList;
    }
    
    public ArrayList<Supplier> searchSuppliers(String value, String by){
        ArrayList<Supplier> supplierList = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM suppliers WHERE " + by + " LIKE " + value;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                Supplier supplier = new Supplier();
                
                supplier.setID(String.valueOf(result.getInt("ID")));
                supplier.setName(result.getString("name"));
                supplier.setContactNum(result.getString("contact_no"));
                supplier.setEmail(result.getString("email"));
                supplier.setDescription(result.getString("description"));
                
                supplierList.add(supplier);
            }
        } catch(SQLException e) {e.printStackTrace();}     
        return supplierList;
    }
    
    public String getSupplierID(String name){
        String id = "0";
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT ID FROM suppliers WHERE name = '" + name + "'";
            
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                id = String.valueOf(result.getInt("ID"));
            }
        } catch (SQLException e) {}
        
        return id;
    }
    
    public String getSupplierName(String id){
        String name = "";
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT name FROM suppliers WHERE ID = " + id;
            
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                name = result.getString("name");
            }
        } catch (SQLException e) {}
        
        return name;
    }
    
    public void deleteSupplier(String id){
        try{
            Statement statement = connection.createStatement();
            
            query = "DELETE FROM suppliers WHERE ID = " + id;
            
            statement.execute(query);
        } catch(SQLException e) {}
    }
    
    public void updateSupplier(String id, String name, String contactNum, String email, String description){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE suppliers SET name = '" + name + "',  contact_no = '" + contactNum +
                    "', email = '" + email + "', description = '" + description + "' WHERE id = " + id;
            
            statement.execute(query);
        } catch (SQLException e){}
    } 
    
    //Settings
    
    public void modSettings(String component, String state){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE inv_set SET state = '" + state + "' WHERE component = '" + component + "'";

            statement.execute(query);
        } catch (SQLException e) {}
    }
    
    public int getComponentState(String component){
        int limit = 0;
        
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT state FROM inv_set WHERE component = '" + component + "'";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                limit = Integer.parseInt(result.getString("state"));
            }
        } catch (SQLException e) {}
        
        return limit;
    }
    
    public String itemStatusChecker(int quantity){
        if(quantity > getComponentState("stock_limit")){
            return "Available";
        } else if (quantity <= getComponentState("stock_limit") && quantity > 0) {
            return "Sustain";
        } else {
            return "Out of Stock";
        }
    }
}
