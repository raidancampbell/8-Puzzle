/* main method (no args)
 *     Holds the user's hand throughout building the puzzle
 * 
 * createPuzzle(int tiles, int response, int moves, String[] initialBoard)
 *    returns a State with the number of given tiles, and either a number of moves away from the solution (if response == 1)
 *    or initializes the board to a given state (if response == 2)
 * 
 */

import java.io.*;
import java.util.*;

public class Solver
{
  public static void main(String[] args)
  {
    InputStreamReader reader = new InputStreamReader(System.in);
    BufferedReader input = new BufferedReader(reader);
    boolean isValidInput = false;
    int tiles = 0;
    int response = 0;
    int randomMoves = 0;
    String initialBoard[] = null;
    
    while(!isValidInput)
    {
      try
      {
        System.out.println("How many tiles does the puzzle have? Valid inputs are (n*n -1): 8, 15, 24, etc...");
        System.out.println("Invalid numerical inputs will be truncated to the nearest valid input");
        tiles = Integer.parseInt(input.readLine());
        if(tiles < 3) tiles =3;
        isValidInput = true;
      } catch (Exception e)
      {
        System.out.println("Invalid input. Please try again.\n");
      }
    }
    isValidInput = false;
    
    while(!isValidInput)
    {
      try{
        System.out.println("Enter 1 for a board with random displacements");
        System.out.println("Enter 2 to define your own board");
        response = Integer.parseInt(input.readLine());
        if(response == 1 || response == 2) isValidInput = true;
      } catch (Exception e)
      {
        System.out.println("Invalid input. Please try again.\n");
      }
    }
    isValidInput = false;
    
    
    if(response == 1)
    {//user's giving me the number of random moves to make
      while(!isValidInput)
      {
        try{
          System.out.println("Enter the number of random displacements: (positive integer)");
          randomMoves = Integer.parseInt(input.readLine());
          isValidInput = true;
        } catch (Exception e){
          System.out.println("Invalid input. Please try again.\n");
        }
      }
    } else {//user's giving me a new board
      while(!isValidInput)
      {
        try{
          System.out.println("Enter a space delimited board.  The standard 8-tile board answer is 0 1 2 3 4 5 6 7 8");
          initialBoard = input.readLine().trim().split(" ");
          for(int i = 0; i< initialBoard.length;i++)
          {//ensures that user's entry was numeric, and in the proper range of possible values
            if(Integer.parseInt(initialBoard[i])>i) throw new IndexOutOfBoundsException();
            if(Integer.parseInt(initialBoard[i])<0) throw new IndexOutOfBoundsException();
            for(int j = 0; j<initialBoard.length;j++)
            {//ensures no duplicates are given
              if(Integer.parseInt(initialBoard[i]) == Integer.parseInt(initialBoard[j]) && i != j) throw new IndexOutOfBoundsException();
            }
          }
          if(tiles == initialBoard.length - 1) isValidInput = true;//ensures that the proper number of values was given
        }catch (Exception e){
          System.out.println("Invalid input. Please try again.\n");
        }
      }
    }
    
    
    
    System.out.println("Print search lineage? (y/n)");
    isValidInput = false;
    String in = "";
    while(!isValidInput)
    {
      try
      {
        in = input.readLine();
        if(in.toLowerCase().equals("y")) isValidInput = true;
        if(in.toLowerCase().equals("n")) isValidInput = true;
      }catch (Exception e) {
        System.out.println("Please enter a valid response (y/n)");
      }
    }
    
    Stack bfs = new Stack();
    Stack dfs = new Stack();
    State myPuzzle = createPuzzle(tiles, response, randomMoves, initialBoard);
    System.out.println("Initial puzzle:\n");
    System.out.println(myPuzzle.hash());
    
    System.out.println("Breadth First Search for answer:");
    State BFSResult = myPuzzle.BFS();
    if(BFSResult.hash().equals(myPuzzle.answerHash()))
    {
      System.out.println("Success");
      int depth = 0;
      while(BFSResult.parent != null)
      {
        depth++;
        if(in.equals("y")) bfs.push(BFSResult.hash());
        BFSResult = BFSResult.parent;
      }
      System.out.println("found at a depth of "+depth);
      if(in.equals("y")) System.out.println(myPuzzle.hash());//prints out the initial state
      while(!bfs.empty())//then all the solution steps after it
      {
        System.out.println(bfs.pop());
      }
    }//end of BFS junk
    
    System.out.println("Depth First Search for answer:");
    State DFSResult = myPuzzle.DFS();
    if(DFSResult.hash().equals(myPuzzle.answerHash()))
    {
      System.out.println("Success");
      int depth = 0;
      while(DFSResult.parent != null)
      {
        depth++;
        if(in.equals("y")) dfs.push(DFSResult.hash());
        DFSResult = DFSResult.parent;
      }
      System.out.println("found at a depth of "+depth+"\n\n");
      boolean isSure = false;
      if(in.equals("y"))
      {
        System.out.println("Are you really sure you want to print out a lineage this long? (y/n)");
        System.out.println("Certain environments can drastically slow down standard outputs,");
        System.out.println("causing this operation to possibly take several minutes if the lineage is over 20,000.");
        System.out.println("For best results run straight from a terminal");
        try{
          if(input.readLine().toLowerCase().equals("y"))isSure = true;
        }catch (Exception e) {
        }
      }
      if(isSure)
      {
        System.out.println(myPuzzle.hash());
        while(!dfs.empty())
        {
          System.out.println(dfs.pop());
        }
      }
    }//end of DFS junk
    
  }//end of main method
  
  
  
  
  public static State createPuzzle(int tiles, int response, int moves, String[] initialBoard)
  {//tiles and response are always given. randomMoves and initialBoard only exist based on what 'response' is
    //TODO: double check the input string
    if(initialBoard == null) initialBoard = new String[]{"0","1","2","3","4","5","6","7","8"};
    if(tiles < 3 || initialBoard.length != tiles)
    {
      if(tiles < 3) return createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"});
      if(response == 2)return createPuzzle(initialBoard.length, 1, 0, initialBoard);//bad pass. Return the answer board
    }
    if(response != 1 && response != 2) return createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"});
    if(response == 2)
    {
      for(int i = 0; i< initialBoard.length;i++)
      {//ensures that user's entry was numeric, and in the proper range of possible values
        if(Integer.parseInt(initialBoard[i])>tiles) throw new IndexOutOfBoundsException();
        if(Integer.parseInt(initialBoard[i])<0) throw new IndexOutOfBoundsException();
        for(int j = 0; j<initialBoard.length;j++)
        {//ensures no duplicates are given
          if(Integer.parseInt(initialBoard[i]) == Integer.parseInt(initialBoard[j]) && i != j) return createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"});
        }
      }
    }
    State s = new State();
    s.length = (int) Math.sqrt(tiles + 1);
    if(s.length * s.length != initialBoard.length && response == 2) return createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"});
    s.puzzleState = new int[s.length][s.length];
    int number = 0;
    for(int i = 0; i<s.length;i++)
    {
      for(int j = 0;j<s.length;j++)
      {
        if(response == 1)s.puzzleState[i][j] = number;//creates the answer
        else s.puzzleState[i][j] = Integer.parseInt(initialBoard[number]);//creates the given board
        number++;
      }
    }
    if(response == 1) s = s.randomize(moves);//randomizes from the answer
    return s;
  }
}//end of Solver class