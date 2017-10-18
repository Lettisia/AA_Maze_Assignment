package mazeSolver;

import maze.Cell;
import maze.HexMaze;
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
     *   if enter to dead end then go previous cell
     *   else pick the next cell to do next iteration
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

            if (!current.wall[i].present && !current.neigh[i].visited)
                return false;
        }

        return true;
    }

    /**
     * Cell checkNextPath(Cell current)
     * get the next cell for visiting
     * because the East direction index is 0
     * therefore the first priority is go East
     * which is follow the right hand rule
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

        //check the East direction first
        int currentR = current.r;
        int currentC = current.c;
        int deltaR = maze.deltaR[Maze.EAST];
        int deltaC = maze.deltaC[Maze.EAST];
        //boundary check
        if((currentR + deltaR < maze.sizeR && currentR + deltaR >= 0) &&
           (currentC + deltaC < maze.sizeC && currentC + deltaC >= 0))
        {
        	cell = maze.map[currentR + deltaR][currentC + deltaC];
            if(!cell.visited)
            {
            	//check the wall is not present
            	for(int i=0 ; i<current.neigh.length ; i++)
            	{
            		if(current.neigh[i] == null)
            			continue;
            		
            		if(current.neigh[i] == cell && !current.wall[i].present)
            			return cell;
            	}
            }
        }	
        
        int wallSize = current.wall.length;
        //choose the available path
        for (int i = 0; i < wallSize; i++) {
            cell = current.neigh[i];
            wall = current.wall[i];

            if (cell == null || wall == null)
                continue;

            if (!current.wall[i].present && !current.neigh[i].visited)
            {
            	pathDirection = i;
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
