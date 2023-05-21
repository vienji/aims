/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.accounting.journal;

import aims.classes.Account;
import aims.classes.EntryAccount;
import aims.classes.Temp;
import aims.databasemanager.AccountingDBController;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vienji
 */
public class AddEntry extends javax.swing.JFrame {
    LocalDate date = LocalDate.now();
    /**
     * Creates new form AddAccount
     */
    public AddEntry() {
        initComponents();
        initAddEntry();    
        ImageIcon icon = new ImageIcon("src\\aims\\images\\aims-logo.png");
        setIconImage(icon.getImage());
    } 
    
    public Temp refID = new Temp();
    
    private void initAddEntry(){
        dateChooser.setDate(Date.valueOf(date));
        saveChanges.setEnabled(false);
        type.setEnabled(false);
        amountChange.setEnabled(false);
        accountComBox.setEnabled(false);
             
        ArrayList<Account> accountsList = new AccountingDBController().getAccountsList();
        
        accountsList.forEach((a) -> {
            debitedAccount.addItem(a.getAccountName());
            creditedAccount.addItem(a.getAccountName());
            accountComBox.addItem(a.getAccountName());
        });
    }
    
    ArrayList<EntryAccount> entryAccountsList = new ArrayList<>();
    
    private void viewAddedAccounts(){
        entryAccountsList = new AddEntry().sortEntriesAction(entryAccountsList);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        addedAccounts.setModel(new DefaultTableModel(null, new String[]{"Account", "Debit", "Credit"}));
        Float debitTotal = 0f, creditTotal = 0f;
        Iterator i = (Iterator) entryAccountsList.iterator();
        DefaultTableModel addAccountsList = (DefaultTableModel) addedAccounts.getModel();
        
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
    }
       
    public ArrayList<EntryAccount> sortEntriesAction(ArrayList<EntryAccount> array){
        ArrayList<EntryAccount> newArray = new ArrayList<>();
        
        array.forEach((e) -> {if(e.getAction().equals("Debit")){newArray.add(e);}});
        array.forEach((e) -> {if(e.getAction().equals("Credit")){newArray.add(e);}});
        
        return newArray;
    }
    
    private boolean checkAccounts(ArrayList<EntryAccount> array){
        ArrayList<EntryAccount> arr = array;
        int credit = 0, debit = 0;
        
        for(EntryAccount a : arr){
            if(a.getAction().equals("Debit")){
                debit++;
            } else {
               credit++;
            }
        }
        
        return credit > 0 && debit > 0;
    }
    
    private boolean checkBalanceEntry(ArrayList<EntryAccount> array){
        ArrayList<EntryAccount> arr = array;
        float credit = 0, debit = 0;
        
        for(EntryAccount a : arr){
        
            if(a.getAction().equals("Debit")){
                debit += Float.parseFloat(a.getAmount());
            } else {
                credit += Float.parseFloat(a.getAmount());
            }
        
        }
        
        return credit == debit;
    }
    
    private boolean checkNumber(String number){       
        try{
            Float.parseFloat(number);
            return false;
        } catch (NumberFormatException e){
            return true;
        }      
    }
    
