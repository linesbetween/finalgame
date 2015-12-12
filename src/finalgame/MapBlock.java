package finalgame;

//this class extends the GridPane and creates an object for the blocks of the game

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ct-gen
 */
public class MapBlock extends GridPane{
    //identify the land
    private String TypeOfBuild;
    
    //set the nodes in the block
    
    private Label level;
    private Label owner;
    private Label soldier;
    private ImageView landV;
    //set the number of level and soldiers
    
    private int buildingId = 0;
    private int levelNum=1;
    private int soldierNum=0;

    public MapBlock(int location){
        //set label for the starting block
        Label start=new Label("Start");
        
        start.setFont(Font.font("Times", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 30));
        
        //set the label for the number of block
        Label localNum=new Label((Integer.toString(location)));
        localNum.setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC, 15));
        localNum.setStyle("-fx-text-fill: #c2c2a3;-fx-background-color: white");
        //set the label for the level soldier and owner
        level=new Label("");
        setLableStyle(level);
        
        soldier=new Label("");
        setLableStyle(soldier);
        
        owner=new Label("");
        setLableStyle(owner);
        //set the styler of field of the block
        setVgap(5);
        setPrefSize(100,100);
        setStyle("-fx-border-width:6;-fx-border-color: #737373;-fx-background-color: #e5ffe5");
        
        //add the nodes for the land
        for(int x=0;x<30;x++)
            if (location==x&&x!=0 && x%3 !=0){
                setLand();
            
                add(level, 1,1);
                add(soldier,1,2);
                add(owner, 1,3);
                TypeOfBuild = "land";
            }
            else
                TypeOfBuild = "event";
        //add the label for location number
        add(localNum,0,0);
        //add label for the starting block
        if (location==0)
            add(start, 1, 1);
        
        
    }
    //set the style for all labels
    void setLableStyle(Label all){
        all.setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC, 10));
        all.setStyle("-fx-text-fill: black;-fx-background-color: #f2f2f2");
        
    }
    //set the image for the land
    void setLand(){
        landV=new ImageView(new Image("image/land.jpg"));
        landV.setFitHeight(88);
        landV.setFitWidth(88);
        add(landV,0,0,3,5);

    }
    //set the image for the hotel
    void setHotel(){
        this.TypeOfBuild="hotel";
        landV.setImage(new Image("image/hotel.jpg"));
    }
    void setFactory(){
        this.TypeOfBuild="factory";
        landV.setImage(new Image("image/factory.gif"));
    }
    //set the image for the castel 
    void setCastle(){
        this.TypeOfBuild="castle";
        landV.setImage(new Image("image/castle.jpg"));
    }
    //get the type of building
    String getBuildType(){
        return this.TypeOfBuild;
    }
    //set the owner of building
    void setOwner(String name){
        
        owner.setText(name);
    }
//set level of the build
    void setLevel(int level){
        
        this.levelNum=level;
        this.level.setText("Level "+level);
    }
    //set number of soldier
    void setSoldier(int soldierNum){
        
        this.soldierNum=soldierNum;
        this.owner.setText("Soldiers "+soldierNum);
        
    }
    
    void setBuildingId(int buildingId){
        this.buildingId = buildingId;
    }
    //get the owner name
    String getOwner(){
        return owner.getText();
    }
    //get the level of building
    int getLevel(){
        return this.levelNum;
    }
    //get number of soldiers 
    int getSoldier(){
        return this.soldierNum;
    }
    
    int getBuildingId(){
        return this.buildingId;
    }
    




    
}
