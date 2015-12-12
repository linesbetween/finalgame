package finalgame;

//this class extends flowPane creates the field under first player field
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
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
public class BottomButton extends FlowPane{
    //create image for the roll button
    private ImageView diceV=new ImageView(new Image("image/dice.jpg"));
    //create exit roll, save button
    private Button exit=new Button("Exit Game");
    private Button roll=new Button(null,diceV);
    private Button save=new Button("Save");
    //create label for the dice number player gets
    
    Label diceResult = new Label(null);
    
    //create the bottom field
    public BottomButton(){
        //set size and style for the label for dice number
        diceResult.setFont(Font.font("Times", FontWeight.BOLD, 35));
        diceResult.setStyle("-fx-background-color: white");
        diceResult.setAlignment(Pos.CENTER);
        
        
        //set style for the buttons
        exit.setStyle("-fx-border-color: red ;-fx-border-width: 3");
        roll.setStyle("-fx-border-color: green ;-fx-border-width: 3");
        save.setStyle("-fx-border-color: orange ;-fx-border-width: 3");
        //set the size for the dice button image
        diceV.setFitHeight(30);
        diceV.setFitWidth(30);
        //set the gap of this pane
        setHgap(95);
        setVgap(5);
        //add all nodes
        getChildren().addAll(save,roll,diceResult,exit);
        
    }
    //pass button roll
    public Button getRollButton(){
        return roll;
    }
    //pass button exit
    public Button getExitButton(){
        return exit;
    }
    //pass button save
    public Button getSaveButton(){
        return save;
    }
    //set dice number on the label
    void setDice(int num){
        diceResult.setPrefSize(50, 50);
        diceResult.setText((Integer.toString(num)));
    }
    
    
    
}
