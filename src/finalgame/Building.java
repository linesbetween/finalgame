/*Program:  Building Class Test Program
Programmer: Christopher Bat-Shalom
Last Modified: 11/14/2015
This is the Building Class and it's three subclasses: Hotel, Factory, Castle. 
All starting values within these classes are temporary placeholders until we 
figure out the exact mechanics of the game. These classes have several variables
to keep track of as well as methods to modify these values.
on their hotel.
*/


package finalgame;



/* This is the building superclass which holds all values and methods that are
used by all three types of buildings. It has a default constructor because the
only time a generic building is created is so that the prices for other buildings
can be accessed. All values and formulas are placeholders until we finalize the
system.
*/

     class Building{
        static private int numberOfBuildings = 0;
        final protected int MAX_LEVEL = 10;//max level for a building
    
        private int buildID;
        private String type;//type of building (Hotel, Factory, Castle)
        private String owner;
        private int level = 1;//starting level for all buildings
        private int upgradePrice;//the price to level up the building
        private int currentDefense = 0;
        private int maxDefense;// maximum number of soldiers a building can hold
        private int resourceOutput;// the amount of gold or soldiers given to
        // the owner when they go around the entire board. For a hotel, this 
        //value represents the amount an opponent must pay to the owner when 
        //landing on it.
        
        //constructor used for creating buildings. Values now come from the
        //BuildingStartValues table.
        Building(String type, String owner, int upgradePrice, 
                int maxDefense, int resourceOutput){
            numberOfBuildings++;
            buildID = numberOfBuildings;
            this.type = type;
            this.owner = owner;
            this.upgradePrice = upgradePrice;
            this.maxDefense = maxDefense;
            this.resourceOutput = resourceOutput;
            
        }
        // constructor for creating a building loaded from the database
        Building(int buildID, String type, String owner, int level, 
                int upgradePrice, int currentDefense, int maxDefense, 
                int resourceOutput){
            this.buildID = buildID;
            this.type = type;
            this.owner = owner;
            this.level = level;
            this.upgradePrice = upgradePrice;
            this.currentDefense = currentDefense;
            this.maxDefense = maxDefense;
            this.resourceOutput = resourceOutput;
            numberOfBuildings++;
        }
        
        //getter and setter methods. The only value that is set outside of the
        //class is ownership of the building. Ownership changes only when the
        //building is captured by an enemy. All other values are changed by the
        //methods below.
        public int getBuildID(){
            return buildID;
        }
        public String getType(){
            return type;
        }
        public void setOwner(String name){
            owner = name;
            currentDefense = 0;
        }
        public String getOwner(){
            return owner;
        }
        public int getLevel(){
            return level;
        }
        public int getUpgradePrice(){
            return upgradePrice;
        }
        public int getCurrentDefense(){
            return currentDefense;
        }
        public int getMaxDefense(){
            return maxDefense;
        }
        public int getResourceOutput(){
            return resourceOutput;
        }
        
        //Adds the given number of soldiers to defend the building by changing 
        //the currentSoldiers variable. The value must be less than the maximum 
        //number of soldiers or the method will return false. This method does 
        //not check that given value does not exceed the player's total number 
        //of soldiers. This should be done before calling this function.
        public boolean raiseSoldierDefense(int newSoldiers){
            if (newSoldiers + currentDefense <= maxDefense){
                currentDefense += newSoldiers;
                
                return true;
            }
            else{
                return false;
            }  
        }
        //removes specified number of soldiers from the building
        public boolean removeSoldiers(int soldiers){
            if (currentDefense - soldiers >= 0){
                currentDefense -= soldiers;
                return true;
            }
            else{
                return false;
            }  
        }
        //This function upgrades the building to the next level. It takes the
        //player's total gold and checks to see if there is enough to upgrade
        //the building. If this is the case, the amount needed is subtracted 
        //from the player's gold. The maxSoldiers, upgradePrice, and rewardAmount
        //variables all rise using a formula which rises the nmber based on it's
        //original number. If the starting value is 350, the next level will 
        //raise the value to 700. The program returns the new gold total if the
        //building was upgraded and returns the original total if it was not. 
        //This program checks that the amount of gold is greater than the upgrade
        //price and also that the building is not at maximum level. If the
        //playerGold value is -1, that means this building is recieving a free
        //upgrade due to an event.
        public int upgrade(int playerGold){
            maxDefense += maxDefense/level;
            upgradePrice += upgradePrice/level;
            resourceOutput += resourceOutput/level;
            level++;

            if (playerGold != -1)
                playerGold -= upgradePrice;

            return playerGold;
       }
       public boolean downgrade(){
           if (level > 1){
                maxDefense -= maxDefense/level;
                upgradePrice -= upgradePrice/level;
                resourceOutput -= resourceOutput/level;
                level--;
                return true;
           }
           else
               return false;
       } 
    }