/* Finn O'Leary
 * Intro to AI 449
 * Project 3 - Wumpus World with Propositional Logic
 * 4/25/24
 */

 // This class is for each cell in the Wumpus world board
public class Cell {
    private boolean visited; //if cell is visited
    private boolean safe; //if cell is safe
    private boolean hasWumpus; //if cell has Wumpus
    private boolean hasPit; //if cell has Pit
    private boolean hasGold; //if cell has Gold
    private boolean hasBreeze; //if cell has breeze
    private boolean hasStench; //if cell has stench
    private boolean hasAgent; //if cell has agent

    //contructor
    public Cell(){ //sets everything to false
        this.visited = false;
        this.safe = false;
        this.hasGold = false;
        this.hasPit = false;
        this.hasWumpus = false;
        this.hasAgent = false;
    }

    // Getters and Setters
    public void setHasAgent(boolean agent){
        this.hasAgent = agent;
    }

    public boolean hasBreeze(){
        return this.hasBreeze;
    }
    public boolean hasStench(){
        return this.hasStench;
    }

    public void setBreeze(boolean res){
        this.hasBreeze = res;
    }
    public void setStench(boolean res){
        this.hasStench = res;
    }
    public boolean isVisited() {
        return visited;
    }
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
    public boolean isSafe() {
        return safe;
    }
    public void setSafe(boolean safe) {
        this.safe = safe;
    }
    public boolean hasWumpus() {
        return hasWumpus;
    }
    public void setHasWumpus(boolean hasWumpus) {
        this.hasWumpus = hasWumpus;
    }
    public boolean hasPit() {
        return hasPit;
    }
    public void setHasPit(boolean hasPit) {
        this.hasPit = hasPit;
    }
    public boolean hasGold() {
        return hasGold;
    }
    public void setHasGold(boolean hasGold) {
        this.hasGold = hasGold;
    }

    //toString
    //used when we print the board
    public String toString(){
        StringBuilder ret = new StringBuilder(); //initialize StringBuilder
        if(this.hasAgent){ //current cell has agent
            ret.append("A"); //add 'A'
        }
        else if (!visited && !safe && !hasGold && !hasPit && !hasWumpus && !hasBreeze && !hasStench) {  //current cell has nothing
            ret.append("X"); //add 'X'
        } 
        else { //current cell has something
            if (visited) { //if visited
                ret.append("V"); //add 'V'
            }
            if (hasGold) { //if has gold
                ret.append("G"); //add 'G'
            }
            if (hasPit) { //if has pit
                ret.append("P"); //add 'P'
            }
            if (hasWumpus) { //if has Wumpus
                ret.append("W"); //add 'W'
            }
            if (hasBreeze) { //if has breeze
                ret.append("B"); //add 'B'
            }
            if (hasStench) { //if has stench
                ret.append("S"); //add 'S'
            }
        }
        return ret.toString(); //return StringBuilder as a String
    }

}
