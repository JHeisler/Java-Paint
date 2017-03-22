package ch15;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

/**
 * Class that creates an ellipse, can draw it 
 * either filled or unfilled, change the color
 * and check to see if it is selected. If it is
 * selected then make it moveable
 *
 */
@SuppressWarnings("serial")
public class MyEllipse extends Ellipse2D.Float implements MyShape{

	private int eX, eY; // top left corner
	private int w, h; // width/height vars
	private Color eColor;
	private boolean isFilled = false;
	
	/**
	 * Ellipse constructor, will figure out the smallest
	 * x and y values and use those for the top left coordinates
	 * 
	 * Then it will find the width and height of the ellipse
	 * 
	 * @param x1- initial click of mouse x coord
	 * @param y1- initial click of mouse y coord
	 * @param x2- end x coord of mouse after drag
	 * @param y2- end y coord of mouse after drag
	 */
	MyEllipse(int x1, int y1, int x2, int y2){
		if(x1>=x2){
			eX = x2;
			w = x1-x2;
		}
		else{
			eX = x1;
			w = x2-x1;
		}
		if(y1>=y2){
			eY = y2;
			h = y1-y2;
		}
		else{
			eY = y1;
			h = y2-y1;
		}
	}
	
	/**
	 * This will draw the ellipse
	 * Also will set the color and fill it accordingly
	 */
	@Override
	public void draw(Graphics g) {
		
		g.setColor(eColor);
		if (isFilled == true){
			g.fillOval(eX,eY,w,h);
		}
		g.drawOval(eX,eY,w,h);
	}

	// Sets the color of the ellipse 
	@Override
	public void setColor(Color color) {
		eColor = color;
	}

	/**
	 * Fills the ellipse or not
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
	 * 
	 * If the ellipse is filled then use a slightly larger ellipse
	 * to check if the point is contained
	 * 
	 * If it is not filled check to see if the point is contained 
	 * between a slightly smaller and slightly larger ellipse
	 */
	@Override
	public boolean isSelected(int x, int y) {
		Ellipse2D.Float e = new Ellipse2D.Float(eX+2,eY+2,w-5,h-5);
		Ellipse2D.Float e2 = new Ellipse2D.Float(eX-4,eY-4,w+8,h+8);
		
		if(isFilled==true){
			if(e2.contains(x,y) ){
				return true;
			}
		}else{
			if(!e.contains(x, y) && e2.contains(x, y)){
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
		eX=dx;
		eY=dy;
		
	}

}
