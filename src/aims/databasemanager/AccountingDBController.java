/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aims.databasemanager;

import aims.classes.Account;
import aims.classes.AccountsGroup;
import aims.classes.Entries;
import aims.classes.EntryAccount;
import aims.classes.LedgerAccount;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *
 * @author Vienji
 */
public class AccountingDBController {
    Connection connection = Driver.getConnection();
    private String query;
    
    //General Journal
    public void addNewJournal(){
        try{
            int count = 1;
            Statement statement = connection.createStatement();
            
            query = "SELECT COUNT(JID) FROM general_journal";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                count += result.getInt("COUNT(JID)");
            }
           
            query = "INSERT INTO general_journal (JID) VALUES (" + count + ")";
            statement.execute(query);          
        } catch (SQLException e) {}
    }
    
    public int getCurrentJournal(){
        int cur = 0;
        try{
            
            Statement statement = connection.createStatement();
            
            query = "SELECT COUNT(JID) FROM general_journal";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                cur = result.getInt("COUNT(JID)");
            } 
           
            return cur;          
        } catch (SQLException e) {}  
        
        return cur;
    }
    
    public int getSetCurrentJournal(){
        int cur = 0;
        try{
            
            Statement statement = connection.createStatement();
            
            query = "SELECT state FROM acc_set";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                cur = result.getInt("state");
            }
           
            return cur;          
        } catch (SQLException e) {}  
        
        return cur;
    }
    
    public void setCurrentJournal(String value){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE acc_set SET state = '" + value +"' WHERE component = 'curr_journal'";
            
            statement.execute(query);
        } catch (SQLException e){}
    }
    
    //Journal Entry
    public void addJournalEntry(Date date, String description, String name, ArrayList<EntryAccount> array, String refNumber){
        try{
            Statement statement = connection.createStatement();
            
            String id = "0";
            LocalTime time = LocalTime.now();
            String timeS = String.valueOf(time);

            query = "SELECT user_ID FROM users_account WHERE name = '" + name + "'";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                id = String.valueOf(result.getInt("user_ID"));           
            }           
         
            query = "INSERT INTO journal_entry (date, time, description, encoder, JID, deleted) VALUES (?, ?, ?, ?, ?, ?)";
                      
            PreparedStatement prepStatement = connection.prepareStatement(query);   
            prepStatement.setDate(1, date); 
            prepStatement.setString(2,timeS);  
            prepStatement.setString(3, description);    
            prepStatement.setInt(4, Integer.parseInt(id)); 
            prepStatement.setInt(5, Integer.parseInt(refNumber));
            prepStatement.setString(6, "No");
            prepStatement.execute();  
         
            query = "SELECT JEID FROM journal_entry WHERE description = '" + description + "' AND date = '" + date + "' AND time = '" + timeS + "' ";
            result = statement.executeQuery(query);
            
            while(result.next()){
                id = String.valueOf(result.getInt("JEID"));
            }

            query = "INSERT INTO journal_item (account, action, amount, JEID) VALUES (?, ?, ?, ?)";
            PreparedStatement prepStatement2 = connection.prepareStatement(query);
            
            for(EntryAccount e: array){
                prepStatement2.setInt(1, Integer.parseInt(getAccountID(e.getAccountName())));
                prepStatement2.setString(2, e.getAction());
                prepStatement2.setFloat(3, Float.parseFloat(e.getAmount()));
                prepStatement2.setInt(4, Integer.parseInt(id));
                prepStatement2.execute();                              
            }           
        } catch (SQLException  e) {}
    }
    
    public ArrayList<Entries> getJournalEntries(String refID){
        ArrayList<Entries> journalEntries = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT journal_entry.JEID, journal_entry.date, journal_entry.description, users_account.name, journal_entry.deleted"
                    + " FROM journal_entry"
                    + " INNER JOIN users_account ON journal_entry.encoder = users_account.user_ID"
                    + " WHERE journal_entry.JID = " + refID;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                    Entries entry = new Entries();
                    entry.setJEID(String.valueOf(result.getInt("JEID")));
                    entry.setDate(String.valueOf(result.getDate("date")));
                    entry.setDescription(result.getString("description"));
                    entry.setEncoder(result.getString("name"));
                    entry.setDeleted(result.getString("deleted"));
                    journalEntries.add(entry);
            }          
        } catch (SQLException e) {}       
        return journalEntries;
    }
    
    public ArrayList<Entries> searchJournalEntries(String refID, String value, String by){
        ArrayList<Entries> journalEntries = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT journal_entry.JEID, journal_entry.date, journal_entry.description, users_account.name, journal_entry.deleted"
                    + " FROM journal_entry"
                    + " INNER JOIN users_account ON journal_entry.encoder = users_account.user_ID"
                    + " WHERE journal_entry.JID = " + refID + " AND " + by + " LIKE " + value;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                    Entries entry = new Entries();
                    entry.setJEID(String.valueOf(result.getInt("JEID")));
                    entry.setDate(String.valueOf(result.getDate("date")));
                    entry.setDescription(result.getString("description"));
                    entry.setEncoder(result.getString("name"));
                    entry.setDeleted(result.getString("deleted"));
                    journalEntries.add(entry);
            }          
        } catch (SQLException e) { e.printStackTrace(); }       
        return journalEntries;
    }
    
    public String getEntryAmount(String JEID){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        String s = "0.00";
        try{
            Statement statement = connection.createStatement();
            query = "SELECT sum(amount) AS amount FROM journal_item WHERE JEID = " + JEID + " AND action = \'Debit\'";
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){     
                s = decimalFormat.format(result.getFloat("amount"));
            }
        } catch (SQLException e) {}
        
        return s;
    }
    
    public ArrayList<EntryAccount> getEntryDetails(String JEID){
        ArrayList<EntryAccount> entryDetails = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            query = "SELECT accounts.account_name, journal_item.amount, journal_item.action"
                    + " FROM journal_item"
                    + " INNER JOIN accounts ON journal_item.account = accounts.AID"
                    + " WHERE JEID = " + JEID;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                EntryAccount entry = new EntryAccount();
                entry.setAccountName(result.getString("account_name"));
                entry.setAmount(String.valueOf(result.getFloat("amount")));
                entry.setAction(result.getString("action"));
                entryDetails.add(entry);
            }          
        } catch (SQLException e) {}
        return entryDetails;
    }
    
    public void deleteEntry(String id, String encoder , String deleted){
        try{
            Statement statement = connection.createStatement();
            
            query = "UPDATE journal_entry SET encoder = " + new UserDBController().getUserID(encoder) + ", deleted = '" 
                    + deleted + "' WHERE JEID = " + id;

            statement.execute(query);
        } catch (SQLException e) {}
    }
    
    public boolean isJEDeleted(String id){
        try{
            Statement statement = connection.createStatement();
            
            query = "SELECT deleted FROM journal_entry WHERE JEID = " + id;
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                if(result.getString("deleted").equals("Yes")){
                    return true;
                }
            }
        } catch(SQLException e){}
        
        return false;
    }
    //Ledger
    public ArrayList<LedgerAccount> getLedgerAccountDetails(String account, String refID){
        ArrayList<LedgerAccount> ledgerAccountDetails = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();

            query = "SELECT journal_entry.JEID, journal_entry.date, journal_entry.description, journal_item.amount, journal_item.action"
                    + " FROM journal_entry"
                    + " INNER JOIN journal_item ON journal_entry.JEID = journal_item.JEID"
                    + " INNER JOIN accounts ON journal_item.account = accounts.AID"
                    + " WHERE accounts.account_name = '" + account + "' AND journal_entry.JID = " + refID ;
            ResultSet result = statement.executeQuery(query);

            while(result.next()){
                LedgerAccount ledgerAccount = new LedgerAccount();
                
                ledgerAccount.setJEID(String.valueOf(result.getInt("JEID")));
                ledgerAccount.setDate(String.valueOf(result.getDate("date")));
                ledgerAccount.setDescription(result.getString("description"));
                ledgerAccount.setAmount(String.valueOf(result.getFloat("amount")));
                ledgerAccount.setAction(result.getString("action"));
                ledgerAccountDetails.add(ledgerAccount);
            }            
        } catch (SQLException e) {}     
        return ledgerAccountDetails;
    }
    
    public String getAccountAction(String account){
        String s = "";
        try{
            Statement statement = connection.createStatement();

            query = "SELECT account_group.type"
                    + " FROM accounts"
                    + " INNER JOIN account_group ON accounts.AGID = account_group.AGID"
                    + " WHERE accounts.account_name = '" + account + "'";
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                s = result.getString("type");
            }
        } catch (SQLException e) {}      
        return s;
    }
    
    //Accounts Group
    public void addAccountsGroup(Date date, String groupName, String description, String type){
        try{
            query = "INSERT INTO account_group (date, group_name, description, type) VALUES (?, ?, ?, ?)";
            PreparedStatement prepStatement = connection.prepareStatement(query);
            
            prepStatement.setDate(1, date);
            prepStatement.setString(2, groupName);
            prepStatement.setString(3, description);
            prepStatement.setString(4, type);
            prepStatement.execute();
        } catch (SQLException e) {}
        
    }
    
    public ArrayList<AccountsGroup> getAccountsGroupList(){
        ArrayList<AccountsGroup> accountsGroupList = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM account_group";
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                AccountsGroup accountsGroup = new AccountsGroup();
                accountsGroup.setAGID(String.valueOf(result.getInt("AGID")));
                accountsGroup.setDate(String.valueOf(result.getDate("date")));
                accountsGroup.setGroupName(result.getString("group_name"));
                accountsGroup.setDescription(result.getString("description"));
                accountsGroup.setType(result.getString("type"));
                accountsGroup.setDefault_ag("No");
                accountsGroupList.add(accountsGroup);
            }          
        } catch (SQLException e) {}       
        return accountsGroupList;
    }
    
    public ArrayList<AccountsGroup> searchAccountGroup(String value, String by){
        ArrayList<AccountsGroup> accountsGroupList = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM account_group WHERE " + by + " LIKE " + value;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                AccountsGroup accountsGroup = new AccountsGroup();
                accountsGroup.setAGID(String.valueOf(result.getInt("AGID")));
                accountsGroup.setDate(String.valueOf(result.getDate("date")));
                accountsGroup.setGroupName(result.getString("group_name"));
                accountsGroup.setDescription(result.getString("description"));
                accountsGroup.setType(result.getString("type"));
                accountsGroup.setDefault_ag("No");
                accountsGroupList.add(accountsGroup);
            }          
        } catch (SQLException e) {}       
        return accountsGroupList;
    }
    
    public ArrayList<AccountsGroup> sortAccountsGroup(String by){
        ArrayList<AccountsGroup> accountsGroupList = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM account_group ORDER BY " + by;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                AccountsGroup accountsGroup = new AccountsGroup();
                accountsGroup.setAGID(String.valueOf(result.getInt("AGID")));
                accountsGroup.setDate(String.valueOf(result.getDate("date")));
                accountsGroup.setGroupName(result.getString("group_name"));
                accountsGroup.setDescription(result.getString("description"));
                accountsGroup.setType(result.getString("type"));
                accountsGroup.setDefault_ag("No");
                accountsGroupList.add(accountsGroup);
            }          
        } catch (SQLException e) {e.printStackTrace();}       
        return accountsGroupList;
    }
    
    public String getAccountsGroupID(String group){
        String id = "";
        try{
            Statement statement = connection.createStatement();
            query = "SELECT AGID FROM account_group WHERE group_name = '" + group + "'";           
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                id =String.valueOf(result.getInt("AGID")) ;
            }
           
        } catch (SQLException e) {}       
        return id; 
    }
    
    public String getAccountsGroupName(String id){
        String name = "";
        try{
            Statement statement = connection.createStatement();
            query = "SELECT group_name FROM account_group WHERE AGID = " + id ;           
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                name = result.getString("group_name") ;
            }
           
        } catch (SQLException e) {}       
        return name;
    }
    
    public String getAccountGroupType(String id){
        String type = "";
        try{
            Statement statement = connection.createStatement();
            query = "SELECT type FROM account_group WHERE AGID = " + id ;           
            ResultSet result = statement.executeQuery(query);
            
            while(result.next()){
                type = result.getString("type") ;
            }
           
        } catch (SQLException e) {}       
        return type;
    }
    
    //Account
    public void addAccount(Date date, String accountName, String description, String group){     
        try{
            query = "INSERT INTO accounts (date, account_name, description, AGID, default_account) VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement prepStatement = connection.prepareStatement(query);
            
            prepStatement.setDate(1, date);
            prepStatement.setString(2, accountName);
            prepStatement.setString(3, description);
            prepStatement.setString(4, getAccountsGroupID(group));
            prepStatement.setString(5, "No");
            prepStatement.execute();
            
        } catch (SQLException e) {}     
    } 
    
    public ArrayList<Account> getAccountsList(){
        ArrayList<Account> accountsList = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM accounts ORDER BY account_name";
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                Account account = new Account();
                account.setAID(String.valueOf(result.getInt("AID")));
                account.setDate(String.valueOf(result.getDate("date")));
                account.setAccountName(result.getString("account_name"));
                account.setDescription(result.getString("description"));
                account.setAGID(String.valueOf(result.getInt("AGID")));
                account.setDefault_account(result.getString("default_account"));
                accountsList.add(account);
            }
            
        } catch (SQLException e) {}       
        return accountsList;
    }
    
    public ArrayList<Account> searchAccount(String value, String by){
        ArrayList<Account> accountsList = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM accounts"
                    + " INNER JOIN account_group ON account_group.AGID = accounts.AGID"
                    + " WHERE " + by + " LIKE " + value;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                Account account = new Account();
                account.setAID(String.valueOf(result.getInt("AID")));
                account.setDate(String.valueOf(result.getDate("date")));
                account.setAccountName(result.getString("account_name"));
                account.setDescription(result.getString("description"));
                account.setAGID(String.valueOf(result.getInt("AGID")));
                account.setDefault_account(result.getString("default_account"));
                accountsList.add(account);
            }
            
        } catch (SQLException e) {e.printStackTrace();}       
        return accountsList;
    }
    
    public ArrayList<Account> sortAccount(String by){
        ArrayList<Account> accountsList = new ArrayList<>();        
        try{
            Statement statement = connection.createStatement();
            query = "SELECT * FROM accounts"
                    + " INNER JOIN account_group ON account_group.AGID = accounts.AGID"
                    + " ORDER BY " + by;
            ResultSet result = statement.executeQuery(query);
            
            while (result.next()){
                Account account = new Account();
                account.setAID(String.valueOf(result.getInt("AID")));
                account.setDate(String.valueOf(result.getDate("date")));
                account.setAccountName(result.getString("account_name"));
                account.setDescription(result.getString("description"));
                account.setAGID(String.valueOf(result.getInt("AGID")));
                account.setDefault_account(result.getString("default_account"));
                accountsList.add(account);
            }
            
        } catch (SQLException e) {e.printStackTrace();}       
        return accountsList;
    }
    
    public String getAccountID(String name){
        String id = "";
        try{
            Statement statement = connection.createStatement();
            query = "SELECT AID FROM accounts WHERE account_name = '" + name + "'";           
            ResultSet result = statement.executeQuery(query);

            while(result.next()){
                id = String.valueOf(result.getInt("AID")) ;
            }
           
        } catch (SQLException e) {}       
        return id;
    }
       
}
