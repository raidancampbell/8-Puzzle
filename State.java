/*State(int[][] puzzleStateGiven) (constructor)
 *   Initializes the puzzleState to the integer array given
 * 
 * String answerHash()
 *   Returns a formatted string representation of the answer board, ready for outputting
 * 
 * State randomize(int moves)
 *   Mixes the board around by the given number of moves
 * 
 * String hash()
 *   Returns a formatted string representation of the current board, ready for outputting
 * 
 * State[] getChildren()
 *   returns the possible children of the current State
 *   BONUS!!! The child with the lowest (closest to the answer) Manhattan Priority is added last
 *   This greatly reduces (but doesn't fully optimize) the depth first search
 * 
 * up() moves the blank cell up
 * down() moves the blank cell down
 * left() moves the blank cell left
 * right() moves the blank cell right
 * 
 * boolean canMoveUp() whether or not the blank cell can move up
 * boolean canMoveDown() whether or not the blank cell can move down
 * boolean canMoveLeft() whether or not the blank cell can move left
 * boolean canMoveRight() whether or not the blank cell can move right
 * 
 * findTile() syncs the tileRow & tileCol with the blank tile's actual position
 * 
 * boolean isAns() whether or not the given state is the answer state
 * 
 * State DFS()
 *   Returns the depth first search answer of the State.
 *   This is NOT just pure DFS, a slight heuristic was added using the Manhattan Priority
 *   This is NOT just pure A*, as the heuristic wasn't 100% efficiently implemented
 *   Cycle through the parents to follow the lineage
 * 
 * State BFS()
 *   Returns the breadth first search answer of the State.
 *   Cycle through the parents to follow the lineage
 * 
 * int manhattanPriority()
 *   Returns the Manhattan Priority of the current State.
 *   Does not implement the lineage moves, only gives the number of moves each
 *   tile must make to get to the goal state.
 */


import java.util.*;
import java.io.*;

public class State
{
  public int[][] puzzleState;
  public int length;//equals the width because it's a square
  public State parent;
  public int tileRow, tileCol = 0;//location of tile that can be moved
  
  State()
  {}//yay for null constructors!!!
  
  State(int[][] puzzleStateGiven)
  {
    this.length = puzzleStateGiven.length;
    this.puzzleState = new int[length][length];
    try
    {
    if(puzzleStateGiven.length < 2 || puzzleStateGiven[0].length < 2)
    {
      puzzleStateGiven = Solver.createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"}).puzzleState;
    }
    if(puzzleStateGiven.length != puzzleStateGiven[0].length)
    {
      puzzleStateGiven = Solver.createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"}).puzzleState;
    }
    } catch (Exception e)
    {
      puzzleStateGiven =  Solver.createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"}).puzzleState;
    }
    this.length = puzzleStateGiven.length;
    try
    {
    for(int i = 0; i<length;i++) this.puzzleState[i] = puzzleStateGiven[i].clone();
    }catch(Exception e) {
      puzzleStateGiven = Solver.createPuzzle(8, 1, 0, new String[]{"0","1","2","3","4","5","6","7","8"}).puzzleState;
    }
  }//end of constructor
  
  public String answerHash()
  {
    StringBuilder ans = new StringBuilder();
    int sum = 0;
    for(int i = 0;i<length;i++)
    {
      for(int j =0;j<length;j++) ans.append(sum++).append(' ');
      ans.append("\n");
    }
    return ans.toString();
  }
  
  public State randomize(int moves)
  {
    State randomized = new State(this.puzzleState);
    while(moves > 0)
    {
      randomized.findTile();
      State children[] = randomized.getChildren();
      randomized = children[(int) Math.floor(Math.random() * children.length)];
      moves--;
    }
    return randomized;
  }//end of randomize
  
  public String hash()
  {//the hash is just the visial representation of the board as a String.  Useful for printing out, too
    StringBuilder hash = new StringBuilder();
    for(int[] array : puzzleState)
    {
      for(int element : array)
      {
        hash.append(element).append(' ');
      }
      hash.append("\n");
    }
    return hash.toString();
  }//end of hash
  
  
  private State[] getChildren()
  {
    LinkedList<State> children = new LinkedList<State>();
    LinkedList<Integer> priorities = new LinkedList();
    LinkedList<State> answer = new LinkedList<State>();
    if(canGoUp())
    {
      children.add(up());
      priorities.add(up().manhattanPriority());
    }
    if(canGoDown())
    {
      children.add(down());
      priorities.add(down().manhattanPriority());
    }
    if(canGoLeft())
    {
      children.add(left());
      priorities.add(left().manhattanPriority());
    }
    if(canGoRight()) 
    {
      children.add(right());
      priorities.add(right().manhattanPriority());
    }
    if(priorities.size() > 0)
    {
      int[] priorityArray = new int[priorities.size()];
      int i = 0;
      for(Integer in:priorities) priorityArray[i++] = in;
      
      for( i = 0; i< priorityArray.length;i++)
      {
        boolean isSmallest = true;
        for(int comparedTo:priorityArray)
        {
          if(Math.min(priorityArray[i], comparedTo) != priorityArray[i]) isSmallest = false;
        }
        if(isSmallest) break;//i now holds the value for the best move
      }
      for(State child: children)
      {
        if(!children.get(i).hash().equals(child.hash())) answer.add(child);
      }
      answer.add(children.get(i));
    }
    
    return answer.toArray(new State[answer.size()]);
  }
  
