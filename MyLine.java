package ch15;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

/**
 * Line class, will draw a line, change the 
 * color of the line, select a line and move a line
 *
 */
@SuppressWarnings("serial")
public class MyLine extends Line2D.Float implements MyShape{
	
	private int x1, y1; // start of line
	private int x2, y2; // end of line
	private Color lColor;
	
	/**
	 * Constructor that sets the private vars to passed what
	 * was passed 
	 * 
	 * @param X1- initial click of mouse x coord
	 * @param Y1- initial click of mouse y coord
	 * @param X2- after dragging, mouse x coord
	 * @param Y2- after dragging, mouse y coord
	 */
	public MyLine(int X1, int Y1, int X2,int Y2){

		x1 = X1;
		y1 = Y1;
		y2 = Y2;
		y1 = Y1;
	}
	
	/**
	 * Sets the color of the line and then draws it
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(lColor);
		g.drawLine(x1, y1, x2, y2);
	}

	// Sets the classwide color variable
	@Override
	public void setColor(Color color) {
		lColor = color;
	}
	
	/**
	 * Will check to see if you clicked on the line
	 */
	@Override
	public boolean isSelected(int x, int y) {

		//loops, check through every x, y, adds a buffer
		for(int i = x1-2; i<=x2+2;i++){
			for(int z=y1-2;z<=y2+2;z++){
				if (x == i && y == z){
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Will move the shape using the top left coords
	 */
	@Override
	public void move(int dx, int dy) {
		x1= dx;
		y1= dy;
	}

	// Line cannot be filled
	@Override
	public void setFilled(boolean filled) {
		
	}
}
