package finalgame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ct-d116
 */
public class BuildingEvent {
    
    public void BuildingEvent(int playerIndex, MapBlock block, Player playerList[], 
            GatherInfo actionInfo ){
        String command;
        if (block.getBuildingId() == 0 )
            command = "Create";
        else
        {
            if(block.getOwner().equals(playerList[playerIndex].getUsername())){
                command = "Improve";
            }
            else
                command = "Enemy";
        }
        callDialog(command, block, playerIndex, playerList, actionInfo);
    }
    
    public void callDialog(String command, MapBlock block, int playerIndex, 
            Player playerList[], GatherInfo actionInfo){
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
                        actionInfo.setActionInfo(playerList[playerIndex].getUsername()
                                + " chose not to buy a building.\n");
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
                        actionInfo.setActionInfo(playerList[playerIndex].getUsername()
                                + " chose not to improve the building.\n");
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
                        actionInfo.setActionInfo(playerList[playerIndex].getUsername() + " paid "
                        + playerList[enemyIndex].getUsername() + " " + 
                        enemyB.getResourceOutput() + " gold.");
                        dialogStage.close();
                    }
                    else{
                        attack(dialogStage, playerIndex, block, actionInfo, playerList);
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
                        
                        block.setOwner(playerList[playerIndex].getUsername());
                        block.setBuildingId(newB.getBuildID());
                        actionInfo.setActionInfo(player + 
                                " bought a " + selection + "!");
                        
                    }catch(SQLException e){}
                }
                else if (command.equals("Improve")){
                    if (selection.equals("Upgrade")){
                        playerList[playerIndex].setGold(
                               playerList[playerIndex].getBuilding(block.getBuildingId())
                                       .upgrade(playerGold));
                       
                        actionInfo.setActionInfo(player + "'s " + playerB.getType() +
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
                                actionInfo.setActionInfo("There are now " + 
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
                                    actionInfo.setActionInfo("There are now " + 
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
                actionInfo.setActionInfo(playerList[playerIndex].getUsername() + 
                        " chose to do nothing.");
                dialogStage.close();
            }
        });
        
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(new Scene(vbox, 400, 250));
        dialogStage.show();
        
    }
    public void attack(Stage dialogStage, int playerIndex, MapBlock block, 
            GatherInfo actionInfo, Player[] playerList){
        actionInfo.setActionInfo("This means war!");
        int enemyIndex;
        
        if (playerIndex == 0)
            enemyIndex = 1;
        else
            enemyIndex = 0;
        Player p = playerList[playerIndex];
        Player e = playerList[enemyIndex];
        String result = new String();
        
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
            result = p.getUsername() + " lost the battle!\r"
                    + p.getUsername() + " lost " + gold + " gold.\r"
                    + p.getUsername() + " lost " + attackSoldiers + " soldiers.\r" 
                    + e.getUsername() + " lost " + (int) (attackSoldiers *.8);
        }
        else if(gold > 0){
            playerList[enemyIndex].getBuildings().get(block.getBuildingId()).
                    setOwner(p.getUsername());
            p.setGold(p.getGold()- (totalDefense/2));
            p.setArmy(p.getArmyNumber() - (attackSoldiers / 2));
            result = p.getUsername() + " is the new owner of the building!\r"
                    + p.getUsername()+ " lost " + totalDefense/2 + " gold.\r"
                    + p.getUsername() + " lost " + attackSoldiers/2 + " soldiers.\r" 
                    + e.getUsername() + " lost all soldiers guarding the building.\r";
        }
        else{
            p.setArmy(p.getArmyNumber() - attackSoldiers);
            e.setArmy(e.getArmyNumber() - defenseSoldiers);
            result = "The battle is a tie!\r"
                    + "Both sides lose all participating soldiers.\r";
        }
        
        actionInfo.setActionInfo(p.getUsername() + "'s Army: " + attackPower + "\r" 
                + p.getUsername() + "'s Power: " + attackPower + "\r"
                + p.getUsername() + "'s Total Power: " + totalAttack + "\r"
                + e.getUsername() + "'s Defenders: " + defenseSoldiers + "\r"
                + e.getUsername() + "'s Defense Power: " + defensePower + "\r"
                + e.getUsername() + "'s Total Defense: " + totalDefense + "\r"
                + "Result: " + result);
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

