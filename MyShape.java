package ch15;

import java.awt.Color;
import java.awt.Graphics;

public interface MyShape {

	void draw(Graphics g);
	
	void setColor(Color color);
	
	void setFilled(boolean filled);
	
	boolean isSelected(int x, int y);
	
	void move(int dx, int dy);
}
