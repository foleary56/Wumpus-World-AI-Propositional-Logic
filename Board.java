/* Finn O'Leary
 * Intro to AI 449
 * Project 3 - Wumpus World with Propositional Logic
 * 4/25/24
 */

 // Neccessary import
 import java.util.Random;

public class Board {
    //instance variables
    private Cell[][] board; //board of cells
    private int[] currentPos; //current position of player

    //constructor
    public Board(){
        //set position of Wumpus and add stench to other cells
        board = new Cell[4][4]; //set board to given size
        for (int i = 0; i < 4; i++) { //all the rows
            for (int j = 0; j < 4; j++) { //all the collumns
                board[i][j] = new Cell(); // create new cell every spot
            }
        }
        this.currentPos = new int[]{0,0}; //starting position set to (0,0)
        this.board[0][0].setHasAgent(true); //(0,0) start with agent

        Random random = new Random(); //create Random
        boolean running = true; //for while loop
        int ran1 = 0; //variable for random number
        int ran2 = 0; //variable for random number
        while(running) //while running
        {
            ran1 = random.nextInt(4); //get random int from 0-3
            ran2 = random.nextInt(4); //get random int from 0-3
            //doesnt place in starting position or adjacent positions to start
            if((ran1==0 && ran2==0)||(ran1==0 && ran2==1)||(ran1==1 && ran2==0)){
                continue; //goes to top of loop again
            }
            else{ // if its not starting position of adjacent to it
                running = false; //no longer have to run to find spot to place Wumpus
            }
        }
        board[ran1][ran2].setHasWumpus(true); //sets the Wumpus true in random cell
        int[][] adjacentSquares = { //all adjacent squares to Wumpus
            {ran1 - 1, ran2}, // Up
            {ran1 + 1, ran2}, // Down
            {ran1, ran2 - 1}, // Left
            {ran1, ran2 + 1}  // Right
        };
        
        // Loop through adjacent squares
        for (int[] square : adjacentSquares) { //goes through all of the adjacent squares
            int x = square[0]; //x value of move
            int y = square[1]; //y value of move
        
            // Check if the adjacent square is within the bounds of the board
            if (x >= 0 && x < 4 && y >= 0 && y < 4) {
                // Set hasStench to true for the adjacent square
                board[x][y].setStench(true); //sets the stench of adjacent cells to true
            } 
        }
        //sets position of Gold randomly
        running = true; //runnning is true
        while(running) // while running
        {
            ran1 = random.nextInt(4); //gets a random int from 0-3 
            ran2 = random.nextInt(4); //gets a random int from 0-3 
            if((ran1==0 && ran2==0)|| board[ran1][ran2].hasWumpus()){ //if random cell has Wumpus or is home spot, continue
                continue; //cell cant have gold and Wumpus
            }
            else{ //any other spot
                running = false; //stop running
            }
        }
        board[ran1][ran2].setHasGold(true); //set gold to true in the spot

        for(int r =0;r<4;r++){ //every row
            for(int c = 0;c<4;c++){ //every column
                //implement code to fill in possible pits and breeze
                //doesnt place in starting position or positions adjacent to start
                //doesnt place pits where gold wumpus or stench is
                if((r==0 && c==0)||(r==0 && c==1)||(r==1 && c==0)||board[r][c].hasWumpus()||board[r][c].hasGold()||board[r][c].hasStench()){
                    continue; //checks if its start or adjacent to start or if it has Wumpus, Gold or adjacent to Wumpus
                }
                else{ //otherwise
                    int probNumber = random.nextInt(5)+1; //gets random int from 1-5
                    //20% chance it gets a pit
                    if(probNumber==1){ //1/5 chance its a pit
                        board[r][c].setHasPit(true); //set pit in position to true

                        int[][] possBreeze = { //posible breeze, adjacent squares
                            {r - 1, c}, // Up
                            {r + 1, c}, // Down
                            {r, c - 1}, // Left
                            {r, c + 1}  // Right
                        };
                        
                        // Loop through adjacent squares
                        for (int[] square : possBreeze) { //all the moves in array
                            int x = square[0]; //x value
                            int y = square[1]; //y value
                        
                            // Check if the adjacent square is within the bounds of the board
                            if (x >= 0 && x < 4 && y >= 0 && y < 4) {
                                // Set breeze to true for the adjacent square
                                board[x][y].setBreeze(true);
                            }
                        }
                    }
                }
            }
        }
    }

