import java.util.Arrays;
import java.util.Random;  // import Random class
import java.awt.Color; // import Color class
import java.awt.Font;
import java.awt.Point; // import Point class

// A class representing the tetromino 
public class Tetromino {
	// Private data fields
	private Color color; // color of the tetromino
	private boolean[][] shapeMatrix; // shape of the tetromino 
	private Point[][] coordinateMatrix; // coordinates of the tetromino w.r.t the game grid
	private int gridWidth, gridHeight; // dimensions of the tetris game grid
	private char shapeID;
	private Point[] rightmostBlock;
	private Point[] leftmostBlock;
	private boolean canRotate;
	private Tile [][] tileMatrix;

	// Constructor
	Tetromino (int gridHeight, int gridWidth, char shapeID) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
		this.shapeID = shapeID;
		// color of the tetromino is determined randomly
		Random random = new Random();
		int red = random.nextInt(256), green = random.nextInt(256), blue = random.nextInt(256);
		color = new Color(red, green, blue);
		

		if(shapeID == 'L') {
			// shape of the tetromino L in its initial orientation
			boolean [][] shape = {{false, false, false}, {true, true, true}, {true, false, false}};
			shapeMatrix = shape; }

		else if(shapeID == 'Z') {
			// shape of the tetromino Z in its initial orientation
			boolean [][] shape = {{true, true, false}, {false, true, true}, {false, false, false}};
			shapeMatrix = shape; }

		else if(shapeID == 'T') {
			// shape of the tetromino T in its initial orientation
			boolean [][] shape = {{false, false, false}, {true, true, true}, {false, true, false}};
			shapeMatrix = shape; }

		else if(shapeID == 'S') {
			// shape of the tetromino S in its initial orientation
			boolean [][] shape = {{false, true, true}, {true, true, false}, {false, false, false}};
			shapeMatrix = shape; }

		else if(shapeID == 'I') {
			// shape of the tetromino I in its initial orientation
			boolean [][] shape = {{false, true, false, false}, {false, true, false, false}, {false, true, false, false}, {false, true, false, false}};
			shapeMatrix = shape; }

		else if(shapeID == 'J') {
			// shape of the tetromino J in its initial orientation
			boolean [][] shape = {{false, false, false}, {true, true, true}, {false, false, true}};
			shapeMatrix = shape; }

		else if(shapeID == 'O') {
			// shape of the tetromino O in its initial orientation
			boolean [][] shape = {{false, false, false, false}, {false, true, true, false}, {false, true, true, false}, {false, false, false, false}};
			shapeMatrix = shape; }


