package mazeSolver;

import maze.Cell;
import maze.Maze;
import maze.Wall;

import java.util.Stack;

/**
 * Implements WallFollowerSolver
 *
 * @author Loso Lai - 16-10-2017
 *
 * ALGORITHM : Using Right-Hand Rule
 * Finding the exit could be done just by
 * keeping one of your hands always touching a wall.
 *
 * pseudo code
 * while(current cell not match the exit cell)
 *   do
 *     if(current cell is in the dead end)
 *       go back the previous cell to check available path
 *     else
 *       pick the next cell for visiting
 * end while
 */

public class WallFollowerSolver implements MazeSolver {
    private final static int DEFAULT_DIRECTION = -1;
    private final Stack<Cell> traverseOrder;
    private boolean isSolved;
    private int cellsExplored;
    private Maze maze;
    private int pathDirection;
    //for different directions
    private final static int FacingNorthWest[] = {0, 1, 2, 3, 4, 5}; //2
    private final static int FacingWest[] = {1, 2, 3, 4, 5, 0}; //3
    private final static int FacingSouthWest[] = {2, 3, 4, 5, 0, 1}; //4
    private final static int FacingSouthEast[] = {3, 4, 5, 0, 1, 2}; //5
    private final static int FacingEast[] = {4, 5, 0, 1, 2, 3}; //0
    private final static int FacingNorthEast[] = {5, 0, 1, 2, 3, 4}; //1


    public WallFollowerSolver() {
        this.maze = null;
        this.isSolved = false;
        this.cellsExplored = 0;
        this.pathDirection = DEFAULT_DIRECTION;
        this.traverseOrder = new Stack<>();
    }

    @Override
    public void solveMaze(Maze maze) {
        this.maze = maze;

        //mark all the cells in the map to be unvisited
        setCellsVisitedValueAsDefault();

        //pick entrance as the first traverse cell
        traverseOrder.push(maze.entrance);

        while (!isSolved) {
            wallFollower();
        }

    } // end of solveMaze()

    /**
     * Mark each cell in the maze to be unvisited
     */
    private void setCellsVisitedValueAsDefault() {
        int row = maze.sizeR;
        int col = maze.sizeC;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (maze.map[i][j] != null)
                    maze.map[i][j].visited = false;
            }
        }
    } // end of setCellsVisitedValueAsDefault()

    /**
     * boolean wallFollower()
     * print footprint on the current cell
     * validation the current cell
     * if the current cell is exit cell
     *   will return true to terminate while loop
     * otherwise, keeping traverse the next cell
     *
     * next cell picking rule:
     * if enter to dead end then go previous cell
     * else pick the next cell to do next iteration
     */
    private void wallFollower() {
        if (traverseOrder.isEmpty())
            return;

        Cell current = traverseOrder.peek();
        maze.drawFtPrt(current);

        if (current == null)
            return;

        //mark current as visited
        current.visited = true;
        //count cellsExplored
        cellsExplored++;
        if (current == maze.exit) {
            isSolved = true;
            return;
        }

        //check the cell is in the dead end or not
        boolean isDeadEnd = isDeadEnd(current);
        if (isDeadEnd) {
            traverseOrder.pop();
            return;
        }

        Cell nextCell = checkNextPath(current);
        traverseOrder.push(nextCell);
    }

    /**
     * boolean isDeadEnd()
     * check the current cell is in the dead end or not
     *
     * @return true to pop the stack
     */
    private boolean isDeadEnd(Cell current) {
        //check available paths
        int wallSize = current.wall.length;
        for (int i = 0; i < wallSize; i++) {
            if (current.tunnelTo != null && !current.tunnelTo.visited)
                return false;

            if (current.wall[i] == null)
                continue;

            if (!current.wall[i].present) {
                if (!current.neigh[i].visited) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Cell checkNextPath(Cell current)
     * get the next cell for visiting which is follow the right hand rule based
     * on current path direction
     *
     * @param current cell
     * @return neigh cell for next visiting
     */
    private Cell checkNextPath(Cell current) {
        Cell cell = null;
        Wall wall;

        //check the tunnel cell
        //if haven't visited then pick as next cell
        if (current.tunnelTo != null && !current.tunnelTo.visited)
            return current.tunnelTo;

        //check the direction first
        int wallSize = current.wall.length;
        int[] dir = new int[wallSize];
        //order to check directions
        switch (pathDirection) {
            // which way is right if i'm facing east? check that way first
            case Maze.EAST: // 0
                dir = FacingEast;
                break;
            case Maze.NORTHEAST: // 1
                dir = FacingNorthEast;
                break;
            case Maze.NORTHWEST: // 2
                dir = FacingNorthWest;
                break;
            case Maze.WEST: // 3
                dir = FacingWest;
                break;
            case Maze.SOUTHWEST: // 4
                dir = FacingSouthWest;
                break;
            case Maze.SOUTHEAST: // 5
                dir = FacingSouthEast;
                break;
            case DEFAULT_DIRECTION:
                dir = FacingNorthWest;
                break;
        }


        //choose the available path
        for (int i = 0; i < wallSize; i++) {
            int index = dir[i];
            cell = current.neigh[index];
            wall = current.wall[index];

            if (cell == null || wall == null)
                continue;

            if (!current.wall[index].present && !current.neigh[index].visited) {
                pathDirection = index;
                return cell;
            }
        }

        return cell;
    }

    @Override
    public boolean isSolved() {
        return isSolved;
    } // end if isSolved()

    @Override
    public int cellsExplored() {
        return cellsExplored;
    } // end of cellsExplored()

} // end of class WallFollowerSolver
