import java.awt.Color; // import Color class
import java.awt.Font;
import java.awt.Point; // import Point class
import java.util.ArrayList;

// A class representing the tetris game grid
public class Grid {
	// Private data fields
	private Color emptySquare; // color used for empty squares 
	private Color[][] colorMatrix; // a matrix storing colors of all squares

	// Constructor
	Grid (int n_rows, int n_cols) {
		// assigning color used for empty squares
		emptySquare = StdDraw.LIGHT_GRAY;
		// creating colorMatrix with given dimensions
		colorMatrix = new Color[n_rows][n_cols];
		// initializing colorMatrix with color emptySquare for all its elements 
		// using initMatrix method defined below
		initMatrix();	
	}

	public Color[][] getColorMatrix(){

		return colorMatrix;
	}

	// Method used for initializing colorMatrix 
	public void initMatrix() {
		for (int row = 0; row < colorMatrix.length; row++)
			for (int col = 0; col < colorMatrix[0].length; col++)
				colorMatrix[row][col] = emptySquare;
	}
	// Method used for checking whether the square with given indices is inside the grid or not
	public boolean isInside(int row, int col) {
		if (row < 0 || row >= colorMatrix.length)
			return false;
		if (col < 0 || col >= colorMatrix[0].length)
			return false;
		return true;
	}
	// Getter method for getting color of the square with given indices
	public Color getColor(int row, int col) {
		if (isInside(row, col))
			return colorMatrix[row][col];
		else
			return null;
	}
	// Setter method for setting color of the square with given indices
	public void setColor(Color color, int row, int col) {
		if (isInside(row, col))
			colorMatrix[row][col] = color;
	}
	// Method used for checking whether the square with given indices is occupied or empty
	public boolean isOccupied(int row, int col) {
		return colorMatrix[row][col] != emptySquare;
	}
	// Method for updating the game grid with a placed (stopped) tetromino
	public void updateGrid(Point[] occupiedSquaresByTetrominoL, Color colorOfTetrominoL) {
		for (Point point: occupiedSquaresByTetrominoL)
			if (isInside(point.y, point.x)) {
				colorMatrix[point.y][point.x] = colorOfTetrominoL;
			}

	}


	public int clear() {

		int score = 0;

		//Finding full lines and storing in array

		for (int m = 0; m < 20; m++) {

			int k = 0;

			for (int r = 0; r < 13; r++) {

				if(colorMatrix[m][r] != StdDraw.LIGHT_GRAY)
					k++;
				if(k == 13) {

					for (int c = 2+m; c < 21; c++) {
						for (int b = 0; b < 13; b++) {
							colorMatrix[c-2][b] = colorMatrix[c-1][b];
							score = 13;

						}
					}
				}
			}
		}
		return score;
	}

	// Method used for displaying the grid
	public void display() {
		// drawing squares
		for (int row = 0; row < colorMatrix.length; row++)
			for (int col = 0; col < colorMatrix[0].length; col++) {
				StdDraw.setPenColor(colorMatrix[row][col]);
				StdDraw.filledSquare(col, row, 0.5);
				StdDraw.setFont(new Font("msyh", Font.BOLD, 20));
				StdDraw.setPenColor(StdDraw.WHITE);
				if(colorMatrix[row][col] != StdDraw.LIGHT_GRAY)
					StdDraw.text(col,row, "2");
			}


		// drawing the grid
		StdDraw.setPenColor(StdDraw.DARK_GRAY);
		for (double x = -0.5; x < colorMatrix[0].length; x++) // vertical lines
			StdDraw.line(x, -0.5, x, colorMatrix.length - 0.5);
		for (double y = -0.5; y < colorMatrix.length; y++) // horizontal lines
			StdDraw.line(-0.5, y, colorMatrix[0].length - 0.5, y);
	}
}