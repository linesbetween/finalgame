package finalgame;

//create the start Page for the game

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
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
 * @author PC
 */
public class StartPage extends BorderPane{
    //create the first start page object
    private BorderPane start1;
    //create button to start game
    private Button b1=new Button("Start Game");
    //create field to enter user name
    public TextField t1=new TextField();
    //set the warning  informatin
    private String warning="Please Enter a User Name";
    private Label warnInfo=new Label(warning);
    
    public StartPage(Button b1){
       
       setStart(b1);
       
    }
    //set the first start Page 
    void setStart (Button b2){
        //create the background for the game
        start1=new BorderPane();
        Image image1=new Image("image/background.jpg");
        ImageView view1=new ImageView(image1);
//set the size of image
        view1.setFitHeight(700);
        view1.setFitWidth(1000);
        //set the size of button for first start page
        b1.setScaleX(2);
        b1.setScaleY(3);
        b1.setShape(new Circle(1));
        //add nodes to the start page
        start1.getChildren().add(view1);
        start1.setCenter(b1);
        setCenter(start1);
        //set the action to access the second start page
        b1.setOnAction(e->{
           getChildren().remove(start1);
           b1=b2;
           setAccount();
          
       });
        
    }
    //set the second start page
    void setAccount(){
        //create the nodes of the second start page
        start1=new BorderPane();
        //set the warning text 
        warnInfo.setStyle("-fx-background-color: #ffb3b3;");
        warnInfo.setFont(Font.font(null, FontWeight.BOLD, FontPosture.ITALIC, 20));
        
        VBox vbox=new VBox(50);
        //create the background of the page
        Image image2=new Image("image/right.jpg");
        ImageView view2=new ImageView(image2);
        //set the size of the label and image
        vbox.setAlignment(Pos.CENTER);
        t1.setAlignment(Pos.CENTER);
        t1.setMaxSize(200, 30);
        view2.setFitHeight(700);
        view2.setFitWidth(1000);
        //add nodes to the page
        vbox.getChildren().addAll(warnInfo,t1,b1);
        start1.getChildren().add(view2);
        
        start1.setCenter(vbox);
        setCenter(start1);
        
    }
    //get the user name
    public String getUserId(){
        return t1.getText();
    }
    //set the user name
    public void setUserId(String id){
        t1.setText(id);
    }
    //set the warning information
    void setWarning(String info){
        warnInfo.setText(info);
    }
    
    
}