    //contructor for the classic Wumpus Game that is studied
    public Board(boolean seed){
        board = new Cell[4][4]; //creates new 2D array
        for (int i = 0; i < 4; i++) { //for all rows
            for (int j = 0; j < 4; j++) { //for all columns
                board[i][j] = new Cell(); //makes it a cell
            }
        }
        if(seed){
            this.currentPos = new int[]{0,0}; //sets agent start
            this.board[0][0].setHasAgent(true); //declares hasAgent to true
            this.board[2][1].setHasGold(true); //sets location of gold
            String[] stenches = getAdjCells(2, 0); //gets adjacent cells to wumpus
            this.board[2][0].setHasWumpus(true); //sets wumpus to true
            for(String move: stenches){ //for all the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //get int of x value
                int y = Integer.parseInt(move.substring(1)); //get int of y value
                this.board[x][y].setStench(true); //set stench to true
            }

            this.board[0][2].setHasPit(true); //set pit to true
            String[] breeze = getAdjCells(0, 2); //get adjacent cells that have breeze
            for(String move: breeze){ //all of the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //gets int of x value
                int y = Integer.parseInt(move.substring(1)); //gets int of y value
                this.board[x][y].setBreeze(true); //set breeze to true
            }

            this.board[2][2].setHasPit(true); //set pit to true
            breeze = getAdjCells(2, 2); //get adjacent cells that have breeze
            for(String move: breeze){ //all of the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //gets int of x value
                int y = Integer.parseInt(move.substring(1)); //gets int of y value
                this.board[x][y].setBreeze(true); //set breeze to true
            }

            this.board[3][3].setHasPit(true); //set pit to true
            breeze = getAdjCells(3, 3); //get adjacent cells that have breeze
            for(String move: breeze){ //all of the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //gets int of x value
                int y = Integer.parseInt(move.substring(1)); //gets int of y value
                this.board[x][y].setBreeze(true); //set breeze to true
            }
        }
        else{
            this.currentPos = new int[]{0,0}; //sets agent start
            this.board[0][0].setHasAgent(true); //declares hasAgent to true
            this.board[1][3].setHasGold(true); //sets location of gold
            String[] stenches = getAdjCells(0, 2); //gets adjacent cells to wumpus
            this.board[0][2].setHasWumpus(true); //sets wumpus to true
            for(String move: stenches){ //for all the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //get int of x value
                int y = Integer.parseInt(move.substring(1)); //get int of y value
                this.board[x][y].setStench(true); //set stench to true
            }

            this.board[2][1].setHasPit(true); //set pit to true
            String[] breeze = getAdjCells(2, 1); //get adjacent cells that have breeze
            for(String move: breeze){ //all of the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //gets int of x value
                int y = Integer.parseInt(move.substring(1)); //gets int of y value
                this.board[x][y].setBreeze(true); //set breeze to true
            }

            this.board[3][1].setHasPit(true); //set pit to true
            breeze = getAdjCells(3, 1); //get adjacent cells that have breeze
            for(String move: breeze){ //all of the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //gets int of x value
                int y = Integer.parseInt(move.substring(1)); //gets int of y value
                this.board[x][y].setBreeze(true); //set breeze to true
            }

            this.board[2][3].setHasPit(true); //set pit to true
            breeze = getAdjCells(2, 3); //get adjacent cells that have breeze
            for(String move: breeze){ //all of the adjacent cells
                int x = Integer.parseInt(move.substring(0, 1)); //gets int of x value
                int y = Integer.parseInt(move.substring(1)); //gets int of y value
                this.board[x][y].setBreeze(true); //set breeze to true
            }
        }
    }

    //gets all of adjacent cells
    public String[] getAdjCells(int x,int y){
        StringBuilder result = new StringBuilder(); //declares string builder

        int[][] adjacentSquares = { //all possible adjacent cells
            {x + 1, y}, // Up
            {x - 1, y}, // Down
            {x, y - 1}, // Left
            {x, y + 1}  // Right
        };

        for (int i = 0; i<4;i++) { //loops through all of them
            int[] cord = adjacentSquares[i]; //gets coordinates
            // Check if the adjacent square is within the bounds of the board
            if (cord[0] >= 0 && cord[0] < 4 && cord[1] >= 0 && cord[1] < 4) {
                result.append(Integer.toString(cord[0])+Integer.toString(cord[1])); //adds location to result
                result.append(" "); //adds space
            }
            
        }
        String string = result.toString(); //cast result to a String
        String[] ret = string.split(" "); //split it at the spaces
        return ret; //return the string
    }