		// initial coordinates just before the tetromino enters the game grid from the upper side
		// at a random horizontal position
		int n_rows = 1;
		int n_cols = 1;
		if(shapeID == 'I'  || shapeID == 'O') {
			n_rows = 4;
			n_cols = 4;
		}
		else {
			n_rows = 3;
			n_cols = 3;
		}
		tileMatrix = new Tile[n_rows][n_cols];
		coordinateMatrix = new Point[n_rows][n_cols];
		int lowerLeftCornerX = random.nextInt(gridWidth - (n_cols - 1)), lowerLeftCornerY = gridHeight;
		coordinateMatrix[n_rows - 1][0] = new Point(lowerLeftCornerX, lowerLeftCornerY);
		for (int row = n_rows - 1; row >= 0; row--)
			for (int col = 0; col < n_cols; col++) {
				if (row == n_rows - 1 && col == 0)
					continue;
				else if (col == 0) { 
					int currentX = coordinateMatrix[row + 1][col].x;
					int currentY = coordinateMatrix[row + 1][col].y + 1;
					coordinateMatrix[row][col] = new Point(currentX, currentY);
					continue;
				}
				int currentX = coordinateMatrix[row][col - 1].x + 1;
				int currentY = coordinateMatrix[row][col - 1].y; 
				coordinateMatrix[row][col] = new Point(currentX, currentY);
			}
	}

	public Point [][] map() {
		return coordinateMatrix;
	}

	// Getter method for getting the color of tetromino
	public Color getColor() {
		return color;
	}
	
	public char getShape() {
		return shapeID;
	}
	

	// Method for displaying tetromino on the game grid
	public void display() { 
		for (int row = 0; row < coordinateMatrix.length; row++)
			for (int col = 0; col < coordinateMatrix[0].length; col++) {
				Point point = coordinateMatrix[row][col];
				// considering newly entered tetromino L objects to the game grid that may have squares with point.y >= gridHeight
				if (point.y < gridHeight && shapeMatrix[row][col]) {
					Point position = new Point(point.x,point.y);
					Tile a = new Tile(position);
					tileMatrix[row][col] = a;
					StdDraw.setPenColor(a.getColor());
					StdDraw.filledSquare(point.x, point.y, 0.5);
					StdDraw.setFont(new Font("msyh", Font.BOLD, 20));
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.text(point.x,point.y, Integer.toString(a.getNumber()));
				}
			}
	}
	// Method for moving tetromino  down by 1 in the game grid
	public boolean goDown(Grid gameGrid) {
		// Check whether tetromino  can go down or not
		boolean canGoDown = true;
		// determine the coordinates of the bottommost block for each column of tetromino 
		Point dummyPoint = new Point(-1, -1);
		Point[] bottommostBlock;
		if(shapeID == 'I'  ||  shapeID == 'O') {
			Point[] bottommostBlockk = {dummyPoint, dummyPoint, dummyPoint, dummyPoint};
			bottommostBlock = bottommostBlockk;
		}
		else {
			Point[] bottommostBlockk = {dummyPoint, dummyPoint, dummyPoint};
			bottommostBlock = bottommostBlockk;
		}


		for (int col = 0; col < shapeMatrix[0].length; col++) {
			for (int row = shapeMatrix.length - 1; row >= 0; row--) {
				if (shapeMatrix[row][col]) {
					bottommostBlock[col] = coordinateMatrix[row][col];
					if (bottommostBlock[col].y == 0) // tetromino cannot go down if it is already at y = 0
						canGoDown = false;
					break; // break the inner for loop
				}
			}
			if (!canGoDown)
				break; // break the outer for loop
		}
		// check if the grid square below the bottommost block is occupied for each column of tetromino
		if (canGoDown) {
			for (int i = 0; i < bottommostBlock.length; i++) {
				// skip each column of tetromino that does not contain any blocks
				if (bottommostBlock[i].equals(dummyPoint))
					continue;
				// skip each column of tetromino whose bottommost block is out of the game grid 
				// (newly entered tetromino objects to the game grid) 
				if (bottommostBlock[i].y > gridHeight)
					continue;
				if (gameGrid.isOccupied(bottommostBlock[i].y - 1, bottommostBlock[i].x)) {
					canGoDown = false;
					break; // break the for loop
				}
			}
		}
		
		// move tetromino down by 1 in the game grid if it can go down
		if (canGoDown) {
			for (int row = 0; row < coordinateMatrix.length; row++)
				for (int col = 0; col < coordinateMatrix[0].length; col++)
					coordinateMatrix[row][col].y--;
		}
		// return the result
		return canGoDown;
	}
	// Method for returning the occupied squares w.r.t. the game grid by a placed (stopped) tetromino
	public Point[] getOccupiedSquares() {
		Point[] occupiedSquares = new Point[4];
		int count = 0;
		for (int row = 0; row < coordinateMatrix.length; row++)
			for (int col = 0; col < coordinateMatrix[0].length; col++)
				if (shapeMatrix[row][col])
					occupiedSquares[count++] = coordinateMatrix[row][col];
		return occupiedSquares;
	}
	// Method for moving tetromino left by 1 in the game grid
	public boolean goLeft(Grid gameGrid) {
		// Check whether tetromino can go left or not
		boolean canGoLeft = true;
		// determine the coordinates of the leftmost block for each row of tetromino
		Point dummyPoint = new Point(-1, -1);

		if(shapeID == 'I'  ||  shapeID == 'O') {
			Point[] leftmostBlockk = {dummyPoint, dummyPoint, dummyPoint, dummyPoint};
			leftmostBlock = leftmostBlockk;
		}
		else {
			Point[] leftmostBlockk = {dummyPoint, dummyPoint, dummyPoint};
			leftmostBlock = leftmostBlockk;
		}
		for (int row = 0; row < shapeMatrix.length; row++) {
			for (int col = 0; col < shapeMatrix[0].length; col++) {
				if (shapeMatrix[row][col]) {
					leftmostBlock[row] = coordinateMatrix[row][col];
					if (leftmostBlock[row].x == 0) // tetromino cannot go left if it is already at x = 0
						canGoLeft = false;
					break; // break the inner for loop
				}
			}
			if (!canGoLeft)
				break; // break the outer for loop
		}
		// check if the grid square on the left of the leftmost block is occupied for each row of tetromino
		if (canGoLeft) {
			for (int i = 0; i < leftmostBlock.length; i++) {
				// skip each row of tetromino L that does not contain any blocks
				if (leftmostBlock[i].equals(dummyPoint))
					continue;
				// skip each row of tetromino whose leftmost block is out of the game grid 
				// (newly entered tetromino objects to the game grid) 
				if (leftmostBlock[i].y >= gridHeight)
					continue;
				if (gameGrid.isOccupied(leftmostBlock[i].y, leftmostBlock[i].x - 1)) {
					canGoLeft = false;
					break; // break the for loop
				}
			}
		}
		// move tetromino left by 1 in the game grid if it can go left
		if (canGoLeft) {
			for (int row = 0; row < coordinateMatrix.length; row++)
				for (int col = 0; col < coordinateMatrix[0].length; col++)
					coordinateMatrix[row][col].x--;
		}
		// return the result
		return canGoLeft;
	}

	// Method for moving tetromino right by 1 in the game grid
	public boolean goRight(Grid gameGrid) {
		// Check whether tetromino can go right or not
		boolean canGoRight = true;
		// determine the coordinates of the rightmost block for each row of tetromino
		Point dummyPoint = new Point(-1, -1);

		if(shapeID == 'I'  ||  shapeID == 'O') {
			Point[] rightmostBlockk = {dummyPoint, dummyPoint, dummyPoint, dummyPoint};
			rightmostBlock = rightmostBlockk;
		}
		else {
			Point[] rightmostBlockk = {dummyPoint, dummyPoint, dummyPoint};
			rightmostBlock = rightmostBlockk;
		}

		for (int row = 0; row < shapeMatrix.length; row++) {
			for (int col = shapeMatrix[0].length - 1; col >= 0; col--) {
				if (shapeMatrix[row][col]) {
					rightmostBlock[row] = coordinateMatrix[row][col];
					if (rightmostBlock[row].x == gridWidth - 1) // tetromino cannot go right if it is already at x = gridWidth - 1
						canGoRight = false;
					break; // break the inner for loop
				}
			}
			if (!canGoRight)
				break; // break the outer for loop
		}
		// check if the grid square on the right of the rightmost block is occupied for each row of tetromino L
		if (canGoRight) {
			for (int i = 0; i < rightmostBlock.length; i++) {
				// skip each row of tetromino L that does not contain any blocks
				if (rightmostBlock[i].equals(dummyPoint))
					continue;
				// skip each row of tetromino whose rightmost block is out of the game grid 
				// (newly entered tetromino objects to the game grid) 
				if (rightmostBlock[i].y >= gridHeight)
					continue;
				if (gameGrid.isOccupied(rightmostBlock[i].y, rightmostBlock[i].x + 1)) {
					canGoRight = false;
					break; // break the for loop
				}
			}
		}
		// move tetromino right by 1 in the game grid if it can go right
		if (canGoRight) {
			for (int row = 0; row < coordinateMatrix.length; row++)
				for (int col = 0; col < coordinateMatrix[0].length; col++)
					coordinateMatrix[row][col].x++;
		}
		// return the result
		return canGoRight;
	}

	public void rotateLeft() {


		boolean canRotateLeft = true;
		// determine the coordinates of the leftmost block for each row of tetromino L
		Point dummyPoint = new Point(-1, -1);

		Point[] leftmostBlock;
		if(shapeID == 'I') {
			Point[] leftmostBlockk = {dummyPoint, dummyPoint, dummyPoint, dummyPoint};
			leftmostBlock = leftmostBlockk;
		}
		else {
			Point[] leftmostBlockk = {dummyPoint, dummyPoint, dummyPoint};
			leftmostBlock = leftmostBlockk;
		}
		for (int row = 0; row < shapeMatrix.length; row++) {
			for (int col = 0; col < shapeMatrix[0].length; col++) {
				if (shapeMatrix[row][col]) {
					leftmostBlock[row] = coordinateMatrix[row][col];
					if (leftmostBlock[row].x == 0) // tetromino L cannot go left if it is already at x = 0
						canRotateLeft = false;
					break; // break the inner for loop
				}
			}
			if (!canRotateLeft)
				break; // break the outer for loop
		}

		boolean canRotateRight = true;
		// determine the coordinates of the rightmost block for each row of tetromino L
		Point dummyPoint2 = new Point(-1, -1);

		Point[] rightmostBlock;
		if(shapeID == 'I' ) {
			Point[] rightmostBlockk = {dummyPoint2, dummyPoint2, dummyPoint2, dummyPoint2};
			rightmostBlock = rightmostBlockk;
		}
		else {
			Point[] rightmostBlockk = {dummyPoint2, dummyPoint2, dummyPoint2};
			rightmostBlock = rightmostBlockk;
		}
		for (int row = 0; row < shapeMatrix.length; row++) {
			for (int col = shapeMatrix[0].length - 1; col >= 0; col--) {
				if (shapeMatrix[row][col]) {
					rightmostBlock[row] = coordinateMatrix[row][col];
					if (rightmostBlock[row].x == gridWidth - 1 || rightmostBlock[row].x == gridWidth - 2) // tetromino L cannot go right if it is already at x = gridWidth - 1
						canRotateRight = false;
					break; // break the inner for loop
				}
			}
			if (!canRotateRight)
				break; // break the outer for loop
		}




		if(canRotateRight!=false && canRotateLeft!=false) {

			if(shapeID == 'I'){



				if(shapeMatrix[0][1]==true) {	
					shapeMatrix[0][1] = false;
					shapeMatrix[2][1] = false;
					shapeMatrix[3][1] = false;
					shapeMatrix[1][0] = true;
					shapeMatrix[1][2] = true;
					shapeMatrix[1][3] = true;
				}

				else if(shapeMatrix[1][0]==true) {
					shapeMatrix[1][0] = false;
					shapeMatrix[1][3] = false;
					shapeMatrix[1][1] = false;		
					shapeMatrix[0][2] = true;
					shapeMatrix[2][2] = true;
					shapeMatrix[3][2] = true;
				}

				else if(shapeMatrix[0][2]==true) {
					shapeMatrix[0][2] = false;
					shapeMatrix[1][2] = false;
					shapeMatrix[3][2] = false;		
					shapeMatrix[2][0] = true;
					shapeMatrix[2][1] = true;
					shapeMatrix[2][3] = true;
				}

				else if(shapeMatrix[2][0]==true) {
					shapeMatrix[2][0] = false;
					shapeMatrix[2][2] = false;
					shapeMatrix[2][3] = false;		
					shapeMatrix[0][1] = true;
					shapeMatrix[1][1] = true;
					shapeMatrix[3][1] = true;
				}	


			}

			if(shapeID == 'L'){
				if(shapeMatrix[2][0]==true) {	
					shapeMatrix[2][0] = false;
					shapeMatrix[1][0] = false;
					shapeMatrix[1][2] = false;
					shapeMatrix[0][0] = true;
					shapeMatrix[0][1] = true;
					shapeMatrix[2][1] = true;
				}

				else if(shapeMatrix[0][0]==true) {
					shapeMatrix[0][0] = false;
					shapeMatrix[0][1] = false;
					shapeMatrix[2][1] = false;		
					shapeMatrix[1][0] = true;
					shapeMatrix[1][2] = true;
					shapeMatrix[0][2] = true;
				}

				else if(shapeMatrix[0][2]==true) {
					shapeMatrix[0][2] = false;
					shapeMatrix[1][2] = false;
					shapeMatrix[1][0] = false;		
					shapeMatrix[0][1] = true;
					shapeMatrix[2][1] = true;
					shapeMatrix[2][2] = true;
				}

				else if(shapeMatrix[2][2]==true) {
					shapeMatrix[2][2] = false;
					shapeMatrix[0][1] = false;
					shapeMatrix[2][1] = false;		
					shapeMatrix[1][0] = true;
					shapeMatrix[1][2] = true;
					shapeMatrix[2][0] = true;
				}	


			}

			if(shapeID == 'J'){
				if(shapeMatrix[2][2]==true) {	
					shapeMatrix[2][2] = false;
					shapeMatrix[1][0] = false;
					shapeMatrix[1][2] = false;
					shapeMatrix[2][0] = true;
					shapeMatrix[0][1] = true;
					shapeMatrix[2][1] = true;
				}

				else if(shapeMatrix[2][0]==true) {
					shapeMatrix[2][0] = false;
					shapeMatrix[2][1] = false;
					shapeMatrix[0][1] = false;		
					shapeMatrix[0][0] = true;
					shapeMatrix[1][0] = true;
					shapeMatrix[1][2] = true;
				}

				else if(shapeMatrix[0][0]==true) {
					shapeMatrix[0][0] = false;
					shapeMatrix[1][2] = false;
					shapeMatrix[1][0] = false;		
					shapeMatrix[0][1] = true;
					shapeMatrix[2][1] = true;
					shapeMatrix[0][2] = true;
				}

				else if(shapeMatrix[0][2]==true) {
					shapeMatrix[0][2] = false;
					shapeMatrix[0][1] = false;
					shapeMatrix[2][1] = false;		
					shapeMatrix[1][0] = true;
					shapeMatrix[1][2] = true;
					shapeMatrix[2][2] = true;
				}	


			}
		}
		if(shapeID == 'S'){
			if(shapeMatrix[0][2]==true) {	
				shapeMatrix[0][2] = false;
				shapeMatrix[1][0] = false;
				shapeMatrix[1][2] = true;
				shapeMatrix[2][2] = true;
			}

			else if(shapeMatrix[2][2]==true) {
				shapeMatrix[0][1] = false;
				shapeMatrix[2][2] = false;	
				shapeMatrix[2][0] = true;
				shapeMatrix[2][1] = true;
			}

			else if(shapeMatrix[2][0]==true) {
				shapeMatrix[2][0] = false;
				shapeMatrix[1][2] = false;
				shapeMatrix[0][0] = true;
				shapeMatrix[1][0] = true;
			}

			else if(shapeMatrix[0][0]==true) {
				shapeMatrix[0][0] = false;
				shapeMatrix[2][1] = false;
				shapeMatrix[0][1] = true;
				shapeMatrix[0][2] = true;
			}	


		}
		if(shapeID == 'Z'){
			if(shapeMatrix[0][0]==true) {	
				shapeMatrix[0][0] = false;
				shapeMatrix[0][1] = false;
				shapeMatrix[2][1] = true;
				shapeMatrix[0][2] = true;
			}

			else if(shapeMatrix[0][2]==true) {
				shapeMatrix[0][2] = false;
				shapeMatrix[1][2] = false;	
				shapeMatrix[1][0] = true;
				shapeMatrix[2][2] = true;
			}

			else if(shapeMatrix[2][2]==true) {
				shapeMatrix[2][2] = false;
				shapeMatrix[2][1] = false;
				shapeMatrix[0][1] = true;
				shapeMatrix[2][0] = true;
			}

			else if(shapeMatrix[2][0]==true) {
				shapeMatrix[2][0] = false;
				shapeMatrix[1][0] = false;
				shapeMatrix[0][0] = true;
				shapeMatrix[1][2] = true;
			}	


		}

		if(canRotateRight!=false && canRotateLeft!=false) {

			if(shapeID == 'T'){
				if(shapeMatrix[1][0]==true && shapeMatrix[1][1]==true && shapeMatrix[1][2]==true && shapeMatrix[2][1]==true) {	
					shapeMatrix[1][2] = false;
					shapeMatrix[0][1] = true;
				}

				else if(shapeMatrix[0][1]==true && shapeMatrix[1][1]==true && shapeMatrix[2][1]==true && shapeMatrix[1][0]==true) {
					shapeMatrix[2][1] = false;
					shapeMatrix[1][2] = true;
				}

				else if(shapeMatrix[1][0]==true && shapeMatrix[1][1]==true && shapeMatrix[1][2]==true && shapeMatrix[0][1]==true) {
					shapeMatrix[1][0] = false;
					shapeMatrix[2][1] = true;
				}

				else if(shapeMatrix[0][1]==true && shapeMatrix[1][1]==true && shapeMatrix[2][1]==true && shapeMatrix[1][2]==true) {
					shapeMatrix[0][1] = false;
					shapeMatrix[1][0] = true;
				}	
			}

		}

	}
}