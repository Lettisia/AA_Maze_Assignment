package mazeSolver;

import java.util.Stack;

import maze.Cell;
import maze.Maze;
import maze.Wall;

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
 * do
 * if(current cell is in the dead end)
 * 		go back the previous cell to check available path
 * else
 * 		pick the next cell for visiting
 * end while
 */

public class WallFollowerSolver implements MazeSolver {
	private Maze maze;
    private boolean isSolved;
    private int cellsExplored;
    private Stack<Cell> traverseOrder;
    
    public WallFollowerSolver()
    {
    	this.maze = null;
    	this.isSolved = false;
    	this.cellsExplored = 0;
    	this.traverseOrder = new Stack<Cell>();
    }

	@Override
	public void solveMaze(Maze maze) {
		this.maze = maze;
		
		//mark all the cells in the map to be unvisited
		setCellsVisitedValueAsDefault();
		
		//pick entrance as the first traverse cell
		traverseOrder.push(maze.entrance);
		
		boolean result = false;
		while(!isSolved)
			result = wallFollower();

	} // end of solveMaze()
	
	/**
     * Mark each cell in the maze to be unvisited
     */
	private void setCellsVisitedValueAsDefault()
	{
		int row = maze.sizeR;
		int col = maze.sizeC;
		
		for(int i=0 ; i<row ; i++)
		{
			for(int j=0 ; j<col ; j++)
			{
				if(maze.map[i][j] != null)
					maze.map[i][j].visited = false;
			}
		}
	} // end of setCellsVisitedValueAsDefault()
	
	/**
	 * boolean wallFollower()
     * print footprint on the current cell
     * validation the current cell
     * if the current cell is exit cell 
     * will return true to terminate while loop
     * otherwise, keeping traverse the next cell
     * 
     * next cell picking rule:
     * if enter to dead end then go previous cell
     * else pick the next cell to do next iteration
     * 
     * @return true to terminate while loop
     */
	private boolean wallFollower()
	{
		if(traverseOrder.isEmpty())
			return true;
		
		Cell current = traverseOrder.peek();
		maze.drawFtPrt(current);
		
		if(current == null)
			return false;
		
		//mark current as visited
		current.visited = true;
		//count cellsExplored
		cellsExplored++;
		if(current == maze.exit)
		{
			isSolved = true;
			return true;
		}
		
		//check the cell is in the dead end or not
		boolean isDeadEnd = isDeadEnd(current);
		if(isDeadEnd)
		{
			traverseOrder.pop();
			return false;
		}
		
		Cell nextCell = checkNextPath(current);
		traverseOrder.push(nextCell);
		return false;
	}
	
	/**
	 * boolean isDeadEnd()
     * check the current cell is in the dead end or not
     * 
     * @return true to pop the stack
     */
	private boolean isDeadEnd(Cell current)
	{
		//check available paths
		int wallSize = current.wall.length;
		for(int i=0 ; i<wallSize ; i++)
		{
			if(current.tunnelTo != null && !current.tunnelTo.visited)
				return false;
			
			if(current.wall[i] == null)
				continue;
			
			if(!current.wall[i].present && !current.neigh[i].visited)
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
     * @param Cell current
     * @return neigh cell for next visiting
     */
	private Cell checkNextPath(Cell current)
	{
		Cell cell = null;
		Wall wall = null;
		
		//check the tunnel cell
		//if haven't visited then pick as next cell
		if(current.tunnelTo != null && !current.tunnelTo.visited)
			return current.tunnelTo;
		
		//check the most right path first
		//East direction
		int wallSize = current.wall.length;
		
		//choose the available path
		//would check East direction first
		//because Maze.East index is 0
		for(int i=0 ; i<wallSize ; i++)
		{
			cell = current.neigh[i];
			wall = current.wall[i];
			
			if(cell == null || wall == null)
				continue;
			
			if(!current.wall[i].present && !current.neigh[i].visited)
				return cell;
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
