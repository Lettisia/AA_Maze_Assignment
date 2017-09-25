package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

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
		
		Queue<Cell> traversalOrder = new LinkedList<Cell>();
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		
		recursiveDFS(maze, maze.entrance, traversalOrder, neighbours);

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
	private void recursiveDFS(Maze maze, Cell current, Queue<Cell> traversalOrder, ArrayList<Cell> neighbours)
	{		
		//add current into traversalOrder
		traversalOrder.add(current);
		System.out.println("Visited :" + current);
		
		//mark as visited
		current.visited = true;
		
		//add current's neighbours into arraylist
		int number = current.neigh.length;
		for(int i=0 ; i<number ; i++)
		{
			Cell neighbour = current.neigh[i];
			if(neighbour != null && neighbour.visited == false)
				neighbours.add(current.neigh[i]);
		}
		
		//pick a random starting cell
        Cell randomCell = neighbours.get(random.nextInt(neighbours.size()));
        
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
        
        //remove from neighbours
      	neighbours.remove(randomCell);
      	
        //recursive
        for(int i=0 ; i<maze.sizeR ; i++)
        {
        	for(int j=0 ; j<maze.sizeC ; j++)
        	{
        		if(!maze.map[i][j].visited)
        			recursiveDFS(maze, randomCell, traversalOrder, neighbours);
        	}
        }
	}

} // end of class RecursiveBacktrackerGenerator
