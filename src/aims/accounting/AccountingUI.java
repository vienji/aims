/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.accounting;

import aims.accounting.journal.AddEntry;
import aims.classes.Account;
import aims.databasemanager.AccountingDBController;
import aims.classes.AccountsGroup;
import aims.classes.Entries;
import aims.accounting.accounts.AddAccount;
import aims.accounting.accountsgroup.AddAccountsGroup;
import aims.LoginUI;
import aims.accounting.journal.ViewEntry;
import aims.admin.AdminUI;
import aims.classes.EntryAccount;
import aims.classes.LedgerAccount;
import aims.images.ImageManipulator;
import aims.inventory.InventoryUI;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;

/**
 *
 * @author Vienji
 */
public class AccountingUI extends javax.swing.JFrame {
    /**
     * Creates new form AccountingUI
     */
    public AccountingUI() {
        initComponents();             
        showJournalEntries(String.valueOf(new AccountingDBController().getCurrentJournal()));
        initGeneralLedger();       
        refNum.setText(String.valueOf(new AccountingDBController().getCurrentJournal()));
        
        if(new AccountingDBController().getCurrentJournal() == 1){
            nextJournal.setEnabled(false);
            backJournal.setEnabled(false);
        } else {
            nextJournal.setEnabled(false);
        }
        journalP.setBackground(new Color(0, 204, 102));  
        
        ImageIcon icon = new ImageIcon("src\\aims\\images\\aims-logo.png");
        setIconImage(icon.getImage());
        
        ImageManipulator icons = new ImageManipulator();
        
        icons.setIcons("src\\aims\\images\\user.png", user);
        //Journal
        icons.setIcons("src\\aims\\images\\search.png", searchEntry);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshJournalEntriesList);
        //WorkSheet
        icons.setIcons("src\\aims\\images\\refresh.png", refreshWorkSheet);
        //Accounts
        icons.setIcons("src\\aims\\images\\search.png", searchAccount);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshAccountsList);
        //Acounts Group
        icons.setIcons("src\\aims\\images\\search.png", searchAccountGroup);
        icons.setIcons("src\\aims\\images\\refresh.png", refreshGroupAccountsList);
    }
    
    private int journalNav(String where){
        int nav = Integer.parseInt(refNum.getText());
        int cur = new AccountingDBController().getCurrentJournal();
        
        if(where.equals("Next")){
            backJournal.setEnabled(true);
            if(nav < cur){
                nav++;
                if(nav == cur){
                    nextJournal.setEnabled(false);
                }
            }                      
        } else {
            nextJournal.setEnabled(true);
            if(nav > 1){
                nav--;
                if(nav == 1){
                    backJournal.setEnabled(false);
                }
            }          
        }
        
        return nav;
    }
    
    private void initGeneralLedger(){
        ArrayList<Account> accountsList = new AccountingDBController().getAccountsList();
        accountsList.forEach((e) -> {
            if(!e.getAccountName().equals("Tax")){}ledgerAccountsComBox.addItem(e.getAccountName());
        });
    }
    
    private void showAccountEntries(String account, String refID){
        accountDetails.setModel(new DefaultTableModel(null, new String[]{"JEID", "Date", "Description", "Debit", "Credit", "Balance"}));
        ArrayList<LedgerAccount> accountEntries = new AccountingDBController().getLedgerAccountDetails(account, refID);
        
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        float balance = 0f;
        
        DefaultTableModel details = (DefaultTableModel) accountDetails.getModel();
        
        Iterator i = (Iterator) accountEntries.iterator();
        
        while(i.hasNext()){
            LedgerAccount ledgerAccount = (LedgerAccount) i.next();
          
            if(new AccountingDBController().isJEDeleted(ledgerAccount.getJEID()) == false){
                if(new AccountingDBController().getAccountAction(account).equals("Debit")){
                    if(ledgerAccount.getAction().equals("Debit")){
                        balance += Float.parseFloat(ledgerAccount.getAmount());
                    } else {
                        balance -= Float.parseFloat(ledgerAccount.getAmount());
                    }
                } else {
                    if(ledgerAccount.getAction().equals("Debit")){
                        balance -= Float.parseFloat(ledgerAccount.getAmount());
                    } else {
                        balance += Float.parseFloat(ledgerAccount.getAmount());
                    }
                }

                if(ledgerAccount.getAction().equals("Debit")){
                    String[] accountEntryData = {ledgerAccount.getJEID(), ledgerAccount.getDate(), ledgerAccount.getDescription()
                    , " ₱ " + decimalFormat.format(Float.parseFloat(ledgerAccount.getAmount())), "", " ₱ " + decimalFormat.format(balance)};
                    details.addRow(accountEntryData);
                } else {
                    String[] accountEntryData = {ledgerAccount.getJEID(), ledgerAccount.getDate(), ledgerAccount.getDescription()
                    , "", " ₱ " + decimalFormat.format(Float.parseFloat(ledgerAccount.getAmount())), " ₱ " + decimalFormat.format(balance)};
                    details.addRow(accountEntryData);
                }
            }           
        }
    }
    
    private void navSwitch(String userCheck){
        if(userCheck.equals("Logout")){
            dispose();
            new LoginUI().setVisible(true);
        } else {
            dispose();
            AdminUI adminUI = new AdminUI();
            adminUI.usernameDisplay.setText(usernameDisplay.getText());
            adminUI.setVisible(true);
        }
    }
    
    private void showAccountsGroupList(){
        accountsGroupList.setModel(new DefaultTableModel(null, new String[]{"Date Created", "Group Name", "Description", "Type"}));
        ArrayList<AccountsGroup> accountsGroup = new AccountingDBController().getAccountsGroupList();
        
        Iterator i = (Iterator) accountsGroup.iterator();
        
        while(i.hasNext()){
            AccountsGroup accountGroup = (AccountsGroup)i.next();
            String[] accountsGroupData = {accountGroup.getDate(), accountGroup.getGroupName(), accountGroup.getDescription(), accountGroup.getType()};
            DefaultTableModel accountsGroupTable = (DefaultTableModel) accountsGroupList.getModel();
            accountsGroupTable.addRow(accountsGroupData);
        }
    }
    
    private void sortAccountsGroupList(String by){
        accountsGroupList.setModel(new DefaultTableModel(null, new String[]{"Date Created", "Group Name", "Description", "Type"}));
        ArrayList<AccountsGroup> accountsGroup = new AccountingDBController().sortAccountsGroup(by);
        
        Iterator i = (Iterator) accountsGroup.iterator();
        
        while(i.hasNext()){
            AccountsGroup accountGroup = (AccountsGroup)i.next();
            String[] accountsGroupData = {accountGroup.getDate(), accountGroup.getGroupName(), accountGroup.getDescription(), accountGroup.getType()};
            DefaultTableModel accountsGroupTable = (DefaultTableModel) accountsGroupList.getModel();
            accountsGroupTable.addRow(accountsGroupData);
        }
    }
    
    private void showAccountsList(){
        accountsList.setModel(new DefaultTableModel(null, new String[]{"Date Created", "Account Name", "Description", "Group"}));
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account account = (Account)i.next();
            String[] accountData = {account.getDate(),account.getAccountName(), account.getDescription(),
            new AccountingDBController().getAccountsGroupName(account.getAGID())};
            DefaultTableModel accountsTable = (DefaultTableModel) accountsList.getModel();
            accountsTable.addRow(accountData);
        }
    }
    
    private void sortAccountsList(String by){
        accountsList.setModel(new DefaultTableModel(null, new String[]{"Date Created", "Account Name", "Description", "Group"}));
        ArrayList<Account> accounts = new AccountingDBController().sortAccount(by);
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account account = (Account)i.next();
            String[] accountData = {account.getDate(),account.getAccountName(), account.getDescription(),
            new AccountingDBController().getAccountsGroupName(account.getAGID())};
            DefaultTableModel accountsTable = (DefaultTableModel) accountsList.getModel();
            accountsTable.addRow(accountData);
        }
    }
    
    private void showJournalEntries(String refID){
        journalEntriesList.setModel(new DefaultTableModel(null, new String[]{"JEID", "Date", "Description", "Amount", "Encoder", "Deleted"}));
        ArrayList<Entries> entries = new AccountingDBController().getJournalEntries(refID);
        
        Iterator i = (Iterator) entries.iterator();
        
        while(i.hasNext()){
            Entries entry = (Entries)i.next();
            String[] entryData = { entry.getJEID() ,entry.getDate(), entry.getDescription(), " ₱ " + sumEntryAmount(entry.getJEID()), 
                entry.getEncoder(), entry.getDeleted()};
            DefaultTableModel journalEntriesTable = (DefaultTableModel) journalEntriesList.getModel();
            journalEntriesTable.addRow(entryData);
        }
    }
    
    private String sumEntryAmount(String JEID){       
        return new AccountingDBController().getEntryAmount(JEID);
    }
    
    public String getAccountBalance(String account, String ref){
        float balance = 0f;
        
        ArrayList<LedgerAccount> accountEntries = new AccountingDBController().getLedgerAccountDetails(account, ref);
        
        Iterator i = (Iterator) accountEntries.iterator();
              
        while(i.hasNext()){
            LedgerAccount ledgerAccount = (LedgerAccount) i.next();
            
            if(new AccountingDBController().isJEDeleted(ledgerAccount.getJEID()) == false){
                if(new AccountingDBController().getAccountAction(account).equals("Debit")){
                    if(ledgerAccount.getAction().equals("Debit")){
                        balance += Float.parseFloat(ledgerAccount.getAmount());
                    } else {
                        balance -= Float.parseFloat(ledgerAccount.getAmount());
                    }
                } else {
                    if(ledgerAccount.getAction().equals("Debit")){
                        balance -= Float.parseFloat(ledgerAccount.getAmount());
                    } else {
                        balance += Float.parseFloat(ledgerAccount.getAmount());
                    }
                }
            }          
        }
        
        return String.valueOf(balance);
    }
    
    private void showTrialBalance(String ref){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        trialBalanceTable.setModel(new DefaultTableModel(null, new String[]{"Account Title", "Debit", "Credit"}));
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        DefaultTableModel balanceSheet = (DefaultTableModel) trialBalanceTable.getModel();
        
        Iterator i = (Iterator) accounts.iterator();
        
        float debit = 0f, credit = 0f;
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            
            if(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)) > 0){
                if(new AccountingDBController().getAccountGroupType(acc.getAGID()).equals("Debit")){
                    String[] accountData = {acc.getAccountName(), " ₱ " + decimalFormat.format(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref))) ," "};
                    balanceSheet.addRow(accountData);
                    debit += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
                } else {
                    String[] accountData = {acc.getAccountName(), " ", " ₱ " + decimalFormat.format(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)))};
                    balanceSheet.addRow(accountData);
                    credit += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
                }   
            }             
        }
        
        String[] accountData = {"Total", " ₱ " + decimalFormat.format(debit), " ₱ " + decimalFormat.format(credit)};
        balanceSheet.addRow(accountData);
    }
        
    private void showGrossIncomeWithTax(String ref){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        grossIncomeWithTaxTable.setModel(new DefaultTableModel(null, new String[]{"Account Title", "Amount"}));
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        DefaultTableModel grossIncomeWithTax = (DefaultTableModel) grossIncomeWithTaxTable.getModel();           
        
        float revenue = 0f, expenses = 0f, grossIncome, tax;

        //Revenue
        String[] head1 = {"REVENUE", ""};
                grossIncomeWithTax.addRow(head1);
       
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            
            if(new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Revenue")){
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)))};
                grossIncomeWithTax.addRow(accountData);
                revenue += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
            }
        }
        
        String[] total1 = {"Total Revenue", " ₱ "  + decimalFormat.format(revenue)};
                grossIncomeWithTax.addRow(total1);
        
        //Expenses
        String[] head2 = {"EXPENSES", ""};
                grossIncomeWithTax.addRow(head2);
        
        Iterator i2 = (Iterator) accounts.iterator();        
        
        while(i2.hasNext()){
            Account acc = (Account) i2.next();         
            
            if (new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Expenses")
                    && !acc.getAccountName().equals("Tax") && !acc.getAccountName().equals("Salary") 
                    && !acc.getAccountName().equals("Electricity Bill")) {
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)))};
                grossIncomeWithTax.addRow(accountData);
                expenses += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
            }          
        }
        
        String[] total2 = {"Total Expenses", " ₱ "  + decimalFormat.format(expenses)};
                grossIncomeWithTax.addRow(total2);
        
        //Calculation of Gross Income and Tax        
        grossIncome = revenue - expenses;
        tax = grossIncome * 0.03f;
        
        String[] grossIncomeRow = {"Gross Income", " ₱ "  + decimalFormat.format(grossIncome)};
        grossIncomeWithTax.addRow(grossIncomeRow);
        if(tax >= 0){
            taxAmount.setText(" ₱ "  + decimalFormat.format(tax));
        }            
    }
    
    private void showIncomeStatement(String ref){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        incomeStatementTable.setModel(new DefaultTableModel(null, new String[]{"Account Title", "Amount"}));
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        DefaultTableModel incomeStatement = (DefaultTableModel) incomeStatementTable.getModel(); 
        
        float revenue = 0f, expenses = 0f, net;
        
        //Revenue
        String[] head1 = {"REVENUE", ""};
                incomeStatement.addRow(head1);
       
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            
            if(new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Revenue")){
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)))};
                incomeStatement.addRow(accountData);
                revenue += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
            }
        }
        
        String[] total1 = {"Total Revenue", " ₱ "  + decimalFormat.format(revenue)};
                incomeStatement.addRow(total1);
        
        //Expenses
        String[] head2 = {"EXPENSES", ""};
                incomeStatement.addRow(head2);
        
        Iterator i2 = (Iterator) accounts.iterator();        
        
        while(i2.hasNext()){
            Account acc = (Account) i2.next();         
            
            if (new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Expenses")
                    && Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)) > 0) {
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(Float.parseFloat(getAccountBalance(acc.getAccountName(), ref)))};
                incomeStatement.addRow(accountData);
                expenses += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
            }  else if (acc.getAccountName().equals("Tax") && getTax(ref) >= 0){
                String[] accountData = {acc.getAccountName(), " ₱ "  + decimalFormat.format(getTax(ref))};
                incomeStatement.addRow(accountData);
                expenses += getTax(ref);
            }        
        }
        
        String[] total2 = {"Total Expenses", " ₱ "  + decimalFormat.format(expenses)};
                incomeStatement.addRow(total2); 
        
        //Calculation of Net Income        
        net = revenue - expenses;        
                
        String[] netIncome = {"NET INCOME  ( ".concat(incomeResult(net) + " )"), " ₱ "  + decimalFormat.format(net)};
                incomeStatement.addRow(netIncome);        
    }
    
    public String incomeResult(float net){
        return net >= 0 ? "Gain" : "Loss";
    }
    
    public float getTax(String ref){
        float revenue = 0f, expenses = 0f, tax, grossIncome;
        
        ArrayList<Account> accounts = new AccountingDBController().getAccountsList();
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account acc = (Account) i.next();
            if(new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Revenue")){               
                revenue += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
            }
        }
        
        Iterator i2 = (Iterator) accounts.iterator();        
        
        while(i2.hasNext()){
            Account acc = (Account) i2.next();         
            if (new AccountingDBController().getAccountsGroupName(acc.getAGID()).equals("Expenses")
                    && !acc.getAccountName().equals("Tax") && !acc.getAccountName().equals("Salary")) {              
                expenses += Float.parseFloat(getAccountBalance(acc.getAccountName(), ref));
            }          
        }
        
        grossIncome = revenue - expenses;
        tax = grossIncome * 0.03f;
   
        return  tax;
    }
    
    private boolean checkNumber(String number){       
        try{
            Float.parseFloat(number);
            return false;
        } catch (NumberFormatException e){
            return true;
        }      
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        journalPopUp = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        usernameDisplay = new javax.swing.JLabel();
        journalP = new javax.swing.JPanel();
        journalL = new javax.swing.JLabel();
        ledgerP = new javax.swing.JPanel();
        ledgerL = new javax.swing.JLabel();
        workSheetP = new javax.swing.JPanel();
        workSheetL = new javax.swing.JLabel();
        accountsP = new javax.swing.JPanel();
        accountsL = new javax.swing.JLabel();
        exitP = new javax.swing.JPanel();
        exitL = new javax.swing.JLabel();
        logoutP = new javax.swing.JPanel();
        logoutL = new javax.swing.JLabel();
        settingsP = new javax.swing.JPanel();
        settingsL = new javax.swing.JLabel();
        accountsGroupP = new javax.swing.JPanel();
        accountsGroupL = new javax.swing.JLabel();
        inventoryP = new javax.swing.JPanel();
        inventoryL = new javax.swing.JLabel();
        user = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        refNum = new javax.swing.JLabel();
        nextJournal = new javax.swing.JButton();
        backJournal = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        accountingTab = new javax.swing.JTabbedPane();
        journal = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        journalEntriesList = new javax.swing.JTable();
        searchEntry = new javax.swing.JLabel();
        entryValue = new javax.swing.JTextField();
        searchEntryBy = new javax.swing.JComboBox<>();
        refreshJournalEntriesList = new javax.swing.JLabel();
        addEntry = new javax.swing.JButton();
        viewEntry = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        deleteEntry = new javax.swing.JButton();
        ledger = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        accountDetails = new javax.swing.JTable();
        ledgerAccountsComBox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        workSheet = new javax.swing.JPanel();
        workSheetTab = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        trialBalanceTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        grossIncomeWithTaxTable = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        taxAmount = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        incomeStatementTable = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        refreshWorkSheet = new javax.swing.JLabel();
        accounts = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        accountsList = new javax.swing.JTable();
        searchAccount = new javax.swing.JLabel();
        accountValue = new javax.swing.JTextField();
        searchAccountBy = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        sortAccountBy = new javax.swing.JComboBox<>();
        addAccount = new javax.swing.JButton();
        refreshAccountsList = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        accountsGroup = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        accountsGroupList = new javax.swing.JTable();
        searchAccountGroup = new javax.swing.JLabel();
        accountGroupValue = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        sortAccountGroupBy = new javax.swing.JComboBox<>();
        refreshGroupAccountsList = new javax.swing.JLabel();
        addAccountsGroup = new javax.swing.JButton();
        searchAccountGroupBy = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        settings = new javax.swing.JPanel();
        addNewJournal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        currentJournalValue = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        saveCurrentJournal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1216, 750));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 102));

        usernameDisplay.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        usernameDisplay.setText("Username");

        journalP.setBackground(new java.awt.Color(0, 153, 102));
        journalP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                journalPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                journalPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                journalPMouseExited(evt);
            }
        });

        journalL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        journalL.setText("Journal");
        journalL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                journalLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                journalLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                journalLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout journalPLayout = new javax.swing.GroupLayout(journalP);
        journalP.setLayout(journalPLayout);
        journalPLayout.setHorizontalGroup(
            journalPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(journalPLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(journalL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        journalPLayout.setVerticalGroup(
            journalPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(journalPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(journalL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ledgerP.setBackground(new java.awt.Color(0, 153, 102));
        ledgerP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ledgerPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ledgerPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ledgerPMouseExited(evt);
            }
        });

        ledgerL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        ledgerL.setText("Ledger");
        ledgerL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ledgerLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ledgerLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ledgerLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout ledgerPLayout = new javax.swing.GroupLayout(ledgerP);
        ledgerP.setLayout(ledgerPLayout);
        ledgerPLayout.setHorizontalGroup(
            ledgerPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ledgerPLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(ledgerL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ledgerPLayout.setVerticalGroup(
            ledgerPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ledgerPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ledgerL)
                .addContainerGap())
        );

        workSheetP.setBackground(new java.awt.Color(0, 153, 102));
        workSheetP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                workSheetPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                workSheetPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                workSheetPMouseExited(evt);
            }
        });

        workSheetL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        workSheetL.setText("Work Sheet");
        workSheetL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                workSheetLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                workSheetLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                workSheetLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout workSheetPLayout = new javax.swing.GroupLayout(workSheetP);
        workSheetP.setLayout(workSheetPLayout);
        workSheetPLayout.setHorizontalGroup(
            workSheetPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workSheetPLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(workSheetL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        workSheetPLayout.setVerticalGroup(
            workSheetPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workSheetPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(workSheetL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        accountsP.setBackground(new java.awt.Color(0, 153, 102));
        accountsP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountsPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accountsPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accountsPMouseExited(evt);
            }
        });

        accountsL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        accountsL.setText("Accounts");
        accountsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountsLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accountsLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accountsLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout accountsPLayout = new javax.swing.GroupLayout(accountsP);
        accountsP.setLayout(accountsPLayout);
        accountsPLayout.setHorizontalGroup(
            accountsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsPLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(accountsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        accountsPLayout.setVerticalGroup(
            accountsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accountsL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        exitP.setBackground(new java.awt.Color(0, 153, 102));
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
        exitL.setText("Exit");
        exitL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout exitPLayout = new javax.swing.GroupLayout(exitP);
        exitP.setLayout(exitPLayout);
        exitPLayout.setHorizontalGroup(
            exitPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitPLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(exitL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        exitPLayout.setVerticalGroup(
            exitPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(exitPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exitL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        logoutP.setBackground(new java.awt.Color(0, 153, 102));
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
        logoutL.setText("Logout");
        logoutL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout logoutPLayout = new javax.swing.GroupLayout(logoutP);
        logoutP.setLayout(logoutPLayout);
        logoutPLayout.setHorizontalGroup(
            logoutPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logoutPLayout.createSequentialGroup()
                .addGap(57, 57, 57)
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

        settingsP.setBackground(new java.awt.Color(0, 153, 102));
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
        settingsL.setText("Settings");
        settingsL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout settingsPLayout = new javax.swing.GroupLayout(settingsP);
        settingsP.setLayout(settingsPLayout);
        settingsPLayout.setHorizontalGroup(
            settingsPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPLayout.createSequentialGroup()
                .addGap(55, 55, 55)
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

        accountsGroupP.setBackground(new java.awt.Color(0, 153, 102));
        accountsGroupP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountsGroupPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accountsGroupPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accountsGroupPMouseExited(evt);
            }
        });

        accountsGroupL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        accountsGroupL.setText("Accounts Group");
        accountsGroupL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountsGroupLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                accountsGroupLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                accountsGroupLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout accountsGroupPLayout = new javax.swing.GroupLayout(accountsGroupP);
        accountsGroupP.setLayout(accountsGroupPLayout);
        accountsGroupPLayout.setHorizontalGroup(
            accountsGroupPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsGroupPLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(accountsGroupL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        accountsGroupPLayout.setVerticalGroup(
            accountsGroupPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsGroupPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(accountsGroupL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        inventoryP.setBackground(new java.awt.Color(0, 153, 102));
        inventoryP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventoryPMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                inventoryPMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                inventoryPMouseExited(evt);
            }
        });

        inventoryL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        inventoryL.setText("Inventory");
        inventoryL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                inventoryLMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                inventoryLMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                inventoryLMouseExited(evt);
            }
        });

        javax.swing.GroupLayout inventoryPLayout = new javax.swing.GroupLayout(inventoryP);
        inventoryP.setLayout(inventoryPLayout);
        inventoryPLayout.setHorizontalGroup(
            inventoryPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inventoryPLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(inventoryL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inventoryPLayout.setVerticalGroup(
            inventoryPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inventoryPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(inventoryL)
                .addContainerGap())
        );

        user.setPreferredSize(new java.awt.Dimension(50, 50));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameDisplay)
                .addContainerGap(47, Short.MAX_VALUE))
            .addComponent(journalP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ledgerP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(workSheetP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(accountsP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(exitP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(logoutP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(settingsP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(accountsGroupP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(inventoryP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(usernameDisplay)))
                .addGap(26, 26, 26)
                .addComponent(journalP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ledgerP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(workSheetP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountsP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountsGroupP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inventoryP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addComponent(settingsP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 210, 700));

        jPanel2.setBackground(new java.awt.Color(0, 102, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Journal  No.");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(967, 20, -1, -1));

        refNum.setForeground(new java.awt.Color(255, 255, 255));
        refNum.setText("0");
        jPanel2.add(refNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 20, -1, -1));

        nextJournal.setText("Next");
        nextJournal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextJournalActionPerformed(evt);
            }
        });
        jPanel2.add(nextJournal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1128, 15, -1, -1));

        backJournal.setText("Back");
        backJournal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backJournalActionPerformed(evt);
            }
        });
        jPanel2.add(backJournal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1059, 15, -1, -1));

        jLabel8.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 255, 0));
        jLabel8.setText("AIMS");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jLabel14.setFont(new java.awt.Font("Miriam Fixed", 1, 36)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 204, 204));
        jLabel14.setText("|");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, -1, -1));

        jLabel15.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 204, 0));
        jLabel15.setText("JAR Creatives");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, -1, -1));

        jLabel16.setFont(new java.awt.Font("Miriam Fixed", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 153, 0));
        jLabel16.setText("Accounting");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 20, -1, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 60));

        journalEntriesList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JEID", "Date", "Description", "Amount", "Encoder", "Deleted"
            }
        ));
        jScrollPane1.setViewportView(journalEntriesList);

        searchEntry.setPreferredSize(new java.awt.Dimension(25, 25));
        searchEntry.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchEntryMouseClicked(evt);
            }
        });

        searchEntryBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "JEID", "Date", "Description", "Encoder" }));

        refreshJournalEntriesList.setPreferredSize(new java.awt.Dimension(25, 25));
        refreshJournalEntriesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshJournalEntriesListMouseClicked(evt);
            }
        });

        addEntry.setText("Add Entry");
        addEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryActionPerformed(evt);
            }
        });

        viewEntry.setText("View ");
        viewEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewEntryActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setText("General Journal");

        deleteEntry.setText("Delete");
        deleteEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteEntryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout journalLayout = new javax.swing.GroupLayout(journal);
        journal.setLayout(journalLayout);
        journalLayout.setHorizontalGroup(
            journalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(journalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(journalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(journalLayout.createSequentialGroup()
                        .addComponent(searchEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entryValue, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchEntryBy, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshJournalEntriesList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addGroup(journalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(journalLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(0, 405, Short.MAX_VALUE))
                            .addGroup(journalLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(viewEntry)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteEntry)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(addEntry)))))
                .addContainerGap())
        );
        journalLayout.setVerticalGroup(
            journalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(journalLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel13)
                .addGap(34, 34, 34)
                .addGroup(journalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(entryValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchEntryBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshJournalEntriesList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addEntry)
                    .addComponent(viewEntry)
                    .addComponent(deleteEntry))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        accountingTab.addTab("Journal", journal);

        jLabel2.setText("Account:");

        accountDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "JEID", "Date", "Description", "Debit", "Credit", "Balance"
            }
        ));
        jScrollPane4.setViewportView(accountDetails);

        ledgerAccountsComBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledgerAccountsComBoxActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setText("General Ledger");

        javax.swing.GroupLayout ledgerLayout = new javax.swing.GroupLayout(ledger);
        ledger.setLayout(ledgerLayout);
        ledgerLayout.setHorizontalGroup(
            ledgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ledgerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ledgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(ledgerLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ledgerAccountsComBox, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(ledgerLayout.createSequentialGroup()
                .addGap(439, 439, 439)
                .addComponent(jLabel5)
                .addGap(0, 470, Short.MAX_VALUE))
        );
        ledgerLayout.setVerticalGroup(
            ledgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ledgerLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(ledgerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(ledgerAccountsComBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        accountingTab.addTab("Ledger", ledger);

        trialBalanceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Title", "Debit", "Credit"
            }
        ));
        jScrollPane5.setViewportView(trialBalanceTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );

        workSheetTab.addTab("Trial Balance", jPanel4);

        grossIncomeWithTaxTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Title", "Amount"
            }
        ));
        jScrollPane7.setViewportView(grossIncomeWithTaxTable);

        jLabel18.setText("Gross Income");

        jLabel19.setText("Tax:");

        taxAmount.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(124, 887, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(taxAmount)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(taxAmount))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        workSheetTab.addTab("Gross Income with Tax", jPanel7);

        incomeStatementTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Title", "Amount"
            }
        ));
        jScrollPane6.setViewportView(incomeStatementTable);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addContainerGap())
        );

        workSheetTab.addTab("Income Statement", jPanel5);

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Work Sheet");

        refreshWorkSheet.setPreferredSize(new java.awt.Dimension(25, 25));
        refreshWorkSheet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshWorkSheetMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout workSheetLayout = new javax.swing.GroupLayout(workSheet);
        workSheet.setLayout(workSheetLayout);
        workSheetLayout.setHorizontalGroup(
            workSheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workSheetLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(workSheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(workSheetTab)
                    .addGroup(workSheetLayout.createSequentialGroup()
                        .addGroup(workSheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(workSheetLayout.createSequentialGroup()
                                .addGap(410, 410, 410)
                                .addComponent(jLabel7))
                            .addComponent(refreshWorkSheet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        workSheetLayout.setVerticalGroup(
            workSheetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workSheetLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(refreshWorkSheet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(27, 27, 27)
                .addComponent(workSheetTab, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        accountingTab.addTab("Work Sheet", workSheet);

        accountsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date Created", "Account Name", "Description", "Group"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(accountsList);

        searchAccount.setPreferredSize(new java.awt.Dimension(25, 25));
        searchAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchAccountMouseClicked(evt);
            }
        });

        searchAccountBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "Account Name", "Description", "Group" }));

        jLabel6.setText("Sort by");

        sortAccountBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "Account Name", "Group" }));

        addAccount.setText("Add Account");
        addAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountActionPerformed(evt);
            }
        });

        refreshAccountsList.setPreferredSize(new java.awt.Dimension(25, 25));
        refreshAccountsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshAccountsListMouseClicked(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setText("Accounts");

        javax.swing.GroupLayout accountsLayout = new javax.swing.GroupLayout(accounts);
        accounts.setLayout(accountsLayout);
        accountsLayout.setHorizontalGroup(
            accountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(accountsLayout.createSequentialGroup()
                        .addComponent(searchAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accountValue, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchAccountBy, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addGroup(accountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(accountsLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabel11)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(accountsLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sortAccountBy, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refreshAccountsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 287, Short.MAX_VALUE)
                                .addComponent(addAccount)))))
                .addContainerGap())
        );
        accountsLayout.setVerticalGroup(
            accountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel11)
                .addGap(34, 34, 34)
                .addGroup(accountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchAccountBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(sortAccountBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addAccount)
                    .addComponent(refreshAccountsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        accountingTab.addTab("Accounts", accounts);

        accountsGroupList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date Created", "Group Name", "Description", "Type"
            }
        ));
        jScrollPane3.setViewportView(accountsGroupList);

        searchAccountGroup.setPreferredSize(new java.awt.Dimension(25, 25));
        searchAccountGroup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchAccountGroupMouseClicked(evt);
            }
        });

        jLabel4.setText("Sort by");

        sortAccountGroupBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "Group Name", "Type" }));

        refreshGroupAccountsList.setPreferredSize(new java.awt.Dimension(25, 25));
        refreshGroupAccountsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshGroupAccountsListMouseClicked(evt);
            }
        });

        addAccountsGroup.setText("Add Group");
        addAccountsGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAccountsGroupActionPerformed(evt);
            }
        });

        searchAccountGroupBy.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Date", "Group Name", "Description" }));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setText("Accounts Group");

        javax.swing.GroupLayout accountsGroupLayout = new javax.swing.GroupLayout(accountsGroup);
        accountsGroup.setLayout(accountsGroupLayout);
        accountsGroupLayout.setHorizontalGroup(
            accountsGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accountsGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(accountsGroupLayout.createSequentialGroup()
                        .addComponent(searchAccountGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accountGroupValue, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchAccountGroupBy, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(accountsGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(accountsGroupLayout.createSequentialGroup()
                                .addComponent(sortAccountGroupBy, 0, 109, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(refreshGroupAccountsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(305, 305, 305)
                                .addComponent(addAccountsGroup))
                            .addGroup(accountsGroupLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        accountsGroupLayout.setVerticalGroup(
            accountsGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountsGroupLayout.createSequentialGroup()
                .addGroup(accountsGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(accountsGroupLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel12)
                        .addGap(37, 37, 37)
                        .addGroup(accountsGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchAccountGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accountGroupValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(sortAccountGroupBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(refreshGroupAccountsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchAccountGroupBy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountsGroupLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(addAccountsGroup)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        accountingTab.addTab("Accounts Group", accountsGroup);

        addNewJournal.setForeground(new java.awt.Color(51, 51, 255));
        addNewJournal.setText("Add New Journal");
        addNewJournal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addNewJournalMouseClicked(evt);
            }
        });

        jLabel9.setText("Set current journal number to be use: ");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Settings");

        saveCurrentJournal.setText("OK");
        saveCurrentJournal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCurrentJournalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout settingsLayout = new javax.swing.GroupLayout(settings);
        settings.setLayout(settingsLayout);
        settingsLayout.setHorizontalGroup(
            settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addNewJournal)
                    .addComponent(jLabel10)
                    .addGroup(settingsLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentJournalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveCurrentJournal)))
                .addContainerGap(554, Short.MAX_VALUE))
        );
        settingsLayout.setVerticalGroup(
            settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addGroup(settingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(currentJournalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveCurrentJournal))
                .addGap(18, 18, 18)
                .addComponent(addNewJournal)
                .addContainerGap(587, Short.MAX_VALUE))
        );

        accountingTab.addTab("Settings", settings);

        getContentPane().add(accountingTab, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 990, 730));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryActionPerformed
        AddEntry addEntry = new AddEntry();
        addEntry.usernameDisplay.setText(usernameDisplay.getText());
        addEntry.refID.setValue(refNum.getText());
        addEntry.setVisible(true);
    }//GEN-LAST:event_addEntryActionPerformed

    private void journalPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalPMouseClicked
        showJournalEntries(refNum.getText());
        accountingTab.setSelectedIndex(0);
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_journalPMouseClicked

    private void journalLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalLMouseClicked
        showJournalEntries(refNum.getText());
        accountingTab.setSelectedIndex(0);
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_journalLMouseClicked

    private void ledgerPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledgerPMouseClicked
        showAccountEntries((String) ledgerAccountsComBox.getSelectedItem(), String.valueOf(new AccountingDBController().getCurrentJournal()));   
        accountingTab.setSelectedIndex(1);
        journalP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_ledgerPMouseClicked

    private void ledgerLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledgerLMouseClicked
        showAccountEntries((String) ledgerAccountsComBox.getSelectedItem(), String.valueOf(new AccountingDBController().getCurrentJournal()));   
        accountingTab.setSelectedIndex(1);
        journalP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_ledgerLMouseClicked

    private void workSheetLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSheetLMouseClicked
        showTrialBalance(refNum.getText());
        showGrossIncomeWithTax(refNum.getText());
        showIncomeStatement(refNum.getText());
        accountingTab.setSelectedIndex(2);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_workSheetLMouseClicked

    private void workSheetPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSheetPMouseClicked
        showTrialBalance(refNum.getText());
        showGrossIncomeWithTax(refNum.getText());
        showIncomeStatement(refNum.getText());
        accountingTab.setSelectedIndex(2);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_workSheetPMouseClicked

    private void accountsPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsPMouseClicked
        showAccountsList();
        accountingTab.setSelectedIndex(3);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_accountsPMouseClicked

    private void accountsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsLMouseClicked
        showAccountsList();
        accountingTab.setSelectedIndex(3);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_accountsLMouseClicked

    private void logoutLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLMouseClicked
        navSwitch(logoutL.getText());
    }//GEN-LAST:event_logoutLMouseClicked

    private void logoutPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPMouseClicked
        navSwitch(logoutL.getText());
    }//GEN-LAST:event_logoutPMouseClicked

    private void accountsGroupLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsGroupLMouseClicked
        showAccountsGroupList();
        accountingTab.setSelectedIndex(4);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_accountsGroupLMouseClicked

    private void accountsGroupPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsGroupPMouseClicked
        showAccountsGroupList();
        accountingTab.setSelectedIndex(4);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        settingsP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_accountsGroupPMouseClicked

    private void addAccountsGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountsGroupActionPerformed
        new AddAccountsGroup().setVisible(true);
    }//GEN-LAST:event_addAccountsGroupActionPerformed

    private void addAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAccountActionPerformed
        new AddAccount().setVisible(true);
    }//GEN-LAST:event_addAccountActionPerformed

    private void refreshGroupAccountsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshGroupAccountsListMouseClicked
        String by = "";
        
        switch(String.valueOf(sortAccountGroupBy.getSelectedItem())){
            case "Date":
                by = "account_group.date";
                break;
                
            case "Group Name":
                by = "account_group.group_name";
                break;
            case "Type":
                by = "account_group.type"; 
                break;
        }
        
        sortAccountsGroupList(by);
    }//GEN-LAST:event_refreshGroupAccountsListMouseClicked

    private void exitPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitPMouseClicked

    private void exitLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitLMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitLMouseClicked

    private void refreshJournalEntriesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshJournalEntriesListMouseClicked
        showJournalEntries(String.valueOf(refNum.getText()));
    }//GEN-LAST:event_refreshJournalEntriesListMouseClicked

    private void refreshAccountsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshAccountsListMouseClicked
        String by = "";
        
        switch(String.valueOf(sortAccountBy.getSelectedItem())){
            case "Date":
                by = "accounts.date";
                break;
                
            case "Account Name":
                by = "accounts.account_name";
                break;
            case "Group":
                by = "account_group.group_name"; 
                break;
        }        
        
        sortAccountsList(by);
    }//GEN-LAST:event_refreshAccountsListMouseClicked

    private void viewEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewEntryActionPerformed
        
        Float debitTotal = 0f, creditTotal = 0f;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
              
        ArrayList<EntryAccount> entryDetails = new AccountingDBController().getEntryDetails(journalEntriesList.getValueAt(journalEntriesList.getSelectedRow(), 0).toString());
        ArrayList<EntryAccount> sortedEntryDetails = new ArrayList<>();
        
        ViewEntry viewEntry = new ViewEntry();
        viewEntry.encoderDisplay.setText(journalEntriesList.getValueAt(journalEntriesList.getSelectedRow(), 4).toString());
        viewEntry.dateDisplay.setText(journalEntriesList.getValueAt(journalEntriesList.getSelectedRow(), 1).toString());
        viewEntry.description.setText(journalEntriesList.getValueAt(journalEntriesList.getSelectedRow(), 2).toString());
        viewEntry.jeidDisplay.setText(journalEntriesList.getValueAt(journalEntriesList.getSelectedRow(), 0).toString());
        
        entryDetails.forEach((e) -> {if(e.getAction().equals("Debit")){sortedEntryDetails.add(e);}});        
        entryDetails.forEach((e) -> {if(e.getAction().equals("Credit")){sortedEntryDetails.add(e);}});         
        
        DefaultTableModel addAccountsList = (DefaultTableModel) viewEntry.breakdownList.getModel();
        Iterator i = (Iterator) sortedEntryDetails.iterator();
               
        while(i.hasNext()){
            EntryAccount entryAccount = (EntryAccount) i.next();
            
            if(entryAccount.getAction().equals("Debit")){
                String[] entryData ={entryAccount.getAccountName(), " ₱ " + decimalFormat.format(Float.parseFloat(entryAccount.getAmount())), ""};
                addAccountsList.addRow(entryData);
                debitTotal += Float.parseFloat(entryAccount.getAmount());
            } else {
                String[] entryData ={entryAccount.getAccountName(), "", " ₱ " + decimalFormat.format(Float.parseFloat(entryAccount.getAmount()))};
                addAccountsList.addRow(entryData);
                creditTotal += Float.parseFloat(entryAccount.getAmount());
            }
            
        }
        
        String[] entryData ={"Total", " ₱ " + decimalFormat.format(debitTotal), " ₱ " + decimalFormat.format(creditTotal)};
        addAccountsList.addRow(entryData);
        
        viewEntry.setVisible(true);
    }//GEN-LAST:event_viewEntryActionPerformed

    private void deleteEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteEntryActionPerformed
        new AccountingDBController().deleteEntry(
                String.valueOf(journalEntriesList.getValueAt(journalEntriesList.getSelectedRow(), 0)), 
                usernameDisplay.getText(), "Yes");
        showJournalEntries(String.valueOf(refNum.getText()));
    }//GEN-LAST:event_deleteEntryActionPerformed

    private void addNewJournalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNewJournalMouseClicked
        new AccountingDBController().addNewJournal();
        refNum.setText(String.valueOf(new AccountingDBController().getCurrentJournal()));
        backJournal.setEnabled(true);
        nextJournal.setEnabled(false);
    }//GEN-LAST:event_addNewJournalMouseClicked

    private void backJournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backJournalActionPerformed
        refNum.setText(String.valueOf(journalNav("Back")));
        
        int n = accountingTab.getSelectedIndex();
        
        switch(n){
            case 0: 
                showJournalEntries(String.valueOf(refNum.getText()));
                break;
            case 2:
                int n1 = workSheetTab.getSelectedIndex();
                
                switch(n1){
                    case 0:
                        showTrialBalance(refNum.getText());
                        break;
                    case 1:
                        showGrossIncomeWithTax(refNum.getText());
                        break;
                    case 2:
                        showIncomeStatement(refNum.getText());
                        break; 
                }
                break;
        }
    }//GEN-LAST:event_backJournalActionPerformed

    private void nextJournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextJournalActionPerformed
        refNum.setText(String.valueOf(journalNav("Next")));
        
        int n = accountingTab.getSelectedIndex();
        
        switch(n){
            case 0: 
                showJournalEntries(String.valueOf(refNum.getText()));
                break;
            case 2:
                int n1 = workSheetTab.getSelectedIndex();
                
                switch(n1){
                    case 0:
                        showTrialBalance(refNum.getText());
                        break;
                    case 1:
                        showGrossIncomeWithTax(refNum.getText());
                        break;
                    case 2:
                        showIncomeStatement(refNum.getText());
                        break; 
                }
                break;
        }       
    }//GEN-LAST:event_nextJournalActionPerformed

    private void refreshWorkSheetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshWorkSheetMouseClicked
        int n = accountingTab.getSelectedIndex();
        
        switch(n){
            case 0: 
                showJournalEntries(String.valueOf(refNum.getText()));
                break;
            case 2:
                int n1 = workSheetTab.getSelectedIndex();
                
                switch(n1){
                    case 0:
                        showTrialBalance(refNum.getText());
                        break;
                    case 1:
                        showGrossIncomeWithTax(refNum.getText());
                        break;
                    case 2:
                        showIncomeStatement(refNum.getText());
                        break; 
                }
                break;
        } 
    }//GEN-LAST:event_refreshWorkSheetMouseClicked

    private void inventoryPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryPMouseClicked
        InventoryUI inventoryUI = new InventoryUI();
        
        inventoryUI.usernameDisplay.setText(usernameDisplay.getText());
        inventoryUI.logoutLButton.setText("Back");
        inventoryUI.ui.setValue("Accounting");
        inventoryUI.setVisible(true);
        dispose();
    }//GEN-LAST:event_inventoryPMouseClicked

    private void inventoryLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryLMouseClicked
        InventoryUI inventoryUI = new InventoryUI();
        
        inventoryUI.usernameDisplay.setText(usernameDisplay.getText());
        inventoryUI.logoutLButton.setText("Back");
        inventoryUI.ui.setValue("Accounting");
        inventoryUI.setVisible(true);
        dispose();
    }//GEN-LAST:event_inventoryLMouseClicked

    private void settingsPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsPMouseClicked
        currentJournalValue.setText(String.valueOf(new AccountingDBController().getSetCurrentJournal()));
        accountingTab.setSelectedIndex(5);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_settingsPMouseClicked

    private void settingsLMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLMouseClicked
        currentJournalValue.setText(String.valueOf(new AccountingDBController().getSetCurrentJournal()));
        accountingTab.setSelectedIndex(5);
        journalP.setBackground(new Color(0,153,102));
        ledgerP.setBackground(new Color(0,153,102));
        workSheetP.setBackground(new Color(0,153,102));
        accountsP.setBackground(new Color(0,153,102));
        accountsGroupP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_settingsLMouseClicked

    private void saveCurrentJournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCurrentJournalActionPerformed
        AccountingDBController adbc = new AccountingDBController();
        
        if(checkNumber(currentJournalValue.getText())){
            JOptionPane.showMessageDialog(rootPane, "Please enter a valid journal number!");
        } else if (adbc.getCurrentJournal() < Integer.parseInt(currentJournalValue.getText())){
            JOptionPane.showMessageDialog(rootPane, "Journal number does not exist!");
        } else {
            adbc.setCurrentJournal(currentJournalValue.getText());
            JOptionPane.showMessageDialog(rootPane, "Changes saved!");
        }             
    }//GEN-LAST:event_saveCurrentJournalActionPerformed

    private void searchEntryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchEntryMouseClicked
        String value = "", by = "";
        
        switch(String.valueOf(searchEntryBy.getSelectedItem())){
            case "JEID":
                value = "'%" + entryValue.getText() + "%'";
                by = "journal_entry.JEID";
                break;
                
            case "Date":
                value = "'%" + entryValue.getText() + "%'";
                by = "journal_entry.date";
                break;
                
            case "Description":
                value = "'%" + entryValue.getText() + "%'";
                by = "journal_entry.description";
                break;
                
            case "Encoder":
                value = "'%" + entryValue.getText() + "%'";
                by = "users_account.name";
                break;               
        }
        
        journalEntriesList.setModel(new DefaultTableModel(null, new String[]{"JEID", "Date", "Description", "Amount", "Encoder", "Deleted"}));
        ArrayList<Entries> entries = new AccountingDBController().searchJournalEntries(refNum.getText(), value, by);
        
        Iterator i = (Iterator) entries.iterator();
        
        while(i.hasNext()){
            Entries entry = (Entries)i.next();
            String[] entryData = { entry.getJEID() ,entry.getDate(), entry.getDescription(), " ₱ " + sumEntryAmount(entry.getJEID()), 
                entry.getEncoder(), entry.getDeleted()};
            DefaultTableModel journalEntriesTable = (DefaultTableModel) journalEntriesList.getModel();
            journalEntriesTable.addRow(entryData);
        }
        
    }//GEN-LAST:event_searchEntryMouseClicked

    private void searchAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchAccountMouseClicked
        String value = "", by = "";
        
        switch(String.valueOf(searchAccountBy.getSelectedItem())){
            case "Date":
                value = "'%" + accountValue.getText() + "%'";
                by = "accounts.date";
                break;
                
            case "Account Name":
                value = "'%" + accountValue.getText() + "%'";
                by = "accounts.account_name";
                break;
                
            case "Description":
                value = "'%" + accountValue.getText() + "%'";
                by = "accounts.description";
                break;
                
            case "Group":
                value = "'%" + accountValue.getText() + "%'";
                by = "account_group.group_name";
                break;               
        }
        
        accountsList.setModel(new DefaultTableModel(null, new String[]{"Date Created", "Account Name", "Description", "Group"}));
        ArrayList<Account> accounts = new AccountingDBController().searchAccount(value, by);
        
        Iterator i = (Iterator) accounts.iterator();
        
        while(i.hasNext()){
            Account account = (Account)i.next();
            String[] accountData = {account.getDate(),account.getAccountName(), account.getDescription(),
            new AccountingDBController().getAccountsGroupName(account.getAGID())};
            DefaultTableModel accountsTable = (DefaultTableModel) accountsList.getModel();
            accountsTable.addRow(accountData);
        }
    }//GEN-LAST:event_searchAccountMouseClicked

    private void searchAccountGroupMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchAccountGroupMouseClicked
        String value = "", by = "";
        
        switch(String.valueOf(searchAccountGroupBy.getSelectedItem())){
            case "Date":
                value = "'%" + accountGroupValue.getText() + "%'";
                by = "account_group.date";
                break;
                
            case "Group Name":
                value = "'%" + accountGroupValue.getText() + "%'";
                by = "account_group.group_name";
                break;
                
            case "Description":
                value = "'%" + accountGroupValue.getText() + "%'";
                by = "account_group.description";
                break;                                        
        }
        
        accountsGroupList.setModel(new DefaultTableModel(null, new String[]{"Date Created", "Group Name", "Description", "Type"}));
        ArrayList<AccountsGroup> accountsGroup = new AccountingDBController().searchAccountGroup(value, by);
        
        Iterator i = (Iterator) accountsGroup.iterator();
        
        while(i.hasNext()){
            AccountsGroup accountGroup = (AccountsGroup)i.next();
            String[] accountsGroupData = {accountGroup.getDate(), accountGroup.getGroupName(), accountGroup.getDescription(), accountGroup.getType()};
            DefaultTableModel accountsGroupTable = (DefaultTableModel) accountsGroupList.getModel();
            accountsGroupTable.addRow(accountsGroupData);
        }
    }//GEN-LAST:event_searchAccountGroupMouseClicked

    private void journalPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalPMouseEntered
        journalP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_journalPMouseEntered

    private void journalPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalPMouseExited
        if(accountingTab.getSelectedIndex() != 0){journalP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_journalPMouseExited

    private void ledgerPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledgerPMouseEntered
        ledgerP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_ledgerPMouseEntered

    private void ledgerPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledgerPMouseExited
        if(accountingTab.getSelectedIndex() != 1){ledgerP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_ledgerPMouseExited

    private void workSheetPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSheetPMouseEntered
        workSheetP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_workSheetPMouseEntered

    private void workSheetPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSheetPMouseExited
        if(accountingTab.getSelectedIndex() != 2){workSheetP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_workSheetPMouseExited

    private void accountsPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsPMouseEntered
        accountsP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_accountsPMouseEntered

    private void accountsPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsPMouseExited
        if(accountingTab.getSelectedIndex() != 3){accountsP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_accountsPMouseExited

    private void accountsGroupPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsGroupPMouseEntered
        accountsGroupP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_accountsGroupPMouseEntered

    private void accountsGroupPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsGroupPMouseExited
        if(accountingTab.getSelectedIndex() != 4){accountsGroupP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_accountsGroupPMouseExited

    private void inventoryPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryPMouseEntered
       inventoryP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_inventoryPMouseEntered

    private void inventoryPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryPMouseExited
        inventoryP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_inventoryPMouseExited

    private void settingsPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsPMouseEntered
        settingsP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_settingsPMouseEntered

    private void settingsPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsPMouseExited
        if(accountingTab.getSelectedIndex() != 5){settingsP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_settingsPMouseExited

    private void logoutPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPMouseEntered
        logoutP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_logoutPMouseEntered

    private void logoutPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutPMouseExited
        logoutP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_logoutPMouseExited

    private void exitPMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPMouseEntered
        exitP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_exitPMouseEntered

    private void exitPMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitPMouseExited
        exitP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_exitPMouseExited

    private void journalLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalLMouseEntered
        journalP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_journalLMouseEntered

    private void ledgerLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledgerLMouseEntered
        ledgerP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_ledgerLMouseEntered

    private void workSheetLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSheetLMouseEntered
        workSheetP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_workSheetLMouseEntered

    private void accountsLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsLMouseEntered
        accountsP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_accountsLMouseEntered

    private void accountsGroupLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsGroupLMouseEntered
        accountsGroupP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_accountsGroupLMouseEntered

    private void inventoryLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryLMouseEntered
        inventoryP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_inventoryLMouseEntered

    private void settingsLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLMouseEntered
        settingsP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_settingsLMouseEntered

    private void logoutLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLMouseEntered
        logoutP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_logoutLMouseEntered

    private void exitLMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitLMouseEntered
        exitP.setBackground(new Color(0, 204, 102));
    }//GEN-LAST:event_exitLMouseEntered

    private void journalLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalLMouseExited
        if(accountingTab.getSelectedIndex() != 0){journalP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_journalLMouseExited

    private void ledgerLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ledgerLMouseExited
        if(accountingTab.getSelectedIndex() != 1){ledgerP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_ledgerLMouseExited

    private void workSheetLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workSheetLMouseExited
        if(accountingTab.getSelectedIndex() != 2){workSheetP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_workSheetLMouseExited

    private void accountsLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsLMouseExited
        if(accountingTab.getSelectedIndex() != 3){accountsP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_accountsLMouseExited

    private void accountsGroupLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountsGroupLMouseExited
        if(accountingTab.getSelectedIndex() != 4){accountsGroupP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_accountsGroupLMouseExited

    private void inventoryLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_inventoryLMouseExited
        inventoryP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_inventoryLMouseExited

    private void settingsLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLMouseExited
        if(accountingTab.getSelectedIndex() != 5){settingsP.setBackground(new Color(0,153,102));}
    }//GEN-LAST:event_settingsLMouseExited

    private void logoutLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutLMouseExited
        logoutP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_logoutLMouseExited

    private void exitLMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitLMouseExited
        exitP.setBackground(new Color(0,153,102));
    }//GEN-LAST:event_exitLMouseExited

    private void ledgerAccountsComBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledgerAccountsComBoxActionPerformed
         showAccountEntries((String) ledgerAccountsComBox.getSelectedItem(), refNum.getText());
    }//GEN-LAST:event_ledgerAccountsComBoxActionPerformed

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
            java.util.logging.Logger.getLogger(AccountingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccountingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccountingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountingUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccountingUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable accountDetails;
    private javax.swing.JTextField accountGroupValue;
    private javax.swing.JTextField accountValue;
    private javax.swing.JTabbedPane accountingTab;
    private javax.swing.JPanel accounts;
    private javax.swing.JPanel accountsGroup;
    private javax.swing.JLabel accountsGroupL;
    private javax.swing.JTable accountsGroupList;
    private javax.swing.JPanel accountsGroupP;
    private javax.swing.JLabel accountsL;
    private javax.swing.JTable accountsList;
    private javax.swing.JPanel accountsP;
    public javax.swing.JButton addAccount;
    public javax.swing.JButton addAccountsGroup;
    public javax.swing.JButton addEntry;
    private javax.swing.JLabel addNewJournal;
    private javax.swing.JButton backJournal;
    private javax.swing.JTextField currentJournalValue;
    public javax.swing.JButton deleteEntry;
    private javax.swing.JTextField entryValue;
    private javax.swing.JLabel exitL;
    private javax.swing.JPanel exitP;
    private javax.swing.JTable grossIncomeWithTaxTable;
    private javax.swing.JTable incomeStatementTable;
    public javax.swing.JLabel inventoryL;
    public javax.swing.JPanel inventoryP;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JPanel journal;
    private javax.swing.JTable journalEntriesList;
    private javax.swing.JLabel journalL;
    private javax.swing.JPanel journalP;
    private javax.swing.JPopupMenu journalPopUp;
    private javax.swing.JPanel ledger;
    private javax.swing.JComboBox<String> ledgerAccountsComBox;
    private javax.swing.JLabel ledgerL;
    private javax.swing.JPanel ledgerP;
    public javax.swing.JLabel logoutL;
    private javax.swing.JPanel logoutP;
    private javax.swing.JButton nextJournal;
    private javax.swing.JLabel refNum;
    private javax.swing.JLabel refreshAccountsList;
    private javax.swing.JLabel refreshGroupAccountsList;
    private javax.swing.JLabel refreshJournalEntriesList;
    private javax.swing.JLabel refreshWorkSheet;
    private javax.swing.JButton saveCurrentJournal;
    private javax.swing.JLabel searchAccount;
    private javax.swing.JComboBox<String> searchAccountBy;
    private javax.swing.JLabel searchAccountGroup;
    private javax.swing.JComboBox<String> searchAccountGroupBy;
    private javax.swing.JLabel searchEntry;
    private javax.swing.JComboBox<String> searchEntryBy;
    private javax.swing.JPanel settings;
    public javax.swing.JLabel settingsL;
    public javax.swing.JPanel settingsP;
    private javax.swing.JComboBox<String> sortAccountBy;
    private javax.swing.JComboBox<String> sortAccountGroupBy;
    private javax.swing.JLabel taxAmount;
    private javax.swing.JTable trialBalanceTable;
    private javax.swing.JLabel user;
    public javax.swing.JLabel usernameDisplay;
    private javax.swing.JButton viewEntry;
    private javax.swing.JPanel workSheet;
    private javax.swing.JLabel workSheetL;
    private javax.swing.JPanel workSheetP;
    private javax.swing.JTabbedPane workSheetTab;
    // End of variables declaration//GEN-END:variables
}
