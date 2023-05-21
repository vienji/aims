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
public class Account {

    private String AID;
    private String date;
    private String accountName;
    private String description;
    private String AGID;
    private String default_account;
    
    public String getDefault_account() {
        return default_account;
    }

    public void setDefault_account(String default_account) {
        this.default_account = default_account;
    }
    public void setAID(String AID){
        this.AID = AID;
    }
    
    public void setDate(String date){
        this.date = date;
    }
    
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public void setAGID(String AGID){
        this.AGID = AGID;
    }
    
    public String getAID(){
        return AID;
    }
    
    public String getDate(){
        return date;
    }
    
    public String getAccountName(){
        return accountName;
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getAGID(){
        return AGID;
    }
}
