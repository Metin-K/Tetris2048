import java.awt.Color; // import Color class
import java.awt.Font;
import java.awt.Point; // import Point class
import java.util.Arrays;
import java.util.Random;

// A program demonstrating the following tetris basics: 
// 1. drawing the game environment as a grid 
// 2. modeling the tetromino using 2d arrays 
// 3. tetromino entering the game environment from a random horizontal position
// 4. tetromino going down by one grid automatically in each iteration
// 5. using keyboard keys (a and d) for moving the tetromino left/right by one in each iteration
//    (checking for collisions with side boundaries and occupied squares in the grid)
// 6. detecting when the active tetromino stops due to reaching the bottom of the game environment 
//    or colliding with occupied squares in the grid
// 7. updating the game grid with each placed (stopped) tetromino
public class TetrisBasics {
	public static void main(String[] args) {
		// set the size of the drawing canvas
		StdDraw.setCanvasSize(550, 650);
		// set the scale of the coordinate system
		StdDraw.setXscale(-0.5, 17.5);
		StdDraw.setYscale(-0.5, 19.5);
		// double buffering is used for speeding up drawing needed to enable computer animations 
		StdDraw.enableDoubleBuffering();


		int score = 0;

		// create a grid as the tetris game environment
		Grid gameGrid = new Grid(20, 13);
		// create the first tetromino to enter the game grid
		Tetromino t = new Tetromino(20, 13, 'I');
		boolean createANewTetromino = false;
		boolean finish = false;

		// main animation loop
		while (true)  { 
			
			
			Color [][] matrix = gameGrid.getColorMatrix();
			int over = 0;
			for (int r = 0; r < 12; r++) {

				if(matrix[19][r] != StdDraw.LIGHT_GRAY) {
					over++;
					if(over > 4)
						finish = true;
								
				}
					
			}
			
			if(over > 5) {

				break;
                
			}
			// keyboard interaction for moving the active tetromino left or right
			boolean success = false;
			if (StdDraw.hasNextKeyTyped()) {
				char ch = StdDraw.nextKeyTyped();            
				if (ch == 'a') // move the active tetromino left by one
					success = t.goLeft(gameGrid);
				else if (ch == 'd') // move the active tetromino right by one
					success = t.goRight(gameGrid);
				else if (ch == 's') // rotate
					t.rotateLeft();
				else if (ch == ' ') { // drop
					for (int c = 0; c < 20; c++) {
						success = t.goDown(gameGrid);
					}
				}
			}
			// move the active tetromino down by one if a successful move left/right is not performed
			if (!success)
				success = t.goDown(gameGrid);
			// place (stop) the active tetromino on the game grid if it cannot go down anymore
			createANewTetromino = !success;
			if (createANewTetromino) {
				// update the game grid by adding the placed tetromino
				Point[] occupiedSquares = t.getOccupiedSquares();
				Color color = t.getColor();
				gameGrid.updateGrid(occupiedSquares, color);
				// create the next tetromino to enter the game grid 
				Random random = new Random();
				int rand = random.nextInt(6);
				char shape = 'a';

				if(rand == 0) {
					shape = 'T';
				}
				if(rand == 1) {
					shape = 'I';
				}
				if(rand == 2) {
					shape = 'L';
				}
				if(rand == 3) {
					shape = 'O';
				}
				if(rand == 4) {
					shape = 'J';
				}
				if(rand == 5) {
					shape = 'Z';
				}
				if(rand == 6) {
					shape = 'S';
				}

				t = new Tetromino(20, 13, shape);
				score += gameGrid.clear();  //Clearing rows and adding scores

		}

			// clear the background (double buffering)
			StdDraw.clear(StdDraw.GRAY);
			// draw the game grid
			gameGrid.display();
			
			if(finish) {
				
				StdDraw.setFont(new Font("msyh", Font.BOLD, 80));
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.text(8,10, "GAME OVER");
			}

			StdDraw.setFont(new Font("msyh", Font.BOLD, 20));
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.text(15,17, "SCORE");
			StdDraw.text(15,16, String.valueOf(score));
			StdDraw.text(15, 5, "NEXT");
			// draw the active tetromino
			t.display();
			// copy offscreen buffer to onscreen (double buffering)
			StdDraw.show();
			// pause for 300 ms (double buffering) 
			StdDraw.pause(300);
		}
	}
}