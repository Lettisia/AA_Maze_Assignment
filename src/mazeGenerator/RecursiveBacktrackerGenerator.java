package mazeGenerator;

import maze.Maze;

/**
 * Implement maze generator by using
 * Recursive Backtracker algorithm
 * 
 *  @author LosoLai_21/09/2017
 */
public class RecursiveBacktrackerGenerator implements MazeGenerator {

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
     * 6: DFSR (s, traversalOrder)
     * 
     * ******************************************************************************************
     * 
     * @param maze Input Maze.
     * 
     */
	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub

	} // end of generateMaze()

} // end of class RecursiveBacktrackerGenerator
