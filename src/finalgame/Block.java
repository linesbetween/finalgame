/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finalgame;

/**
 *
 * @author mtg
 */
public class Block {
    private int blockId;
    private String landType;
    private int buildingId = 0;
    private int ownerId;
    public Block(){
        
    }
    
    public Block (int blockId){
        this.blockId=blockId;
    }
    
    public int getBlockId(){
        return blockId;
    }
    
    public  String getLandType(){
        return landType;
    }
     
    public  int getBuildingId(){
        return buildingId;
    }
    
    public  int getOwnerId(){
        return ownerId;
    }
    
    public void setBlockId (int blockId){
        this.blockId=blockId;
    }
    
    public void setLandType (String landType){
        this.landType=landType;
    }
    
    public void setBuidlingId (int buildingId){
        this.buildingId=buildingId;
    }
    
    public void setOwnerId (int ownerId){
        this.ownerId=ownerId;
    }
    
}
