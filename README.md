8-Puzzle
========

A proof-of concept to generate and solve an 8-puzzle, as seen here: http://en.wikipedia.org/wiki/15_puzzle
The code has expanded to handle puzzles of N-by-N size

Solver.java is the main file.  Execute it, and it will hold your hand through the whole process.

State.java is where the magic happens.

Tester.java is where the "testing" happens.  I had no concept of testing when I wrote this.

Depth First Search has a wee bit of a heuristic applied to it, allowing the total lineage length to decrease from tens of thousands to a few hundred or less.

Yes, there is memory management.  When the available memory becomes a certain fraction of the total memory, the loop terminates.  In both depth first and breadth first search.  If you don't believe me, check the source. Lines 269 and 304 of State.java have the stuff.

Due to some inefficiencies within various environments (I'm looking at you, Dr. Java), the System.out.println() function is greatly slowed.  If you actually care about printing out the lineage of a search, I'd strongly recommend running the code through a native terminal.
