package finalgame;

//this class extends FlowPane and create the field for one player of the information

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */

//
public class PlayerBlock extends FlowPane{
    //create variable for accessing passed value
    public int gold=0;
    public int building=0;
    public int soldiers=0;
    private String name="";
   //create shape of the field
    private Rectangle playerBlock=new Rectangle(30,100);
    //set label and images for field
    private Label []sign=new Label[4];
    private ImageView goldV;
    private ImageView buildV;
    private  ImageView soldierV;
    private  ImageView userV;
    
    public PlayerBlock(String name,int gold,int building,int soldiers){
        this.name=name;
        this.building=building;
        this.gold=gold;
        this.soldiers=soldiers;
        setPlayerField();
    }
    void setPlayerField(){
        
        setImage();
        
        setHgap(500);
        setVgap(20);
        setPadding(new Insets(50,80,50,80));
        
        setShapeRec(playerBlock);
        for(int x=0;x<4;x++)
            getChildren().add(sign[x]);
        
        setShape(playerBlock);
        setStyle("-fx-border-width:6;-fx-border-color: gold;-fx-background-color: #ffffcc");
        setPrefSize(100, 100);
        
        

    }
    //set shapes of the field
    void setShapeRec(Rectangle r1){
        r1.setArcHeight(20);
        r1.setArcWidth(20);
        r1.setStroke(Color.YELLOW);
        r1.setFill(Color.ALICEBLUE);

    }
    //set style of label
    void setImage(){
        //create images for label
        userV=new ImageView(new Image("image/mono.gif"));
        goldV=new ImageView(new Image("image/gold.jpg"));
        buildV=new ImageView(new Image("image/build.png"));
        soldierV=new ImageView(new Image("image/right.jpg"));
        //set size of image
        userV.setFitHeight(80);
        userV.setFitWidth(80);
        goldV.setFitHeight(30);
        goldV.setFitWidth(30);
        buildV.setFitHeight(30);
        buildV.setFitWidth(30);
        soldierV.setFitHeight(30);
        soldierV.setFitWidth(30);
        //asign image to label
        sign[0]=new Label(name,userV);
        sign[1]=new Label(getGold(),goldV);
        sign[2]=new Label(getBuilding(),buildV);
        sign[3]=new Label(getSoldiers(),soldierV);
        
        
        //set information next to image
        for(int x=0;x<4;x++)
            sign[x].setContentDisplay(ContentDisplay.LEFT);
        sign[0].setContentDisplay(ContentDisplay.TOP);
        
    
}
    //set number of gold
    void setGold(int gold){
        this.gold=gold;
    }
    //get number of gold
    String getGold(){
        return Integer.toString(gold);
    }
    //set number of soldier
    void setSoldier(int soldiers ){
        this.soldiers=soldiers;
}
    //get number of soldiers
    String getSoldiers(){
          return Integer.toString(soldiers);
    }
    //set number of building 
    void setBuilding(int building){
        this.building=building;
    }
    //get number of building
    String getBuilding(){
        return Integer.toString(building);
    }
  
}
