import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DrawPane drawPane;
	
	public View() {
		buildGUI();
	}

	public void addMessage(String str) {
		this.drawPane.setMessage(this.drawPane.getMessage() + str);
	}
	
	/** change the displayed message and reset the position */
	public void setMessage(String str) {
		this.drawPane.setMessage(str);
	}
	
	/** changes the options and displays them */
	public void displayOptions(List<String> list) {
		this.drawPane.displayOptions(list);
	}
	
	/** hides the options */
	public void hideOptions() {
		this.drawPane.hideOptions();
	}
	
	/** scrolls the displayed message up */
	public void scrollMessage() {
		this.drawPane.scrollMessage();
	}
	
	/** changes the background image */
	public void changeBackground(String imageName) {
		this.drawPane.changeBackground(imageName);
	}
	
	/** sets the gold value */
	public void setGold(String value) {
		this.drawPane.setDisplayValue("gold", value);
	}
	
	/** sets the battlepoints value */
	public void setBattlepoints(String value) {
		this.drawPane.setDisplayValue("battlepoints", value);
	}
	
	/** add an item to the inventory */
	public void addItem(String imageName) {
		this.drawPane.addItem(imageName);
	}
	
	/** remove an item from the inventory */
	public void removeItem(String imageName) {
		this.drawPane.removeItem(imageName);
	}
	
	/** add an image to the image map and assign it to a given label */
	public void addImage(String filename, String label) {
		this.drawPane.addImage(filename, label);
	}
	
	/** adds an image positioning to the list */
	public void addImagePositioning(String imageName, int valX, int valY) {
		this.drawPane.addImagePositioning(imageName, valX, valY);
	}
	
	/** resets the list of image positionings */
	public void resetImagePositionings() {
		this.drawPane.resetImagePositionings();
	}
	
	/** show or hide the escape dialog */
	public void setEscapeDialog(boolean value) {
		this.drawPane.setEscapeDialog(value);
	}
	
	/** go to fullscreen mode */
	public void activateFullscreen() {
		this.dispose();
		this.setUndecorated(true);  
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.setVisible(true);
	}
	
	/** return from fullscreen mode */
	public void deactivateFullscreen() {
		this.setUndecorated(false);  
		this.setExtendedState(JFrame.NORMAL); 
	}
	
	/** builds the overall GUI */
	public void buildGUI() {		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		
//		// get size of the window without the borders
//		Dimension dim = this.getContentPane().getSize();
		
		// create drawPane object
		this.drawPane = new DrawPane();
		setContentPane(this.drawPane);
		
		// define window size constraints
//		int windowWidth = this.drawPane.getWindowWidth();
//		int windowHeight = this.drawPane.getWindowHeight();
		int windowWidth = 1024;
		int windowHeight = 768;
		this.setSize(windowWidth,windowHeight);
		this.setMinimumSize(new Dimension(windowWidth,windowHeight));
	 
        // show window
        setVisible(true); 
        
        
        addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) { SceneLoader.keyPressed(e); }

            public void keyReleased(KeyEvent e) {  }

            public void keyTyped(KeyEvent e) {  }
        });
	}
	
	/** test program */
	public static void main(String[] args) {
		View view = new View();
		String str = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
		view.setMessage(str);
		
		List<String> optionStrings = new ArrayList<String>();
		optionStrings.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet");
		optionStrings.add("a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a");
		optionStrings.add("a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a");
		optionStrings.add("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet");
		view.displayOptions(optionStrings);
		
		view.changeBackground("bg_town");
		
		view.setGold("1000");
		view.setBattlepoints("1337");
		
		view.addItem("item_book");
		view.addItem("item_flower");
		view.addItem("item_horse");
		view.addItem("item_armor");

		view.addImagePositioning("char_fighter_armored", 180, 220);
		view.addImagePositioning("char_mage", 300, 200);
		view.addImagePositioning("char_priest", 400, 200);
		
//		view.setEscapeDialog(true);
//		view.activateFullscreen();
//		view.deactivateFullscreen();
	}
}
