import java.awt.Font;
import java.util.Arrays;
import java.util.Random;  // import Random class
import java.awt.Color; // import Color class
import java.awt.Point; // import Point class

public class Tile {
	
	private Font numfont = new Font("myth",Font.BOLD,18);
	private int number;  //tile number
	private Point position;
	private Color tileColor;
	
	Tile(){
		
		Random random = new Random();
		int num = random.nextInt(2);
		
		if(num == 1) {
			number = 2;
			tileColor = new Color(255,0,0);
		}
		    
		if(num == 0) {
			number = 4;
			tileColor = new Color(25,162,253);
		}
	}
	
	Tile(Point position) {
		
		this(); //Information of default constructor
		this.position = position; 
		
	}
	
	public Color getColor() {
		return tileColor;
	}
	
	public int getNumber() {
		return number;
	}
}
