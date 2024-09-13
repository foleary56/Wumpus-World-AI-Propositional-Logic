/* Finn O'Leary
 * Intro to AI 449
 * Project 3 - Wumpus World with Propositional Logic
 * 4/25/24
 */

// All neccesary imports
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Player {
    private Board board; //The game board
    private int[] currentPos; //track the position of the player
    private boolean hasGold; //checks if we have the gold
    boolean wumpusDead = false; //checks if the wumpus is dead
    private String directions; //keeps track of just moves, so we can traverse home // resets when we get home
    private String moves; //keep track of all of the moves and decisions
    boolean arrow = true; //if we still have an arrow
    private Set<String> knowledgeBase;//knowledge base


    //contructor to create the player
    public Player(Board board){
        this.board = board;
        this.currentPos = new int[]{0,0};
        this.hasGold = false;
        this.directions = "";
        this.moves = "";
        this.knowledgeBase = new HashSet<>();
    }

    //neccessary getters and setters
    public String getMoves(){
        return this.moves;
    }
    public Board getBoard(){
        return this.board;
    }
    public int[] getPos(){
        return this.currentPos;
    }
    public void setHasGold(boolean set){
        this.hasGold = set;
    }
    public boolean hasGold(){
        return hasGold;
    }

    //shoot method
    public void shoot(String direction){
        if(this.arrow){//if we dont have arrow we can shoot
            this.arrow=false;//no longer have arrow
            boolean check = board.shoot(direction);//shoot returns boolean to see if we killed Wumpus
            if(check){//if we did wumpus is dead
                wumpusDead = true;
                this.moves += "WK"; //stands for 'Wumpus Kill'
            }
        }
        else{//if we dont have arrow tell the user
            System.out.println("Dont have arrow :(");
        }
    }

    public void resolveKnowledgeBase() {
        Set<String> newKnowledgeBase = new HashSet<String>(knowledgeBase); //clone of knowledge base
        boolean done = false; 
        while(!done) //while the resolution is not done, continue
        {
            //done=true;
            Set<String> allClauses = new HashSet<String>(newKnowledgeBase); //clones the knowledge base agai
            for (String clause1 : allClauses) { //goes through all clauses in both, so we compare all the clauses
                for (String clause2 : allClauses) {
                    if(clause1!=clause2) //checks if they arent equal, because if they are they are duplicates and can skip
                    {
                        if(clause1.length()>1 && clause2.length()>1) //both of their lengths are greater than 1
                        {
                            if(clause1.charAt(0)=='W'||clause1.charAt(1)=='W'){ //checks if the clause is comparing Wumpus'
                                if(clause2.charAt(0)=='W' || clause2.charAt(1)=='W'){ //checks if the other clause is a Wumpus, if not we dont resolve, because we cant
                                    String resolve = resolve(clause1, clause2, "W"); //Both Wumpus so we resolve
                                    if(resolve!=null){ //if we dont get a null back, we remove the 2 clauses and add the resolution
                                        newKnowledgeBase.remove(clause1); 
                                        newKnowledgeBase.remove(clause2);
                                        newKnowledgeBase.add(resolve);
                                    }
                                
                                }
                            }
                            else{ //first term not Wumpus
                                if(clause2.charAt(0)=='P' || clause2.charAt(1)=='P'){ //checks if clause 2 is a Pit
                                    if(clause1.charAt(0)!='V'){ //makes sure clause one is not our visited knowledge
                                        if(clause1.charAt(1)=='P'){//first digit is a ! for clause1
                                            String resolve = resolve(clause1, clause2, "P");
                                            if(resolve!=null){
                                                newKnowledgeBase.remove(clause2); //dont remove clause1 because we need knowledge base to keep where there is not a pit
                                                newKnowledgeBase.add(resolve); // add resolution
                                            }

                                        }
                                        else if(clause2.charAt(1)=='P'){ //first digit in clause2 is a !
                                            String resolve = resolve(clause1, clause2, "P");
                                            if(resolve!=null){
                                                newKnowledgeBase.remove(clause1); //dont remove clause2 because we need knwoledge base to keep location of no pit
                                                newKnowledgeBase.add(resolve); //add resolution
                                            }
                                        }
                                        else{ //neither start with !
                                            String resolve = resolve(clause1, clause2, "P");
                                            if(resolve!=null){
                                                newKnowledgeBase.remove(clause1); //we remove both clauses
                                                newKnowledgeBase.remove(clause2);
                                                newKnowledgeBase.add(resolve); //add resolution
                                            }
                                        }
                                        
                                    }
                                    
                                }
                            }
                        }
                    }
                }
                
            }
            //after loop
            if(allClauses.equals(newKnowledgeBase)){ //this means we didnt update the knowledge base at all in this run, so resolution complete
                done = true; //end the loop
            }
        }
        knowledgeBase=(newKnowledgeBase); //set our knowledge base to the new one that we created
    }

    public String resolve(String clause1, String clause2, String letter) {
        HashSet<String> literals1 = new HashSet<>(Arrays.asList(clause1.split(" OR "))); //transfers String to HashSet
        HashSet<String> literals2 = new HashSet<>(Arrays.asList(clause2.split(" OR "))); //transfers String to HashSet
        ArrayList<String> list1 = new ArrayList<>(literals1); //transfers hash to an array list so we can sort easy
        ArrayList<String> list2 = new ArrayList<>(literals2); //transfers hash to an array list so we can sort easy
        Collections.sort(list1); //sorts the arrays
        Collections.sort(list2);
        literals1 = new HashSet<>(list1);//makes them HashSet again
        literals2 = new HashSet<>(list2);

        //if we know location of what we want
        if(list1.size()==1){ //check if only one thing is in list1
            if(list1.get(0).charAt(0)==letter.charAt(0)){ //makes sure it is what we are trying to resolve, not pit or visited when we want wumpus, and same vise versa
                for(String clause:list2){ //goes through all the Strings in the list2
                    if(clause.startsWith(letter)){ //fi string starts with desired letter
                        literals2.remove(clause); //we remove the clause from the 2nd literals set
                    }
                }
                return list1.get(0); // we return the location of what we are looking for
            }
        }
        //if we know location of what we want
        //this does the same thing as the one before, but for the 2nd list instead of the 1st one
        if(list2.size()==1){
            if(list2.get(0).charAt(0)==letter.charAt(0)){
                for(String clause:list1){
                    if(clause.startsWith(letter)){
                        literals1.remove(clause);
                    }
                }
                return list2.get(0);
            }
        }

        //resolves if there is a not symbol
        // if it knows there is not Wumpus at location it take out all of the occurences of that spot
        if(list1.get(0).contains("!")){ //if its a !, meaning the location doesnt have something
            String val = list1.get(0).substring(1); //this gets just the item and location, without the !
            if(list2.contains(val)){ //if the other list has that item in same location, without the !
                list2.remove(val);//remove it
                return String.join(" OR ",list2); //join the 2nd list with OR and return it, because its resolved
            }
        }
        //resolves if there is a not symbol
        // if it knows there is not Wumpus at location it take out all of the occurences of that spot
        // same as before but for the other list
        if(list2.get(0).contains("!")){
            String val = list2.get(0).substring(1);
            if(list1.contains(val)){
                list1.remove(val);
                return String.join(" OR ",list1);
            }
        }

        Set<String> intersection = new HashSet<>(literals1);// creates new instance of literals1
        intersection.retainAll(literals2); //keeps every clause that is in both, so all items that are in both clauses are put in a set
        for(String w: intersection){ //loop because no way to just grab a value out of Set otherwise
            if (w.startsWith(letter)) { // Check if the literal starts with "W"
                return String.join(" OR ", intersection); // if it starts with W, its a Wumpus so we return only commonalities
            }
        }
        return null; //if we couldnt make resolution we return null

    }

    //checks if current state has win
    public boolean checkWin(){ //checks if we've won
        if(this.hasGold && this.currentPos[0]==0 && this.currentPos[1]==0){ // at home and have gold
            return true; //returns we've won
        }
        else return false; //otherwise we lose
    }

    //returns what the agent is perceiving, this is for real person playing
    public String percepts(){
        String ret = ""; 
        if(this.board.getBoard()[currentPos[0]][currentPos[1]].hasBreeze()){//current cell has breeze 
            ret+="Breeze "; //add breeze to our return
        }
        if(this.board.getBoard()[currentPos[0]][currentPos[1]].hasStench()){//current cell has stench
            ret +="Stench "; //add stench to our return
        }
        if(this.board.getBoard()[currentPos[0]][currentPos[1]].hasGold()){//current cell has glitter
            ret +="Glitter "; //adds glitter to our return
        }
        return ret; //returns the String
    }

    //move for player, not for computer
    public int movee(String direction){
        int moveRes = board.move(direction); //calls the board's move method
        if(moveRes==0){ //if its 0, we bumped into wall
            return 0; //return 0 again
        }
        else if(moveRes==1){ // its its 1 we are alive still
            if(direction.equals("right")){
                //moving right, so update our position
                this.currentPos[0] = this.currentPos[0];
                this.currentPos[1] = this.currentPos[1]+1;
                this.directions += "R"; //add to directions our move
                this.moves += "R"; //add to our moves our move
            }
            else if(direction.equals("left")){
                //moving left, so update our position
                this.currentPos[0] = this.currentPos[0];
                this.currentPos[1] = this.currentPos[1]-1;
                this.directions += "L"; //add to directions our move
                this.moves += "L"; //add to our moves our move
            }
            else if(direction.equals("down")){
                //moving down, so update our position
                this.currentPos[0] = this.currentPos[0]-1;
                this.currentPos[1] = this.currentPos[1];
                this.directions += "D"; //add to directions our move
                this.moves += "D"; //add to our moves our move
            }
            else{
                //moving up, so update our position
                this.currentPos[0] = this.currentPos[0]+1;
                this.currentPos[1] = this.currentPos[1];
                this.directions += "U"; //add to directions our move
                this.moves += "U"; //add to our moves our move
            }
            return 1; //return 1 again, since alive
        }
        else{ //not 0 or run we are dead
            return moveRes; //so we return same value
            // -1 is dead to Pit
            // -2 is dead to Wumpus
        }
    }

    //move method for the computer move
    public int move(String direction){
        updateKnowledgeBase();
        int moveRes = board.move(direction);//calls boards move
        if(moveRes==0){ //if its 0, we hit a wall(computer should never hit wall)
            return 0; //returns 0 again
        }
        else if(moveRes==1){ //if we are alive swtill
            if(direction.equals("right")){
                //moving right, so update our position
                this.currentPos[0] = this.currentPos[0];
                this.currentPos[1] = this.currentPos[1]+1;
                if(this.hasGold()==false) //do these so when we are going back home it doesnt save directions
                {
                    this.directions += "R"; //add to directions our move
                    this.moves += "R"; //add to moves our move
                    updateKnowledgeBase(); //update our database
                }
            }
            else if(direction.equals("left")){
                //moving left, so update our position
                this.currentPos[0] = this.currentPos[0];
                this.currentPos[1] = this.currentPos[1]-1;
                if(this.hasGold()==false) //do these so when we are going back home it doesnt save directions
                {
                    this.directions += "L"; //add to directions our move
                    this.moves += "L"; //add to moves our move
                    updateKnowledgeBase(); //update our database
                }
            }
            else if(direction.equals("down")){
                //moving down, so update our position
                this.currentPos[0] = this.currentPos[0]-1;
                this.currentPos[1] = this.currentPos[1];
                if(this.hasGold()==false) //do these so when we are going back home it doesnt save directions
                {
                    this.directions += "D"; //add to directions our move
                    this.moves += "D"; //add to moves our move
                    updateKnowledgeBase(); //update our database
                }
                
            }
            else{
                //moving up, so update our position
                this.currentPos[0] = this.currentPos[0]+1;
                this.currentPos[1] = this.currentPos[1];
                if(this.hasGold()==false) //do these so when we are going back home it doesnt save directions
                {
                    this.directions += "U"; //add to directions our move
                    this.moves += "U"; //add to moves our move
                    updateKnowledgeBase(); //update our database
                }
            }
            return 1; //return 1 showing we are alive
        }
        else{
            this.moves += direction.substring(0,1).toUpperCase(); //adds the final move before death
            return moveRes; // return showing we are dead
            //-1 for dead to Pits
            //-2 for dead to Wumpus
        }
    }

    // makes the computers move using the knowledge base
    public int makeComputerMove(){
        //System.out.println("KB: "+knowledgeBase); //just to see in chat out knowledge base
        this.updateKnowledgeBase(); //update KB
        this.resolveKnowledgeBase(); //resolve KB
        String move = makeComputerChoice(); //make our choice
        if(move=="Home"){ //this means we have the gold and the choice method traversed us to go home
            System.out.println("We win!"); //display that we won
            return 1;// return 1 showing we won
        }
        else if(move=="good"){ //means we are going back, using goBack method
            return 0; //returns 0, showing alive
        }
        else if(move.length()>6){ //if length is greater than 5 our choice, is to shoot wumpus
            this.shoot(move.substring(5)); //method returns 'shoot'+direction, so we call shoot in the direction
            //System.out.println("SHOOT"); //tell the user we shot
            //System.out.println(this.board);//display the board
            //System.out.println(move.substring(5));//print the direction we shot
            return 0;//return 0, showing alive
        }
        else if(move.substring(0,1).equals("P")){
            this.move(move.substring(1));
            this.updateKnowledgeBase();
            return 1;
        }
        else{ //if none of these true, we make normal move
            int checker = this.move(move); //make move in direction choice gave us
            if(checker==-1){ //means we died to Pit
                this.moves+="P"; //show in moves that we died to pit
                return -1; //return -1, showing dead to pit
            }//was else if, can just be if
            if(checker==-2){ //means we died to Wumpus
                this.moves+="W";//add to moves we died to Wumpus
                return -2;//return -2 showing we are dead to Wumpus
            }
            updateKnowledgeBase(); //update KB after move (probably not neccessary)
            return 0; //return 0, showing alive
        }
    }
     
    public String goBack(){
        if(this.directions.length()!=0){ // showing we can go back, and it wont cause an out of bounds error
            String move = this.directions.substring(directions.length()-1); // get the last letter of directions
            if(move.equals("R")){ //if last letter is R
                this.directions = this.directions.substring(0,directions.length()-1); //refresh directions, deleting last letter
                //update position
                this.currentPos[0] = this.currentPos[0];
                this.currentPos[1] = this.currentPos[1]-1;
                board.move("left"); //move opposite of last move
                this.moves += "L"; //update moves to add our new move
                return "good"; //return good showing we are still going back
            }
            else if(move.equals("L")){
                this.directions = this.directions.substring(0,directions.length()-1); //refresh directions, deleting last letter
                //update position
                this.currentPos[0] = this.currentPos[0];
                this.currentPos[1] = this.currentPos[1]+1;
                board.move("right"); //move opposite of last move
                this.moves += "R"; //update moves to add our new move
                return "good"; //return good showing we are still going back
            }
            else if(move.equals("U")){
                this.directions = this.directions.substring(0,directions.length()-1); //refresh directions, deleting last letter
                //update position
                this.currentPos[0] = this.currentPos[0]-1;
                this.currentPos[1] = this.currentPos[1];
                board.move("down"); //move opposite of last move
                this.moves += "D"; //update moves to add our new move
                return "good"; //return good showing we are still going back
            }
            else{
                this.directions = this.directions.substring(0,directions.length()-1); //refresh directions, deleting last letter
                //update position
                this.currentPos[0] = this.currentPos[0]+1;
                this.currentPos[1] = this.currentPos[1];
                board.move("up"); //move opposite of last move
                this.moves += "U"; //update moves to add our new move
                return "good"; //return good showing we are still going back
            }
            
        }
        this.directions = ""; //reset directions to nothing because this means we are home
        return "start"; //shows we are back at home
    }
    
     //this method sends us back home, by traversing through directions
     public void goHome(){
        //System.out.println("Directions left: "+directions);
        while(this.directions.length()!=0){ //runs until no more directions left, meaning we are home
            String move = this.directions.substring(directions.length()-1); // get the last letter of directions, showing where we moved
            if(move.equals("R")){ //last move was right so we move left and update necessary variables
                this.move("left"); //move opposite of last move
                this.moves += "L"; //add move to our total moves
                this.directions = this.directions.substring(0,directions.length()-1); //take last move off directions, becuase we traversed it
                continue; //continue
            }
            else if(move.equals("L")){
                this.move("right"); //move opposite of last move
                this.moves += "R"; //add move to our total moves
                this.directions = this.directions.substring(0,directions.length()-1); //take last move off directions, becuase we traversed it
                continue; //continue
            }
            else if(move.equals("U")){
                this.move("down"); //move opposite of last move
                this.moves += "D"; //add move to our total moves
                this.directions = this.directions.substring(0,directions.length()-1); //take last move off directions, becuase we traversed it
                continue; //continue
            }
            else{
                this.move("up"); //move opposite of last move
                this.moves += "U"; //add move to our total moves
                this.directions = this.directions.substring(0,directions.length()-1); //take last move off directions, becuase we traversed it
                continue; //continue
            }
            
        }
    }

    //makes the choice of our computer
    public String makeComputerChoice(){
        // need to implement hasGold and if it does traverse back to start
        updateKnowledgeBase(); //maybe not necessary
        if(this.hasGold) //if we have the gold
        {
            this.moves+=("G"); //show in moves we picked up Gold
            this.goHome(); //go home, becuase we win if we get home
            return "Home"; //show we are home
            //go home 
        }
        else{
            //implement the code that decides where the computer wants to move
            String[] moves = getPossibleMoves(); //get all possible moves(this checks if they are in bounds of game)
            this.updateKnowledgeBase(); //probably not needed
            //sets our current pos to variables
            int x = currentPos[0]; 
            int y = currentPos[1];
            
            for(String move: moves){ // all possible moves,looping through
                //maybe move the x and y locations here so we dont call it multiple times in different locations
                if(knowledgeBase.contains("W"+move) && this.arrow==true){ // if we have arrow and possible move has a Wumpus(we are in shooting range)
                    String intx = move.substring(0, 1); //gets x location of Wumpus
                    String inty = move.substring(1); //gets Y location of Wumpus
                    // Convert substrings to integers
                    int int1 = Integer.parseInt(intx);
                    int int2 = Integer.parseInt(inty);

                    if(x+1==int1){ //Wumpus is above us
                        return "shootup"; //return our choice, shoot up
                    }
                    else if(x-1==int1) { // Wumpus is below us
                        return "shootdown";//return our choice, shoot down
                    }
                    else if(y+1==int2){ // Wumpus is to our right
                        return "shootright"; // return our choice, shoot right
                    }
                    else{ //Wumpus is on our left
                        return "shootleft"; //return our choice, shoot left
                    }
                }
                // if the move has no Wumpus, no Pit and we havent visited
                else if(knowledgeBase.contains("!W"+move)&&knowledgeBase.contains("!P"+move)&&knowledgeBase.contains("V"+move)==false){
                    String intx = move.substring(0, 1); //get X location of move
                    String inty = move.substring(1); // get Y location of move
                    // Convert substrings to integers
                    int int1 = Integer.parseInt(intx);
                    int int2 = Integer.parseInt(inty);
                    if(x+1==int1){ // safe move is up
                        return "up"; // move up
                    }
                    else if(x-1==int1){ // safe move is down
                        return "down"; // move down
                    }
                    else if(y+1==int2){ // safe move is right
                        return "right"; // move right
                    }
                    else{ // safe move is left
                        return "left"; // move left
                    }
                }
            }
            // if we reach here, there is no guarranteed safe move
            String check = goBack(); //no safe move so go back one position
            if(check != "start"){ //if we are not at start (0,0)
                return check; //returns good, showing we went back one move successfully
            }
            else{ //now we are at home again so we cant go back
                String path = getPath(getBestPossibleMove()); //get the path of our best possible move
                if(path.length()>1) //checks if path is more than 1 move away
                // we check this because we make all moves except the final move, because we return that to the makeComputerMove to make
                {
                    //System.out.println(path);  //displays the path to the best move
                    for(int i =0;i<path.length()-1;i++){ // goes through all of the moves except the final move
                        String dir = path.substring(i, i+1); //get the move that we need
                        if(dir.equals("L")){ //if the move is left
                            this.move("left"); //move left
                        }
                        else if(dir.equals("R")){ //if the move is right
                            this.move("right"); // move right
                        }
                        else if(dir.equals("U")){ //if the move is up
                            this.move("up"); // move up
                        }
                        else{ //if the move is down
                            this.move("down"); // move down
                        }
                    }
                }
                
                if(path.substring(path.length()-1).equals("L")){ //if final move is left
                    return "Pleft"; //move left
                }
                else if(path.substring(path.length()-1).equals("R")){ //if final move is right
                    return "Pright"; //move right
                }
                else if(path.substring(path.length()-1).equals("U")){ //if final move is up
                    return "Pup"; //move up
                }
                else{ //if final move is down
                    return "Pdown"; //move down
                }
                
            }
        }
    }

    //returns the move in a string
    //put this move in getPath
    //than move through path to get to this state
    //once at move update knowledge base and resolve it
    public String getBestPossibleMove(){
        for(String clause:knowledgeBase){ //goes through all our clauses in KB
            String[] splitClause = clause.split(" OR "); //splits the clause at the OR, and puts in array
            if(splitClause.length==1) 
            //only 1 thing in array, meaning its either a Visited, or declaring we know where something is or know were something isnt
            {
                if(splitClause[0].substring(0, 1).equals("P")){ //means we know location of a pit
                    int x = Integer.parseInt(splitClause[0].substring(1,2)); //get the x location in an int
                    int y = Integer.parseInt(splitClause[0].substring(2,3)); //get the y location in an int
                    String[] adjMoves = getSurrounding(x,y); //gets all the surrounding moves (this includes diagonal)
                    for(String move: adjMoves){ //checking all moves around to see if we have visited there, if we havent it is a possible safe move
                        // we will return the possible safe move to go to.
                        int xx = Integer.parseInt(move.substring(0,1)); //gets x location of adj move
                        int yy = Integer.parseInt(move.substring(1,2)); //gets y location of adj move
                        if(knowledgeBase.contains("V"+move)==false && knowledgeBase.contains("P"+move)==false){//check if we havent explored possible safe move
                            String[] around = getAdj(xx, yy); // if we havent we get the adjacent squares of the move
                            // now we are going to look for a move next to safe move that we have visited before
                            for(String stuffAround:around){//goes through adjacent moves
                                if(knowledgeBase.contains("V"+stuffAround)){//we have been to ajacent square so capable of reaching it
                                    //System.out.println("MOVE: "+move);
                                    return move; // move is the possible safe move to check
                                }
                            }
                        }
                        
                    }

                }
            }
            
        }
        return null; // if none true return null
    }

    //gets path to a move, will utilize the getBestPossibleMove and than get path to move and try it
    public String getPath(String goal){
        //declare neccessary variables
        String path = ""; //path to get to move
        int currx = 0; //current x location
        int curry = 0; //current y location 
        int x = Integer.parseInt(goal.substring(0,1)); //gets the x value of our goal state
        int y = Integer.parseInt(goal.substring(1,2)); //gets the y value of our goal state
        String[] adjCells = getAdj(x, y); //get all the adjacent cells of goal
        for(String possible: adjCells){ //go through all adjacent cells, and this loop only gets our first move
            int xx = Integer.parseInt(possible.substring(0,1)); // gets x location of adjacent move
            int yy = Integer.parseInt(possible.substring(1,2)); //gets y location of adjacent move
            currx = xx; //sets it to our current x(wont necessarily stay current y)
            curry = yy; //sets it to our current y(wont necessarily stay current y)
            if(knowledgeBase.contains("V"+possible)){ //visited next to goal node
                // in the if statements, we add the opposite of the location, becuase we are moving backwards
                if(xx>x){ //visited move is above
                    path = "D"+path; //add opposite to the path, at the front, because when we read path, we read front first
                    break; //break loop
                }
                else if(xx<x){//visited move below
                    path = "U"+path; //add opposite to the path, at the front, because when we read path, we read front first
                    break; //break loop
                }
                else if(yy>y){//visited to the right
                    path = "L"+path; //add opposite to the path, at the front, because when we read path, we read front first
                    break; //break loop
                }
                else{//visited to the left
                    path = "R"+path; //add opposite to the path, at the front, because when we read path, we read front first
                    break; //break loop
                }
            }
        }
        //at this point we only currently have one move in our path, so now we add the rest of them
        while(currx!=0 || curry!=0){ //meaning our current location is not at home, so continue
            if(knowledgeBase.contains("V"+Integer.toString(currx)+Integer.toString(curry-1))){ //if we have visited cell to the left
                path = "R"+path; //update our path (opposite of where visited is because we are moving backwards) 
                curry = curry-1; //update current y
            }
            else if(knowledgeBase.contains("V"+Integer.toString(currx-1)+Integer.toString(curry))){ //if we have visited cell below us
                path = "U"+path; //update our path (opposite of where visited is because we are moving backwards) 
                currx = currx-1; //update current x
            }
            else if(knowledgeBase.contains("V"+Integer.toString(currx)+Integer.toString(curry+1))){ //if we have visited cell to the right
                path = "L"+path; //update our path (opposite of where visited is because we are moving backwards)
                curry = curry+1; //update current y
            }
            else{ //if we have visited cell above us
                path = "D"+path; //update our path (opposite of where visited is because we are moving backwards)
                currx = currx-1; //update current x
            }
        }
        return path; //return our path
    }
    
    //updates our knowledge base, based on current location
    public void updateKnowledgeBase(){
        String x = Integer.toString(this.currentPos[0]); //current x in String
        String y = Integer.toString(this.currentPos[1]); //current y in String

        String[] possibleMoves = getPossibleMoves(); //gets all our possible moves
        if(knowledgeBase.contains("V"+x+y)==false){//check if knowledge base knows we have visited the current move
            knowledgeBase.add("V"+x+y);//if we it doesnt know, add it to the KB
        }
        if(knowledgeBase.contains("!W"+x+y)==false){//check if we marked current cell safe for Wumpus in KB already
            knowledgeBase.add("!W"+x+y);//if we havent, add to KB
        }
        if(knowledgeBase.contains("!P"+x+y)==false){//check if we marked current cell safe for Pit in KB already
            knowledgeBase.add("!P"+x+y);//if we havent, add to KB
        }
        boolean hasStench = this.board.getBoard()[currentPos[0]][currentPos[1]].hasStench(); //gets the boolean showing if current cell has stench
        boolean hasBreeze = this.board.getBoard()[currentPos[0]][currentPos[1]].hasBreeze(); //gets the boolean showing if current cell has breeze
        if(this.board.getBoard()[this.currentPos[0]][this.currentPos[1]].hasGold()){ //current cell has Gold
            this.hasGold = true; //computer picks up gold
            this.board.getBoard()[this.currentPos[0]][this.currentPos[1]].setHasGold(false); //board no longer has gold
        }
        // initialize 2 array list, 1 to keep track of Wumpus statement, and one to keep track of pit statement
        ArrayList<String> st1 = new ArrayList<>();
        ArrayList<String> st2 = new ArrayList<>();

        for(int i = 0;i<possibleMoves.length;i++){ // goes through all possible moves
            if(hasStench){ //if it has a stench
                if(!knowledgeBase.contains("V"+possibleMoves[i])){//if we havent yet visited cell
                    st1.add("W"+possibleMoves[i]); //show cell has a possible Wumpus on location
                }
            }
            else{ //no stench
                if(!knowledgeBase.contains("V"+possibleMoves[i])){//if we havent yet visited cell
                    knowledgeBase.add(("!W"+possibleMoves[i])); // no Wumpus in adjacent cell, add to KB
                }
            }

            if(hasBreeze){ //if current cell has breeze
                if(!knowledgeBase.contains("V"+possibleMoves[i])){//if we havent yet visited cell
                    st2.add("P"+possibleMoves[i]); //possible Pit in cell, adds to statement array
                }
            }
            else{
                if(!knowledgeBase.contains("V"+possibleMoves[i])){//if we havent yet visited cell
                    knowledgeBase.add(("!P"+possibleMoves[i])); //no Pit in adjacent cell, add to KB
                }
            }
        }
        //final Strings for the 2 statements
        String final1 = ""; 
        String final2 = "";
        if(st1.size()>0) //meaning we need an OR
        {
            for(int i =0;i<st1.size()-1;i++){ //go through all but last
                final1 += st1.get(i).toString()+" OR "; //add the clause followed by OR
            }
            final1 += st1.get(st1.size()-1); //add the final clause
        }
        if(st2.size()>0) //meaning we need an OR
        {
            for(int i =0;i<st2.size()-1;i++){ //go through all but last
                final2 += st2.get(i).toString()+" OR "; //add the clause followed by OR
            }
            final2 += st2.get(st2.size()-1); //add the final clause
        }
        // if the statements lengths are greater than 0 we add to KB
        // dont add blank statments to KB
        if(final1.length()>0) knowledgeBase.add(final1);
        if(final2.length()>0) knowledgeBase.add(final2);
        //System.out.println(final1);
        //System.out.println(final2);
    }

    // gets all possible moves not out of bounds
    // doesnt check if move is safe or anything
    // returns coordinates as a String
    public String[] getPossibleMoves(){
        int x = currentPos[0]; //current x position
        int y = currentPos[1]; // current y position
        StringBuilder result = new StringBuilder(); //String builder declaration
        int[][] adjacentSquares = { //all of the possible moves
            {x + 1, y}, // Up
            {x - 1, y}, // Down
            {x, y - 1}, // Left
            {x, y + 1}  // Right
        };

        for (int i = 0; i<4;i++) {// loops through all the moves
            int[] cord = adjacentSquares[i]; //gets the move from 2D array declared
            // Check if the adjacent square is within the bounds of the board
            if (cord[0] >= 0 && cord[0] < 4 && cord[1] >= 0 && cord[1] < 4) {//if possible move
                result.append(Integer.toString(cord[0])+Integer.toString(cord[1])); //convert location to string, add the x and y together and append it to result
                result.append(" "); //append a space to seperate the mvoes
            }
        }
        String string = result.toString(); //convert to String
        String[] ret = string.split(" "); //Split into array at the space that we put throughout it
        return ret; //return the array
    }

    //gets all squares around cube, diagonal too
    // takes in the coordinates of the cell we want to get locations around
    public String[] getSurrounding(int x, int y){
        StringBuilder result = new StringBuilder(); //declare Stringbuilder
        int[][] surroundingSquares = { //all of the moves surrounding the given location
            {x + 1, y}, // Up
            {x - 1, y}, // Down
            {x, y - 1}, // Left
            {x, y + 1},  // Right
            {x + 1, y-1}, // Up left
            {x - 1, y +1}, // Down right
            {x-1, y - 1}, // down Left
            {x+1, y + 1}  // up Right
        };

        for (int i = 0; i<8;i++) { //goes through all squares
            int[] cord = surroundingSquares[i]; //gets the move coordinates from 2D array
            // Check if the adjacent square is within the bounds of the board
            if (cord[0] >= 0 && cord[0] < 4 && cord[1] >= 0 && cord[1] < 4) {//if possible
                result.append(Integer.toString(cord[0])+Integer.toString(cord[1])); //convert location to string, add the x and y together and append it to result
                result.append(" "); //append space to the result
            }
        }
        String string = result.toString(); //convert stringbuilder to a String
        String[] ret = string.split(" "); //split into an array at the spaces we put throughout it
        return ret; //return the array
    }

    //gets the adjacent squares of a given x and y coordinates
    public String[] getAdj(int x, int y){ 
        StringBuilder result = new StringBuilder(); //String builder declaration
        int[][] adjacentSquares = { //all of the possible moves
            {x + 1, y}, // Up
            {x - 1, y}, // Down
            {x, y - 1}, // Left
            {x, y + 1}  // Right
        };

        for (int i = 0; i<4;i++) {// loops through all the moves
            int[] cord = adjacentSquares[i]; //gets the move from 2D array declared
            // Check if the adjacent square is within the bounds of the board
            if (cord[0] >= 0 && cord[0] < 4 && cord[1] >= 0 && cord[1] < 4) {//if possible move
                result.append(Integer.toString(cord[0])+Integer.toString(cord[1])); //convert location to string, add the x and y together and append it to result
                result.append(" "); //append a space to seperate the mvoes
            }
        }
        String string = result.toString(); //convert to String
        String[] ret = string.split(" "); //Split into array at the space that we put throughout it
        return ret; //return the array
    }
}