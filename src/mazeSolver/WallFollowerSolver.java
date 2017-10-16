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
		
		while(!isSolved)
			isSolved = wallFollower();

	} // end of solveMaze()
	
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
	
	private boolean wallFollower()
	{
		Cell current = traverseOrder.peek();
		maze.drawFtPrt(current);
		
		if(current == null)
			return false;
		
		//mark current as visited
		current.visited = true;
		//count cellsExplored
		cellsExplored++;
		if(current == maze.exit)
			return true;
		
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
	
	private boolean isDeadEnd(Cell current)
	{
		//check available paths
		int wallSize = current.wall.length;
		for(int i=0 ; i<wallSize ; i++)
		{
			if(current.wall[i] == null)
				continue;
			
			if(!current.wall[i].present && !current.neigh[i].visited)
				return false;
		}
		
		return true;
	}
	
	private Cell checkNextPath(Cell current)
	{
		Cell cell = null;
		Wall wall = null;
		
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
