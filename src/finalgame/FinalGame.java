//*NOTE2:
/* RUNTIME ERROR: map exits right after launching.
* display issue still under processing
* good news: structure of dicision is kinda clear now. ctrl+F "TODO" to see
    where to add player/building/event code
*/


/*
 * NOTE: 
 * copy IniMap.sql in to project root folder since it's the current running directory.
 * create an empty database (in this example, database name is game) before run.
 * the map table will be created at run time.
 * the ScriptRunner.java and Table.java are used to read sql scripts. 
 ***issue: looks like it can't handle loop or comments

 *
 * What this does now: 
 * initilize layout and place map blocks.
 * initilize map table in database.
 * dice randomly generates number generater) to select block (row) in map table.
 * put indicater (like flag or color change) on selected block.
 * when player1 lands on a block, change block border to red. reset border color to black when leave.
 ***ISSUE: setStyle method seems to override the former setStyle method calss.
           thus changing border color will mess up with backgroud color 
 * assign block to the player who first steps on it. 
 * stores current map into into array of blocks.
 */

package finalgame;

/**
 *
 * @author xhf
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;



public class FinalGame extends Application {
  // Statement for executing queries
  private Statement stmt;
  
  
  //For display
  private GridPane[] blockPaneArray; 
  private Label turnLabel;// display who's turn is in current round
  private Label diceResult;//display result of dice rolling
  public  Label announce;
  private Button rollDice;
  private Button stop_Save;
  
  public static int diceNum;
  private boolean turn;//indicate which player's turn
  public boolean eventActive = false;//Determines if a player is moving or in 
  //the middle of an event.
  
  //Array for map table data
  //Each block as an object 
  final int blockNum=30;
  public Block[] blockData;
  
  //Create list of players to hold all player objects
  //Will detect current player by array index.
  private Player[] playerList; 
  
 
  /***************************************
  /*Main method 
  /****************************************/
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
    // Initialize database connection and create a Statement object
    initializeDB();
    blockPaneArray = new GridPane[blockNum]; 
    //Initialize user interface   
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(getTop());
    borderPane.setRight(getRight());
    borderPane.setBottom(getBottom());
    borderPane.setLeft(getLeft());
    borderPane.setCenter(getCenter());
    borderPane.setStyle("-fx-background-color: #FFFFFF;");    
    // Create a scene and place it in the stage
    Scene scene = new Scene(borderPane);
    primaryStage.setTitle("Game"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage   
    
    diceNum=0;
    turn = false;    
    
     //create player objects with player id "P1" and "P2"
    playerList = new Player [2];
  playerList[0] = new Player();
  playerList[1] = new Player();
  playerList[0].setUsername("Player1");
  playerList[1].setUsername("Player2");   
    
    //initialize blockData array
        //no reload from last map yet
    blockData = new Block [blockNum];
       for (int i=0;i<blockNum;i++){
        blockData[i]= new Block();
        blockData[i].setBlockId(i);
        String newType;
        //TODO set which block is event type
        if (i%3 == 0){
            newType="Event";
        }
        else{
            newType="Land";
        }
        
        blockData[i].setLandType(newType);
    }
  }
  
 
  
    public static void main(String[] args) {
    launch(args);
  }
  
  /***************************************
  /*User Interface
  /****************************************/  
  
  private HBox getTop(){
      HBox hBox = new HBox();
      for (int i=0;i<10;i++){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
          Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          hBox.getChildren().add(blockPaneArray[i]);
      }
      return hBox;
  }
  
   private HBox getBottom(){
      HBox hBox = new HBox();
      for (int i=24;i>14;i--){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0 ,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
           Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          hBox.getChildren().add(blockPaneArray[i]);
      }
      return hBox;
  }
  
  private VBox getRight(){
      VBox vBox = new VBox();
      for (int i=10;i<15;i++){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
           Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          vBox.getChildren().add(blockPaneArray[i]);
      }
      return vBox;
  }
  
  private VBox getLeft(){
      VBox vBox = new VBox();
      for (int i=29;i>24;i--){
          blockPaneArray[i] = new GridPane();
          blockPaneArray[i].setPadding(new Insets(10, 10, 10, 10));
          blockPaneArray[i].setPrefSize(100,100);
          blockPaneArray[i].setStyle("-fx-border-color: black");
          blockPaneArray[i].add(new Label("Block ID"+ (i+1)), 0, 0,2,1);
          blockPaneArray[i].add(new Label("Building ID:"), 0, 1,2,1);
          blockPaneArray[i].add(new Label("Land Type:"), 0, 2,2,1);
           Rectangle playerIndicator = new Rectangle(0,0,20,20);
          playerIndicator.setFill(null);
          playerIndicator.setStroke(Color.BLACK);
          blockPaneArray[i].add(playerIndicator, 0, 3,1,1);
          vBox.getChildren().add(blockPaneArray[i]);
      }
      return vBox;
  }
  
  private BorderPane getCenter(){
      BorderPane pane = new BorderPane();
      pane.setCenter(getDice());
      stop_Save =new Button("Stop & Save map to database");
      pane.setBottom(stop_Save);
      stop_Save.setOnAction(e -> stopNSave());
      return pane;
  }
  
    
  private GridPane getDice(){
      GridPane pane= new GridPane();
      turnLabel = new Label("Player's turn");
      diceResult = new Label("default");
      rollDice = new Button("Roll Dice");
      announce = new Label("Let the games begin!");
      pane.add(turnLabel, 0, 0, 2,1);
      pane.add(rollDice, 0, 1, 1,1);
      pane.add(diceResult, 1, 1, 1,1);
      pane.add(announce, 0,2,1,1);
    //  try{
      rollDice.setOnAction(e -> roll_improved());
   //   }
     // catch(SQLException e){e.printStackTrace();}
    //  catch(ClassNotFoundException e){e.printStackTrace();}
      
      return pane;
  }

  /***************************************
  /*Database
  /****************************************/
  
   private void initializeDB() {
    try {
      // Load the JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      System.out.println("Driver loaded");
     
      // Establish a connection
      Connection connection = DriverManager.getConnection
        ("jdbc:mysql://localhost/mydb", "root", "weirdal"); 
    //sub with your own database name,  username and password

      System.out.println("Database connected");


    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
 
   
  /***************************************
  /*Event Handler
  /*Improved version of roll() 
  /*Most functions called in roll_improved() 
  /* 
  /****************************************/
   
   private void roll_improved(){
        //roll dice
       diceNum=21;
       diceResult.setText(Integer.toString(diceNum));
       int playerIndex;
       //variables hold data of curren player, shared by all players
       int location;//location of current player
       
       //check whose turn. 
       //currently using boolean toggle for 2 players. will use int for more players.
       //this if-else structure contains duplicate codes for both players because:
       // checking if player has made round trip involves both old and new location
       // display (setStyle) is not working properly, further test needed.
        if (turn==false){
           //set current player as P1 by using array index of playerList
           playerIndex = 0;
        }
        else
           playerIndex = 1;
             // check if player has made one round trip
        if((playerList[playerIndex].getLocation()+diceNum)%30<playerList[playerIndex].getLocation()){ 
            ArrayList <Building> buildings = playerList[playerIndex].getBuildings();
            Player player = playerList[playerIndex];
            int goldReward = 0;
            int armyReward = 0;
                
            for (int counter = 0; counter < buildings.size() ; counter++){
                Building b = buildings.get(counter);
                if (b.getType().equals("Factory"))
                    player.setGold(player.getGold() + b.getResourceOutput());
                else if (b.getType().equals("Castle"))
                    player.setArmy(player.getArmyNumber() + b.getResourceOutput());
            }
            announce.setText(playerList[playerIndex].getUsername() + " earned " + 
                    goldReward + " gold and " + armyReward + " soldiers for "
                    + "passing Start!");
        }
           
           //reset border color and width before leaving current block (a.k.a. adding dice number)
        blockPaneArray[playerList[playerIndex].getLocation() %30].setStyle("-fx-border-color: #000000 ;-fx-border-width: 1");
           //update location with dice number
        playerList[playerIndex].setLocation((playerList[playerIndex].getLocation()+diceNum)%30);
        System.out.println(playerList[playerIndex].getLocation());
           //set curren block border color and width
        if (playerIndex == 0)
            blockPaneArray[playerList[playerIndex].getLocation()].setStyle("-fx-border-color: #ff0000; -fx-border-width: 8");
        else
           blockPaneArray[playerList[playerIndex].getLocation()].setStyle("-fx-background-color: #00FFFF;"); 
      
       //assign current player's value to shared variables
       location = playerList[playerIndex].getLocation(); 
      
       
       //shared functions
       
       
        if (blockData[location].getLandType().equals("Event")){
            executeEvent(playerIndex, location);
        }
        else{
            BuildingEvent(playerIndex, blockData[location]);
        }
      System.out.println(turn);
       turn = !turn; // switch turn 
   }
   
   
   private void stopNSave(){
       
       //display info in output
       System.out.println("Current map data saved");
       for (int i=0;i<blockNum;i++){
           int blockId=blockData[i].getBlockId();
           String landType=blockData[i].getLandType();
           System.out.format("Block%d ID is %s , landType is %s", i, blockId,landType);
           System.out.println();
       }
   }
   
   
   private void linkDatabase(){
        /*
            try {
             Connection connection = DriverManager.getConnection
        ("jdbc:mysql://localhost/game", "root", "bhcc"); 
             Statement statement = connection.createStatement();
             //String queryString = "insert into Student (firstName, mi, lastName) " + "values (?, ?, ?)";
             //PreparedStatement preparedStatement = connection.prepareStatement(queryString);
             /** TEMP DELETE
            ResultSet resultSet = statement.executeQuery("update map set ownerId="
                    + " 'p1' where blockId='blo'+ Convert(Varchar,location1) ");
                    
            } catch(SQLException e){e.printStackTrace();}
          */
       
         /*
            try {
              Connection connection = DriverManager.getConnection
        ("jdbc:mysql://localhost/game", "root", "bhcc"); 
             Statement statement = connection.createStatement();
             //String queryString = "insert into Student (firstName, mi, lastName) " + "values (?, ?, ?)";
             //PreparedStatement preparedStatement = connection.prepareStatement(queryString);
             /*
            ResultSet resultSet = statement.executeQuery("update map set ownerId="
                    + " 'p2' where blockId='blo'+ Convert(Varchar,location2) ");
                     
             ResultSet resultSet = statement.executeQuery("update map set ownerId="
                    + " 'p2' where blockId='blo'+ Convert(Varchar,location2) ");
            } catch(SQLException e){e.printStackTrace();}
                    */
   }
   
   //**********************Code for ExecuteEvent***************************
    public void executeEvent(int playerIndex, int location){
        if (location == 9){
            boolean emptySpace = false;
                        // Player gets free building in the first empty building
                        //space. 
            for (int counter = 1; emptySpace == false && counter < 30; 
                counter++){
                if (blockData[counter].getBuildingId() == 0){
                    callFreeDialog(blockData[counter], playerIndex);
                    blockData[counter].setBuidlingId(playerList[playerIndex]
                            .getBuildings().size());
                    emptySpace = true;
                }              
            }
            if (emptySpace == false){
                ArrayList <Building> buildings = playerList[playerIndex].getBuildings();
                boolean notMax = false;
                
                for (int counter = 0; counter < buildings.size() && notMax == false; counter++){
                    Building b = buildings.get(counter);
                    if(b.getLevel() < 10){
                        b.upgrade(-1);
                        announce.setText(playerList[playerIndex].getUsername() + " got a "
                        + "free upgrade!");
                        notMax = true;
                    }
                }
                if (notMax == false){
                    announce.setText(playerList[playerIndex].getUsername() + " has no "
                        + "buildings to upgrade.");
                }
            }          
        }
        else{
            callEventDialog(location, playerIndex);
        }
    }
    
    public void callFreeDialog(Block block, int playerIndex){
        Stage dialogStage = new Stage();
        VBox vbox = new VBox();
        ComboBox comboBox = new ComboBox();
        Label instructions = new Label();
        instructions.setWrapText(true);
        Button OK = new Button("OK");
        OK.setDisable(true);
        
        instructions.setText("Choose a free building! ");
        comboBox.getItems().addAll("Hotel", "Factory", "Castle");
        comboBox.setValue("Hotel");
        vbox.getChildren().addAll(instructions, comboBox, OK);
        
        OK.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                String selection = comboBox.getValue().toString();
                String player = playerList[playerIndex].getUsername();
                try{
                    Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost/mydb","root", "weirdal");
                    System.out.println("Database Connected.");
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("select * from "
                        + "BuildingStartValues where Type = '" + selection +"'");
                    resultSet.next();
                    Building newB = new Building(
                        selection, player, resultSet.getInt("UpgradePrice"), 
                        resultSet.getInt("MaximumDefense"),
                        resultSet.getInt("ResourceOutput"));
                    playerList[playerIndex].getBuildings().add(newB);
                    blockData[block.getBlockId()].setOwnerId(playerIndex);
                    blockData[block.getBlockId()].setBuidlingId(newB.getBuildID());
                    announce.setText(playerList[playerIndex].getUsername() + 
                                " got a free " + selection + "!");
                }catch(SQLException e){}
            }
        });
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(vbox, 400, 250));
        dialogStage.showAndWait();
    }
    
    public void callEventDialog(int location, int playerIndex){
        Stage dialogStage = new Stage();
        VBox vbox = new VBox();
        Label instructions = new Label("Roll the die to determine your fate.");
        Label rollResult =  new Label();
        instructions.setWrapText(true);
        Button roll = new Button("Roll Die");
        Button OK = new Button("OK");
        vbox.getChildren().addAll(instructions, roll, rollResult, OK);
        
        int enemyIndex;
        
        if (playerIndex == 0)
            enemyIndex = 1;
        else
            enemyIndex = 0;
            
        roll.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                int num = (int)(Math.random()*6+1);
                int result;
                Player p = playerList[playerIndex];
                
                switch (location){
                    case 3:     result = num * 100;
                                instructions.setText("You lost " + result + " gold!");
                                rollResult.setText(num +"*100 = " + result);
                                playerList[playerIndex].setGold(p.getGold()- result);
                                announce.setText(p.getUsername() + " lost " + result
                                        + " gold!");
                                break;
                    case 6:     result = num * 50;
                                instructions.setText(playerList[enemyIndex].getUsername() +
                                        " stole " + result + " gold!");
                                rollResult.setText(num +"*50 = " + result);
                                playerList[playerIndex].setGold(p.getGold()- result);
                                playerList[enemyIndex].setGold(p.getGold()+ result);
                                announce.setText(instructions.getText());
                                break;
                    case 12:     num = (int)(Math.random()* p.getBuildings().size());
                                Building building = p.getBuilding(num);
                                if (building.downgrade()){
                                    instructions.setText("Your " + building.getType() +
                                            " was downgraded to level " + building.getLevel());
                                    announce.setText(p.getUsername() + "'s " + 
                                            building.getType() + " was downgraded "
                                            + "to level " + building.getLevel());
                                }
                                else{
                                    instructions.setText("Your " + building.getType() +
                                            " was not downgraded");
                                    announce.setText(p.getUsername() + "'s " + 
                                            building.getType() + " was not"
                                            + "downgraded.");

                                }
                                rollResult.setText(Integer.toString(num));
                                break;
                    case 15:    result = p.getBuildings().size() * num * 100;
                                instructions.setText("You lost " + result + " gold!");
                                rollResult.setText(num +"*" + p.getBuildings().size() 
                                        + "*" + 100 + "=" + result);
                                playerList[playerIndex].setGold(p.getGold()- result);
                                announce.setText(p.getUsername() + " lost " + result
                                        + " gold!");
                                break;
                    case 18:    result = p.getBuildings().size() * num * 100;
                                instructions.setText("You gained " + result + " gold!");
                                rollResult.setText(num +"*" + p.getBuildings().size() 
                                        + "*" + 100 + "=" + result);
                                playerList[playerIndex].setGold(p.getGold()+ result);
                                announce.setText(p.getUsername() + " gained " + result
                                        + " gold!");
                                break;
                    case 21:    roll_improved();
                                dialogStage.close();
                                break;
                    case 24:    result = num * 100;
                                instructions.setText("Both players lose " + result
                                + " gold and soldiers.");
                                rollResult.setText(num +"*100 = " + result);
                                playerList[playerIndex].setGold(p.getGold()- result);
                                playerList[enemyIndex].setGold(p.getGold()- result);
                                playerList[playerIndex].setArmy(p.getGold()+ result);
                                playerList[enemyIndex].setArmy(p.getGold()+ result);  
                                announce.setText(instructions.getText());
                }
            }
        });
        OK.setOnAction(e -> dialogStage.close());
        
        
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(vbox, 400, 250));
        dialogStage.showAndWait();
    }
   //********************Code for Building Events**************************
    public void BuildingEvent(int playerIndex, Block block){
        String command;
        if (block.getBuildingId() == 0 )
            command = "Create";
        else
        {
            if(block.getOwnerId() == playerIndex){
                command = "Improve";
            }
            else
                command = "Enemy";
        }
        callDialog(command, block, playerIndex);
    }
    
    public void callDialog(String command, Block block, int playerIndex){
        Stage dialogStage = new Stage();
        VBox vbox = new VBox();
        ComboBox comboBox = new ComboBox();
        Label instructions = new Label();
        instructions.setWrapText(true);
        Label playerGold = new Label(playerList[playerIndex].getUsername() +"'s gold: " +
                    playerList[playerIndex].getGold());
        Label playerArmy = new Label(playerList[playerIndex].getUsername() +"'s army: " +
                    playerList[playerIndex].getArmyNumber());
        Button proceed = new Button("Proceed");
        Button OK = new Button("OK");
        OK.setDisable(true);
        Button cancel = new Button("Cancel");
        
        int enemyIndex;
            if (playerIndex == 0)
                enemyIndex = 1;
            else
                enemyIndex = 0;
        Building playerB = playerList[playerIndex].getBuilding(block.getBuildingId());
        Building enemyB = playerList[enemyIndex].getBuilding(block.getBuildingId());
        
        if (command.equals("Create")){
            instructions.setText("Do you want to buy a new building?");
            comboBox.getItems().addAll("Yes", "No");
            comboBox.setValue("Yes");
            vbox.getChildren().addAll(instructions, playerGold, comboBox, proceed,
                    OK, cancel);
            
        }
        else if (command.equals("Improve")){
            Label buildingLevel = new Label("Building level: " + playerB.getLevel());
            Label buildingArmy = new Label("Current Soldiers: " + playerB.getCurrentDefense());
            instructions.setText("Do you want to improve this building?");
            comboBox.getItems().addAll("Yes", "No");
            comboBox.setValue("Yes");
            vbox.getChildren().addAll(instructions, playerGold, playerArmy, 
                    buildingLevel, buildingArmy, comboBox, proceed, OK, cancel);
        }
        else if (command.equals("Enemy")){
            Label buildingArmy = new Label("Enemy Army: " + enemyB.getCurrentDefense()) ;
            instructions.setText("You must give your opponent " + 
                    enemyB.getResourceOutput() + " gold. Will you pay or fight?");
            comboBox.getItems().addAll("Pay", "Fight");
            comboBox.setValue("Pay");
            vbox.getChildren().addAll(instructions, playerGold, playerArmy, 
                    buildingArmy, comboBox, proceed, OK);
        }
        
        proceed.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                Label instructions = (Label)vbox.getChildren().get(0);
                String selection = comboBox.getValue().toString();
                
                if (command.equals("Create")){
                    if (selection.equals("Yes")){
                        comboBox.getItems().clear();
                        if(playerList[playerIndex].getGold() >= 500){
                            instructions.setText("Choose the Building you want to buy.");
                            comboBox.setValue("Hotel- 500");
                            comboBox.getItems().add("Hotel- 500");
                            if (playerList[playerIndex].getGold() >= 750){
                                comboBox.getItems().add("Factory- 750");
                                if (playerList[playerIndex].getGold() >= 1000){
                                    comboBox.getItems().add("Castle- 1000");
                                }
                            }
                        }
                        else{
                            instructions.setText("You can't afford a building. Press OK.");
                            comboBox.setValue("Not Enough");
                        }
                        
                        proceed.setDisable(true);
                        OK.setDisable(false);
                        cancel.setDisable(false);
                    }
                    else{
                        announce.setText(playerList[playerIndex].getUsername()
                                + " chose not to buy a building.");
                        dialogStage.close();
                    }
                }
                else if (command.equals("Improve")){
                    if(selection.equals("Yes")){
                        comboBox.getItems().clear();
                        instructions.setText("Do you want to upgrade the building,"
                             + " add soldiers, or remove soldiers from it?");
                        if (playerList[playerIndex].getGold() >= playerB.getUpgradePrice()
                                && playerB.getLevel() < 10){
                            comboBox.getItems().add("Upgrade- " + playerB.getUpgradePrice()
                                + " gold");
                            comboBox.setValue("Upgrade- " + playerB.getUpgradePrice()
                                + " gold");
                            if(playerList[playerIndex].getArmyNumber() > 0)
                                comboBox.getItems().add("Add Soldiers- Max " + 
                                    playerB.getMaxDefense());
                            if(playerB.getCurrentDefense() > 0)
                                comboBox.getItems().add("Remove Soldiers- Max " 
                                    + playerB.getCurrentDefense());
                        }   
                        else if(playerList[playerIndex].getArmyNumber() > 0){
                            comboBox.setValue("Add Soldiers- Max " + 
                                    playerB.getMaxDefense());
                            comboBox.getItems().addAll("Add Soldiers- Max " + 
                                    playerB.getMaxDefense(), 
                                "Remove Soldiers- Max " + playerB.getCurrentDefense());
                        }
                        else if(playerB.getCurrentDefense() > 0){
                            comboBox.setValue("Remove Soldiers- Max " 
                                    + playerB.getCurrentDefense());
                            comboBox.getItems().add("Remove Soldiers- Max " 
                                    + playerB.getCurrentDefense());
                        }
                        else{
                            instructions.setText("You lack the necessary resources"
                                    + ". Press OK.");
                            comboBox.setValue("Not Enough");
                            proceed.setDisable(true);
                            OK.setDisable(false);
                            cancel.setDisable(false);
                        }
                    }
                    else{
                        announce.setText(playerList[playerIndex].getUsername()
                                + " chose not to improve the building.");
                        dialogStage.close();
                    }
                    
                    proceed.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String[] s = comboBox.getValue().toString().split("-");
                            String selection = s[0];
                            comboBox.setDisable(true);
                            proceed.setDisable(true);
                            OK.setDisable(false);
                            cancel.setDisable(false);
                            if (selection.equals("Upgrade")){

                                instructions.setText("You must pay " + 
                                        playerB.getUpgradePrice() + " gold.\rPress"
                                        + " OK to proceed or Cancel to cancel this"
                                        + " transaction.");
                            }
                            else if(selection.equals("Add Soldiers")){
                                instructions.setText("How many soldiers do you "
                                        + "want to add?");
                                vbox.getChildren().set(5, new TextField());
                                
                            }
                            else{
                                instructions.setText("How many soldiers do you "
                                        + "want to remove?");
                                vbox.getChildren().set(5, new TextField());
                                
                            }
                        }
                    });
                }
                else{
                    if (selection.equals("Pay")){
                        int newGoldP, newGoldE;
                        
                        if(enemyB.getType().equals("Hotel")){
                            newGoldP = playerList[playerIndex].getGold() - 
                                enemyB.getResourceOutput();
                            newGoldE = playerList[enemyIndex].getGold() +  
                                enemyB.getResourceOutput();
                        }
                        else{
                            newGoldP = playerList[playerIndex].getGold() - 
                                100 * enemyB.getLevel();
                            newGoldE = playerList[playerIndex].getGold() +
                                100 * enemyB.getLevel();
                        }
                        playerList[playerIndex].setGold(newGoldP);
                        playerList[enemyIndex].setGold(newGoldE);
                        announce.setText(playerList[playerIndex].getUsername() + " paid "
                        + playerList[enemyIndex].getUsername() + " " + 
                        enemyB.getResourceOutput() + " gold.");
                        dialogStage.close();
                    }
                    else{
                        attack(dialogStage, playerIndex, block);
                    }
                }     
            }
        });
        
        OK.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                String[] s = comboBox.getValue().toString().split("-");
                String selection = s[0];
                String player =playerList[playerIndex].getUsername();
                int playerGold = playerList[playerIndex].getGold();
                if (command.equals("Create")){
                    try{
                        Connection connection = DriverManager.getConnection(
                                    "jdbc:mysql://localhost/mydb","root", "weirdal");
                            System.out.println("Database Connected.");
                            Statement statement = connection.createStatement();
                             ResultSet resultSet = statement.executeQuery("select * from "
                            + "BuildingStartValues where Type = '" + selection +"'");
                            resultSet.next();
                        Building newB = new Building(
                                selection, player, resultSet.getInt("UpgradePrice"), 
                                resultSet.getInt("MaximumDefense"),
                                resultSet.getInt("ResourceOutput"));
                        playerList[playerIndex].getBuildings().add(newB);
                        playerList[playerIndex].setGold(playerGold - 
                                resultSet.getInt("SalePrice"));
                        
                        blockData[block.getBlockId()].setOwnerId(playerIndex);
                        blockData[block.getBlockId()].setBuidlingId(newB.getBuildID());
                        announce.setText(player + 
                                " bought a " + selection + "!");
                        
                    }catch(SQLException e){}
                }
                else if (command.equals("Improve")){
                    if (selection.equals("Upgrade")){
                        playerList[playerIndex].setGold(
                               playerList[playerIndex].getBuilding(block.getBuildingId())
                                       .upgrade(playerGold));
                       
                        announce.setText(player + "'s " + playerB.getType() +
                                " was upgraded to level " + playerB.getLevel() 
                                + "!\r");
                    }
                    else{
                        TextField textField = (TextField)vbox.getChildren().get(5);
                        String text = textField.getText();
                        boolean isInteger = isNumeric(text);
                        
                        if (isInteger){
                           int soldiers = Integer.parseInt(text);
                           boolean isValid;
                           
                            if (selection.equals("Add Soldiers")){
                               isValid = playerList[playerIndex].getBuilding
                                    (block.getBuildingId()).raiseSoldierDefense
                                        (soldiers);

                                if (isValid){
                                announce.setText("There are now " + 
                                       playerB.getCurrentDefense() +
                                      " soldiers guarding this Building.");
                                }
                                else{
                                    instructions.setText("Only " + playerB.getMaxDefense()
                                       + "soldiers can be deployed. Please try again.");
                                } 
                            }
                            else if (selection.equals("Remove Soldiers")){
                                isValid = playerList[playerIndex].getBuilding
                                (block.getBuildingId()).removeSoldiers(soldiers);
                           
                                if (isValid){
                                    announce.setText("There are now " + 
                                          playerB.getCurrentDefense() +
                                        " soldiers guarding this Building.");
                                }
                                else{
                                    instructions.setText("There are only " + 
                                            playerB.getMaxDefense()
                                         + " soldiers deployed here. Please try again.");
                                } 
                            } 
                        }
                        else{
                            instructions.setText("Invalid data. Please try again.");
                        }
                    }                  
                }
               dialogStage.close();
            }
        });
        
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                announce.setText(playerList[playerIndex].getUsername() + 
                        " chose to do nothing.");
                dialogStage.close();
            }
        });
        
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(vbox, 400, 250));
        dialogStage.showAndWait();
        
    }
    public void attack(Stage dialogStage, int playerIndex, Block block){
        announce.setText("This means war!");
        int enemyIndex;
        
        if (playerIndex == 0)
            enemyIndex = 1;
        else
            enemyIndex = 0;
        Player p = playerList[playerIndex];
        Player e = playerList[enemyIndex];
        
        dialogStage.close();
        int attackSoldiers = (int) (Math.random() * 6 + 1) * 100;
        if (attackSoldiers < p.getArmyNumber()){
            attackSoldiers = p.getArmyNumber();
        }
        int attackPower = (int) (Math.random() * 6 + 1);
        int defenseSoldiers = playerList[playerIndex].getBuilding
            (block.getBuildingId()).getCurrentDefense();
        int defensePower = (int) (Math.random() * 6 + 1);
        int totalAttack = attackSoldiers * attackPower;
        int totalDefense = defenseSoldiers * defensePower;
        int gold = totalAttack - totalDefense;
        
        if (gold < 0){
            playerList[playerIndex].setGold(p.getGold() + gold);
            p.setArmy(p.getArmyNumber() - attackSoldiers);
            e.setArmy(e.getArmyNumber() - (int)(attackSoldiers * .8));
        }
        else if(gold > 0){
            playerList[enemyIndex].getBuildings().get(block.getBuildingId()).
                    setOwner(p.getUsername());
            p.setGold(p.getGold()- (totalDefense/2));
            p.setArmy(p.getArmyNumber() - (attackSoldiers / 2));
        }
        else{
        }
        
        announce.setText("Player Army: " + attackPower + "\r" 
                + "Player Power: ");
        
        
    }
    public boolean isNumeric(String text){
        if (!text.isEmpty()){
            boolean isNumeric = true;
            for (int counter = 0; isNumeric == true && 
                counter < text.length(); counter++){
                    if (!Character.isDigit(text.charAt(counter)))
                        return false;
            }
            return true;
        }
        else{
            return false;
        }
    }
}



 