    //performs the shoot on the board
    public boolean shoot(String direction){
        if(direction.equals("up")) //shooting up
        {
            if(this.board[currentPos[0]+1][currentPos[1]].hasWumpus()){ //if spot above has Wumpus
                this.board[currentPos[0]+1][currentPos[1]].setHasWumpus(false); //sets Wumpus to dead in cell
                String[] adj = getAdjCells(currentPos[0]+1,currentPos[1]); //gets adjacent cells of where Wumpus was
                for(String move: adj){ //loops through them
                    int x = Integer.parseInt(move.substring(0, 1));
                    int y = Integer.parseInt(move.substring(1)); //get y value as int
                    this.board[x][y].setStench(false); //set all the stenches to false
                }
                System.out.println("You have killed the Wumpus!"); //display we killed Wumpus
                System.out.println();
                
                return true; //return true, meaning we successfully killed Wumpus
            }
        }
        else if(direction.equals("down")) //shooting down
        {
            if(this.board[currentPos[0]-1][currentPos[1]].hasWumpus()){  //if spot below has Wumpus
                this.board[currentPos[0]-1][currentPos[1]].setHasWumpus(false); //sets Wumpus to dead in cell
                String[] adj = getAdjCells(currentPos[0]-1,currentPos[1]); //gets adjacent cells of where Wumpus was
                for(String move: adj){ //loops through them
                    int x = Integer.parseInt(move.substring(0, 1)); //get x value as int
                    int y = Integer.parseInt(move.substring(1)); //get y value as int
                    this.board[x][y].setStench(false); //set all the stenches to false
                }
                System.out.println("You have killed the Wumpus!"); //display we killed Wumpus
                return true; //return true, meaning we successfully killed Wumpus
            }
        }
        else if(direction.equals("left")) //shooting left
        {
            if(this.board[currentPos[0]][currentPos[1]-1].hasWumpus()){ //if spot to left has Wumpus
                this.board[currentPos[0]][currentPos[1]-1].setHasWumpus(false); //sets Wumpus to dead in cell
                String[] adj = getAdjCells(currentPos[0],currentPos[1]-1); //gets adjacent cells of where Wumpus was
                for(String move: adj){ //loops through them
                    int x = Integer.parseInt(move.substring(0, 1)); //get x value as int
                    int y = Integer.parseInt(move.substring(1)); //get y value as int
                    this.board[x][y].setStench(false); //set all the stenches to false
                }
                System.out.println("You have killed the Wumpus!"); //display we killed Wumpus
                return true; //return true, meaning we successfully killed Wumpus
            }
        }
        else{  //shooting right
            if(this.board[currentPos[0]][currentPos[1]+1].hasWumpus()){ //if spot to right has Wumpus
                this.board[currentPos[0]][currentPos[1]+1].setHasWumpus(false); //sets Wumpus to dead in cell
                String[] adj = getAdjCells(currentPos[0],currentPos[1]+1); //gets adjacent cells of where Wumpus was
                for(String move: adj){ //loops through them
                    int x = Integer.parseInt(move.substring(0, 1)); //get x value as int
                    int y = Integer.parseInt(move.substring(1)); //get y value as int
                    this.board[x][y].setStench(false); //set all the stenches to false
                }
                System.out.println("You have killed the Wumpus!"); //display we killed Wumpus
                return true; //return true, meaning we successfully killed Wumpus
            }
        }
        return false; //return false, didnt kill the Wumpus
    }

