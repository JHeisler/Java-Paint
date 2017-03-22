package ch15;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;

/**
 * Class that will create a JFrame with the proper GUI layout
 *
 * Then it will allow shapes to be drawn on the drawArea JPanel
 *
 */
@SuppressWarnings("serial")
public class Paint extends JFrame{

	// Gui components
	private JPanel topInterface; // Top bar
	private JPanel drawArea; // Area where shapes are drawn
	private JPanel statusBar; // Bottom bar
	private JLabel mouseStats = new JLabel("Mouse is out");
	private JCheckBox filled;
	private JLabel filledLabel;
	private JButton undo;
	private JButton clear;
	private JComboBox<ColorsEnum> colorChoose; // Choose colors
	private JComboBox<String> shapeChoose; // Choose shapes


	private Color color = Color.BLACK; // color chosen
	private boolean fill = false;
	private boolean mousedrag = false;
	private boolean moveMouse = false;
	private MyRectangle r = null;
	private MyEllipse ec = null;
	private MyLine l = null;
	private MyShape shapeMove;

	// This is used to store shapes drawn and remove them if needed
	private ArrayList<MyShape> shapesDrawn = new ArrayList<MyShape>();
	private int arrListSize; // Size of shapesDrawn

	private final String shapes[] = {"Rectangle","Oval",
			"Line"};

	private int mouseX, mouseY, mouseDX, mouseDY; // Mouse coords

	/**
	 * Constructor that sets up the gui
	 * Also will add the handlers to gui objects
	 *
	 * This uses multiple panels and BorderLayout
	 * drawArea is a custom panel class "MyPanel" which
	 * extends JPanel, this will use paintComponent to draw
	 *
	 */
	public Paint(){
		super("Paint");

		Container content = this.getContentPane();
		topInterface = new JPanel();
		drawArea = new MyPanel();
		statusBar = new JPanel();

		content.setLayout(new BorderLayout());


		//sets sizes
		statusBar.setPreferredSize(new Dimension(this.getWidth(),20));
		topInterface.setPreferredSize(new Dimension(this.getWidth(),45));

		// buttons
		undo = new JButton("Undo");
		clear = new JButton("Clear");

		//2 drop downs, one for colors another for shapes
		colorChoose = new JComboBox<ColorsEnum>();
		for (ColorsEnum c : ColorsEnum.values()){
			colorChoose.addItem(c);
		}
		shapeChoose = new JComboBox<String>(shapes);

		// 1 check box and its label
		filledLabel = new JLabel("Filled");
		filled = new JCheckBox();

		// add gui components to topInterface
		topInterface.add(undo);
		topInterface.add(clear);
		topInterface.add(colorChoose);
		topInterface.add(shapeChoose);
		topInterface.add(filledLabel);
		topInterface.add(filled);

		//status bar for mouse
		statusBar.add(mouseStats);

		// Add panels to main panel
		add(topInterface, BorderLayout.NORTH);
		add(drawArea);
		add(statusBar, BorderLayout.SOUTH);

		// Add handlers
		mouseHandler mHandler = new mouseHandler();
		itemHandler iHandler = new itemHandler();
		paintInterface pI = new paintInterface();
		drawArea.addMouseMotionListener(mHandler);
		drawArea.addMouseListener(mHandler);
		colorChoose.addItemListener(iHandler);
		shapeChoose.addItemListener(iHandler);
		filled.addItemListener(iHandler);
		undo.addActionListener(pI);
		clear.addActionListener(pI);
	}

	/**
	 * Enum which stores color names and values to be used
	 * with the color combobox
	 */
	private enum ColorsEnum{
		Black(Color.black), Red(Color.red), Green(Color.green),
			Blue(Color.blue),Yellow(Color.yellow),YellowOrange(Color.orange),
			Orange(new Color(255,165,0)),Cyan(Color.cyan),
			Magenta(Color.magenta),Pink(Color.pink);

		private final Color color;

		private ColorsEnum(Color color){
			this.color = color;
		}

		public Color getColor(){
			return color;
		}
	}

	/**
	 * Private MyPanel class which extends JPanel
	 * Implements paintComponent so that it can be drawn on
	 */
	private class MyPanel extends JPanel {

		/**
		 * Changes the background to white, clears the canvas,
		 * and then draws the shapes
		 */
		public void paintComponent(Graphics g){
			super.paintComponents(g);
			this.setBackground(Color.WHITE);
			g.clearRect(0, 0, this.getWidth(), this.getHeight());

			// Redrwas all the shapes after clearing
			for (MyShape s: shapesDrawn){
				s.draw(g);
			}


			/**
			 * This displays a temporary image while dragging
			 * so that you can see the shape growing while dragging
			 *
			 * For ellipse and rectangle it will find the top left corner
			 * and then the length and width
			 */
			if(mousedrag == true){
				int tempX = 0;
				int tempY = 0;
				int tempW = 0; //width
				int tempH = 0; //height

				if(mouseX>=mouseDX){
					tempX = mouseDX;
					tempW = mouseX-mouseDX;
				}
				else{
					tempX = mouseX;
					tempW= mouseDX-mouseX;
				}
				if(mouseY>=mouseDY){
					tempY = mouseDY;
					tempH = mouseY - mouseDY;
				}
				else{
					tempY = mouseY;
					tempH = mouseDY-mouseY;
				}
				g.setColor(color);
				if (shapeChoose.getSelectedIndex()==0){
					if(fill == true){
						g.fillRect(tempX, tempY, tempW, tempH);
					}
					g.drawRect(tempX,tempY,tempW,tempH);
				}
				else if(shapeChoose.getSelectedIndex()==1){
					if(fill == true){
						g.fillOval(tempX, tempY, tempW, tempH);
					}
					g.drawOval(tempX,tempY,tempW,tempH);
				}

				/**
				 * Temp values are used to create a top left corner and
				 * a length and width, this isn't needed for line
				 */
				else if(shapeChoose.getSelectedIndex()==2){
					g.drawLine(mouseX,mouseY,mouseDX,mouseDY);
				}
			}


		}
	}

