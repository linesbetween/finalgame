package finalgame;

//this class extends scrollpane, gathers all the action players did
//and shows in field


import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
public class GatherInfo extends ScrollPane{
    
    //create text area
    private TextArea text=new TextArea();
    
    public GatherInfo(){
        //set size of text area
        text.setPrefColumnCount(30);
        text.setPrefRowCount(3);
        text.setEditable(false);
        text.setWrapText(true);
        text.setFont(new Font("Serif",14));
        //set first textin the field
        text.setText("Welcome to Our Game!!!\n");
        //add text to the pane
        
        setContent(text);
        
    }
    //add information to text area
    void setActionInfo(String info){
        
        this.text.appendText(info);
    }
}
