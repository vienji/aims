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
public class AccountsGroup {
    private String AGID;
    private String date;
    private String groupName;
    private String description;
    private String type;
    private String default_ag;
    
    public String getDefault_ag() {
        return default_ag;
    }

    public void setDefault_ag(String default_ag) {
        this.default_ag = default_ag;
    }
    public void setAGID(String AGID){
        this.AGID = AGID;
    }
    
    public void setDate(String date){
        this.date = date;
    }
    
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public void setType(String type){
        this.type = type;
    }
    
    public String getAGID(){
        return AGID;
    }
    
    public String getDate(){
        return date;
    }
    
    public String getGroupName(){
        return groupName;
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getType(){
        return type;
    }
}
