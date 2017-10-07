package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.Stack;

/**
 * Implements the BiDirectional recursive backtracking maze solving algorithm.
 *
 * @author Lettisia George - 07-10-2017
 * <p>
 * ALGORITHM Recursive Backtracker Solver / DFS
 * Solve the maze by performing a depth first search traversal of a maze from both
 * the entrance and exit.
 * Stop when two paths meet.
 * Input: Maze maze, starting entrance cell and exit cell
 * Output: Maze maze with its cell marked with consecutive integers in the order
 * they were visited/processed.
 * <p>
 * traversalOrder = {}
 * // mark all vertices unvisited
 * for i = 0 to row do
 * for j = 0 to col do
 * marked[i][j] = false
 * end for
 * // initiate DFS from entrance cell and exit cell
 * add entrance and exit to separate stacks
 * while !pathsCross
 * for each of next cells in entrance and exit traversal stacks
 * for i = 0 to maze.NUMDIR
 * select a neighbouring cell to the current cell
 * if (visited)
 * if (neighbour is a member of stack from other direction)
 * pathsCross = true
 * end if
 * end if
 * if (!visited)
 * add neighbour to correct stack
 * visited = true
 * break
 * end if
 * end for
 * end while
 */
public class BiDirectionalRecursiveBacktrackerSolver implements MazeSolver {
    private Maze maze = null;
    private boolean solved = false;
    private int cellsExplored = 0;
    private Stack<Cell> traverseFromStart = new Stack<>();
    private Stack<Cell> traverseFromEnd = new Stack<>();


    @Override
    public void solveMaze(Maze maze) {
        this.maze = maze;

        // Mark all cells as unvisited
        markCellsUnvisited();

        // Start with the entrance and exit
        Cell entrance = maze.entrance;
        Cell exit = maze.exit;
        // Mark them as visited
        entrance.visited = true;
        exit.visited = true;
        // Draw a footprint
        maze.drawFtPrt(entrance);
        maze.drawFtPrt(exit);
        // Add each to the correct traversal stack
        traverseFromStart.push(entrance);
        traverseFromEnd.push(exit);
        // Add one each for exit and entrance
        cellsExplored += 2;
        // used for loop stopping condition
        boolean done = false;

        // Loop that does the work. Check if paths have met in the middle and
        // whether either stack is empty
        while (!done && !traverseFromStart.empty() && !traverseFromEnd.empty()) {
            // Perform one iteration of DFS on each traverse path
            // Done will be true is the solution paths meet
            done = isDoneMazeIteration(traverseFromStart, traverseFromEnd);
            done = done || isDoneMazeIteration(traverseFromEnd, traverseFromStart);
        }

        // If done is true then we made it through the maze!
        solved = done;

    } // end of solveMaze()


    /**
     * One iteration of the Depth First Search algorithm
     *
     * @param sameDirection  the traversal stack to find the next cell for
     * @param otherDirection the traversal stack for the opposite direction
     * @return true if a neighbouring cell is found that is contained in otherDirection
     */
    private boolean isDoneMazeIteration(Stack<Cell> sameDirection, Stack<Cell> otherDirection) {
        boolean done = false;
        Cell entrance = sameDirection.peek();
        Cell nextEntrance = null;

        if (maze.type == Maze.TUNNEL && entrance.tunnelTo != null) {
            if (entrance.tunnelTo.visited) {
                done = otherDirection.contains(entrance.tunnelTo);
            } else {
                sameDirection.push(entrance.tunnelTo);
            }
        } else {

            // Loop through each direction looking for unvisited cells
            for (int i = 0; i < Maze.NUM_DIR && nextEntrance == null; i++) {
                // Cells must be not null and there must be no wall
                if (entrance.neigh[i] != null && !entrance.wall[i].present) {
                    // If the cell is not visited we select it as the next cell
                    if (!entrance.neigh[i].visited) {
                        // select the neighbour of entrance in direction i
                        nextEntrance = entrance.neigh[i];
                        // add to the appropriate stack
                        sameDirection.push(nextEntrance);
                        // Mark the new cell as visited
                        nextEntrance.visited = true;
                        // increment the number of cells explored
                        cellsExplored++;
                        // Add a footprint to the maze
                        maze.drawFtPrt(nextEntrance);
                    } else {
                        // If the cell has been visited check if it is part of the
                        // solution found from the other end and stop if it is
                        done = done || otherDirection.contains(entrance.neigh[i]);
                    }
                }
            }
            // If no unvisited cell was found remove current cell from stack
            if (nextEntrance == null) {
                sameDirection.pop();
            }
        }
        return done;
    }

    /**
     * Mark each cell in the maze to unvisited
     */
    private void markCellsUnvisited() {
        // Setup special number of columns for hex mazes
        int numCol = maze.type == Maze.HEX ? ((maze.sizeR + 1) / 2 + maze.sizeC) : maze.sizeC;

        // Mark every cell as not visited
        for (int i = 0; i < maze.sizeR; i++) {
            for (int j = 0; j < numCol; j++) {
                // false == not visited
                if (maze.map[i][j] != null)
                    maze.map[i][j].visited = false;
            }
        }
    }


    @Override
    public boolean isSolved() {
        return solved;
    } // end of isSolved()


    @Override
    public int cellsExplored() {
        return cellsExplored;
    } // end of cellsExplored()

} // end of class BiDirectionalRecursiveBackTrackerSolver
