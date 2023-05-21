/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.inventory;

import aims.LoginUI;
import aims.accounting.AccountingUI;
import aims.admin.AdminUI;
import aims.classes.Item;
import aims.classes.OrderEntry;
import aims.classes.OrderItem;
import aims.classes.Supplier;
import aims.classes.Temp;
import aims.databasemanager.InventoryDBController;
import aims.databasemanager.UserDBController;
import aims.images.ImageManipulator;
import aims.inventory.orders.AddOrder;
import aims.inventory.orders.ViewOrder;
import aims.inventory.stocks.AddItem;
import aims.inventory.stocks.SellItem;
import aims.inventory.stocks.UpdateItem;
import aims.inventory.stocks.ViewItem;
import aims.inventory.supplier.AddSupplier;
import aims.inventory.supplier.ViewSupplier;
import java.awt.Color;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class InventoryUI extends javax.swing.JFrame {
    /**
     * Creates new form InventoryUI
     */
    public InventoryUI() {
        initComponents();       
        showOrders("To Receive", toReceiveList);
        showOrders("Received", receivedList);
        showOrders("Cancelled", cancelledList);
        ui.setValue("Inventory");      
        
        ImageIcon icon = new ImageIcon("src\\aims\\images\\aims-logo.png");
        setIconImage(icon.getImage());
        
        ImageManipulator icons = new ImageManipulator();
        //User
        icons.setIcons("src\\aims\\images\\user.png", userIcon);
        //Orders
        icons.setIcons("src\\aims\\images\\search.png", searchOrder);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshOrders);
        //Stocks
        icons.setIcons("src\\aims\\images\\search.png", searchStocks);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshStocks);
        //Stocks
        icons.setIcons("src\\aims\\images\\search.png", searchSupplier);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshSupplier);
        ordersPButton.setBackground(new Color(153, 153, 153));
    }
    
    public Temp ui = new Temp();
    
    private void navSwitch(String userCheck){
        
        if(ui.getValue().equals("Accounting")){
           if(userCheck.equals("Logout")){
               dispose();
               new LoginUI().setVisible(true);
           } else {
               dispose();
               AccountingUI accountingUI= new AccountingUI();
               accountingUI.usernameDisplay.setText(usernameDisplay.getText());
               accountingUI.setVisible(true);
           }
        } else {
           if(userCheck.equals("Logout")){
               dispose();
               new LoginUI().setVisible(true);
           } else {
               dispose();
               AdminUI adminUI= new AdminUI();
               adminUI.usernameDisplay.setText(usernameDisplay.getText());
               adminUI.setVisible(true);
           }
        }
      
    }
    
    public int countToReceive(){
        ArrayList<OrderEntry> orders = new InventoryDBController().getOrders();
        int count = 0;
        
        Iterator i = (Iterator) orders.iterator();
        
        while(i.hasNext()){
            OrderEntry order = (OrderEntry) i.next();
                       
            if(order.getStatus().equals("To Receive")){
                count++;
            }           
        }        
        return count;
    }
    
    private void showOrders(String status, JTable tableName){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        
        tableName.setModel(new DefaultTableModel(null, new String[] {"Date", "OID", "Supplier", "No. of Items"
        , "Order Total", "Status", "Encoder"}));
        
        DefaultTableModel ordersTable = (DefaultTableModel) tableName.getModel();
        
        ArrayList<OrderEntry> orders = new InventoryDBController().getOrders();
        
        Iterator i = (Iterator) orders.iterator();
        
        while(i.hasNext()){
            OrderEntry order = (OrderEntry) i.next();
            
            String encoder = order.getStatus().equals(status) ? order.getOrderedBy() : order.getReceivedBy();
            
            if(order.getStatus().equals(status)){
                String[] orderData = {order.getDate(), order.getOID(), order.getSupplier(), order.getNumOfItems()
                , " ₱ " + decimalFormat.format(Float.parseFloat(order.getOrderTotal())), order.getStatus(), encoder};

                ordersTable.addRow(orderData);
            }           
        }
    }
    
    private void sortOrders(String status, JTable tableName, String sortBy){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        String sortValue = "";
        
        switch(sortBy){
            case "Date":
                sortValue = "orders.date";
                break;
            case "OID":
                sortValue = "orders.OID";
                break;
            case "Supplier":
                sortValue = "suppliers.name";
                break;
            case "No. of Items":
                sortValue = "orders.no_of_items";
                break;
            case "Order Total":
                sortValue = "orders.order_total";
                break;    
            case "Encoder":
                sortValue = "users_account.name";
                break;    
        }
        
        tableName.setModel(new DefaultTableModel(null, new String[] {"Date", "OID", "Supplier", "No. of Items"
        , "Order Total", "Status", "Encoder"}));
        
        DefaultTableModel ordersTable = (DefaultTableModel) tableName.getModel();
        
        ArrayList<OrderEntry> orders = new InventoryDBController().sortOrder(sortValue);
        
        Iterator i = (Iterator) orders.iterator();
        
        while(i.hasNext()){
            OrderEntry order = (OrderEntry) i.next();
            
            String encoder = order.getStatus().equals(status) ? order.getOrderedBy() : order.getReceivedBy();
            
            if(order.getStatus().equals(status)){
                String[] orderData = {order.getDate(), order.getOID(), order.getSupplier(), order.getNumOfItems()
                , " ₱ " + decimalFormat.format(Float.parseFloat(order.getOrderTotal())), order.getStatus(), encoder};

                ordersTable.addRow(orderData);
            }           
        }
    } 
    
    private void showStocks(){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        stocksList.setModel(new DefaultTableModel(null, new String[]{"SID", "Item Name", "Supplier",
            "Quantity", "Cost Per Item", "Status", "Encoder", "Deleted"}));
        
        DefaultTableModel stocksTable = (DefaultTableModel) stocksList.getModel();
        
        ArrayList<Item> stocks = new InventoryDBController().getItems();
        
        Iterator i = (Iterator) stocks.iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            String[] itemData = {item.getSID(), item.getItemName(), new InventoryDBController().getSupplierName(item.getSupplier()),
                item.getQuantity(), " ₱ " + decimalFormat.format(Float.parseFloat(item.getCost())), item.getStatus(), 
            new UserDBController().getUserName(Integer.parseInt(item.getEncoder())), item.getDeleted()};
            
            stocksTable.addRow(itemData);
        }       
    }
    
    private void sortStocks(String sortBy){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        stocksList.setModel(new DefaultTableModel(null, new String[]{"SID", "Item Name", "Supplier",
            "Quantity", "Cost Per Item", "Status", "Encoder", "Deleted"}));
        
        DefaultTableModel stocksTable = (DefaultTableModel) stocksList.getModel();
        
        ArrayList<Item> stocks = new InventoryDBController().sortItem(sortBy);
        
        Iterator i = (Iterator) stocks.iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            String[] itemData = {item.getSID(), item.getItemName(), new InventoryDBController().getSupplierName(item.getSupplier()),
                item.getQuantity(), " ₱ " + decimalFormat.format(Float.parseFloat(item.getCost())), item.getStatus(), 
            new UserDBController().getUserName(Integer.parseInt(item.getEncoder())), item.getDeleted()};
            
            stocksTable.addRow(itemData);
        }
    }
    
    private void showSuppliers(){
        supplierList.setModel(new DefaultTableModel(null, new String[]{"ID", "Name", "Contact Number", 
            "Email Address", "Description"}));
        
        DefaultTableModel suppliersTable = (DefaultTableModel) supplierList.getModel();
        
        ArrayList<Supplier> suppliers = new InventoryDBController().getSuppliersList();
         
        Iterator i = (Iterator) suppliers.iterator();

        while(i.hasNext()){
            Supplier supplier = (Supplier) i.next();

            String[] supplierData = {supplier.getID(), supplier.getName(), supplier.getContactNum(),
            supplier.getEmail(), supplier.getDescription()};

            suppliersTable.addRow(supplierData);     
        }      
    }
    
    private void showSustain(){
        sustainList.setModel(new DefaultTableModel(null, new String[]{"SID", "Item Name", "Supplier", "Remaining Stocks"}));
        DefaultTableModel sustainTable = (DefaultTableModel) sustainList.getModel();
        ArrayList<Item> stocks = new InventoryDBController().getItems();
        
        Iterator i = (Iterator) stocks.iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            if(item.getStatus().equalsIgnoreCase("Sustain")){
                String[] itemData = {item.getSID(), item.getItemName(), new InventoryDBController().getSupplierName(item.getSupplier()), item.getQuantity()};

                sustainTable.addRow(itemData);
            }     
        } 
    }
    
    private void showOutOfStocks(){
        outOfStockList.setModel(new DefaultTableModel(null, new String[]{"SID", "Item Name", "Supplier",}));
        DefaultTableModel outOfStockTable = (DefaultTableModel) outOfStockList.getModel();
        ArrayList<Item> stocks = new InventoryDBController().getItems();
        
        Iterator i = (Iterator) stocks.iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            if(item.getStatus().equalsIgnoreCase("Out of Stock")){
                String[] itemData = {item.getSID(), item.getItemName(), new InventoryDBController().getSupplierName(item.getSupplier())};

                outOfStockTable.addRow(itemData);
            }     
        } 
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        stocksPButton = new javax.swing.JPanel();
        stocksLButton = new javax.swing.JLabel();
        reportPButton = new javax.swing.JPanel();
        reportLButton = new javax.swing.JLabel();
        logoutPButton = new javax.swing.JPanel();
        logoutLButton = new javax.swing.JLabel();
        exitPButton = new javax.swing.JPanel();
        exitLButton = new javax.swing.JLabel();
        usernameDisplay = new javax.swing.JLabel();
        suppliersPButton = new javax.swing.JPanel();
        suppliersLButton = new javax.swing.JLabel();
        ordersPButton = new javax.swing.JPanel();
        ordersLButton = new javax.swing.JLabel();
        userIcon = new javax.swing.JLabel();
        menuPane = new javax.swing.JTabbedPane();
        ordersPanel = new javax.swing.JPanel();
        searchOrder = new javax.swing.JLabel();
        searchOrderValue = new javax.swing.JTextField();
        searchOrderBy = new javax.swing.JComboBox<>();
        refreshOrders = new javax.swing.JLabel();
        addOrderButton = new javax.swing.JButton();
        view = new javax.swing.JButton();
        sortOrderBy = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        ordersTabbedPanel = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        toReceiveList = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        receivedList = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        cancelledList = new javax.swing.JTable();
        stocksPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stocksList = new javax.swing.JTable();
        searchStocks = new javax.swing.JLabel();
        searchStockValue = new javax.swing.JTextField();
        searchStockBy = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        sortStockBy = new javax.swing.JComboBox<>();
        addItem = new javax.swing.JButton();
        refreshStocks = new javax.swing.JLabel();
        deleteItem = new javax.swing.JButton();
        viewItem = new javax.swing.JButton();
        updateItem = new javax.swing.JButton();
        sell = new javax.swing.JButton();
        supplierPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplierList = new javax.swing.JTable();
        addSupplier = new javax.swing.JButton();
        searchSupplier = new javax.swing.JLabel();
        supplierValue = new javax.swing.JTextField();
        refreshSupplier = new javax.swing.JLabel();
        searchSupplierBy = new javax.swing.JComboBox<>();
        deleteSupplier = new javax.swing.JButton();
        viewSupplier = new javax.swing.JButton();
        reportPanel = new javax.swing.JPanel();
        itemsTabbedPane = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        sustainList = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        outOfStockList = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        settingsPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Inventory");
        setPreferredSize(new java.awt.Dimension(1210, 760));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Miriam Fixed", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 153, 153));
        jLabel1.setText("Inventory");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 20, -1, -1));

        jLabel3.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 153, 0));
        jLabel3.setText("AIMS");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel5.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 102));
        jLabel5.setText("JAR Creatives");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        jLabel6.setFont(new java.awt.Font("Miriam Fixed", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("|");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 60));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));
        jPanel2.setForeground(new java.awt.Color(0, 153, 153));

        stocksPButton.setBackground(new java.awt.Color(102, 102, 102));
        stocksPButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        stocksPButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stocksPButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stocksPButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                stocksPButtonMouseExited(evt);
            }
        });

        stocksLButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        stocksLButton.setForeground(new java.awt.Color(255, 255, 255));
        stocksLButton.setText("Stocks");
        stocksLButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stocksLButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout stocksPButtonLayout = new javax.swing.GroupLayout(stocksPButton);
        stocksPButton.setLayout(stocksPButtonLayout);
        stocksPButtonLayout.setHorizontalGroup(
            stocksPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stocksPButtonLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(stocksLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        stocksPButtonLayout.setVerticalGroup(
            stocksPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stocksPButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stocksLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        reportPButton.setBackground(new java.awt.Color(102, 102, 102));
        reportPButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        reportPButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportPButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reportPButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reportPButtonMouseExited(evt);
            }
        });

        reportLButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        reportLButton.setForeground(new java.awt.Color(255, 255, 255));
        reportLButton.setText("Status");
        reportLButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportLButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout reportPButtonLayout = new javax.swing.GroupLayout(reportPButton);
        reportPButton.setLayout(reportPButtonLayout);
        reportPButtonLayout.setHorizontalGroup(
            reportPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPButtonLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(reportLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        reportPButtonLayout.setVerticalGroup(
            reportPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(reportLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logoutPButton.setBackground(new java.awt.Color(102, 102, 102));
        logoutPButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logoutPButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutPButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutPButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutPButtonMouseExited(evt);
            }
        });

        logoutLButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        logoutLButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutLButton.setText("Logout");
        logoutLButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutLButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout logoutPButtonLayout = new javax.swing.GroupLayout(logoutPButton);
        logoutPButton.setLayout(logoutPButtonLayout);
        logoutPButtonLayout.setHorizontalGroup(
            logoutPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPButtonLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(logoutLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        logoutPButtonLayout.setVerticalGroup(
            logoutPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        exitPButton.setBackground(new java.awt.Color(102, 102, 102));
        exitPButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        exitPButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitPButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitPButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitPButtonMouseExited(evt);
            }
        });

        exitLButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        exitLButton.setForeground(new java.awt.Color(255, 255, 255));
        exitLButton.setText("Exit");
        exitLButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitLButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout exitPButtonLayout = new javax.swing.GroupLayout(exitPButton);
        exitPButton.setLayout(exitPButtonLayout);
        exitPButtonLayout.setHorizontalGroup(
            exitPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitPButtonLayout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(exitLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        exitPButtonLayout.setVerticalGroup(
            exitPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitPButtonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exitLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        usernameDisplay.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        usernameDisplay.setForeground(new java.awt.Color(255, 255, 255));
        usernameDisplay.setText("Username");

        suppliersPButton.setBackground(new java.awt.Color(102, 102, 102));
        suppliersPButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        suppliersPButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliersPButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                suppliersPButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                suppliersPButtonMouseExited(evt);
            }
        });

        suppliersLButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        suppliersLButton.setForeground(new java.awt.Color(255, 255, 255));
        suppliersLButton.setText("Suppliers");
        suppliersLButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                suppliersLButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout suppliersPButtonLayout = new javax.swing.GroupLayout(suppliersPButton);
        suppliersPButton.setLayout(suppliersPButtonLayout);
        suppliersPButtonLayout.setHorizontalGroup(
            suppliersPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersPButtonLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(suppliersLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        suppliersPButtonLayout.setVerticalGroup(
            suppliersPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliersPButtonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(suppliersLButton)
                .addContainerGap())
        );

        ordersPButton.setBackground(new java.awt.Color(102, 102, 102));
        ordersPButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ordersPButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ordersPButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ordersPButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ordersPButtonMouseExited(evt);
            }
        });

        ordersLButton.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        ordersLButton.setForeground(new java.awt.Color(255, 255, 255));
        ordersLButton.setText("Orders");
        ordersLButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ordersLButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ordersPButtonLayout = new javax.swing.GroupLayout(ordersPButton);
        ordersPButton.setLayout(ordersPButtonLayout);
        ordersPButtonLayout.setHorizontalGroup(
            ordersPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordersPButtonLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(ordersLButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ordersPButtonLayout.setVerticalGroup(
            ordersPButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ordersPButtonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ordersLButton)
                .addContainerGap())
        );

        userIcon.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stocksPButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(reportPButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logoutPButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(exitPButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(suppliersPButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(userIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(usernameDisplay)
                .addContainerGap(71, Short.MAX_VALUE))
            .addComponent(ordersPButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(userIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(usernameDisplay)
                        .addGap(9, 9, 9)))
                .addGap(32, 32, 32)
                .addComponent(ordersPButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stocksPButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suppliersPButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportPButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
                .addComponent(logoutPButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitPButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 55, 250, 680));

        searchOrder.setPreferredSize(new java.awt.Dimension(20, 20));
        searchOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchOrderMouseClicked(evt);
            }
        });

        searchOrderBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "Supplier", "OID", "Encoder" }));

        refreshOrders.setPreferredSize(new java.awt.Dimension(20, 20));
        refreshOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshOrdersMouseClicked(evt);
            }
        });

        addOrderButton.setText("Add Order");
        addOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOrderButtonActionPerformed(evt);
            }
        });

        view.setText("View");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });

        sortOrderBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "OID", "Supplier", "No. of Items", "Order Total", "Encoder" }));

        jLabel2.setText("Sort by");

        toReceiveList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "OID", "Supplier", "No. of Items", "Order Total", "Status", "Encoder"
            }
        ));
        jScrollPane3.setViewportView(toReceiveList);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addContainerGap())
        );

        ordersTabbedPanel.addTab("To Receive", jPanel3);

        receivedList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "OID", "Supplier", "No. of Items", "Order Total", "Status", "Encoder"
            }
        ));
        jScrollPane4.setViewportView(receivedList);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addContainerGap())
        );

        ordersTabbedPanel.addTab("Received", jPanel4);

        cancelledList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "OID", "Supplier", "No. of Items", "Order Total", "Status", "Encoder"
            }
        ));
        jScrollPane5.setViewportView(cancelledList);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 872, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                .addContainerGap())
        );

        ordersTabbedPanel.addTab("Cancelled", jPanel5);

        javax.swing.GroupLayout ordersPanelLayout = new javax.swing.GroupLayout(ordersPanel);
        ordersPanel.setLayout(ordersPanelLayout);
        ordersPanelLayout.setHorizontalGroup(
            ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ordersPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ordersTabbedPanel)
                    .addGroup(ordersPanelLayout.createSequentialGroup()
                        .addComponent(searchOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchOrderValue, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshOrders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(179, 179, 179)
                        .addComponent(view)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addOrderButton)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        ordersPanelLayout.setVerticalGroup(
            ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ordersPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refreshOrders, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchOrder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ordersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchOrderValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addOrderButton)
                        .addComponent(view)
                        .addComponent(sortOrderBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(18, 18, 18)
                .addComponent(ordersTabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        menuPane.addTab("Orders", ordersPanel);

        stocksList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SID", "Item Name", "Supplier", "Quantity", "Cost Per Item", "Status", "Encoder", "Deleted"
            }
        ));
        jScrollPane2.setViewportView(stocksList);

        searchStocks.setPreferredSize(new java.awt.Dimension(20, 20));
        searchStocks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchStocksMouseClicked(evt);
            }
        });

        searchStockBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SID", "Item Name", "Supplier", "Encoder" }));

        jLabel18.setText("Sort by");

        sortStockBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SID", "Item Name", "Supplier", "Quantity", "Cost Per Item", "Status", "Encoder", "Deleted" }));

        addItem.setText("Add ");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        refreshStocks.setPreferredSize(new java.awt.Dimension(20, 20));
        refreshStocks.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshStocksMouseClicked(evt);
            }
        });

        deleteItem.setText("Delete");
        deleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteItemActionPerformed(evt);
            }
        });

        viewItem.setText("View");
        viewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewItemActionPerformed(evt);
            }
        });

        updateItem.setText("Update");
        updateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateItemActionPerformed(evt);
            }
        });

        sell.setText("Sell");
        sell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout stocksPanelLayout = new javax.swing.GroupLayout(stocksPanel);
        stocksPanel.setLayout(stocksPanelLayout);
        stocksPanelLayout.setHorizontalGroup(
            stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stocksPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stocksPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(updateItem)
                            .addComponent(deleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sell, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(addItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(viewItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))))
                    .addGroup(stocksPanelLayout.createSequentialGroup()
                        .addComponent(searchStocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchStockValue, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchStockBy, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortStockBy, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshStocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        stocksPanelLayout.setVerticalGroup(
            stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stocksPanelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchStocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStockValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchStockBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(sortStockBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshStocks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(stocksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(stocksPanelLayout.createSequentialGroup()
                        .addComponent(addItem)
                        .addGap(18, 18, 18)
                        .addComponent(viewItem)
                        .addGap(18, 18, 18)
                        .addComponent(sell)
                        .addGap(18, 18, 18)
                        .addComponent(updateItem)
                        .addGap(18, 18, 18)
                        .addComponent(deleteItem))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        menuPane.addTab("Stocks", stocksPanel);

        supplierList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Contact Number", "Email Address", "Description"
            }
        ));
        jScrollPane1.setViewportView(supplierList);

        addSupplier.setText("Add Supplier");
        addSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSupplierActionPerformed(evt);
            }
        });

        searchSupplier.setPreferredSize(new java.awt.Dimension(20, 20));
        searchSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchSupplierMouseClicked(evt);
            }
        });

        refreshSupplier.setPreferredSize(new java.awt.Dimension(20, 20));
        refreshSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshSupplierMouseClicked(evt);
            }
        });

        searchSupplierBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Name", "Contact Number", "Email" }));

        deleteSupplier.setText("Delete");
        deleteSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSupplierActionPerformed(evt);
            }
        });

        viewSupplier.setText("View");
        viewSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSupplierActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout supplierPanelLayout = new javax.swing.GroupLayout(supplierPanel);
        supplierPanel.setLayout(supplierPanelLayout);
        supplierPanelLayout.setHorizontalGroup(
            supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(supplierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createSequentialGroup()
                        .addComponent(searchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierValue, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchSupplierBy, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 299, Short.MAX_VALUE)
                        .addComponent(viewSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addSupplier)))
                .addContainerGap())
        );
        supplierPanelLayout.setVerticalGroup(
            supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addSupplier)
                        .addComponent(deleteSupplier)
                        .addComponent(viewSupplier))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(supplierValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(refreshSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchSupplierBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        menuPane.addTab("Supplier", supplierPanel);

        sustainList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SID", "Item Name", "Supplier", "Remaining Stocks"
            }
        ));
        jScrollPane6.setViewportView(sustainList);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                .addContainerGap())
        );

        itemsTabbedPane.addTab("Sustain", jPanel6);

        outOfStockList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SID", "Item Name", "Supplier"
            }
        ));
        jScrollPane7.setViewportView(outOfStockList);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                .addContainerGap())
        );

        itemsTabbedPane.addTab("Out of Stock", jPanel7);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Items");

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(itemsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        menuPane.addTab("Report", reportPanel);

        javax.swing.GroupLayout settingsPanelLayout = new javax.swing.GroupLayout(settingsPanel);
        settingsPanel.setLayout(settingsPanelLayout);
        settingsPanelLayout.setHorizontalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 945, Short.MAX_VALUE)
        );
        settingsPanelLayout.setVerticalGroup(
            settingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 683, Short.MAX_VALUE)
        );

        menuPane.addTab("Settings", settingsPanel);

        getContentPane().add(menuPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 950, 710));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void stocksPButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stocksPButtonMouseClicked
        ordersPButton.setBackground(new Color(102, 102, 102));
        suppliersPButton.setBackground(new Color(102, 102, 102));
        reportPButton.setBackground(new Color(102, 102, 102));
        showStocks();
        menuPane.setSelectedIndex(1);
        stocksPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_stocksPButtonMouseClicked

    private void stocksLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stocksLButtonMouseClicked
        ordersPButton.setBackground(new Color(102, 102, 102));
        suppliersPButton.setBackground(new Color(102, 102, 102));
        reportPButton.setBackground(new Color(102, 102, 102));
        showStocks();
        menuPane.setSelectedIndex(1);
        stocksPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_stocksLButtonMouseClicked

    private void reportPButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportPButtonMouseClicked
        ordersPButton.setBackground(new Color(102, 102, 102));
        suppliersPButton.setBackground(new Color(102, 102, 102));
        stocksPButton.setBackground(new Color(102, 102, 102));
        showSustain();
        showOutOfStocks();
        menuPane.setSelectedIndex(3);
        reportPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_reportPButtonMouseClicked

    private void reportLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportLButtonMouseClicked
        ordersPButton.setBackground(new Color(102, 102, 102));
        suppliersPButton.setBackground(new Color(102, 102, 102));
        stocksPButton.setBackground(new Color(102, 102, 102));
        showSustain();
        showOutOfStocks();
        menuPane.setSelectedIndex(3);
        reportPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_reportLButtonMouseClicked

    private void logoutPButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPButtonMouseClicked
        navSwitch(logoutLButton.getText());              
    }//GEN-LAST:event_logoutPButtonMouseClicked

    private void logoutLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLButtonMouseClicked
        navSwitch(logoutLButton.getText());     
    }//GEN-LAST:event_logoutLButtonMouseClicked

    private void exitPButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPButtonMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitPButtonMouseClicked

    private void exitLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitLButtonMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitLButtonMouseClicked

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        AddItem addItem = new AddItem();       
        addItem.encoder.setText(usernameDisplay.getText());
        addItem.setVisible(true);
    }//GEN-LAST:event_addItemActionPerformed

    private void addSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSupplierActionPerformed
        new AddSupplier().setVisible(true);
    }//GEN-LAST:event_addSupplierActionPerformed

    private void viewSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSupplierActionPerformed
        if(supplierList.getSelectedRow() < 0){
            JOptionPane.showMessageDialog(rootPane, "Please select an item to delete!");
        } else {
            ViewSupplier viewSupplier = new ViewSupplier();
        
            viewSupplier.id.setText(supplierList.getValueAt(supplierList.getSelectedRow(), 0).toString());
            viewSupplier.name.setText(supplierList.getValueAt(supplierList.getSelectedRow(), 1).toString());
            viewSupplier.contactNum.setText(supplierList.getValueAt(supplierList.getSelectedRow(), 2).toString());
            viewSupplier.email.setText(supplierList.getValueAt(supplierList.getSelectedRow(), 3).toString());
            viewSupplier.description.setText(supplierList.getValueAt(supplierList.getSelectedRow(), 4).toString());
            viewSupplier.setVisible(true);
        }    
    }//GEN-LAST:event_viewSupplierActionPerformed

    private void deleteSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplierActionPerformed
      
        if(supplierList.getSelectedRow() < 0){
            JOptionPane.showMessageDialog(rootPane, "Please select an item to delete!");
        } else {
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure that you want to delete this supplier?");

            if(n == 0) {
                new InventoryDBController().deleteSupplier(
                        supplierList.getValueAt(supplierList.getSelectedRow(), 0).toString());
                showSuppliers();
            } 
        }                  
    }//GEN-LAST:event_deleteSupplierActionPerformed

    private void ordersPButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersPButtonMouseClicked
        stocksPButton.setBackground(new Color(102, 102, 102));
        suppliersPButton.setBackground(new Color(102, 102, 102));
        reportPButton.setBackground(new Color(102, 102, 102));
        menuPane.setSelectedIndex(0);
        ordersPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_ordersPButtonMouseClicked

    private void ordersLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersLButtonMouseClicked
        stocksPButton.setBackground(new Color(102, 102, 102));
        suppliersPButton.setBackground(new Color(102, 102, 102));
        reportPButton.setBackground(new Color(102, 102, 102));
        menuPane.setSelectedIndex(0);
        ordersPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_ordersLButtonMouseClicked

    private void suppliersPButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersPButtonMouseClicked
        ordersPButton.setBackground(new Color(102, 102, 102));
        stocksPButton.setBackground(new Color(102, 102, 102));
        reportPButton.setBackground(new Color(102, 102, 102));
        showSuppliers();
        menuPane.setSelectedIndex(2);
        suppliersPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_suppliersPButtonMouseClicked

    private void suppliersLButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersLButtonMouseClicked
        ordersPButton.setBackground(new Color(102, 102, 102));
        stocksPButton.setBackground(new Color(102, 102, 102));
        reportPButton.setBackground(new Color(102, 102, 102));
        showSuppliers();
        menuPane.setSelectedIndex(2);
        suppliersPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_suppliersLButtonMouseClicked

    private void refreshSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshSupplierMouseClicked
        showSuppliers();
    }//GEN-LAST:event_refreshSupplierMouseClicked

    private void addOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addOrderButtonActionPerformed
        int limit = new InventoryDBController().getComponentState("order_limit");
        
        if( limit > countToReceive()){
            AddOrder addOrder = new AddOrder();      
            addOrder.encoder.setText(usernameDisplay.getText());
            addOrder.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Order limit of "+ limit +" has been reached!");
        }       
    }//GEN-LAST:event_addOrderButtonActionPerformed

    private void refreshOrdersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshOrdersMouseClicked
        sortOrders("To Receive", toReceiveList, String.valueOf(sortOrderBy.getSelectedItem()));
        sortOrders("Received", receivedList, String.valueOf(sortOrderBy.getSelectedItem()));
        sortOrders("Cancelled", cancelledList, String.valueOf(sortOrderBy.getSelectedItem()));
    }//GEN-LAST:event_refreshOrdersMouseClicked

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        
        JTable tableName;
        
        switch (ordersTabbedPanel.getSelectedIndex()) {
            case 0:
                tableName = toReceiveList;
                break;
            case 1:
                tableName = receivedList;
                break;
            default:
                tableName = cancelledList;
                break;
        }
        
        if(tableName.getSelectedRow() < 0){
            JOptionPane.showMessageDialog(rootPane, "Please select an order to view!");
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);

            ArrayList<OrderItem> itemsList = new InventoryDBController().getOrderItems(tableName.getValueAt(tableName.getSelectedRow(), 1).toString());        
            OrderEntry order = new InventoryDBController().getSelectedOrder(tableName.getValueAt(tableName.getSelectedRow(), 1).toString());

            ViewOrder viewOrder = new ViewOrder();       
            viewOrder.oID.setText(order.getOID());
            viewOrder.supplier.setText(order.getSupplier());
            viewOrder.dateOrdered.setText(order.getDate());
            viewOrder.numOfItems.setText(order.getNumOfItems());
            viewOrder.orderTotal.setText(decimalFormat.format(Float.parseFloat(order.getOrderTotal())));
            viewOrder.status.setText(order.getStatus());
            viewOrder.modeOfPayment.setText(order.getPaymentMethod());
            viewOrder.temp.setValue(usernameDisplay.getText());
            viewOrder.orderedBy.setText(order.getOrderedBy());

            if(order.getStatus().equals("Cancelled")){
                viewOrder.dateModified.setText("Date Cancelled: ");
                viewOrder.dateReceived.setText(order.getDateReceived());
                viewOrder.modifiedBy.setText("Cancelled by: ");
                viewOrder.receiver.setText(order.getReceivedBy());
                viewOrder.receivedButton.setEnabled(false);
                viewOrder.cancelOrderButton.setEnabled(false);
            } else if (order.getStatus().equals("Received")){
                viewOrder.receivedButton.setEnabled(false);
                viewOrder.cancelOrderButton.setEnabled(false);
                viewOrder.dateReceived.setText(order.getDateReceived());
                viewOrder.receiver.setText(order.getReceivedBy());
            }

            DefaultTableModel orderDetails = (DefaultTableModel) viewOrder.itemsList.getModel();
            Iterator i = (Iterator) itemsList.iterator();

            while(i.hasNext()){
                OrderItem item = (OrderItem) i.next();

                String[] itemData = {item.getItemName(), item.getQuantity(), 
                    " ₱ " + decimalFormat.format(Float.parseFloat(item.getAmount()))};
                orderDetails.addRow(itemData);
            }

            viewOrder.setVisible(true);
        }       
        
    }//GEN-LAST:event_viewActionPerformed

    private void refreshStocksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshStocksMouseClicked
        String sortBy = "";
        
        switch(String.valueOf(sortStockBy.getSelectedItem())){
            case "SID":
               sortBy = "stocks.SID"; 
               break;
                
            case "Item Name":
               sortBy = "stocks.item_name"; 
               break;
                
            case "Supplier":
               sortBy = "suppliers.name"; 
               break;
               
            case "Quantity":
               sortBy = "stocks.quantity"; 
               break;
               
            case "Cost Per Item":
               sortBy = "stocks.cost_per_item"; 
               break;
               
            case "Status":
               sortBy = "stocks.status"; 
               break;
               
            case "Encoder":
               sortBy = "users_account.name"; 
               break;
               
            case "Deleted":
               sortBy = "stocks.deleted"; 
               break;             
        }
        
        sortStocks(sortBy);
    }//GEN-LAST:event_refreshStocksMouseClicked

    private void viewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewItemActionPerformed
        if(stocksList.getSelectedRow() > -1){
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            decimalFormat.setGroupingUsed(true);
            decimalFormat.setGroupingSize(3);

            ViewItem itemView = new ViewItem();

            Item item = new InventoryDBController().getSelectedItem(String.valueOf(stocksList.getValueAt(stocksList.getSelectedRow(), 0)));

            ImageIcon icon = new ImageIcon(item.getImagePath());
            Image image = icon.getImage().getScaledInstance(itemView.itemImage.getWidth(), 
                    itemView.itemImage.getWidth(), Image.SCALE_SMOOTH);

            itemView.itemImage.setIcon(new ImageIcon(image)); 

            itemView.sID.setText(item.getSID());
            itemView.itemName.setText(item.getItemName());
            itemView.quantity.setText(item.getQuantity());
            itemView.supplier.setText(new InventoryDBController().getSupplierName(item.getSupplier()));
            itemView.cost.setText(decimalFormat.format(Float.parseFloat(item.getCost())));
            itemView.status.setText(item.getStatus());
            itemView.description.setText(item.getDescription());

            itemView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to view!");
        }       
    }//GEN-LAST:event_viewItemActionPerformed

    private void sellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellActionPerformed
        SellItem sellItem = new SellItem();
        
        sellItem.vendor.setText(usernameDisplay.getText());
        sellItem.setVisible(true);
    }//GEN-LAST:event_sellActionPerformed

    private void deleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteItemActionPerformed
        if(stocksList.getSelectedRow() > -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure that you want to delete this item?");

            if(n == 0){
                new InventoryDBController().deleteItem(String.valueOf(
                        stocksList.getValueAt(stocksList.getSelectedRow(), 0)), 
                        usernameDisplay.getText(), "Yes");
                showStocks();
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item that you want to delete!");
        }
    }//GEN-LAST:event_deleteItemActionPerformed

    private void updateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateItemActionPerformed
        if(stocksList.getSelectedRow() > -1){
            UpdateItem update = new UpdateItem();
            
            update.sID.setText(String.valueOf(stocksList.getValueAt(stocksList.getSelectedRow(), 0)));
            update.itemName.setText(String.valueOf(stocksList.getValueAt(stocksList.getSelectedRow(), 1)));
            update.supplier.setText(String.valueOf(stocksList.getValueAt(stocksList.getSelectedRow(), 2)));
            update.quantity.setValue(Integer.parseInt(String.valueOf(stocksList.getValueAt(stocksList.getSelectedRow(), 3))));
            update.temp.setValue(usernameDisplay.getText());
            update.costPerItem.setText(String.valueOf(stocksList.getValueAt(stocksList.getSelectedRow(), 4)).substring(2));
            update.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item that you want to update!");
        }
    }//GEN-LAST:event_updateItemActionPerformed

    private void searchOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchOrderMouseClicked
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        String value = "";
        String by = "";
        String status = "";
        JTable tableName;
        
        switch(String.valueOf(searchOrderBy.getSelectedItem())){
            case "Date":
                value = "'%" + searchOrderValue.getText() + "%'";
                by = "orders.date";
                break;
            case "Supplier":
                value = "'%" + searchOrderValue.getText() + "%'";
                by = "suppliers.name";
                break;    
            case "OID":
                value = "'%" + searchOrderValue.getText() + "%'";
                by = "OID";
                break;
            case "Encoder":
                value = "'%" + searchOrderValue.getText() + "%'";
                by = "users_account.name";
                break;    
        }

        switch (ordersTabbedPanel.getSelectedIndex()) {
            case 0:
                tableName = toReceiveList;
                status = "To Receive";
                break;
            case 1:
                tableName = receivedList;
                status = "Received";
                break;
            default:
                tableName = cancelledList;
                 status = "Cancelled";
                break;
        }
        
        //Table
        
        tableName.setModel(new DefaultTableModel(null, new String[] {"Date", "OID", "Supplier", "No. of Items"
        , "Order Total", "Status", "Encoder"}));
        
        DefaultTableModel ordersTable = (DefaultTableModel) tableName.getModel();
        
        ArrayList<OrderEntry> orders = new InventoryDBController().searchOrder(value, by);
        
        if(orders.isEmpty()){
            String[] orderData = {"No order found", "No order found", "No order found", "No order found"
                    , "No order found", "No order found", "No order found"};

                    ordersTable.addRow(orderData);
        } else {
            Iterator i = (Iterator) orders.iterator();

            while(i.hasNext()){
                OrderEntry order = (OrderEntry) i.next();

                String encoder = order.getStatus().equals(order.getStatus()) ? order.getOrderedBy() : order.getReceivedBy();

                if(order.getStatus().equals(status)){
                    String[] orderData = {order.getDate(), order.getOID(), order.getSupplier(), order.getNumOfItems()
                    , " ₱ " + decimalFormat.format(Float.parseFloat(order.getOrderTotal())), order.getStatus(), encoder};

                    ordersTable.addRow(orderData);
                }           
            }
        }     
    }//GEN-LAST:event_searchOrderMouseClicked

    private void searchStocksMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchStocksMouseClicked
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        String searchBy = "";
        String value = "";
        
        switch(String.valueOf(searchStockBy.getSelectedItem())){
            case "SID":
                value = "'%" + searchStockValue.getText() + "%'";
                searchBy = "stocks.SID";
                break;
                
            case "Item Name":
                value = "'%" + searchStockValue.getText() + "%'";
                searchBy = "stocks.item_name";
                break;
                
            case "Supplier":
                value = "'%" + searchStockValue.getText() + "%'";
                searchBy = "suppliers.name";
                break;
                
            case "Encoder": 
                value = "'%" + searchStockValue.getText() + "%'";
                searchBy = "users_account.name";
                break;
        }
        ArrayList<Item> stocks = new InventoryDBController().searchItem(value, searchBy);
        
        stocksList.setModel(new DefaultTableModel(null, new String[]{"SID", "Item Name", "Supplier",
            "Quantity", "Cost Per Item", "Status", "Encoder", "Deleted"}));
        
        DefaultTableModel stocksTable = (DefaultTableModel) stocksList.getModel();
        
        if(stocks.isEmpty()){
            String[] itemData = {"No item found", "No item found", "No item found",
                   "No item found", "No item found","No item found", 
                "No item found", "No item found"};

                stocksTable.addRow(itemData);
        } else {
            Iterator i = (Iterator) stocks.iterator();

            while(i.hasNext()){
                Item item = (Item) i.next();

                String[] itemData = {item.getSID(), item.getItemName(), new InventoryDBController().getSupplierName(item.getSupplier()),
                    item.getQuantity(), " ₱ " + decimalFormat.format(Float.parseFloat(item.getCost())), item.getStatus(), 
                new UserDBController().getUserName(Integer.parseInt(item.getEncoder())), item.getDeleted()};

                stocksTable.addRow(itemData);
            } 
        }     
    }//GEN-LAST:event_searchStocksMouseClicked

    private void searchSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchSupplierMouseClicked
        String value = "", by = "";
        
        switch(String.valueOf(searchSupplierBy.getSelectedItem())){
            case "ID":
                value = "'%" + supplierValue.getText() + "%'";
                by = "suppliers.ID";
                break;
                
            case "Name":
                value = "'%" + supplierValue.getText() + "%'";
                by = "suppliers.name";
                break;
                
            case "Contact Number":
                value = "'%" + supplierValue.getText() + "%'";
                by = "suppliers.contact_no";
                break;
                
            case "Email":
                value = "'%" + supplierValue.getText() + "%'";
                by = "suppliers.email";
                break;
        }
        
        supplierList.setModel(new DefaultTableModel(null, new String[]{"ID", "Name", "Contact Number", 
            "Email Address", "Description"}));
        
        DefaultTableModel suppliersTable = (DefaultTableModel) supplierList.getModel();
        
        ArrayList<Supplier> suppliers = new InventoryDBController().searchSuppliers(value, by);
        
        if(suppliers.isEmpty()){
           String[] supplierData = {"No supplier found", "No supplier found", "No supplier found",
            "No supplier found", "No supplier found"};
            
            suppliersTable.addRow(supplierData); 
        } else {
            Iterator i = (Iterator) suppliers.iterator();

            while(i.hasNext()){
                Supplier supplier = (Supplier) i.next();

                String[] supplierData = {supplier.getID(), supplier.getName(), supplier.getContactNum(),
                supplier.getEmail(), supplier.getDescription()};

                suppliersTable.addRow(supplierData);
            }
        } 
        
    }//GEN-LAST:event_searchSupplierMouseClicked

    private void ordersPButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersPButtonMouseEntered
        ordersPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_ordersPButtonMouseEntered

    private void ordersPButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersPButtonMouseExited
        if(menuPane.getSelectedIndex() != 0){ordersPButton.setBackground(new Color(102, 102, 102));}       
    }//GEN-LAST:event_ordersPButtonMouseExited

    private void stocksPButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stocksPButtonMouseEntered
        stocksPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_stocksPButtonMouseEntered

    private void stocksPButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stocksPButtonMouseExited
        if(menuPane.getSelectedIndex() != 1){stocksPButton.setBackground(new Color(102, 102, 102));}
    }//GEN-LAST:event_stocksPButtonMouseExited

    private void suppliersPButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersPButtonMouseEntered
        suppliersPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_suppliersPButtonMouseEntered

    private void suppliersPButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_suppliersPButtonMouseExited
        if(menuPane.getSelectedIndex() != 2){suppliersPButton.setBackground(new Color(102, 102, 102));}
    }//GEN-LAST:event_suppliersPButtonMouseExited

    private void reportPButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportPButtonMouseEntered
        reportPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_reportPButtonMouseEntered

    private void reportPButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportPButtonMouseExited
        if(menuPane.getSelectedIndex() != 3){reportPButton.setBackground(new Color(102, 102, 102));}
    }//GEN-LAST:event_reportPButtonMouseExited

    private void logoutPButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPButtonMouseEntered
        logoutPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_logoutPButtonMouseEntered

    private void logoutPButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPButtonMouseExited
        logoutPButton.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_logoutPButtonMouseExited

    private void exitPButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPButtonMouseEntered
        exitPButton.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_exitPButtonMouseEntered

    private void exitPButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPButtonMouseExited
        exitPButton.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_exitPButtonMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InventoryUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InventoryUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InventoryUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InventoryUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InventoryUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItem;
    private javax.swing.JButton addOrderButton;
    private javax.swing.JButton addSupplier;
    private javax.swing.JTable cancelledList;
    private javax.swing.JButton deleteItem;
    private javax.swing.JButton deleteSupplier;
    private javax.swing.JLabel exitLButton;
    private javax.swing.JPanel exitPButton;
    private javax.swing.JTabbedPane itemsTabbedPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    public javax.swing.JLabel logoutLButton;
    private javax.swing.JPanel logoutPButton;
    private javax.swing.JTabbedPane menuPane;
    private javax.swing.JLabel ordersLButton;
    private javax.swing.JPanel ordersPButton;
    private javax.swing.JPanel ordersPanel;
    private javax.swing.JTabbedPane ordersTabbedPanel;
    private javax.swing.JTable outOfStockList;
    private javax.swing.JTable receivedList;
    private javax.swing.JLabel refreshOrders;
    private javax.swing.JLabel refreshStocks;
    private javax.swing.JLabel refreshSupplier;
    private javax.swing.JLabel reportLButton;
    private javax.swing.JPanel reportPButton;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JLabel searchOrder;
    private javax.swing.JComboBox<String> searchOrderBy;
    private javax.swing.JTextField searchOrderValue;
    private javax.swing.JComboBox<String> searchStockBy;
    private javax.swing.JTextField searchStockValue;
    private javax.swing.JLabel searchStocks;
    private javax.swing.JLabel searchSupplier;
    private javax.swing.JComboBox<String> searchSupplierBy;
    private javax.swing.JButton sell;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JComboBox<String> sortOrderBy;
    private javax.swing.JComboBox<String> sortStockBy;
    private javax.swing.JLabel stocksLButton;
    private javax.swing.JTable stocksList;
    private javax.swing.JPanel stocksPButton;
    private javax.swing.JPanel stocksPanel;
    private javax.swing.JTable supplierList;
    private javax.swing.JPanel supplierPanel;
    private javax.swing.JTextField supplierValue;
    private javax.swing.JLabel suppliersLButton;
    private javax.swing.JPanel suppliersPButton;
    private javax.swing.JTable sustainList;
    private javax.swing.JTable toReceiveList;
    private javax.swing.JButton updateItem;
    private javax.swing.JLabel userIcon;
    public javax.swing.JLabel usernameDisplay;
    private javax.swing.JButton view;
    private javax.swing.JButton viewItem;
    private javax.swing.JButton viewSupplier;
    // End of variables declaration//GEN-END:variables
}
