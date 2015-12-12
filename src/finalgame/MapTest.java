package finalgame;

/*
 *  
 * copy IniMap.sql in to project root folder since it's the current running directory.
 * create an empty database (in this example, database name is game) before run.
 * the map table will be created at run time.
 * the ScriptRunner.java and Table.java are used to read sql scripts. 
 ***issue: looks like it can't handle loop or comments
 *
 * What this does now: 
 * initilize layout and place map blocks.
 * initilize map table in database.
 * What needs to be done
 * dice randomly generates number generater) to select block (row) in map table.
 * put indicater (like flag or color change) on selected block.
 * assign block to the player who first steps on it. 
 * 
 */



/**
 *
 * @author xhf
 */
import java.io.*;
import java.sql.*;
import java.util.Random;
import javafx.application.Application;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MapTest extends Application {
 
  

  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) {
      
 //create an object of map, which contaiin most of the graphic work
        Map map = new Map();
        
        
        Scene scene = new Scene(map,1000,700);
        
        
        primaryStage.setTitle("Game"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage   
        
        //set the size of the stage
        primaryStage.setResizable(false);
        primaryStage.setHeight(725);
        primaryStage.setWidth(1006);
        
        //set the exit button action to close the stage
        map.control.getExitButton().setOnAction(e->primaryStage.close() );
       
  
  }  
      
    public static void main(String[] args) {
        launch(args);
    }
   
   
}