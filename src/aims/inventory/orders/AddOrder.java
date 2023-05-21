/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.inventory.orders;

import aims.classes.OrderItem;
import aims.classes.Supplier;
import aims.databasemanager.InventoryDBController;
import aims.inventory.InventoryUI;
import java.sql.Date;
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
public class AddOrder extends javax.swing.JFrame {
    LocalDate date = LocalDate.now();
    /**
     * Creates new form addOrder
     */
    public AddOrder() {
        initComponents();
        initSupplierList();
        
        save.setEnabled(false);
        dateChooser.setDate(Date.valueOf(date));    
        
        ImageIcon icon = new ImageIcon("src\\aims\\images\\aims-logo.png");
        setIconImage(icon.getImage());
    }
    
    ArrayList<OrderItem> itemList = new ArrayList<>();
    int numberOfItems = 0;
    
    private void initSupplierList(){
        ArrayList<Supplier> supplier = new InventoryDBController().getSuppliersList();
        
        supplier.forEach((s) -> {
            supplierList.addItem(s.getName());
        });
    }
    
    private void viewAddedItem(){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        
        orderedItemsList.setModel(new DefaultTableModel(null, new String[]{"Item Name", "Quantity", "Amount"}));
        
        Iterator i = (Iterator) itemList.iterator();
        
        DefaultTableModel orderedItemsTable = (DefaultTableModel) orderedItemsList.getModel();
        
        while(i.hasNext()){
            OrderItem item = (OrderItem) i.next();
            
            String[] itemData = {item.getItemName(), item.getQuantity(), 
                " ₱ " + decimalFormat.format(Float.parseFloat(item.getAmount()))};
            orderedItemsTable.addRow(itemData);
        }      
        
        showOrderTotal.setText(decimalFormat.format(sumOfOrder()));
        numOfItems.setText(String.valueOf(numberOfItems));
    }
    
    private boolean checkNumber(String number){       
        try{
            Float.parseFloat(number);
            return false;
        } catch (NumberFormatException e){
            return true;
        }      
    }
    
    private float sumOfOrder(){
        float sum = 0f;
        
        for(OrderItem i : itemList){
            sum += Float.parseFloat(i.getAmount());
        }
        
        return sum;
    }
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        supplierList = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderedItemsList = new javax.swing.JTable();
        addItem = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        showOrderTotal = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        modeOfPayment = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        placeOrder = new javax.swing.JButton();
        cancel = new javax.swing.JButton();
        done = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        encoder = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        numOfItems = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        itemName = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        quantity = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        amount = new javax.swing.JTextField();
        save = new javax.swing.JButton();
        dateChooser = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Add Order");

        jLabel2.setText("Supplier");

        jLabel3.setText("Date");

        jLabel4.setText("Ordered Items");

        orderedItemsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Name", "Quantity", "Amount "
            }
        ));
        jScrollPane1.setViewportView(orderedItemsList);

        addItem.setText("Add Item");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        jLabel5.setText("Order Total:  ₱");

        showOrderTotal.setText(" 0.00");

        jLabel7.setText("Mode of Payment:");

        modeOfPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Accounts Payable" }));

        placeOrder.setText("Place Order");
        placeOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placeOrderActionPerformed(evt);
            }
        });

        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        done.setText("Done");
        done.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doneActionPerformed(evt);
            }
        });

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });

        jLabel8.setText("Encoder:");

        encoder.setText("username");

        jLabel10.setText("No. of Items:");

        numOfItems.setText("0");

        jLabel12.setText("Item Name");

        jLabel13.setText("Qty");

        jLabel14.setText("Amount:   ₱");

        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierList, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(itemName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(addItem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(numOfItems))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(showOrderTotal))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(modeOfPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(encoder))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(save)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(placeOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(done)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancel)))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(supplierList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel13)
                        .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14)
                        .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addItem)
                    .addComponent(delete)
                    .addComponent(edit)
                    .addComponent(save))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(numOfItems)
                    .addComponent(jLabel8)
                    .addComponent(encoder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(showOrderTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(modeOfPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(placeOrder)
                    .addComponent(cancel)
                    .addComponent(done)
                    .addComponent(clear))
                .addGap(21, 21, 21))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        if(itemName.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Please enter an item name!");
        } else if ( (int) quantity.getValue() <= 0 ){
            JOptionPane.showMessageDialog(rootPane, "Quantity must be more than 0!");
        } else if (amount.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Please enter the right amount!");
        } else if (checkNumber(amount.getText())){
            JOptionPane.showMessageDialog(rootPane, "Enter a numeric amount!");
        } else {            
            OrderItem item = new OrderItem();
        
            item.setItemName(itemName.getText());
            item.setQuantity(String.valueOf((int) quantity.getValue()));
            item.setAmount(amount.getText());
            itemList.add(item);
            numberOfItems++;
        
            itemName.setText("");
            quantity.setValue(0);
            amount.setText("");
        
            viewAddedItem();
        }
        
        if(save.isEnabled()){
            save.setEnabled(false);
        }
    }//GEN-LAST:event_addItemActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if(itemName.getText().equals("")){
            JOptionPane.showMessageDialog(rootPane, "Please enter an item name!");
        } else if ((int) quantity.getValue() <= 0){
            JOptionPane.showMessageDialog(rootPane, "Quantity must be more than 0!");
        } else if (Float.parseFloat(amount.getText()) <= 0 || amount.getText().equals("")) {
            JOptionPane.showMessageDialog(rootPane, "Please enter the right amount!");
        } else if (checkNumber(amount.getText())){
            JOptionPane.showMessageDialog(rootPane, "Enter a numeric amount!");
        } else {
            
            OrderItem item = new OrderItem();
        
            item.setItemName(itemName.getText());
            item.setQuantity(String.valueOf((int) quantity.getValue()));
            item.setAmount(amount.getText());
            itemList.set(orderedItemsList.getSelectedRow(), item);
        
            itemName.setText("");
            quantity.setValue(0);
            amount.setText("");
            save.setEnabled(false);
            
            viewAddedItem();
        }
    }//GEN-LAST:event_saveActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if(orderedItemsList.getSelectedRow() >= 0){
            itemList.remove(orderedItemsList.getSelectedRow());
            numberOfItems--;
            viewAddedItem();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to delete!");
        }
    }//GEN-LAST:event_deleteActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        if(orderedItemsList.getSelectedRow() >= 0){
            itemName.setText(itemList.get(orderedItemsList.getSelectedRow()).getItemName());
            quantity.setValue(Integer.parseInt(itemList.get(orderedItemsList.getSelectedRow()).getQuantity()));
            amount.setText(itemList.get(orderedItemsList.getSelectedRow()).getAmount());
            save.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Please select an item to edit!");
        }
    }//GEN-LAST:event_editActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        dispose();
    }//GEN-LAST:event_cancelActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        supplierList.setSelectedIndex(0);
        dateChooser.setDate(Date.valueOf(date));
        itemList.clear();
        itemName.setText("");
        quantity.setValue(0);
        amount.setText("");
        modeOfPayment.setSelectedIndex(0);
        numberOfItems = 0;
        viewAddedItem();
    }//GEN-LAST:event_clearActionPerformed

    private void doneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doneActionPerformed
        dispose();
    }//GEN-LAST:event_doneActionPerformed

    private void placeOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placeOrderActionPerformed
        
        int limit = new InventoryDBController().getComponentState("order_limit");
        
        if( limit > new InventoryUI().countToReceive() ){
            if(Integer.parseInt(numOfItems.getText()) == 0){
            JOptionPane.showMessageDialog(rootPane, "You didn't order anything!");
            } else {          
                int n = JOptionPane.showConfirmDialog(rootPane, "Are you sure with your orders?");

                if(n == 0){
                    new InventoryDBController().addOrder((String)supplierList.getSelectedItem() , 
                            new java.sql.Date(dateChooser.getDate().getTime()), itemList, numOfItems.getText(), 
                            String.valueOf(sumOfOrder()), (String) modeOfPayment.getSelectedItem(), encoder.getText());

                    JOptionPane.showMessageDialog(rootPane, "Order was Successfully added!");
                }        
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Order limit of "+ limit +" has been reached!");
        }                  
    }//GEN-LAST:event_placeOrderActionPerformed

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
            java.util.logging.Logger.getLogger(AddOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItem;
    private javax.swing.JTextField amount;
    private javax.swing.JButton cancel;
    private javax.swing.JButton clear;
    private com.toedter.calendar.JDateChooser dateChooser;
    private javax.swing.JButton delete;
    private javax.swing.JButton done;
    private javax.swing.JButton edit;
    public javax.swing.JLabel encoder;
    private javax.swing.JTextField itemName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox<String> modeOfPayment;
    private javax.swing.JLabel numOfItems;
    private javax.swing.JTable orderedItemsList;
    private javax.swing.JButton placeOrder;
    private javax.swing.JSpinner quantity;
    private javax.swing.JButton save;
    private javax.swing.JLabel showOrderTotal;
    private javax.swing.JComboBox<String> supplierList;
    // End of variables declaration//GEN-END:variables
}
