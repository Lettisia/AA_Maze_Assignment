package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Random;

/**
 * Growing tree maze generator. As it is very general, here we implement as
 * "usually pick the most recent cell, but occasionally pick a random cell"
 * <p>
 * Algorithm: (from assignment specification)
 * 1. Pick a random starting cell and add it to set Z (initially Z is empty,
 * after addition it contains just the starting cell).
 * 2. Using a particular strategy (see below) select a cell b from Z. If cell
 * b has unvisited neighbouring cells, randomly select a neighbour, carve a
 * path to it, and add the selected neighbour to set Z.
 * If b has no unvisited neighbours, remove it from Z.
 * 3. Repeat step 2 until Z is empty.
 * <p>
 * The strategy:
 * A. With a probability of threshold select a random cell from those already
 * visited.
 * B. With a probability of 1-threshold select the last cell marked.
 *
 * @author Lettisia George
 */

public class GrowingTreeGenerator implements MazeGenerator {
    //

    @Override
    public void generateMaze(Maze maze) {
        Random random = new Random(System.currentTimeMillis());

        // if tunnel, do nothing
        if (maze.type == Maze.TUNNEL) {
            return;
        }


        // setup marked, frontier and unmarked
        ArrayList<Cell> marked = new ArrayList<>();
        ArrayList<Cell> unmarked = new ArrayList<>();

        // If hex maze use larger column index if rectangular use number of columns
        int maxCol = maze.type == Maze.HEX ? ((maze.sizeR + 1) / 2 + maze.sizeC) : maze.sizeC;

        // add all cells to unmarked
        for (int j = 0; j < maxCol; j++) {
            for (int i = 0; i < maze.sizeR; i++) {
                if (maze.map[i][j] != null) {
                    unmarked.add(maze.map[i][j]);
                }
            }
        }

        // Pick a random starting cell
        Cell currentCell = unmarked.get(random.nextInt(unmarked.size()));

        // move it from unmarked to marked
        marked.add(currentCell);
        unmarked.remove(currentCell);

        Cell nextCell;
        Cell lastCell = currentCell;

        // while unmarked is not empty
        while (unmarked.size() > 0) {
            // Select the last cell most of the time but sometimes select a random cell
            double threshold = 0.1;
            if (random.nextDouble() < threshold) {
                // Select random cell from marked
                nextCell = marked.get(random.nextInt(marked.size()));
            } else {
                // use the last cell added
                nextCell = lastCell;
            }

            //System.out.println("nextCell: " + nextCell);

            // find the cells that are adjacent to nextCell and not in marked
            ArrayList<Cell> adj = new ArrayList<>();
            // loop through nextCell's neighbours
            for (int i = 0; i < nextCell.neigh.length; i++) {
                // does marked contain neigh?
                if (nextCell.neigh[i] != null && unmarked.contains(nextCell.neigh[i])) {
                    // found it! save it.
                    adj.add(nextCell.neigh[i]);
                }
            }

            // no adjacent unvisited cells? try again
            if (adj.size() == 0) {
                continue;
            }

            // pick a random adjacent cell
            Cell neighCell = adj.get(random.nextInt(adj.size()));

            // delete the shared wall
            for (int i = 0; i < nextCell.wall.length; i++) {
                for (int j = 0; j < neighCell.wall.length; j++) {
                    // Is it null? Is it shared?
                    if (nextCell.wall[i] != null && neighCell.wall[j] != null
                            && nextCell.wall[i].equals(neighCell.wall[j])) {
                        // DELETE!!!
                        nextCell.wall[i].present = false;
                        // DELETE!!!
                        neighCell.wall[j].present = false;
                    }
                }
            }

            // add nextCell to marked
            marked.add(neighCell);
            // remove it from unmarked
            unmarked.remove(neighCell);
            // save neighCell for later
            lastCell = neighCell;
        }
    }

}