    //function moves the agent around through the board
    //return -1 if dead to wumpus or pit
    //return 0 if hits wall
    // return 1 if valid
    public int move(String direction){
        //These positioning are reverse based on how the board is constucted
        // up shows on board as moving down
        //left on board shows as moving right etc
        //System.out.println(this);
        if(direction.equals("right")){ //moving to the right
            //moving right
            if(this.currentPos[1]+1>3) //checks if move is off board
            {
                System.out.println("Bump! thats a wall"); //hit wall so display to user
                return 0; //return 0 showing we hit wall
            }
            //otherwise
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(false); //current position no longer has agent
            this.currentPos[0] = this.currentPos[0]; //adjust current x pos
            this.currentPos[1] = this.currentPos[1]+1; //adjust current y pos
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(true); //set agent to true in new location
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasPit()){  //check if we fell into pit
                System.out.println("You have fallen into a pit"); //display to user
                System.out.println("Game Over :("); //display to user
                return -1; //return -1 showing dead to pit
            }
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasWumpus()){ //check if we ran in to Wumpus
                System.out.println("You have been killed by Wumpus"); //display to user what happened
                System.out.println("Game Over :("); //display to user
                return -2; //return -2 showing dead to Wumpus
            }

        }
        else if(direction.equals("left")){ //moving to the left
            //moving left
            if(this.currentPos[1]-1<0) //check if move out of range
            {
                System.out.println("Bump! thats a wall"); //if it is we hit wall
                return 0; //return 0 showing we bumped into wall
            }
            
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(false); //current pos no longer has agent
            this.currentPos[0] = this.currentPos[0]; //adjust current x pos
            this.currentPos[1] = this.currentPos[1]-1; //adjust current y pos
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(true); //set agent to true in new pos
            
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasPit()){ //check if new spot has a pit
                System.out.println("You have fallen into a pit"); //if it does tell user they died
                System.out.println("Game Over :("); //display to user
                return -1; //return -1, meaning dead to a pit
            }
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasWumpus()){ //if the current cell has a Wumpus
                System.out.println("You have been killed by Wumpus"); //display to user dead to Wumpus
                System.out.println("Game Over :("); //display to user
                return -2; //return -2, showing dead to Wumpus
            }
        }
        else if(direction.equals("down")){ //moving down
            //moving down
            if(this.currentPos[0]-1<0) //checks if move is out of bounds
            {
                System.out.println("Bump! thats a wall"); //if it is tell user they hit wall
                return 0; //return 0, showing they hit wall
            }
            //otherwise
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(false); //set current cell to no longer have agent
            this.currentPos[0] = this.currentPos[0]-1; //adjust current x pos
            this.currentPos[1] = this.currentPos[1]; //adjust current y pos
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(true); //change agent location to new cell
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasPit()){ //check if new cell has pit
                System.out.println("You have fallen into a pit"); //if it does, tell user they fell in
                System.out.println("Game Over :("); //display to user
                return -1; //return -1, showing dead to pit
            }
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasWumpus()){ //if the current cell has a Wumpu
                System.out.println("You have been killed by Wumpus"); //tell user they are dead to a Wumpus
                System.out.println("Game Over :("); //display that game is over
                return -2; //return -2, showing we are dead to Wumpus
            }
        }
        else{ //moving up
            if(this.currentPos[0]+1>3) // check if the move is out of bounds
            {
                System.out.println("Bump! thats a wall"); //if out of bounds say we hit wall
                return 0; //return 0, meaning we hit wall
            }
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(false); //current pos no longer has agent
            this.currentPos[0] = this.currentPos[0]+1; //adjust x location value
            this.currentPos[1] = this.currentPos[1]; //adjust y location value
            this.board[this.currentPos[0]][this.currentPos[1]].setHasAgent(true); //set agent to true in new spot
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasPit()){ //check if new cell has pit
                System.out.println("You have fallen into a pit"); //if it does tell user they fell into pit
                System.out.println("Game Over :("); //display to user game is over
                return -1; //return -1, showing dead to a pit
            }
            if(this.board[this.currentPos[0]][this.currentPos[1]].hasWumpus()){ //if current cell has Wumpus
                System.out.println("You have been killed by Wumpus"); //display to user dead to Wumpus
                System.out.println("Game Over :("); //display game is over
                return -2; //return -2, showing dead to Wumpus
            }
        }
        return 1; //otherwise return 1, showing move was safe
    }

    //getter for the board 
    public Cell[][] getBoard(){
        return this.board;
    }

    //to String
    public String toString() {
        StringBuilder result = new StringBuilder(); //utilizes a string builder
        for (int row = board.length-1; row >= 0; row--) { //all of rows, starting at the last spot
            for (int col = 0; col < board[row].length; col++) { //all of columns
                result.append(board[row][col].toString()); //adds the given cell to the Stringbuilder
                result.append(" "); //seperates the cells by spacesa
            }
            result.append("\n"); //seperates the rows by enters
        }
        return result.toString(); //sets it to string and returns it
    }
}