    private void showPopUp(MouseEvent e){
        listPopUp.show(this, e.getXOnScreen() - 360, e.getYOnScreen() - 100);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        debitedAccount = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        debitedAmount = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        creditedAccount = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        creditedAmount = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        description = new javax.swing.JTextArea();
        addDebitedAccount = new javax.swing.JButton();
        addCreditedAccount = new javax.swing.JButton();
        add = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        usernameDisplay = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        addedAccounts = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        accountComBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        amountChange = new javax.swing.JTextField();
        saveChanges = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        type = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        done = new javax.swing.JButton();
        dateChooser = new com.toedter.calendar.JDateChooser();

        listPopUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listPopUpMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listPopUpMouseReleased(evt);
            }
        });

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        listPopUp.add(edit);

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        listPopUp.add(delete);

        cancel.setText("Cancel");
        listPopUp.add(cancel);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Date");

        jLabel2.setText("Debited to");

        jLabel3.setText("Amount");

        jLabel4.setText("Credited to");

        jLabel5.setText("Amount");

        jLabel6.setText("Add Entry");

        jLabel7.setText("Description");

        description.setColumns(20);
        description.setRows(5);
        jScrollPane1.setViewportView(description);

        addDebitedAccount.setText("Add Account");
        addDebitedAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addDebitedAccountMouseClicked(evt);
            }
        });

        addCreditedAccount.setText("Add Account");
        addCreditedAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addCreditedAccountMouseClicked(evt);
            }
        });

        add.setText("Add");
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        jLabel8.setText("Encoder:");

        usernameDisplay.setText("username");

        addedAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account", "Debit", "Credit"
            }
        ));
        addedAccounts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addedAccountsMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addedAccountsMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(addedAccounts);

        jLabel9.setText("Account");

        jLabel10.setText("Amount");

        saveChanges.setText("Save");
        saveChanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChangesActionPerformed(evt);
            }
        });

        jLabel11.setText("Type");

        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Debit", "Credit" }));

        jLabel12.setText("Edit Entry");

        done.setText("Done");
        done.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(done)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(accountComBox, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amountChange, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(type, 0, 66, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(saveChanges))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(creditedAccount, 0, 180, Short.MAX_VALUE)
                                    .addComponent(debitedAccount, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(creditedAmount)
                                            .addComponent(debitedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(addDebitedAccount)
                                            .addComponent(addCreditedAccount)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(usernameDisplay))))
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(248, 248, 248)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(usernameDisplay))
                        .addGap(23, 23, 23))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(debitedAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(debitedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addDebitedAccount))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(creditedAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(creditedAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCreditedAccount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(accountComBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(amountChange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveChanges)
                    .addComponent(jLabel11)
                    .addComponent(type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(add)
                    .addComponent(cancelButton)
                    .addComponent(clear)
                    .addComponent(done))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addDebitedAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addDebitedAccountMouseClicked
        if(debitedAmount.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Please add an amount!");
        } else if (checkNumber(debitedAmount.getText())){
            JOptionPane.showMessageDialog(rootPane, "Enter a numeric amount!");
        } else {
            EntryAccount entryAccount = new EntryAccount();

            entryAccount.setAccountName((String) debitedAccount.getSelectedItem());
            entryAccount.setAmount(debitedAmount.getText());
            entryAccount.setAction("Debit");           
            entryAccountsList.add(entryAccount);
            debitedAccount.setSelectedIndex(0);
            debitedAmount.setText("");
            viewAddedAccounts();      
        }       
    }//GEN-LAST:event_addDebitedAccountMouseClicked

    private void addCreditedAccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addCreditedAccountMouseClicked
        if(creditedAmount.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Please add an amount!");
        } else if (checkNumber(creditedAmount.getText())){
            JOptionPane.showMessageDialog(rootPane, "Enter a numeric amount!");
        } else {
            EntryAccount entryAccount = new EntryAccount();

            entryAccount.setAccountName((String) creditedAccount.getSelectedItem());
            entryAccount.setAmount(creditedAmount.getText());
            entryAccount.setAction("Credit");           
            entryAccountsList.add(entryAccount);
            creditedAccount.setSelectedIndex(0);
            creditedAmount.setText("");
            viewAddedAccounts();      
        } 
    }//GEN-LAST:event_addCreditedAccountMouseClicked

    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
       
        if(checkAccounts(entryAccountsList) == false){
            JOptionPane.showMessageDialog(rootPane, "Please add at least 1 debit and credit!");
        } else if(description.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Please write a description!");
        } else if(checkBalanceEntry(entryAccountsList) == false){
            JOptionPane.showMessageDialog(rootPane, "Please make sure that debit and credit are equal!");
        } else {       
            
            int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure that all of the data that you've entered are correct?");
            
            if(n == 0){
                
                new AccountingDBController().addJournalEntry(new java.sql.Date(dateChooser.getDate().getTime()),
                        description.getText(),usernameDisplay.getText(), entryAccountsList, refID.getValue());

                JOptionPane.showMessageDialog(rootPane, "Entry was added");

                dateChooser.setDate(Date.valueOf(date));
                debitedAccount.setSelectedIndex(0);
                creditedAccount.setSelectedIndex(0);
                debitedAmount.setText("");
                creditedAmount.setText("");
                description.setText("");
                entryAccountsList.clear();
                viewAddedAccounts();
            }                         
        }
    }//GEN-LAST:event_addMouseClicked

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        dateChooser.setDate(Date.valueOf(date));
        debitedAccount.setSelectedIndex(0);
        creditedAccount.setSelectedIndex(0);
        debitedAmount.setText("");
        creditedAmount.setText("");
        description.setText("");
        entryAccountsList.clear();
        viewAddedAccounts();
    }//GEN-LAST:event_clearActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

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

    private void addedAccountsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addedAccountsMousePressed
        if(evt.isPopupTrigger()){
            showPopUp(evt);
        }
    }//GEN-LAST:event_addedAccountsMousePressed

    private void addedAccountsMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addedAccountsMouseReleased
        if(evt.isPopupTrigger()){
            showPopUp(evt);
        }
    }//GEN-LAST:event_addedAccountsMouseReleased

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        entryAccountsList.remove(addedAccounts.getSelectedRow());
        viewAddedAccounts();
    }//GEN-LAST:event_deleteActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        accountComBox.setSelectedItem(entryAccountsList.get(addedAccounts.getSelectedRow()).getAccountName());
        amountChange.setText(entryAccountsList.get(addedAccounts.getSelectedRow()).getAmount());
        type.setSelectedItem(entryAccountsList.get(addedAccounts.getSelectedRow()).getAction());
        saveChanges.setEnabled(true);
        type.setEnabled(true);
        amountChange.setEnabled(true);
        accountComBox.setEnabled(true);
    }//GEN-LAST:event_editActionPerformed

    private void saveChangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveChangesActionPerformed
        EntryAccount account = new EntryAccount();
        account.setAccountName((String)accountComBox.getSelectedItem());
        account.setAmount(amountChange.getText());
        account.setAction((String)type.getSelectedItem());
        entryAccountsList.set(addedAccounts.getSelectedRow(), account);
        accountComBox.setSelectedIndex(0);
        amountChange.setText("");
        type.setSelectedIndex(0);       
        type.setEnabled(false);
        amountChange.setEnabled(false);
        accountComBox.setEnabled(false);
        viewAddedAccounts();
        saveChanges.setEnabled(false);
    }//GEN-LAST:event_saveChangesActionPerformed

    private void doneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneActionPerformed
        dispose();
    }//GEN-LAST:event_doneActionPerformed

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
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddEntry().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> accountComBox;
    private javax.swing.JButton add;
    private javax.swing.JButton addCreditedAccount;
    private javax.swing.JButton addDebitedAccount;
    private javax.swing.JTable addedAccounts;
    private javax.swing.JTextField amountChange;
    private javax.swing.JMenuItem cancel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton clear;
    private javax.swing.JComboBox<String> creditedAccount;
    private javax.swing.JTextField creditedAmount;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JComboBox<String> debitedAccount;
    private javax.swing.JTextField debitedAmount;
    private javax.swing.JMenuItem delete;
    private javax.swing.JTextArea description;
    private javax.swing.JButton done;
    private javax.swing.JMenuItem edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPopupMenu listPopUp;
    private javax.swing.JButton saveChanges;
    private javax.swing.JComboBox<String> type;
    public javax.swing.JLabel usernameDisplay;
    // End of variables declaration//GEN-END:variables
}
