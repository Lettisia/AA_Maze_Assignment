package mazeGenerator;

import java.util.ArrayList;
import java.util.Random;

import maze.*;

public class ModifiedPrimsGenerator implements MazeGenerator {
    private Random random;

    @Override
    public void generateMaze(Maze maze) {
        random = new Random(System.currentTimeMillis());

        // if tunnel, do nothing
        if (maze.type == Maze.TUNNEL) {
            return;
        }

        // setup done, frontier and theRest
        ArrayList<Cell> done = new ArrayList<>();
        ArrayList<Cell> frontier = new ArrayList<>();
        ArrayList<Cell> theRest = new ArrayList<>();

        // If hex maze use larger column index if rectangular use number of columns
        int maxCol = maze.type == Maze.HEX ? ((maze.sizeC + 1) / 2 + maze.sizeC) : maze.sizeC;

        // add all cells to theRest
        for (int i = 0; i < maze.sizeR; i++) {
            for (int j = 0; j < maxCol; j++) {
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
            System.out.println("newCell: " + newCell);

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
