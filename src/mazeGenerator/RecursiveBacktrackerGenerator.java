package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Implement maze generator by using
 * Recursive Backtracker algorithm
 *
 * @author LosoLai_21/09/2017
 */
public class RecursiveBacktrackerGenerator implements MazeGenerator {
    private Random random;

    /**
     * Depth first search traversal of input maze from the entrance cell
     * <p>
     * ******************************************************************************************
     * <p>
     * ALGORITHM Recursive Backtracker / DFS ( G )
     * Perform a Depth first search traversal of a maze.
     * Input: Maze maze, starting entrance cell
     * OUTPUT : Maze maze with its cell marked with consecutive integers in the order they were visited/processed.
     * <p>
     * 1: traversalOrder = {entrance}
     * // add all unvisited cells into the unVisitedCells ArrayList
     * 2: for i = 0 to row do
     * 3:    for j = 0 to col do
     * 4:    	if(!maze.map[i][j].visited)
     * 5:				unvisited.add(maze.map[i][j]);
     * 6: end for
     * // initiate DFS from entrance cell
     * 7: while loop terminate condition: when unVisitedCells is none
     * 8: recursiveDFS (unVisitedCells, traversalOrder)
     * <p>
     * ******************************************************************************************
     *
     * @param maze Input Maze.
     */
    @Override
    public void generateMaze(Maze maze) {
        random = new Random(System.currentTimeMillis());

        Stack<Cell> traversalOrder = new Stack<>();
        ArrayList<Cell> unVisitedCells = checkAllVisited(maze);

        //pick entrance as the starting order
        traversalOrder.push(maze.entrance);

        while (unVisitedCells.size() > 0)
            backtracker(unVisitedCells, traversalOrder);

    } // end of generateMaze()


    /**
     * backtracker method, that implements DFS visitation semantics.
     * <p>
     * ******************************************************************************************
     * <p>
     * INPUT: A seed/starting cell, array of cell unvisited, traversal order of cell visited so far
     * OUTPUT : None
     * <p>
     * 1: current.visited = true
     * 2: remove current cell from unVisitedCells
     * 3: if traversalOrder is not empty
     * 4: 	pick next random cell
     * 5: 	remove the connected walls
     * 6:  	push the next cell into stack for visiting next
     * 7: else
     * 8:   pop the stack for checking the previous cell
     * <p>
     * ******************************************************************************************
     *
     * @param unVisitedCells LinkedList of cells
     * @param traversalOrder Stack of cells, stored in the order they were visited so far in the
     *                       DFS traversal.
     */
    private void backtracker(ArrayList<Cell> unVisitedCells, Stack<Cell> traversalOrder) {
        if (traversalOrder.isEmpty())
            return;

        Cell current = traversalOrder.peek();

        //mark as visited
        current.visited = true;
        System.out.println("Visited :" + current);

        //remove from unVisitedCells
        if (unVisitedCells.isEmpty())
            return;

        unVisitedCells.remove(current);

        //check the tunnel cell
        //if haven't visited then pick as next cell
        if (current.tunnelTo != null && !current.tunnelTo.visited) {
            //push into the stack
            traversalOrder.push(current.tunnelTo);
            return;
        }

        Cell randomCell;
        ArrayList<Cell> unvisited = checkAllVisited(current.neigh);
        if (unvisited.size() > 0) {
            //pick a random starting cell
            randomCell = unvisited.get(random.nextInt(unvisited.size()));

            //remove the wall
            for (int i = 0; i < current.wall.length; i++) {
                for (int j = 0; j < randomCell.wall.length; j++) {
                    if (current.wall[i] != null &&
                            randomCell.wall[j] != null &&
                            current.wall[i].equals(randomCell.wall[j])) {
                        //remove walls
                        current.wall[i].present = false;
                        randomCell.wall[j].present = false;
                    }
                }
            }
        } else {
            //trace back
            if (traversalOrder.isEmpty())
                return;

            traversalOrder.pop();
            return;
        }

        //push into the stack
        traversalOrder.push(randomCell);
    }

    private ArrayList<Cell> checkAllVisited(Cell[] neighbours) {
        ArrayList<Cell> unvisited = new ArrayList<>();
        for (Cell neighbour : neighbours) {
            if (neighbour == null)
                continue;

            if (!neighbour.visited)
                unvisited.add(neighbour);
        }
        return unvisited;
    }

    private ArrayList<Cell> checkAllVisited(Maze maze) {
        ArrayList<Cell> unvisited = new ArrayList<>();
        int numCol = maze.type == Maze.HEX ? ((maze.sizeR + 1) / 2 + maze.sizeC) : maze.sizeC;
        for (int i = 0; i < maze.sizeR; i++) {
            for (int j = 0; j < numCol; j++) {
                if (maze.map[i][j] == null)
                    continue;

                if (!maze.map[i][j].visited)
                    unvisited.add(maze.map[i][j]);
            }
        }
        return unvisited;
    }

} // end of class RecursiveBacktrackerGenerator
