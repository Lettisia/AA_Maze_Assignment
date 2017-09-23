package mazeGenerator;

import java.util.ArrayList;
import java.util.Random;

import maze.*;

public class ModifiedPrimsGenerator implements MazeGenerator {
	private Random random = new Random();
	
	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub
		switch (maze.type) {
		case Maze.NORMAL:
			generateNormalMaze(maze);
		case Maze.HEX:
			generateHexMaze(maze);
		case Maze.TUNNEL:
			generateTunnelMaze(maze);
		}
	}

	private void generateTunnelMaze(Maze maze) {
		// TODO Auto-generated method stub
		
	}

	private void generateHexMaze(Maze maze) {
		// TODO Auto-generated method stub
		
	}

	private void generateNormalMaze(Maze maze) {
		// TODO Auto-generated method stub
		ArrayList<Cell> z = new ArrayList<>();
		z.add(maze.map[random.nextInt(maze.sizeR)][random.nextInt(maze.sizeC)]);
		
		
		
	} // end of generateMaze()

} // end of class ModifiedPrimsGenerator
