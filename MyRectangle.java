package ch15;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * class that will create a rectangle, fill a rectangle
 * select a rectangle, and move a selected rectangle
 *
 */
@SuppressWarnings("serial")
public class MyRectangle extends Rectangle implements MyShape{

	private int rX, rY; // top left corner
	private int w, h; // width and height
	private Color rColor; // color
	private boolean isFilled = false; 
	
	/**
	 * Rectangle constructor, takes in 2 x values and 2 y vals
	 * the first two values are the initial click point 
	 * and the 2nd two are the end point after dragging a shape
	 * 
	 * This constructor then figures out which x and/or y values
	 * are larger so that the smallest values can be used for the
	 * top left corner. Then it figures out the width and height 
	 * of the rectangle
	 * 
	 * 
	 * @param x1- initial click of mouse x coord
	 * @param y1- initial click of mouse y coord
	 * @param x2- end x coord of mouse after drag
	 * @param y2- end y coord of mouse after drag
	 */
	public MyRectangle(int x1, int y1, int x2, int y2) {

		if(x1>=x2){
			rX = x2;
			w = x1-x2;
		}
		else{
			rX = x1;
			w = x2-x1;
		}
		if(y1>=y2){
			rY = y2;
			h = y1-y2;
		}
		else{
			rY = y1;
			h = y2-y1;
		}
	}

	/**
	 * This will draw the rectangle
	 * Also will set the color and fill it accordingly
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(rColor);

		if(isFilled == true){
			g.fillRect(rX, rY, w, h);	
		}

		g.drawRect(rX, rY, w, h);
	}

	/**
	 * Sets the color of the rectangle using a
	 * classwide variable "rColor" 
	 * "color" is a passed color variable
	 */
	@Override
	public void setColor(Color color) {
		rColor = color;
	}

	/**
	 * Sets if the rectangle is filled or not
	 */
	@Override
	public void setFilled(boolean filled) {
		if (filled == true){
			isFilled = true;
		}
		else{
			isFilled = false;
		}
	}

	/**
	 * Checks to see if the shape is selected
	 */
	@Override
	public boolean isSelected(int x, int y) {
		
		// Finds if the point is in the area of the rectangle
		if(isFilled == true){
			if(x>=rX && x<=rX+w && y>=rY && y<=rY+h){
				return true;
			}
		}
		else{
			// top line
			if(x>=rX && x<=rX+w && y>=rY-3 && y<=rY+3){
				return true;
			}
			// bottom line
			else if(x>=rX && x<=rX+w && y>=rY+h-3 && y<=rY+h+3){
				return true;
			}
			// left line
			else if(x>=rX-3 && x<=rX+3 && y>=rY && y<=rY+h){
				return true;
			}
			// right line
			else if(x>=rX+w-3 && x<=rX+w+3 && y>=rY && y<=rY+h){
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Will move the shape using the top left coords
	 */
	@Override
	public void move(int dx, int dy) {
		rX=dx;
		rY=dy;	
	}		
}
