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
public class EntryAccount {    
    private String accountName;
    private String amount;
    private String action;
    private String index;
    
    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
    
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }
    
    public void setAmount(String amount){
        this.amount = amount;
    }
    
    public void setAction(String action){
        this.action = action;
    }
    
    public String getAccountName(){
        return accountName;
    }
    
    public String getAmount(){
        return amount;
    }
    
    public String getAction(){
        return action;
    }
}
