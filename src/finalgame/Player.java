/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package finalgame;

import java.util.ArrayList;

/**
 *
 * @author Christopher
 */
public class Player {
    private int gold = 5000 , armyNumber = 1000, location = 0;
    private String username;
    private ArrayList<Building> buildings = new ArrayList<>();
        
        void setGold(int gold) {
            if (gold < 0){
                this.gold = 0;
            }
            else
                this.gold = gold;
        }
        
        void setArmy(int army) {
            if (army < 0){
                armyNumber = 0;
            }
            else
                armyNumber = army;
        }

        void setLocation(int location){
            this.location = location;
        }
        void setUsername(String username){
            this.username = username;
        }

        int getGold() {
            return gold;
        }

        int getArmyNumber() {
            return armyNumber;
        }
        
        ArrayList<Building> getBuildings() {
            return buildings;
        }
        
        Building getBuilding(int buildID){
            for (int counter = 0; counter < buildings.size(); counter++){
                if (buildings.get(counter).getBuildID() == buildID){
                    return buildings.get(counter);
                }
            }
            return null;
        } 
        
        int getLocation(){
            return location;
        }
        
        String getUsername(){
            return username;
        }
}
