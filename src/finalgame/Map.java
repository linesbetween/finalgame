package finalgame;

//This class extends borderPand and creates all the objects the graphic needs


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PC
 */
public class Map extends BorderPane{
    //create array of 30 objects of mapBlock
    final int blockNum=30;
    private MapBlock[] blockArray= new MapBlock[blockNum]; 
  //create two block for the player field
    private PlayerBlock player1;
    private PlayerBlock player2;
    
    public Player[] playerList = new Player[2];
    

    
  //create start location
    private int location1=0;
    private int location2=0;
 
    public static int diceNum;
    
    //indicate which player's turn
    private boolean turn=false;
    
    //set default start resources
    public int gold=50000;
    public int building=0;
    public int soldiers=1000;
    
    //create the field under the first player field
    public BottomButton control;
    
    //create a text area for introduce the actions of players
    private GatherInfo actionInfo;
    private BuildingEvent buildingEvent;
    private ExecuteEvent executeEvent;
    
    public Map(){
        //create a pane for the field inside the map
        FlowPane inside=new FlowPane();
        //create an object to gather information
        actionInfo=new GatherInfo();
        //create and object to put in most of the control parts of the game
        control=new BottomButton();
        //set the field setting of the inside part of map
        inside.setHgap(150);
        inside.setVgap(10);
        inside.setPadding(new Insets(20,0,20,80));
        //insert an image as background
        inside.setStyle("-fx-background-image: url('image/game.jpg')");
        //create button for entering the game
        Button b1 = new Button("Enter Game");
        //create start page for the game
        StartPage start=new StartPage(b1);
        //add the page to  the pane
        setCenter(start);
        
        BuildingEvent b = new BuildingEvent();
        //set the action for the enter game button
        b1.setOnAction(e->{
            
           
            
            //avoid empty user name
            if(!start.t1.getText().isEmpty()){
                //create object for each player field and pass the information that needs
                player1=new PlayerBlock(start.getUserId(),gold,building,soldiers);
                player2=new PlayerBlock(start.getUserId(),gold,building,soldiers);
                playerList[0] = new Player();
                playerList[1] = new Player();
                playerList[0].setUsername(start.getUserId());
                playerList[1].setUsername(start.getUserId());
                //add the nodes to the inside field of the map
                inside.getChildren().addAll(player1,player2,control,actionInfo);
                
                //remove the start page
                getChildren().remove(start);
                //add the nodes to map
                setTop(getTopNode());
                setRight(getRightNode());
                setBottom(getBottomNode());
                setLeft(getLeftNode());
                //set the color for the start point
                if (location1==location2)
                    blockArray[location2%30].setStyle("-fx-border-width:6;-fx-border-color: #33cc33;-fx-background-color: #e5ffe5");
                setCenter(inside);
                
                
                
                
            }
            
        });
        //set action to roll button
        control.getRollButton().setOnAction(e->{
            //set the size of label
            control.diceResult.setPrefSize(30, 30);
            
            //method to invoke this button
            roll();
            
                });
        
    }
    //set the top nodes blocks
    private HBox getTopNode(){
      HBox hBox = new HBox();
      for (int i=0;i<10;i++){
          blockArray[i] = new MapBlock(i);
          hBox.getChildren().add(blockArray[i]);
      }
      return hBox;
  }
  //set the bottom nodes blocks
   private HBox getBottomNode(){
      HBox hBox = new HBox();
      for (int i=24;i>14;i--){
          blockArray[i] = new MapBlock(i);
          
          hBox.getChildren().add(blockArray[i]);
      }
      return hBox;
  }
  //set the right blocks
  private VBox getRightNode(){
      VBox vBox = new VBox();
      for (int i=10;i<15;i++){
          blockArray[i] = new MapBlock(i);
          vBox.getChildren().add(blockArray[i]);
      }
      return vBox;
  }
  //set the left blocks
  private VBox getLeftNode(){
      VBox vBox = new VBox();
      for (int i=29;i>24;i--){
          blockArray[i] = new MapBlock(i);
          vBox.getChildren().add(blockArray[i]);
      }
      return vBox;
  }
   
   //main method to roll dice
  protected void roll() { 
       //roll dice
       diceNum=(int)(Math.random()*6+1);
       //set the label for dice
       control.setDice(diceNum);
       int playerIndex;
       MapBlock currentBlock;
   //set the location of the player one
       if (turn==false){      
           playerIndex = 0;
       //update location & display on map
           
           blockArray[location1 %30].setStyle("-fx-border-width:6;-fx-border-color: #737373;-fx-background-color: #e5ffe5");
           
            if (location1==location2)
                blockArray[location2%30].setStyle("-fx-border-width:6;-fx-border-color: #00FFFF;-fx-background-color: #e5ffe5");
           
           

            location1=location1+diceNum;
            blockArray[location1 %30].setStyle("-fx-border-width:6;-fx-border-color: #FFFF00;-fx-background-color: #e5ffe5");
            currentBlock = blockArray[location1];
          //  control.setTurn(null);
            actionInfo.setActionInfo("Player One ");
       }  
       //set the location for the player two
       else{
           playerIndex = 1;
            blockArray[location2%30].setStyle("-fx-border-width:6;-fx-border-color: #737373;-fx-background-color: #e5ffe5");
            
            if (location1==location2)
                blockArray[location2%30].setStyle("-fx-border-width:6;-fx-border-color: #FFFF00;-fx-background-color: #e5ffe5");
             
             
            location2=location2+diceNum;
            blockArray[location2 %30].setStyle("-fx-border-width:6;-fx-border-color: #00FFFF;-fx-background-color: #e5ffe5");
            currentBlock = blockArray[location2];
            //control.setTurn(null);
            actionInfo.setActionInfo("Player Two ");
       }
       
       if (location1==location2)
           blockArray[location2%30].setStyle("-fx-border-width:6;-fx-border-color: #33cc33;-fx-background-color: #e5ffe5");
     
       actionInfo.setActionInfo("rolls "+diceNum+"\n");
       
       if (currentBlock.getBuildType().equals("event")){
           executeEvent.ExecuteEvent(playerIndex, location1, blockArray, playerList, actionInfo);
       }
       else
           buildingEvent.BuildingEvent(playerIndex, currentBlock, playerList, actionInfo);
           
       turn = !turn;
   }
    
}
