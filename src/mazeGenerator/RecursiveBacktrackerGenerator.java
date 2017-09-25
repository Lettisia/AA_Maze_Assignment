package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * Implement maze generator by using
 * Recursive Backtracker algorithm
 * 
 *  @author LosoLai_21/09/2017
 */
public class RecursiveBacktrackerGenerator implements MazeGenerator {
	private Random random;

    /** 
     * Depth first search traversal of input maze from the entrance cell
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM Recursive Backtracker / DFS ( G )
     * Perform a Depth first search traversal of a maze.
     * Input: Maze maze, starting entrance cell
     * OUTPUT : Maze maze with its cell marked with consecutive integers in the order they were visited/processed.
     * 
     * 1: traversalOrder = {}
     * // mark all vertices unvisited
     * 2: for i = 0 to row do
     * 3:    for j = 0 to col do
     * 4:    marked[ i ][ j ] = false
     * 5: end for
     * // initiate DFS from entrance cell 
     * 6: DFSR (entrance, traversalOrder)
     * 
     * ******************************************************************************************
     * 
     * @param maze Input Maze.
     * 
     */
	@Override
	public void generateMaze(Maze maze) {
		random = new Random(System.currentTimeMillis());
		
		Stack<Cell> traversalOrder = new Stack<Cell>();
		
		recursiveDFS(maze, maze.entrance, traversalOrder);

	} // end of generateMaze()
	
	
	/**
     * Recursive DFS method, that implements DFS visitation semantics.
     * 
     * ******************************************************************************************
     * 
     * ALGORITHM DFSR(current, traversalOrder)
     * Recursively visit all connected vertices.
     * INPUT: A seed/starting cell, array of cell visited, traversal order of cell visited so far
     * OUTPUT : None
     * 
     * 1: traversalOrder.add(current)
     * 2: visited[cell.Row][cell.Col] = true
     * 3: for neighbour(cell) do
     * 4:     if not marked[ w ] then
     * 5:         DFSR (w, traversalOrder)
     * 6:     end if
     * 7: end for
     * 
     * ******************************************************************************************
     * 
     * @param maze Input Maze.
     * @param cell Current cell visited.
     * @param marked Array of booleans, that indicate whether a vertex has been
     *      visited yet.
     * @param traversalOrder LinkedList of cells, stored in the order they were visited so far in the
     *      DFS traversal.
     */
	private void recursiveDFS(Maze maze, Cell current, Stack<Cell> traversalOrder)
	{		
		System.out.println("Visited :" + current);
		
		//mark as visited
		current.visited = true;
		
		Cell randomCell = null;
		ArrayList<Cell> unvisited = checkAllVisited(current.neigh);
		if(unvisited.size() > 0)
		{
			//add current into traversalOrder
	      	traversalOrder.push(current);
	      	
			//pick a random starting cell
			randomCell = unvisited.get(random.nextInt(unvisited.size()));
	      	
	      	//remove the wall
	        for(int i=0 ; i<current.wall.length ; i++)
	        {
	        	for(int j=0 ; j<randomCell.wall.length ; j++)
	        	{
	        		if(current.wall[i] != null &&
	        		   randomCell.wall[j] != null &&
	        		   current.wall[i].equals(randomCell.wall[j]))
	        		{
	        			//remove walls
	        			current.wall[i].present = false;
	        			randomCell.wall[j].present = false;
	        		}
	        	}
	        }
		}
		else
		{
			//trace back
			randomCell = traversalOrder.pop();
		}
      	
        //recursive
        if(!traversalOrder.isEmpty())
        {
        	recursiveDFS(maze, randomCell, traversalOrder);
        }
	}
	
	private ArrayList<Cell> checkAllVisited(Cell[] neighbours)
	{
		ArrayList<Cell> unvisited = new ArrayList<Cell>();
		for(int i=0 ; i<neighbours.length ; i++)
		{
			if(neighbours[i] == null)
				continue;
			
			if(!neighbours[i].visited)
				unvisited.add(neighbours[i]);
		}
		return unvisited;
	}

} // end of class RecursiveBacktrackerGenerator
