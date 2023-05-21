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
public class Entries {   

    private String JEID;
    private String date;
    private String time;
    private String description;
    private String UID;
    private String encoder;
    private String JID;
    private String deleted;
    
    public String getJID() {
        return JID;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setJID(String JID) {
        this.JID = JID;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
       
    public String getJEID() {
        return JEID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getUID() {
        return UID;
    }

    public String getEncoder() {
        return encoder;
    }

    public void setJEID(String JEID) {
        this.JEID = JEID;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setEncoder(String encoder) {
        this.encoder = encoder;
    }          
}
