/* Finn O'Leary
 * Intro to AI 449
 * Project 3 - Wumpus World with Propositional Logic
 * 4/25/24
 */

 //all neccessary imports
import java.util.Scanner;
//import java.util.Arrays;

public class Game {
  
    public static void main(String[] args)
  {
    Board board = new Board(); //creates board object
    //System.out.println(board);
    Scanner in = new Scanner(System.in); //creates Scanner
    boolean alive = true; //tracks when alive or dead
    Player player = new Player(board); //creates our player
    boolean inputs = false; //dont have inputs
    boolean isComputer = false; //not a computer as of now
    while(!inputs){ //while we dont have inputs
      System.out.print("Are you a computer?(y or n): "); //ask if we are computer
      String inp = in.nextLine(); //gets the text given
      if(inp.equals("y")||inp.equals("Y")||inp.equals("n")||inp.equals("N")){ //checks if text valid
        inp = inp.toLowerCase(); //make it lowercase
        isComputer = inp.equals("y"); //if its 'y' sets computer to true, if 'n' sets it to false
        inputs = true; //inputs true, because we are done
      }
      else continue; //if input isnt valid we try again
    }
    //System.out.println(board); //displays starting board

    if(isComputer) //if player is computer
    {
      System.out.println();
      System.out.println("Board Before: ");
      System.out.println(board); //prints board to show start
      //player.resolveKnowledgeBase(); //resolve knowledge base to start NOT NEEDED
      while(alive){ //while we are alive
        if(player.hasGold()){ //if we have gold
          player.makeComputerChoice(); //call makeComputerChoice, because it will traverse us home
          alive = false;//alive is false to end loop 'we are really still alive in game, we won'
        }
        else{ //if we dont have gold
          int move = player.makeComputerMove(); //call make Computer move
          if(move==-1 || move==-2){ // -1 means dead to Pit, -2 means dead to Wumpus
            alive = false; //end loop we are dead
            break; // break loop
          }
          continue; //otherwise continue
        }
      }
      //displays the Board after the game and all of the moves are made
      //System.out.println();
      //System.out.println("Board after Game:");
      //System.out.println(board);
    }
    else{ //user is playing, not computer
      //System.out.println("Board Before: ");
      //System.out.println(board); //prints board, remove comment to test it out!!!!
      while(alive){ //while we are alive
        String percept = player.percepts(); //get percepts from cell
        System.out.println(percept); //display percepts
        String move = getInput(in); //get our move
        if(move.equals("shoot")){ //if we want to try and shoot
          String shot = getShootInput(in); //get input for shot
          player.shoot(shot); //shoot in that direction
        }
        else if(move.equals("pick")){ //means we perceived glitter and want to pick up Gold
          if(player.getBoard().getBoard()[player.getPos()[0]][player.getPos()[1]].hasGold()){ //if gold is at current location
            player.getBoard().getBoard()[player.getPos()[0]][player.getPos()[1]].setHasGold(false); //board no longer has gold
            player.setHasGold(true); //we know have the gold
            System.out.println("Got the Gold!!"); //display that we got the Gold to user
          }
          else{ //no gold at location
            System.out.println("No Gold here."); //tell user no gold
          } 
        }
        else{ //means it is a valid move
          int resultTrack = player.movee(move); //call the players move method, 'movee'
          if(resultTrack == -1){ //means we fell into pit
            System.out.println("Fallen into a pit"); //tell user
            alive = false; // we are now dead
          }
          else if(resultTrack == -2){ //means we ran into Wumpus
            System.out.println("Killed by Wumpus"); //tell user we are dead
            alive = false; //we are now dead
          }
          else if(resultTrack ==1){ //if move was valid
            boolean check = player.checkWin(); //check if we won
            if(check){ //if we did win
              System.out.println("You win!"); //display we win
              alive = false; //end the loop
            }
            else continue; //if we didnt win, continue
          }
          else{ //means we bumped into wall
            continue; //continue
          }
        }
      }
    }
    //display the moes taken throughout the game
    //final Board
    System.out.println("Board after game:");
    System.out.println(board);
    System.out.println("Moves taken: "); 
    System.out.println(player.getMoves());
    in.close(); //close scanner
  }

  //getInput for player choice
  public static String getInput(Scanner in){
    boolean good = false; //input not good yet
    String inp = "";
    while(!good){ // while we dont have good input
      //ask what direction we want to move or if we want to shoot or pick up gold
      System.out.print("What direction do you want to move(if want to shoot, type 'shoot' and if you want to pick up gold, 'pick'): ");
      inp = in.nextLine(); //get the input
      inp = inp.toLowerCase(); //set it to lowercase
      if(inp.equals("up")||inp.equals("down")||inp.equals("left")||inp.equals("right")|| inp.equals("shoot")||inp.equals("pick")){ //check if input is valid
        good = true; //set good to true because we have good input
      }
      else continue; //if invalid continue
    }
    return inp; //return the input
  }

  //gets the input for shot direction
  public static String getShootInput(Scanner in){
    boolean good = false; //input not good yet
    String inp = "";
    while(!good){ //while we dont have good input
      System.out.print("What direction do you want to shoot: "); //ask where we want to shoot
      inp = in.nextLine(); //read the input with scanner
      inp = inp.toLowerCase(); //make it lowercase
      if(inp.equals("up")||inp.equals("down")||inp.equals("left")||inp.equals("right")){ //check if its valid
        good = true; //if it is good input is true, so end loop
      }
      else continue; //otherwise, bad input, continue
    }
    return inp; //return input
  }
}
