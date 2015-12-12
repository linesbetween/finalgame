/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ct-d119
 */
public class ExecuteEvent {
    
     public void ExecuteEvent(int playerIndex, int location, MapBlock blocks[], 
             Player playerList[], GatherInfo actionInfo){
        if (location == 9){
            boolean emptySpace = false;
                        // Player gets free building in the first empty building
                        //space. 
            for (int counter = 1; emptySpace == false && counter < 30; 
                counter++){
                if (blocks[counter].getBuildingId() == 0){
                    callFreeDialog(location, playerIndex, blocks, playerList, actionInfo);
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
                        actionInfo.setActionInfo(playerList[playerIndex].getUsername()
                                + " got a " + "free upgrade!");
                        notMax = true;
                    }
                }
                if (notMax == false){
                    actionInfo.setActionInfo(playerList[playerIndex].getUsername() + " has no "
                        + "buildings to upgrade.");
                }
            }          
        }
        else{
            callEventDialog(location, playerIndex, playerList, actionInfo);
        }
    }
    
    public void callFreeDialog(int playerIndex, int location, MapBlock blocks[], 
             Player playerList[], GatherInfo actionInfo){
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
                    blocks[location].setOwner(playerList[playerIndex].getUsername());
                    blocks[location].setBuildingId(newB.getBuildID());
                    actionInfo.setActionInfo(playerList[playerIndex].getUsername() + 
                                " got a free " + selection + "!");
                }catch(SQLException e){}
            }
        });
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(vbox, 400, 250));
        dialogStage.showAndWait();
    }
    
    public void callEventDialog(int playerIndex, int location, Player playerList[], 
            GatherInfo actionInfo){
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
                Map map = new Map();
                int num = (int)(Math.random()*6+1);
                int result;
                Player p = playerList[playerIndex];
                
                switch (location){
                    case 3:     result = num * 100;
                                instructions.setText("You lost " + result + " gold!");
                                rollResult.setText(num +"*100 = " + result);
                                playerList[playerIndex].setGold(p.getGold()- result);
                                actionInfo.setActionInfo(p.getUsername() + " lost " + result
                                        + " gold!");
                                break;
                    case 6:     result = num * 50;
                                instructions.setText(playerList[enemyIndex].getUsername() +
                                        " stole " + result + " gold!");
                                rollResult.setText(num +"*50 = " + result);
                                playerList[playerIndex].setGold(p.getGold()- result);
                                playerList[enemyIndex].setGold(p.getGold()+ result);
                                actionInfo.setActionInfo(instructions.getText());
                                break;
                    case 12:     num = (int)(Math.random()* p.getBuildings().size());
                                Building building = p.getBuilding(num);
                                if (building.downgrade()){
                                    instructions.setText("Your " + building.getType() +
                                            " was downgraded to level " + building.getLevel());
                                    actionInfo.setActionInfo(p.getUsername() + "'s " + 
                                            building.getType() + " was downgraded "
                                            + "to level " + building.getLevel());
                                }
                                else{
                                    instructions.setText("Your " + building.getType() +
                                            " was not downgraded");
                                    actionInfo.setActionInfo(p.getUsername() + "'s " + 
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
                                actionInfo.setActionInfo(p.getUsername() + " lost " + result
                                        + " gold!");
                                break;
                    case 18:    result = p.getBuildings().size() * num * 100;
                                instructions.setText("You gained " + result + " gold!");
                                rollResult.setText(num +"*" + p.getBuildings().size() 
                                        + "*" + 100 + "=" + result);
                                playerList[playerIndex].setGold(p.getGold()+ result);
                                actionInfo.setActionInfo(p.getUsername() + " gained " + result
                                        + " gold!");
                                break;
                    case 21:    map.roll();
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
                                actionInfo.setActionInfo(instructions.getText());
                }
            }
        });
        OK.setOnAction(e -> dialogStage.close());
        
        
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(vbox, 400, 250));
        dialogStage.showAndWait();
    }

}
