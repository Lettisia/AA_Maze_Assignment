package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates a maze using a modified version of Prim's Algorithm.
 * <p>
 * MODIFIED PRIM'S ALGORITHM (from assignment specification):
 * 1. Pick a random starting cell and add it to set Z (initially Z is empty, after addition it contains
 * just the starting cell). Put all neighbouring cells of starting cell into the frontier set F.
 * 2. Randomly select a cell c from the frontier set and remove it from F. Randomly select a cell b
 * that is in Z and adjacent to the cell c. Carve a path between c and b.
 * 3. Add cell c to the set Z. Add the neighbours of cell c to the frontier set F.
 * 4. Repeat step 2 until Z includes every cell in the maze. At the end of the process, we have
 * generated a perfect maze.
 *
 * @param maze Input maze
 * @author Lettisia George
 */

public class ModifiedPrimsGenerator implements MazeGenerator {

    @Override
    public void generateMaze(Maze maze) {
        Random random = new Random(System.currentTimeMillis());

        // if tunnel, do nothing
        if (maze.type == Maze.TUNNEL) {
            return;
        }

        // setup done, frontier and theRest
        ArrayList<Cell> done = new ArrayList<>();
        ArrayList<Cell> frontier = new ArrayList<>();
        ArrayList<Cell> theRest = new ArrayList<>();

        // If hex maze use larger column index if rectangular use number of columns
        int maxCol = maze.type == Maze.HEX ? ((maze.sizeR + 1) / 2 + maze.sizeC) : maze.sizeC;

        // add all cells to theRest
        for (int j = 0; j < maxCol; j++) {
            for (int i = 0; i < maze.sizeR; i++) {
                if (maze.map[i][j] != null) {
                    theRest.add(maze.map[i][j]);
                }
            }
        }

        // Pick a random starting cell
        Cell currentCell = theRest.get(random.nextInt(theRest.size()));

        // move it from theRest to done
        done.add(currentCell);
        theRest.remove(currentCell);
        // Move all non-null neighbouring cells from theRest into frontier
        for (int i = 0; i < currentCell.neigh.length; i++) {
            if (currentCell.neigh[i] != null) {
                frontier.add(currentCell.neigh[i]);
                theRest.remove(currentCell.neigh[i]);
            }
        }

        // while done is not empty
        while (theRest.size() > 0 || frontier.size() > 0) {
            // randomly select a non-null cell from frontier
            Cell newCell = frontier.get(random.nextInt(frontier.size()));
            //System.out.println("newCell: " + newCell);

            // find the cells in done that are adjacent to newCell
            ArrayList<Cell> adj = new ArrayList<>();
            // loop through newCell's neighbours
            for (int i = 0; i < newCell.neigh.length; i++) {
                // does done contain neigh?
                if (newCell.neigh[i] != null && done.contains(newCell.neigh[i])) {
                    // found it! save it.
                    adj.add(newCell.neigh[i]);
                }
            }
            // pick a random adjacent cell
            Cell doneCell = adj.get(random.nextInt(adj.size()));

            // delete the shared wall
            for (int i = 0; i < newCell.wall.length; i++) {
                for (int j = 0; j < doneCell.wall.length; j++) {
                    // Is it null? Is it shared?
                    if (newCell.wall[i] != null && doneCell.wall[j] != null && newCell.wall[i].equals(doneCell.wall[j])) {
                        // DELETE!!!
                        newCell.wall[i].present = false;
                        // DELETE!!!
                        doneCell.wall[j].present = false;
                    }
                }
            }

            // add neighbours of newCell to frontier
            for (int i = 0; i < newCell.neigh.length; i++) {
                // Is it null? Is it outside the frontier and done?
                if (newCell.neigh[i] != null && theRest.contains(newCell.neigh[i])) {
                    // move it from theRest to frontier
                    frontier.add(newCell.neigh[i]);
                    theRest.remove(newCell.neigh[i]);
                }
            }
            // add newCell to done
            done.add(newCell);
            // remove it from frontier
            frontier.remove(newCell);
        }
    } // end of generateMaze()

} // end of class ModifiedPrimsGenerator