  private State up()
  {
    if(!canGoUp()) return this;//invalid call
    State changed = new State(this.puzzleState);
    changed.puzzleState[tileRow][tileCol] = changed.puzzleState[tileRow-1][tileCol];
    changed.puzzleState[tileRow-1][tileCol] = 0;
    changed.tileRow--;
    return changed;
  }
  
  private State down()
  {
    if(!canGoDown()) return this;//invalid call
    State changed = new State(this.puzzleState);
    changed.puzzleState[tileRow][tileCol] = changed.puzzleState[tileRow+1][tileCol];
    changed.puzzleState[tileRow+1][tileCol] = 0;
    changed.tileRow++;
    return changed;
  }
  private State left()
  {
    if(!canGoLeft()) return this;//invalid call
    State changed = new State(this.puzzleState);
    changed.puzzleState[tileRow][tileCol] = changed.puzzleState[tileRow][tileCol-1];
    changed.puzzleState[tileRow][tileCol-1] = 0;
    changed.tileCol--;
    return changed;
  }
  private State right()
  {
    if(!canGoRight()) return this;//invalid call
    State changed = new State(this.puzzleState);
    changed.puzzleState[tileRow][tileCol] = changed.puzzleState[tileRow][tileCol+1];
    changed.puzzleState[tileRow][tileCol+1] = 0;
    changed.tileCol++;
    return changed;
  }
  
  private boolean canGoUp(){return (tileRow != 0);}
  private boolean canGoLeft(){return(tileCol != 0);}
  private boolean canGoDown(){return (tileRow != length - 1);}
  private boolean canGoRight(){return(tileCol != length - 1);}
  
  private void findTile()
  {
    for(int i = 0; i<length;i++)
    {
      for(int j = 0; j<length;j++)
      {
        if(puzzleState[i][j] == 0)
        {
          tileRow = i;
          tileCol = j;
        }
      }
    }
  }//end of findTile
  
  public boolean isAns()
  {
    return this.hash().equals(answerHash());
  }
  
  
  public State DFS() {
    findTile();
    HashSet<String> inStack = new HashSet<String>();
    Stack stack = new Stack();
    this.parent = null;
    stack.push(this);
    inStack.add(((State)stack.peek()).hash());
    while (!stack.empty()) 
    {
      State current = (State) stack.pop();
      current.findTile();
      if (current.isAns())
      {
        return current;
      }
      inStack.add(current.hash());
      for (State child : current.getChildren())
      {
        if (!inStack.contains(child.hash())) 
        {
          child.parent = current;
          if(child.isAns()) return child;
          stack.push(child);
          inStack.add(child.hash());
        }
      }
      if((((double)Runtime.getRuntime().freeMemory()) / ((double)Runtime.getRuntime().totalMemory())) < 0.00001)
      {
        System.out.println("Failed due to lack of memory");
        return current;
      }
    }
    System.out.println("Failed to find a valid solution");
    return this;
  }
  
  public State BFS()
  {
    Queue q = new LinkedList();//queue is too funny to type.  q is used as a variable name instead.
    State current = this;
    q.add(current);
    HashSet<String> inQueue = new HashSet<String>();
    while(!q.isEmpty())
    {
      current = (State) q.remove();
      if(current.isAns())
      {
        return current;
      }
      inQueue.add(current.hash());
      current.findTile();
      for (State child : current.getChildren())
      {
        if (!inQueue.contains(child.hash())) 
        {
          child.parent = current;
          if(child.isAns()) return child;
          q.add(child);
          inQueue.add(child.hash());
        }
      }
      if((((double)Runtime.getRuntime().freeMemory()) / ((double)Runtime.getRuntime().maxMemory())) < 0.00001)
      {
        System.out.println("Failed due to lack of memory");
        return current;
      }
    }
    System.out.println("Failed to find an answer using breadth first search");
    return current;//failure
  }//end of BFS
  
  
  private int manhattanPriority()
  {//lower priority means closer to the solution
    int priority = 0;
    for(int row = 0; row<length;row++)
    {
      for(int col = 0; col<length;col++)
      {
        if(puzzleState[row][col] != 0)
        {
          int temp[] = Solver.createPuzzle(length*length - 1,1,0,new String[]{""}).findTile(puzzleState[row][col]);
          priority += Math.abs(row-temp[0])+Math.abs(col-temp[1]);
        }
      }
    }
    return priority;
  }
  
  private int[][] answerState()
  {
    State temp = Solver.createPuzzle(length*length - 1,1,0,new String[]{""});
    return temp.puzzleState;
  }
  
  private int[] findTile(int x)
  {
    int ans[] = new int[2];
    for(int row = 0; row<length;row++)
    {
      for(int col = 0; col<length;col++)
      {
        if(puzzleState[row][col] == x)
        {
          ans[0] = row;
          ans[1] = col;
          return ans;
        }
      }
    }
    return ans;
  }//end of findTile
  
}//end of State class