	/**
	 * Button handler which implements ActionListener
	 */
	private class paintInterface implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			// clear button
			if(e.getSource() == clear){
				shapesDrawn.clear();
				repaint();
			}

			/*
			 *  Undo Button
			 *
			 *  arrListSize is equal to the size of the arraylist - 1
			 *  which will be used to remove the last entry in the arraylist
			 */
			if(e.getSource() == undo){
				arrListSize = shapesDrawn.size()-1;

				if(shapesDrawn.size()>=1){
					shapesDrawn.remove(arrListSize);
					repaint();
				}
			}
		}
	}

	/**
	 * Mouse listener, this handles all mouse events
	 */
	private class mouseHandler implements MouseListener, MouseMotionListener{

		/**
		 * Once the mouse is released finish creating the shape
		 * and add the shape to the shapesDrawn array
		 */
		@Override
		public void mouseReleased(MouseEvent e) {

			if (mousedrag == true){
				if (shapeChoose.getSelectedIndex()==0){

					r = new MyRectangle(mouseX,mouseY,mouseDX,mouseDY);
					r.setColor(color);
					r.setFilled(fill);
					shapesDrawn.add(r);
				}
				else if(shapeChoose.getSelectedIndex()==1){
					// ellipse
					ec = new MyEllipse(mouseX,mouseY,mouseDX,mouseDY);
					ec.setColor(color);
					ec.setFilled(fill);
					shapesDrawn.add(ec);
				}
				else if(shapeChoose.getSelectedIndex()==2){
					l = new MyLine(mouseX,mouseY,mouseDX,mouseDY);
					l.setColor(color);
					l.setFilled(fill);
					shapesDrawn.add(l);
				}

				// repaint after creating a new shape
				repaint();
			}

			// reset values after completed
			mousedrag=false;
			moveMouse = false;
			mouseX = 0;
			mouseY = 0;
			mouseDX = 0;
			mouseDY = 0;
		}

		/**
		 * This will display the coordinates on the bottom bar
		 * and also be used to move shapes if a shape was selected
		 */
		@Override
		public void mouseDragged(MouseEvent e) {

			int xLoc = e.getX();
			int yLoc = e.getY();

			/* If the mouse is outside of drawArea's area while dragging
			 * then mouse is out
			 */
			if(xLoc > drawArea.getWidth() || yLoc > drawArea.getHeight() ||
					xLoc < 0 || yLoc < 0){
				mouseStats.setText("Mouse is out");
			}else{

				// Else print the values to the bottom label
				mouseStats.setText("("+Integer.toString(xLoc)+","+
						Integer.toString(yLoc)+")");
			}

			mouseDX = xLoc;
			mouseDY = yLoc;

			if(moveMouse == false){
				mousedrag = true;
			}
			else{
				shapeMove.move(e.getX(), e.getY());
			}

			// Repaint while dragging to show the shape growing
			repaint();
		}

		/**
		 * This also displays the mouse coordinates but the mouse doesn't
		 * need to be held down like in mouseDragged
		 */
		@Override
		public void mouseMoved(MouseEvent e) {

			int xLoc = e.getX();
			int yLoc = e.getY();
			mouseStats.setText("("+Integer.toString(xLoc)+","+
					Integer.toString(yLoc)+")");
		}

		/**
		 * If the mouse exits the JPanel then mouse is out
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			mouseStats.setText("Mouse is out");
			mousedrag = false;
		}

		/**
		 * if the mouse is simply clicked then it isn't being
		 * dragged
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			mousedrag=false;
			moveMouse = false;
		}

		/**
		 * if the mouse is pressed on a component then check
		 * if a shape is selected or not
		 *
		 * if a shape has been selected then set moveMouse to true
		 * which is used to invoke the move method inside mouseDragged
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();

			for(MyShape s : shapesDrawn){
				if(s.isSelected(e.getX(), e.getY())==true){
					mousedrag=false;
					moveMouse = true;
					shapeMove = s;
				}
			}
		}

		// Mouse enters the JPanel
		@Override
		public void mouseEntered(MouseEvent e) {
			// do nothing
		}
	}

	/**
	 * Handler for the check box and combo boxes
	 */
	private class itemHandler implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {

			/**
			 * If the fill checkbox is selected then
			 * set the fill var to true
			 */
			if (e.getSource() == filled){
				if(filled.isSelected() == true){
					fill = true;
				}
				else{
					fill = false;
				}
			}

			/**
			 * Selects color and sets the color var
			 * when using the color combobox
			 */
			if(e.getSource() == colorChoose){

				// Checks if state change is selected
				int state = e.getStateChange();
				if(state == ItemEvent.SELECTED){

					// Switch, will change colors
					ColorsEnum co = (ColorsEnum)
							colorChoose.getSelectedItem();
					color = co.getColor();
				}
			}

			// Grays out checkbox when edge or line is chosen
			if(e.getSource()==shapeChoose){
				if(shapeChoose.getSelectedIndex()==2){
					filled.setEnabled(false);
				}
				else{
					filled.setEnabled(true);
				}
			}
		}
	}

	/**
	 * Main which will create the window and set window properties
	 * @param args
	 */
	public static void main(String args[]){
		Paint paintWindow = new Paint();
		paintWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
		paintWindow.setSize( 700, 700 ); // set frame size
		paintWindow.setLocationRelativeTo(null);
		paintWindow.setResizable(true);
		paintWindow.setVisible( true ); // display frame
	}
}
