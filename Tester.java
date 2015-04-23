/* Solver's Class
 * 
 * createPuzzle(int tiles, int response, int moves, String[] initialBoard)
 *    returns a State with the number of given tiles, and either a number of moves away from the solution (if response == 1)
 *    or initializes the board to a given state (if response == 2)
 */

/* State's Class
 * 
 * State(int[][] puzzleStateGiven) (constructor)
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
 */


public class Tester {
    public static void main(String[] args) {
        System.out.println("testing Solver's method createPuzzle()");
        testSolverCreatePuzzle();
        System.out.println("testing State's constructor");
        testStateConstructor();
        System.out.println("Testing State's method answerHash()");
        testStateAnswerHash();
        System.out.println("Testing State's method hash()");
        testStateHash();
        System.out.println("Testing State's method DFS()");
        testStateDFS();
        System.out.println("Testing State's method BFS()");
        testStateBFS();
    }//end of main method

    public static void testSolverCreatePuzzle() {//createPuzzle(int tiles, int response, int moves, String[] initialBoard)
        //returns a State
        //testing the possible size of puzzles
        System.out.println("Testing faulty tile inputs");
        State puz8 = Solver.createPuzzle(8, 1, 0, new String[]{""});//valid
        System.out.println(puz8.hash());
        State puz9 = Solver.createPuzzle(9, 1, 0, new String[]{""});
        System.out.println(puz9.hash());
        State puz0 = Solver.createPuzzle(0, 1, 0, new String[]{""});
        System.out.println(puz0.hash());
        State puzNeg = Solver.createPuzzle(-10, 1, 0, new String[]{""});
        System.out.println(puzNeg.hash());
        State puzBig = Solver.createPuzzle(9999, 1, 0, new String[]{""});
        System.out.println(puzBig.hash());
        System.out.println("That was intentional.  It was a really big tile grid.");

        //testing different responses that can be given
        System.out.println("Testing faulty responses");
        State response1 = Solver.createPuzzle(8, 1, 0, new String[]{""});//valid
        System.out.println(response1.hash());
        State response2 = Solver.createPuzzle(8, 2, 0, new String[]{""});//valid
        System.out.println(response2.hash());
        State response0 = Solver.createPuzzle(8, 0, 0, new String[]{""});
        System.out.println(response0.hash());
        State responseNeg = Solver.createPuzzle(8, -1, 0, new String[]{""});
        System.out.println(responseNeg.hash());
        State responseBig = Solver.createPuzzle(8, 99, 0, new String[]{""});
        System.out.println(responseBig.hash());

        //testing number of possible randomizations
        System.out.println("Testing faulty randomizations");
        State random0 = Solver.createPuzzle(8, 1, 0, new String[]{""});//valid
        System.out.println(random0.hash());
        State random1 = Solver.createPuzzle(8, 1, 1, new String[]{""});//valid
        System.out.println(random1.hash());
        State randomBig = Solver.createPuzzle(8, 1, 123, new String[]{""});//valid
        System.out.println(randomBig.hash());
        State randomNeg = Solver.createPuzzle(8, 1, -10, new String[]{""});
        System.out.println(randomNeg.hash());

        //testing possible given board states
        System.out.println("testing faulty board states");
        String[] boardNorm = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        State norm = Solver.createPuzzle(8, 2, 0, boardNorm);
        System.out.println(norm.DFS().hash());
        String[] boardBadLength = new String[]{"0", "1", "2", "3", "4", "5", "6", "7"};
        State badLength = Solver.createPuzzle(8, 2, 0, boardBadLength);
        System.out.println(badLength.DFS().hash());
        String[] boardChar = new String[]{"0", "1", "2", "u", "n", "f", "r", "l", "q"};
        State bChar = Solver.createPuzzle(8, 2, 0, boardChar);
        System.out.println(bChar.DFS().hash());
        String[] boardNonChar = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8675309"};
        State nonChar = Solver.createPuzzle(8, 2, 0, boardNonChar);
        System.out.println(nonChar.DFS().hash());
        String[] boardNull = {""};
        State bNull = Solver.createPuzzle(8, 2, 0, boardNull);
        System.out.println(bNull.DFS().hash());
        String[] boardRepeat = new String[]{"0", "1", "1", "1", "1", "1", "1", "1", "1"};
        State repeat = Solver.createPuzzle(8, 2, 0, boardRepeat);
        System.out.println(repeat.DFS().hash());
        String[] boardNoZero = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        State noZero = Solver.createPuzzle(8, 2, 0, boardNoZero);
        System.out.println(noZero.DFS().hash());
    }

    public static void testStateConstructor() {//State(int[][] puzzleStateGiven)
        System.out.println("This test fails.  When passed funky array length values, response is erratic");
        int[][] asymmetric = new int[3][4];
        int[][] singleNull = new int[4][0];
        int[][] doubleNull = new int[0][0];
        int[][] proper = new int[3][3];
        State sAsymmetric = new State(asymmetric);
        System.out.println(sAsymmetric.hash());
        State sSingleNull = new State(singleNull);
        System.out.println(sSingleNull.hash());
        State sDoubleNull = new State(doubleNull);
        System.out.println(sDoubleNull.hash());
    }

    public static void testStateAnswerHash() {
        String[] boardNorm = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        State norm = Solver.createPuzzle(8, 2, 0, boardNorm);
        System.out.println(norm.answerHash());
        System.out.println(Solver.createPuzzle(15, 1, 0, boardNorm).answerHash());
        System.out.println(Solver.createPuzzle(24, 1, 0, boardNorm).answerHash());
        System.out.println(Solver.createPuzzle(35, 1, 0, boardNorm).answerHash());
    }

    public static void testStateHash() {
        String[] boardNorm = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        State norm = Solver.createPuzzle(8, 1, 123, boardNorm);
        System.out.println(norm.hash());
        System.out.println(Solver.createPuzzle(15, 1, 123, boardNorm).hash());
        System.out.println(Solver.createPuzzle(24, 1, 123, boardNorm).hash());
        System.out.println(Solver.createPuzzle(35, 1, 123, boardNorm).hash());
    }

    public static void testStateDFS() {
        String[] boardNorm = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        State norm = Solver.createPuzzle(8, 1, 123, boardNorm);
        System.out.println(norm.DFS().hash());
        System.out.println(Solver.createPuzzle(15, 1, 20, boardNorm).DFS().hash());
        System.out.println("Attempting to fill memory and bring the program to a halt.  Be patient.");
        System.out.println(Solver.createPuzzle(255, 1, 50, boardNorm).DFS().hash());
    }

    public static void testStateBFS() {
        String[] boardNorm = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        State norm = Solver.createPuzzle(8, 1, 123, boardNorm);
        System.out.println(norm.BFS().hash());
        System.out.println(Solver.createPuzzle(15, 1, 20, boardNorm).BFS().hash());
        System.out.println("Attempting to fill memory and bring the program to a halt.  Be patient.");
        System.out.println(Solver.createPuzzle(255, 1, 50, boardNorm).BFS().hash());
    }
}