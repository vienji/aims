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
public class LedgerAccount {   
    private String JEID;
    private String date;
    private String description;
    private String ref;
    private String amount;
    private String action;
    private String account;
    
    public String getAmount() {
        return amount;
    }

    public String getJEID() {
        return JEID;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getRef() {
        return ref;
    }

    public String getAction() {
        return action;
    }

    public String getAccount() {
        return account;
    }

    public void setJEID(String JEID) {
        this.JEID = JEID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setAccount(String account) {
        this.account = account;
    } 
    
    public void setAmount(String amount) {
        this.amount = amount;
    }
    
}
