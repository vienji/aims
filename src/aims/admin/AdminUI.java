/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.admin;

import aims.databasemanager.UserDBController;
import aims.accounting.AccountingUI;
import aims.classes.User;
import aims.admin.usersmanager.AddUser;
import aims.inventory.InventoryUI;
import aims.LoginUI;
import aims.admin.settings.Settings;
import aims.admin.usersmanager.UpdateUser;
import aims.classes.Account;
import aims.classes.Item;
import aims.databasemanager.AccountingDBController;
import aims.databasemanager.InventoryDBController;
import aims.images.ImageManipulator;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Vienji
 */
public class AdminUI extends javax.swing.JFrame {

    /**
     * Creates new form AdminDashboard
     */
    public AdminUI() {
        initComponents();           
        showIncomeStatement(String.valueOf(new AccountingDBController().getSetCurrentJournal()));
        cashBalance.setText(getCashBalance(String.valueOf(new AccountingDBController().getSetCurrentJournal())));
        sales.setText(getRevenue(String.valueOf(new AccountingDBController().getSetCurrentJournal())));
        liabilities.setText(getLiabilities(String.valueOf(new AccountingDBController().getSetCurrentJournal())));   
        financialReportsP.setBackground(new Color(153, 153, 153));
        
        ImageIcon icon = new ImageIcon("src\\aims\\images\\aims-logo.png");
        setIconImage(icon.getImage());
        
        ImageManipulator icons = new ImageManipulator();
        
        icons.setIcons("src\\aims\\images\\search.png", searchUsersAccount);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshList);
        icons.setIcons("src\\aims\\images\\user.png", user);
    }

    private void showUsersList(){
        usersList.setModel(new DefaultTableModel(null, new String[]{"UID","Name", "Username", "Access Level", "Status"}));
        ArrayList<User> userList = new UserDBController().getAllAccounts();
        
        Iterator i = (Iterator) userList.iterator();
        
        while(i.hasNext()){
            User user = (User)i.next();
            String[] userData = {user.getId(), user.getName(), user.getUsername(), user.getAccessLevel(), user.getStatus()};
            DefaultTableModel accountsTable = (DefaultTableModel) usersList.getModel();            
            accountsTable.addRow(userData);
        }
    }
    
    private void sortUsersList(String by){
        usersList.setModel(new DefaultTableModel(null, new String[]{"UID","Name", "Username", "Access Level"}));
        ArrayList<User> userList = new UserDBController().sortUsers(by);
        
        Iterator i = (Iterator) userList.iterator();
        
        while(i.hasNext()){
            User user = (User)i.next();
            String[] userData = {user.getId(), user.getName(), user.getUsername(), user.getAccessLevel()};
            DefaultTableModel accountsTable = (DefaultTableModel) usersList.getModel();            
            accountsTable.addRow(userData);
        }
    }
    
    //Financial Report
    
    private String getCashBalance(String ref){
        float cash = 0f;
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            
            if(acc.getAccountName().equals("Cash")){
                if(new AccountingDBController().getAccountGroupType(acc.getAGID()).equals("Debit")){
                   
                    cash += Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref));
                }  
            }         
        }      
        return decimalFormat.format(cash);
    }
    
    private String getLiabilities(String ref){
        float balance = 0f;
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            if(new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Liability")){                 
                 balance += Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref));
            }         
        }      
        return decimalFormat.format(balance);
    }
    
    private String getRevenue(String ref){
        float balance = 0f;
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            if(new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Revenue")){                 
                 balance += Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref));
            }         
        }      
        return decimalFormat.format(balance);
    }
    
    private void showIncomeStatement(String ref){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        incomeStatement.setModel(new DefaultTableModel(null, new String[]{"Account Title", "Amount"}));
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
    
        DefaultTableModel incomeStatementTable = (DefaultTableModel) incomeStatement.getModel(); 
        
        float revenue = 0f, expenses = 0f, net;
        
        //Revenue
        String[] head1 = {"REVENUE", ""};
                incomeStatementTable.addRow(head1);
       
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            
            if(new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Revenue")){
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref)))};
                incomeStatementTable.addRow(accountData);
                revenue += Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref));
            }
        }
        
         String[] total1 = {"Total Revenue", " ₱ "  + decimalFormat.format(revenue)};
                incomeStatementTable.addRow(total1);
        
        //Expenses
        String[] head2 = {"EXPENSES", ""};
                incomeStatementTable.addRow(head2);
        
        Iterator i2 = (Iterator) accounts.iterator();        
        
        while(i2.hasNext()){
            Account acc = (Account) i2.next();         
            
            if (new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Expenses")
                    && Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref)) > 0) {
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref)))};
                incomeStatementTable.addRow(accountData);
                expenses += Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref));
            }  else if (acc.getAccountName().equals("Tax") && new AccountingUI().getTax("1") >= 0){
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(new AccountingUI().getTax(ref))};
                incomeStatementTable.addRow(accountData);
                expenses += new AccountingUI().getTax(ref);
            }        
        }
        
        String[] total2 = {"Total Expenses", " ₱ "  + decimalFormat.format(expenses)};
                incomeStatementTable.addRow(total2);      
       
        //Calculation of Net Income        
        net = revenue - expenses;        
                
        String[] netIncome = {"NET INCOME  ( ".concat(new AccountingUI().incomeResult(net) + " )"), " ₱ "  + decimalFormat.format(net)};
                incomeStatementTable.addRow(netIncome);        
    }
   
    
    //Inventory Report
    
    private String getCurrentInventoryCost(String ref){
        float cost = 0f;
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            
            if(acc.getAccountName().equals("Inventory")){
                if(new AccountingDBController().getAccountGroupType(acc.getAGID()).equals("Debit")){
                   
                    cost += Float.parseFloat(new AccountingUI().getAccountBalance(acc.getAccountName(), ref));
                }  
            }         
        }      
        return decimalFormat.format(cost);
    }
    
    private int getNumOfPendingOrders(){           
       return new InventoryDBController().getSortedOrders("orders.status", "Pending", "String").size();
    }
    
    private void showSustain(){
        sustainList.setModel(new DefaultTableModel(null, new String[]{"SID", "Item Name", "Supplier", "Remaining Stocks"}));
        DefaultTableModel sustainTable = (DefaultTableModel) sustainList.getModel();
        ArrayList<Item> stocks = new InventoryDBController().getItems();
        
        Iterator i = (Iterator) stocks.iterator();
        
        while(i.hasNext()){
            Item item = (Item) i.next();
            
            if(item.getStatus().equalsIgnoreCase("Sustain")){
                String[] itemData = {item.getSID(), item.getItemName(), 
                    new InventoryDBController().getSupplierName(item.getSupplier()), item.getQuantity()};

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
    
    //Accounts Table Pop Up
    private void showPopUp(MouseEvent e){       
        listPopUp.show(this, e.getXOnScreen() - 30, e.getYOnScreen());
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        listPopUp = new javax.swing.JPopupMenu();
        edit = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        cancel = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        manageAccountsP = new javax.swing.JPanel();
        manageAccountsL = new javax.swing.JLabel();
        financialReportsP = new javax.swing.JPanel();
        financialReportsL = new javax.swing.JLabel();
        inventoryReportsP = new javax.swing.JPanel();
        inventoryReportsL = new javax.swing.JLabel();
        gotoInventoryP = new javax.swing.JPanel();
        gotoInventoryL = new javax.swing.JLabel();
        gotoAccountingP = new javax.swing.JPanel();
        gotoAccountingL = new javax.swing.JLabel();
        settingsP = new javax.swing.JPanel();
        settingsL = new javax.swing.JLabel();
        logoutP = new javax.swing.JPanel();
        logoutL = new javax.swing.JLabel();
        exitP = new javax.swing.JPanel();
        exitL = new javax.swing.JLabel();
        usernameDisplay = new javax.swing.JLabel();
        user = new javax.swing.JLabel();
        adminMenuPane = new javax.swing.JTabbedPane();
        financialReports = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        incomeStatement = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cashBalance = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sales = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        liabilities = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        inventoryReports = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        currInventoryCost = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ordersToReceive = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        itemsTabbedPane = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        sustainList = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        outOfStockList = new javax.swing.JTable();
        manageAccountsPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersList = new javax.swing.JTable();
        searchUsersAccount = new javax.swing.JLabel();
        usersValue = new javax.swing.JTextField();
        searchUsersBy = new javax.swing.JComboBox<>();
        sortUsersBy = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        addUser = new javax.swing.JButton();
        refreshList = new javax.swing.JLabel();

        listPopUp.setAlignmentY(0.0F);
        listPopUp.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        listPopUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listPopUpMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listPopUpMouseReleased(evt);
            }
        });

        edit.setText("Edit");
        edit.setAlignmentX(0.0F);
        edit.setAlignmentY(0.0F);
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        listPopUp.add(edit);

        delete.setText("Delete");
        delete.setAlignmentX(0.0F);
        delete.setAlignmentY(0.0F);
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        listPopUp.add(delete);

        cancel.setText("Cancel");
        cancel.setAlignmentX(0.0F);
        cancel.setAlignmentY(0.0F);
        listPopUp.add(cancel);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Administrator");
        setPreferredSize(new java.awt.Dimension(1200, 750));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 0));
        jLabel5.setText("AIMS");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel8.setFont(new java.awt.Font("Miriam Fixed", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("|");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel10.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 102));
        jLabel10.setText("JAR Creatives");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        jLabel11.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(153, 153, 153));
        jLabel11.setText("Administrator");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 60));

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        manageAccountsP.setBackground(new java.awt.Color(102, 102, 102));
        manageAccountsP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        manageAccountsP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageAccountsPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                manageAccountsPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                manageAccountsPMouseExited(evt);
            }
        });

        manageAccountsL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        manageAccountsL.setForeground(new java.awt.Color(255, 255, 255));
        manageAccountsL.setText("Manage Accounts");
        manageAccountsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageAccountsLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout manageAccountsPLayout = new javax.swing.GroupLayout(manageAccountsP);
        manageAccountsP.setLayout(manageAccountsPLayout);
        manageAccountsPLayout.setHorizontalGroup(
            manageAccountsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageAccountsPLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(manageAccountsL)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        manageAccountsPLayout.setVerticalGroup(
            manageAccountsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageAccountsPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(manageAccountsL)
                .addContainerGap())
        );

        financialReportsP.setBackground(new java.awt.Color(102, 102, 102));
        financialReportsP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        financialReportsP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                financialReportsPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                financialReportsPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                financialReportsPMouseExited(evt);
            }
        });

        financialReportsL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        financialReportsL.setForeground(new java.awt.Color(255, 255, 255));
        financialReportsL.setText("Financial Reports");
        financialReportsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                financialReportsLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout financialReportsPLayout = new javax.swing.GroupLayout(financialReportsP);
        financialReportsP.setLayout(financialReportsPLayout);
        financialReportsPLayout.setHorizontalGroup(
            financialReportsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(financialReportsPLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(financialReportsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        financialReportsPLayout.setVerticalGroup(
            financialReportsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialReportsPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(financialReportsL)
                .addContainerGap())
        );

        inventoryReportsP.setBackground(new java.awt.Color(102, 102, 102));
        inventoryReportsP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        inventoryReportsP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventoryReportsPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                inventoryReportsPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                inventoryReportsPMouseExited(evt);
            }
        });

        inventoryReportsL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        inventoryReportsL.setForeground(new java.awt.Color(255, 255, 255));
        inventoryReportsL.setText("Inventory Status");
        inventoryReportsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventoryReportsLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout inventoryReportsPLayout = new javax.swing.GroupLayout(inventoryReportsP);
        inventoryReportsP.setLayout(inventoryReportsPLayout);
        inventoryReportsPLayout.setHorizontalGroup(
            inventoryReportsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryReportsPLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(inventoryReportsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inventoryReportsPLayout.setVerticalGroup(
            inventoryReportsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryReportsPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(inventoryReportsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gotoInventoryP.setBackground(new java.awt.Color(102, 102, 102));
        gotoInventoryP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gotoInventoryP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gotoInventoryPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gotoInventoryPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                gotoInventoryPMouseExited(evt);
            }
        });

        gotoInventoryL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        gotoInventoryL.setForeground(new java.awt.Color(255, 255, 255));
        gotoInventoryL.setText("Inventory");
        gotoInventoryL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gotoInventoryLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout gotoInventoryPLayout = new javax.swing.GroupLayout(gotoInventoryP);
        gotoInventoryP.setLayout(gotoInventoryPLayout);
        gotoInventoryPLayout.setHorizontalGroup(
            gotoInventoryPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gotoInventoryPLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(gotoInventoryL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gotoInventoryPLayout.setVerticalGroup(
            gotoInventoryPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gotoInventoryPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gotoInventoryL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        gotoAccountingP.setBackground(new java.awt.Color(102, 102, 102));
        gotoAccountingP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        gotoAccountingP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gotoAccountingPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gotoAccountingPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                gotoAccountingPMouseExited(evt);
            }
        });

        gotoAccountingL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        gotoAccountingL.setForeground(new java.awt.Color(255, 255, 255));
        gotoAccountingL.setText("Accounting");
        gotoAccountingL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                gotoAccountingLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout gotoAccountingPLayout = new javax.swing.GroupLayout(gotoAccountingP);
        gotoAccountingP.setLayout(gotoAccountingPLayout);
        gotoAccountingPLayout.setHorizontalGroup(
            gotoAccountingPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gotoAccountingPLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(gotoAccountingL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gotoAccountingPLayout.setVerticalGroup(
            gotoAccountingPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gotoAccountingPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(gotoAccountingL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsP.setBackground(new java.awt.Color(102, 102, 102));
        settingsP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        settingsP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsPMouseExited(evt);
            }
        });

        settingsL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        settingsL.setForeground(new java.awt.Color(255, 255, 255));
        settingsL.setText("Settings");
        settingsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout settingsPLayout = new javax.swing.GroupLayout(settingsP);
        settingsP.setLayout(settingsPLayout);
        settingsPLayout.setHorizontalGroup(
            settingsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(settingsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsPLayout.setVerticalGroup(
            settingsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(settingsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logoutP.setBackground(new java.awt.Color(102, 102, 102));
        logoutP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logoutP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutPMouseExited(evt);
            }
        });

        logoutL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        logoutL.setForeground(new java.awt.Color(255, 255, 255));
        logoutL.setText("Logout");
        logoutL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout logoutPLayout = new javax.swing.GroupLayout(logoutP);
        logoutP.setLayout(logoutPLayout);
        logoutPLayout.setHorizontalGroup(
            logoutPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(logoutL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        logoutPLayout.setVerticalGroup(
            logoutPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        exitP.setBackground(new java.awt.Color(102, 102, 102));
        exitP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        exitP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitPMouseExited(evt);
            }
        });

        exitL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        exitL.setForeground(new java.awt.Color(255, 255, 255));
        exitL.setText("Exit");
        exitL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitLMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout exitPLayout = new javax.swing.GroupLayout(exitP);
        exitP.setLayout(exitPLayout);
        exitPLayout.setHorizontalGroup(
            exitPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitPLayout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(exitL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        exitPLayout.setVerticalGroup(
            exitPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exitPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exitL)
                .addContainerGap())
        );

        usernameDisplay.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        usernameDisplay.setForeground(new java.awt.Color(255, 255, 255));
        usernameDisplay.setText("Username");

        user.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(manageAccountsP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(financialReportsP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(inventoryReportsP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(gotoInventoryP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(gotoAccountingP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(settingsP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logoutP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(exitP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameDisplay)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(usernameDisplay)
                        .addGap(12, 12, 12)))
                .addGap(30, 30, 30)
                .addComponent(financialReportsP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventoryReportsP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gotoInventoryP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gotoAccountingP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageAccountsP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(settingsP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 250, 700));

        incomeStatement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Title", "Amount"
            }
        ));
        incomeStatement.setEnabled(false);
        jScrollPane2.setViewportView(incomeStatement);

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setForeground(new java.awt.Color(0, 51, 102));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cash Balance:  ₱ ");

        cashBalance.setForeground(new java.awt.Color(255, 255, 255));
        cashBalance.setText("0.00");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Liabilities:  ₱ ");

        sales.setForeground(new java.awt.Color(255, 255, 255));
        sales.setText("0.00");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Sales:  ₱ ");

        liabilities.setForeground(new java.awt.Color(255, 255, 255));
        liabilities.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashBalance)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sales)
                .addGap(233, 233, 233)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(liabilities)
                .addGap(121, 121, 121))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cashBalance)
                    .addComponent(jLabel4)
                    .addComponent(sales)
                    .addComponent(jLabel6)
                    .addComponent(liabilities))
                .addGap(20, 20, 20))
        );

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Income Statement");

        javax.swing.GroupLayout financialReportsLayout = new javax.swing.GroupLayout(financialReports);
        financialReports.setLayout(financialReportsLayout);
        financialReportsLayout.setHorizontalGroup(
            financialReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialReportsLayout.createSequentialGroup()
                .addContainerGap(157, Short.MAX_VALUE)
                .addGroup(financialReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialReportsLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 647, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(151, 151, 151))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialReportsLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(395, 395, 395))))
        );
        financialReportsLayout.setVerticalGroup(
            financialReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, financialReportsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        adminMenuPane.addTab("Financial Reports", financialReports);

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("Current Inventory Cost: ₱");

        currInventoryCost.setText("0.00");

        jLabel7.setText("Orders To Receive:");

        ordersToReceive.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currInventoryCost)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ordersToReceive)
                .addGap(38, 38, 38))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(currInventoryCost)
                    .addComponent(jLabel7)
                    .addComponent(ordersToReceive))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Items");

        sustainList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SID", "Item Name", "Supplier", "Remaning Stocks"
            }
        ));
        jScrollPane3.setViewportView(sustainList);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
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
        jScrollPane4.setViewportView(outOfStockList);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 874, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );

        itemsTabbedPane.addTab("Out of Stock", jPanel7);

        javax.swing.GroupLayout inventoryReportsLayout = new javax.swing.GroupLayout(inventoryReports);
        inventoryReports.setLayout(inventoryReportsLayout);
        inventoryReportsLayout.setHorizontalGroup(
            inventoryReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryReportsLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(inventoryReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemsTabbedPane))
                .addGap(29, 29, 29))
        );
        inventoryReportsLayout.setVerticalGroup(
            inventoryReportsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryReportsLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        adminMenuPane.addTab("Inventory Reports", inventoryReports);

        usersList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UID", "Name", "Username", "Access Level", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                usersListMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                usersListMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(usersList);

        searchUsersAccount.setPreferredSize(new java.awt.Dimension(20, 20));
        searchUsersAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchUsersAccountMouseClicked(evt);
            }
        });

        searchUsersBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "UID", "Name", "Username" }));

        sortUsersBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "UID", "Name", "Username", "Access Level" }));

        jLabel16.setText("Sort by");

        addUser.setText("Add User");
        addUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addUserMouseClicked(evt);
            }
        });

        refreshList.setPreferredSize(new java.awt.Dimension(20, 20));
        refreshList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshListMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout manageAccountsPanelLayout = new javax.swing.GroupLayout(manageAccountsPanel);
        manageAccountsPanel.setLayout(manageAccountsPanelLayout);
        manageAccountsPanelLayout.setHorizontalGroup(
            manageAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageAccountsPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(manageAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(manageAccountsPanelLayout.createSequentialGroup()
                        .addComponent(searchUsersAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(usersValue, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchUsersBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortUsersBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addUser)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        manageAccountsPanelLayout.setVerticalGroup(
            manageAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageAccountsPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(manageAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchUsersAccount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(manageAccountsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(usersValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchUsersBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sortUsersBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16)
                        .addComponent(addUser)
                        .addComponent(refreshList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        adminMenuPane.addTab("Manage Accounts", manageAccountsPanel);

        getContentPane().add(adminMenuPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 960, 740));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPMouseClicked
        setVisible(false);
        new LoginUI().setVisible(true);
    }//GEN-LAST:event_logoutPMouseClicked

    private void logoutLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLMouseClicked
        setVisible(false);
        new LoginUI().setVisible(true);
    }//GEN-LAST:event_logoutLMouseClicked

    private void exitPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitPMouseClicked

    private void exitLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitLMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitLMouseClicked

    private void addUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addUserMouseClicked
        new AddUser().setVisible(true);
    }//GEN-LAST:event_addUserMouseClicked

    private void usersListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersListMousePressed
        if(evt.isPopupTrigger()){
            showPopUp(evt);
        }
    }//GEN-LAST:event_usersListMousePressed

    private void usersListMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersListMouseReleased
        if(evt.isPopupTrigger()){
            showPopUp(evt);
        }
    }//GEN-LAST:event_usersListMouseReleased

    private void listPopUpMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listPopUpMousePressed
        if(evt.isPopupTrigger()){
            showPopUp(evt);
        }
    }//GEN-LAST:event_listPopUpMousePressed

    private void listPopUpMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listPopUpMouseReleased
        if(evt.isPopupTrigger()){
            showPopUp(evt);
        }
    }//GEN-LAST:event_listPopUpMouseReleased

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        if(usersList.getSelectedRow() != -1){
            new UpdateUser().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select at least one user to edit!");
        }       
    }//GEN-LAST:event_editActionPerformed

    private void financialReportsPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_financialReportsPMouseClicked
        manageAccountsP.setBackground(new Color(102, 102, 102));
        inventoryReportsP.setBackground(new Color(102, 102, 102));
        adminMenuPane.setSelectedIndex(0);
        financialReportsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_financialReportsPMouseClicked

    private void financialReportsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_financialReportsLMouseClicked
        manageAccountsP.setBackground(new Color(102, 102, 102));
        inventoryReportsP.setBackground(new Color(102, 102, 102));
        adminMenuPane.setSelectedIndex(0);
        financialReportsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_financialReportsLMouseClicked

    private void inventoryReportsPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryReportsPMouseClicked
        currInventoryCost.setText(getCurrentInventoryCost(String.valueOf(new AccountingDBController().getSetCurrentJournal())));
        ordersToReceive.setText(String.valueOf(getNumOfPendingOrders()));
        showSustain();
        showOutOfStocks();
        adminMenuPane.setSelectedIndex(1);
        manageAccountsP.setBackground(new Color(102, 102, 102));
        financialReportsP.setBackground(new Color(102, 102, 102));
        inventoryReportsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_inventoryReportsPMouseClicked

    private void inventoryReportsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryReportsLMouseClicked
        currInventoryCost.setText(getCurrentInventoryCost(String.valueOf(new AccountingDBController().getSetCurrentJournal())));
        ordersToReceive.setText(String.valueOf(getNumOfPendingOrders()));
        showSustain();
        showOutOfStocks();
        adminMenuPane.setSelectedIndex(1);
        manageAccountsP.setBackground(new Color(102, 102, 102));
        financialReportsP.setBackground(new Color(102, 102, 102));
        inventoryReportsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_inventoryReportsLMouseClicked

    private void gotoInventoryPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoInventoryPMouseClicked
        dispose();
        InventoryUI inventoryUI = new InventoryUI();
        inventoryUI.usernameDisplay.setText(usernameDisplay.getText());
        inventoryUI.logoutLButton.setText("Back");
        inventoryUI.ui.setValue("Admin");
        inventoryUI.setVisible(true);
    }//GEN-LAST:event_gotoInventoryPMouseClicked

    private void gotoInventoryLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoInventoryLMouseClicked
        dispose();
        InventoryUI inventoryUI = new InventoryUI();
        inventoryUI.usernameDisplay.setText(usernameDisplay.getText());
        inventoryUI.logoutLButton.setText("Back");
        inventoryUI.ui.setValue("Admin");
        inventoryUI.setVisible(true);
    }//GEN-LAST:event_gotoInventoryLMouseClicked

    private void gotoAccountingPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoAccountingPMouseClicked
        dispose();
        AccountingUI accountingUI = new AccountingUI();
        accountingUI.usernameDisplay.setText(usernameDisplay.getText());        
        accountingUI.logoutL.setText("Back");
        accountingUI.inventoryL.setVisible(false);
        accountingUI.inventoryP.setVisible(false);
        accountingUI.settingsP.setVisible(false);
        accountingUI.settingsL.setVisible(false);
        accountingUI.addEntry.setEnabled(false);
        accountingUI.addAccount.setEnabled(false);
        accountingUI.addAccountsGroup.setEnabled(false);
        accountingUI.deleteEntry.setEnabled(false);
        accountingUI.setVisible(true);        
    }//GEN-LAST:event_gotoAccountingPMouseClicked

    private void gotoAccountingLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoAccountingLMouseClicked
        dispose();
        AccountingUI accountingUI = new AccountingUI();
        accountingUI.usernameDisplay.setText(usernameDisplay.getText());
        accountingUI.logoutL.setText("Back");
        accountingUI.inventoryL.setVisible(false);
        accountingUI.inventoryP.setVisible(false);
        accountingUI.settingsP.setVisible(false);
        accountingUI.settingsL.setVisible(false);
        accountingUI.addEntry.setEnabled(false);
        accountingUI.addAccount.setEnabled(false);
        accountingUI.addAccountsGroup.setEnabled(false);
        accountingUI.deleteEntry.setEnabled(false);
        accountingUI.setVisible(true); 
    }//GEN-LAST:event_gotoAccountingLMouseClicked

    private void manageAccountsPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageAccountsPMouseClicked
        showUsersList();
        adminMenuPane.setSelectedIndex(2);
        inventoryReportsP.setBackground(new Color(102, 102, 102));
        financialReportsP.setBackground(new Color(102, 102, 102));
        manageAccountsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_manageAccountsPMouseClicked

    private void manageAccountsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageAccountsLMouseClicked
        showUsersList();
        adminMenuPane.setSelectedIndex(2);
        inventoryReportsP.setBackground(new Color(102, 102, 102));
        financialReportsP.setBackground(new Color(102, 102, 102));
        manageAccountsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_manageAccountsLMouseClicked

    private void settingsPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsPMouseClicked
        new Settings().setVisible(true);
    }//GEN-LAST:event_settingsPMouseClicked

    private void settingsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLMouseClicked
        new Settings().setVisible(true);
    }//GEN-LAST:event_settingsLMouseClicked

    private void refreshListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshListMouseClicked
        String by = "";
        
        switch(String.valueOf(sortUsersBy.getSelectedItem())){
            case "UID":
                by = "users_account.user_ID";
                break;
                
            case "Name":
                by = "users_account.name";
                break;
                
            case "Username":
                by = "users_account.username";
                break;
                
            case "Access Level":
                by = "users_account.access_level";
                break;               
        }
        
        sortUsersList(by);
    }//GEN-LAST:event_refreshListMouseClicked

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if(usersList.getSelectedRow() != -1){
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure that you want to delete this user?");
            
            if(n == 0){
                new UserDBController().delete(usersList.getModel().getValueAt(usersList.getSelectedRow(), 0).toString());
                showUsersList();
            }           
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select at least one user to be deleted!");
        }  
    }//GEN-LAST:event_deleteActionPerformed

    private void searchUsersAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchUsersAccountMouseClicked
        String value = "", by = "";
        
        switch(String.valueOf(searchUsersBy.getSelectedItem())){
            case "UID":
                value = "'%" + usersValue.getText() + "%'";
                by = "users_account.user_ID";
                break;
                
            case "Name":
                value = "'%" + usersValue.getText() + "%'";
                by = "users_account.name";
                break;
                
            case "Username":
                value = "'%" + usersValue.getText() + "%'";
                by = "users_account.username";
                break;                                        
        }
        
        usersList.setModel(new DefaultTableModel(null, new String[]{"UID","Name", "Username", "Access Level"}));
        ArrayList<User> userList = new UserDBController().searchUsers(value, by);
        
        Iterator i = (Iterator) userList.iterator();
        
        while(i.hasNext()){
            User user = (User)i.next();
            String[] userData = {user.getId(), user.getName(), user.getUsername(), user.getAccessLevel()};
            DefaultTableModel accountsTable = (DefaultTableModel) usersList.getModel();            
            accountsTable.addRow(userData);
        }
    }//GEN-LAST:event_searchUsersAccountMouseClicked

    private void financialReportsPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_financialReportsPMouseEntered
        financialReportsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_financialReportsPMouseEntered

    private void financialReportsPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_financialReportsPMouseExited
        if(adminMenuPane.getSelectedIndex() != 0){financialReportsP.setBackground(new Color(102, 102, 102));}
    }//GEN-LAST:event_financialReportsPMouseExited

    private void inventoryReportsPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryReportsPMouseEntered
        inventoryReportsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_inventoryReportsPMouseEntered

    private void inventoryReportsPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryReportsPMouseExited
        if(adminMenuPane.getSelectedIndex() != 1){inventoryReportsP.setBackground(new Color(102, 102, 102));}
    }//GEN-LAST:event_inventoryReportsPMouseExited

    private void gotoInventoryPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoInventoryPMouseEntered
        gotoInventoryP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_gotoInventoryPMouseEntered

    private void gotoInventoryPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoInventoryPMouseExited
        gotoInventoryP.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_gotoInventoryPMouseExited

    private void gotoAccountingPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoAccountingPMouseEntered
        gotoAccountingP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_gotoAccountingPMouseEntered

    private void gotoAccountingPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_gotoAccountingPMouseExited
        gotoAccountingP.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_gotoAccountingPMouseExited

    private void manageAccountsPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageAccountsPMouseEntered
        manageAccountsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_manageAccountsPMouseEntered

    private void manageAccountsPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageAccountsPMouseExited
        if(adminMenuPane.getSelectedIndex() != 2){manageAccountsP.setBackground(new Color(102, 102, 102));}
    }//GEN-LAST:event_manageAccountsPMouseExited

    private void settingsPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsPMouseEntered
        settingsP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_settingsPMouseEntered

    private void settingsPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsPMouseExited
        settingsP.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_settingsPMouseExited

    private void logoutPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPMouseEntered
        logoutP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_logoutPMouseEntered

    private void logoutPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPMouseExited
        logoutP.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_logoutPMouseExited

    private void exitPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPMouseEntered
        exitP.setBackground(new Color(153, 153, 153));
    }//GEN-LAST:event_exitPMouseEntered

    private void exitPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPMouseExited
        exitP.setBackground(new Color(102, 102, 102));
    }//GEN-LAST:event_exitPMouseExited

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
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addUser;
    private javax.swing.JTabbedPane adminMenuPane;
    private javax.swing.JMenuItem cancel;
    private javax.swing.JLabel cashBalance;
    private javax.swing.JLabel currInventoryCost;
    private javax.swing.JMenuItem delete;
    private javax.swing.JMenuItem edit;
    private javax.swing.JLabel exitL;
    private javax.swing.JPanel exitP;
    private javax.swing.JPanel financialReports;
    private javax.swing.JLabel financialReportsL;
    private javax.swing.JPanel financialReportsP;
    private javax.swing.JLabel gotoAccountingL;
    private javax.swing.JPanel gotoAccountingP;
    private javax.swing.JLabel gotoInventoryL;
    private javax.swing.JPanel gotoInventoryP;
    private javax.swing.JTable incomeStatement;
    private javax.swing.JPanel inventoryReports;
    private javax.swing.JLabel inventoryReportsL;
    private javax.swing.JPanel inventoryReportsP;
    private javax.swing.JTabbedPane itemsTabbedPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel liabilities;
    private javax.swing.JPopupMenu listPopUp;
    private javax.swing.JLabel logoutL;
    private javax.swing.JPanel logoutP;
    private javax.swing.JLabel manageAccountsL;
    private javax.swing.JPanel manageAccountsP;
    private javax.swing.JPanel manageAccountsPanel;
    private javax.swing.JLabel ordersToReceive;
    private javax.swing.JTable outOfStockList;
    private javax.swing.JLabel refreshList;
    private javax.swing.JLabel sales;
    private javax.swing.JLabel searchUsersAccount;
    private javax.swing.JComboBox<String> searchUsersBy;
    private javax.swing.JLabel settingsL;
    private javax.swing.JPanel settingsP;
    private javax.swing.JComboBox<String> sortUsersBy;
    private javax.swing.JTable sustainList;
    private javax.swing.JLabel user;
    public javax.swing.JLabel usernameDisplay;
    private javax.swing.JTable usersList;
    private javax.swing.JTextField usersValue;
    // End of variables declaration//GEN-END:variables
}
