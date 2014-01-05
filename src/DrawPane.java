import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * panel class containing all the elements of the layout
 */
public class DrawPane extends JPanel {
	/** the full text message */
	String textMessage = null;
	/** a list of the parts of the text message that fit into one line */
	List<String> messageParts;
	/** the first line that is still showed in the view */
	int messagePosition = 0;
	/** a list containing the 4 option strings */
	List<String> optionStrings;
	/** show the options or not? */
	boolean showOptions = false;
	/** saved values for the displays */
	HashMap<String,String> displayValues;
	/** the selected background image */
	Image backgroundImage = null;		
	/** list of displayed items */
	List<Image> itemList;		
	/** list of image positionings on the background */
	List<ImagePositioning> imgPositionings;
	/** show the escape dialog or not? */
	boolean showEscapeDialog = false;
	
	/** map from image names to image objects */
	HashMap<String,Image> imageMap;
	
	// main layout coordinates
	Dimension standardPaneDimension = new Dimension(1016,740);
//	Dimension paneDimension = standardPaneDimension;
	int imageWidth = 750;
	int imageHeight = 350;
	int textboxWidth = standardPaneDimension.width;
	int textboxHeight = standardPaneDimension.height - imageHeight;
	int displayHeight = 40;
	int[] displayPosX = {imageWidth+40,imageWidth+150};
	int displayPaddingX = 7;
	int displayPaddingY = 5;
	int inventoryWidth = textboxWidth - imageWidth;
	int inventoryHeight = imageHeight - displayHeight;
	
	// display positions
	int displayIconWidth = 30;
	int displayIconHeight = 30;
	
	// inventory object positions
	int objectWidth = 60;
	int objectHeight =  60;
	int inventoryPaddingX = (inventoryWidth - 2*objectWidth) / 3;
//	int inventoryPaddingY = (inventoryHeight - 2*objectHeight) / 4;
	int[] objectY = {displayHeight+95,displayHeight+165};
	
	// message positions
	int messageLinesWithOptions = 4;
	int messageLinesWithoutOptions = 8;
	int messagePadding = 120;
	int messageX = messagePadding;
	int messageWidth = textboxWidth - 2*messagePadding;
	int messageDist = 40;
	int messageDistY = 20;
	int[] messageY = {imageHeight+messageDist+messageDistY,
			          imageHeight+messageDist*2+messageDistY,
			          imageHeight+messageDist*3+messageDistY,
			          imageHeight+messageDist*4+messageDistY,
			          imageHeight+messageDist*5+messageDistY,
			          imageHeight+messageDist*6+messageDistY,
			          imageHeight+messageDist*7+messageDistY,
			          imageHeight+messageDist*8+messageDistY};
	
	// button positions
	int buttonWidth = 60;
	int buttonHeight = 60;
	int buttonDistY = 35;
	int[] buttonsX = {messageX+5,messageX+410};
	int[] buttonsY = {messageY[3]+buttonDistY,messageY[5]+buttonDistY};
	
	// option fields (the space that remains between the buttons)
	int optionWidth = buttonsX[1] - buttonsX[0] + buttonWidth;
//	int optionPadding = 8;
	int optionSpace = 270;
	int optionAlt2 = 20;
	int optionAlt3 = 25;
	int[] optionX = {buttonsX[0] + buttonWidth, buttonsX[1] + buttonWidth};
	int[] optionY = {buttonsY[0] + (buttonHeight * 2 / 3), buttonsY[1] + (buttonHeight * 2 / 3)};

	// escape dialog
	int escapeDialogWidth = 400;
	int escapeDialogHeight = 250;
	int escapeDialogX = (textboxWidth - escapeDialogWidth) / 2;
	int escapeDialogY = (imageHeight + textboxHeight - escapeDialogHeight) / 2;
	int escapeDialogPadding = 40;
	int escapeDialogTextDistance = 40;
	
//	// main layout coordinates
//	int windowWidth = 1280;
//	int windowHeight = 1024;
//	int imageWidth = 950;
//	int imageHeight = 418;
//	int textboxWidth = windowWidth;
//	int textboxHeight = windowHeight - imageHeight;
//	int displayHeight = 40;
//	int displayPadding = 5;
//	int inventoryWidth = textboxWidth - imageWidth;
//	int inventoryHeight = imageHeight - displayHeight;
//	
//	// inventory object positions
//	int objectWidth = 100;
//	int objectHeight =  100;
//	int inventoryPaddingX = (inventoryWidth - 2*objectWidth) / 3;
//	int inventoryPaddingY = (inventoryHeight - 3*objectHeight) / 4;
//	
//	// message positions
//	int messageLinesWithOptions = 3;
//	int messageLinesWithoutOptions = 8;
//	int messagePadding = 20;
//	int messageX = messagePadding;
//	int messageWidth = textboxWidth - 2*messagePadding;
//	int messageDist = 50;
//	int[] messageY = {imageHeight+messageDist,imageHeight+messageDist*2,imageHeight+messageDist*3,
//			          imageHeight+messageDist*4,imageHeight+messageDist*5,imageHeight+messageDist*6,
//			          imageHeight+messageDist*7,imageHeight+messageDist*8};
//	
//	// button positions
//	int buttonWidth = 60;
//	int buttonHeight = 60;
//	int[] buttonsX = {200,700};
//	int[] buttonsY = {650,750};
//	
//	// option fields (the space that remains between the buttons)
//	int optionWidth = buttonsX[1] - buttonsX[0] + buttonWidth;
//	int optionPadding = 20;
//	int[] optionX = {buttonsX[0] + buttonWidth, buttonsX[1] + buttonWidth};
//	int[] optionY = {buttonsY[0] + (buttonHeight * 2 / 3), buttonsY[1] + (buttonHeight * 2 / 3)};
	
	// define fonts
	Font fontMessage = new Font("Times New Roman", Font.PLAIN, 32);
	Font fontOptions = new Font("Times New Roman", Font.PLAIN, 32);
	Font fontOptionsSmaller = new Font("Times New Roman", Font.PLAIN, 28);
	Font fontDisplay = new Font("Times New Roman", Font.PLAIN, 24);
	Font fontEscapeDialog = new Font("Times New Roman", Font.PLAIN, 36);

	/** 
	 * constructor
	 */
	public DrawPane() {
		// initialize stuff
		this.textMessage = "";
		this.messageParts = new ArrayList<String>();
		this.optionStrings = new ArrayList<String>();
		this.displayValues = new HashMap<String,String>();
		this.itemList = new ArrayList<Image>();
		this.imageMap = new HashMap<String,Image>();
		this.imgPositionings = new ArrayList<ImagePositioning>();
		// read in all images
		//readImages();
	}
	
//	/** sets the dimension of the pane */
//	public void setPaneDimension(Dimension d) {
//		this.paneDimension = d;
//	}
//	
//	/** returns the dimension of the pane */
//	public Dimension getPaneDimension() {
//		return this.paneDimension;
//	}
	
//	/** calculates the optimal width of the window */
//	public int getWindowWidth() {
//		return this.textboxWidth;
//	}
//	
//	/** calculates the optimal height of the window */
//	public int getWindowHeight() {
//		return this.imageHeight + this.textboxHeight;
//	}
	
	/** change the displayed message and reset the position */
	public void setMessage(String str) {
		this.textMessage = str;
		// split string
		if (str == null) {
			this.messageParts = new ArrayList<String>();
		} else {
			this.messageParts = splitText(this.textMessage, messageWidth, fontMessage);
		}
		// reset the position
		this.messagePosition = 0;
		
		updateUI();
	}
	
	/** returns the displayed message */
	public String getMessage() {
		return this.textMessage;
	}
	
	/** scrolls the displayed message up */
	public void scrollMessage() {
		if (this.messagePosition < this.messageParts.size()-1) {
			this.messagePosition++;
		}
		
		updateUI();
	}
	
	/** changes the options and displays them */
	public void displayOptions(List<String> list) {
		this.optionStrings = list;
		this.showOptions = true;
		updateUI();
	}
	
	/** hides the options */
	public void hideOptions() {
		this.showOptions = false;
		updateUI();
	}
	
	/** sets a display value */
	public void setDisplayValue(String key, String value) {
		this.displayValues.put(key, value);
	}
	
	/** returns a display value */
	public String getDisplayValue(String key) {
		if (this.displayValues.containsKey(key)) {
			return this.displayValues.get(key);
		} else {
			return "0";
		}
	}
	
	/** changes the background image */
	public void changeBackground(String imageName) {
		this.backgroundImage = imageMap.get(imageName);
		updateUI();
	}
	
	/** returns the background image object */
	public Image getBackgroundImage() {
		return this.backgroundImage;
	}
	
	/** adds an item to the inventory */
	public void addItem(String imageName) {
		Image img = imageMap.get(imageName);
		if (!itemList.contains(img)) {
			this.itemList.add(img);
		}
		updateUI();
	}
	
	/** removes an item from the inventory */
	public void removeItem(String imageName) {
		Image img = imageMap.get(imageName);
		this.itemList.remove(img);
		updateUI();
	}
	
	/** returns the item with the given number */
	public Image getItem(int count) {
		if (count < this.itemList.size()) {
			return this.itemList.get(count);
		} else {
			return null;
		}
	}
	
	/** removes all items from the inventory */
	public void clearItems()
	{
		itemList.clear();
		updateUI();
	}
	
	/** adds an image positioning to the list */
	public void addImagePositioning(String imageName, int valX, int valY) {
		Image img = imageMap.get(imageName);
		if (img != null) {
			this.imgPositionings.add(new ImagePositioning(img,valX,valY));
		}
	}
	
	/** resets the list of image positionings */
	public void resetImagePositionings() {
		this.imgPositionings = new ArrayList<ImagePositioning>();
	}
	
	/** returns the message part with a certain number */
	public String getMessagePart(int part) {
		if (this.messageParts.size() <= part) {
			return "";
		} else {
			return this.messageParts.get(part);
		}
	}
	
	/** returns the currently selected message part */
	public String getSelectedMessagePart() {
		return this.messageParts.get(this.messagePosition);
	}
	
	/** returns the option string with the selected number */
	public String getOptionString(int number) {
		if (this.optionStrings.size() <= number) {
			return "";
		} else {
			return this.optionStrings.get(number);
		}
	}
	
	/** return the current number of displayed message lines */
	public int getMessageLines() {
		if (showOptions) {
			return this.messageLinesWithOptions;
		} else {
			return this.messageLinesWithoutOptions;
		}
	}
	
	/** show or hide the escape dialog */
	public void setEscapeDialog(boolean value) {
		this.showEscapeDialog = value;
		updateUI();
	}

	/** read in all images and save them in the image hash */
	/*public void readImages() {
		HashMap<String,String> filenameMap = new HashMap<String,String>();
		filenameMap.put("./images/item_bg_trix.png", "item_bg");
		filenameMap.put("./images/icon_a_32_trix.png", "buttonA");
		filenameMap.put("./images/icon_b_32_trix.png", "buttonB");
		filenameMap.put("./images/icon_x_32_trix.png", "buttonX");
		filenameMap.put("./images/icon_y_32_trix.png", "buttonY");
		filenameMap.put("./images/ui_gold_compo.png", "icon_gold");
		filenameMap.put("./images/ui_xp_compo.png", "icon_battlepoints");
		filenameMap.put("./images/ui_papyrus_trix.png", "textbox");
		filenameMap.put("./images/ui_bag_trix_scaled.png", "inventory_bg");
		
		for (String filename : filenameMap.keySet()) {
//			System.out.println(filename);
			addImage(filename,filenameMap.get(filename));
		}
	}*/
	
	/** add an image to the image map and assign it to a given label */
	public void addImage(String filename, String label) {
//		try {
//			Image newImage = getToolkit().getImage(ClassLoader.getSystemResource(filename));
//			imageMap.put(label, newImage);
////			System.out.println(filename + " OK");
//		} catch (NullPointerException e) {
////			System.out.println(filename + ": " + e);
//			System.exit(0);
//		}
		
		try {
			Image newImage = ImageIO.read(new File(filename));
			imageMap.put(label, newImage);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(0);
		}
	}
	
	/** splits up a text into lines that fit into
	 *  the textbox
	 */
	public List<String> splitText(String text, int width, Font font) {
		List<String> resultList = new ArrayList<String>();
		
		// create the font metrics object
		FontMetrics metrics = this.getGraphics().getFontMetrics(font);
		
		// split up the string at whitespaces
		String pattern = "\\s";
		String[] parts = text.split(pattern);

		int partAmount = parts.length;
		int pos = 0;
		
		PARTS_LOOP: while (pos < partAmount) {
			String part = "";
			String lastPart = "";
			while (metrics.stringWidth(part) < width) {
//				System.out.println(metrics.stringWidth(part) + " / " + width);
				lastPart = part;
				part += " " + parts[pos];
				pos++;
				// finished before the end of the line (last line)
				if (pos >= partAmount) {
//					System.out.println("fbeol " + part);
					resultList.add(part);
					break PARTS_LOOP;
				}
			}
//			System.out.println("fn " + lastPart);
			resultList.add(lastPart);
			pos--; // last part was one too much -> go back one step
		}
		
		/* for debugging */
//			for (String s : resultList) {
//				System.out.println(s);
//			}
//			System.exit(0);
		
		return resultList;
	}
	
	/** overridden paintComponent function
	 * to draw the layout
	 */
	@Override
	public void paintComponent(Graphics g) {	
		/* distance from the upper left corner
		 * to center the view
		 */
		Dimension dim = this.getSize();
		int altX = (dim.width - this.textboxWidth) / 2;
		int altY = (dim.height - this.imageHeight - this.textboxHeight) / 2;
		
		
		// draw background
		g.setColor(Color.WHITE);
		if (getBackgroundImage() != null) {
			g.drawImage(getBackgroundImage(), altX, altY, altX+imageWidth, altY+imageHeight, 0, 0, imageWidth, imageHeight, null);
		} else {
			g.drawRect(altX, altY, imageWidth, imageHeight);
		}
		
		// draw images on background
		for (ImagePositioning imgPos : this.imgPositionings) {
			int sizeX = imgPos.getImage().getWidth(null);
			int sizeY = imgPos.getImage().getHeight(null);
			g.drawImage(imgPos.getImage(), altX+imgPos.getValX(), altY+imgPos.getValY(),
					                       altX+imgPos.getValX()+sizeX, altY+imgPos.getValY()+sizeY,
					                       0,0,sizeX,sizeY,null);
		}
		
		// draw textbox
		g.setColor(Color.BLACK);
		g.fillRect(altX, altY+imageHeight, textboxWidth, textboxHeight);
		g.drawImage(this.imageMap.get("textbox"), altX, altY+imageHeight, altX+textboxWidth, altY+imageHeight+textboxHeight,
				                0,0,textboxWidth,textboxHeight,null);
		
		// draw display
		g.setColor(Color.BLACK);
		g.fillRect(altX+imageWidth, altY, inventoryWidth, displayHeight);
		g.setColor(Color.YELLOW);
		g.setFont(fontDisplay);
		g.drawImage(imageMap.get("icon_gold"),
				    altX+displayPosX[0],altY+displayPaddingY,altX+displayPosX[0]+displayIconWidth,altY+displayPaddingY+displayIconHeight,
				    0,0,displayIconWidth,displayIconHeight,null);
		g.drawString(getDisplayValue("gold"),altX+displayPosX[0]+displayIconWidth+displayPaddingX,altY+displayIconHeight);
		g.drawImage(imageMap.get("icon_battlepoints"),
			    altX+displayPosX[1],altY+displayPaddingY,altX+displayPosX[1]+displayIconWidth,altY+displayPaddingY+displayIconHeight,
			    0,0,displayIconWidth,displayIconHeight,null);
		g.drawString(getDisplayValue("battlepoints"),altX+displayPosX[1]+displayIconWidth+displayPaddingX,altY+displayIconHeight);
		
		// draw inventory
		g.setColor(Color.BLACK);
		g.fillRect(altX+imageWidth, altY+displayHeight, inventoryWidth, inventoryHeight);
		g.drawImage(this.imageMap.get("inventory_bg"),altX+imageWidth,altY+displayHeight,altX+imageWidth+inventoryWidth,altY+displayHeight+inventoryHeight,
				                        0,0,inventoryWidth,inventoryHeight,null);
		
		// draw inventory objects
		g.setColor(Color.BLACK);
		int itemCount = this.itemList.size();
		int count = 0;
		Image imgInventoryBG = imageMap.get("item_bg");
		for (int y=0; y < 2; y++) {
			for (int x=0; x < 2; x++) {	
				int xPos = altX + imageWidth + (x+1)*inventoryPaddingX + x*objectWidth;
				int yPos = altY + objectY[y];
				g.drawImage(imgInventoryBG,
					     xPos, yPos, xPos+objectWidth, yPos+objectHeight,
				         0, 0, 100, 100,
				         null);
				if (count < itemCount) {
					Image img = getItem(count);
					g.drawImage(img,
							     xPos, yPos, xPos+objectWidth, yPos+objectHeight,
						         0, 0, 100, 100,
						         null);
				}
//				} else {
//					g.fillRect(xPos, yPos, objectWidth, objectHeight);
//				}
				count++;
			}
		}

		// write message text
		g.setColor(Color.BLACK);
		count = 0;
		for (int i=this.messagePosition; i<this.messagePosition+getMessageLines(); i++) {
			String mPart = getMessagePart(i);
			if (mPart.isEmpty()) break;
			AttributedString as = new AttributedString(mPart);
			as.addAttribute(TextAttribute.FONT, fontMessage);
			g.drawString(as.getIterator(), altX+messageX, altY+messageY[count]);
			count++;
		}

		if (this.showOptions) {
			int optionsCount = this.optionStrings.size();
					
			// draw option buttons
			g.setColor(Color.BLACK);
			count = 0;
			Image imgButton = null;
			OUTER_BUTTON_LOOP:
			for (int yPos : this.buttonsY) {
				for (int xPos : this.buttonsX) {	
//					g.fillRect(altX+xPos, altY+yPos, buttonWidth, buttonHeight);
					switch (count) {
						case 0: imgButton = this.imageMap.get("buttonA"); break;
						case 1: imgButton = this.imageMap.get("buttonB"); break;
						case 2: imgButton = this.imageMap.get("buttonX"); break;
						case 3: imgButton = this.imageMap.get("buttonY"); break;
					}
					try {
						g.drawImage(imgButton, altX+xPos, altY+yPos, altX+xPos+buttonWidth, altY+yPos+buttonHeight,
							               0, 0, 90, 90, null);					
					} catch (NullPointerException e) {
						System.exit(0);
					}
					
					count++;
					if (count >= optionsCount) break OUTER_BUTTON_LOOP;
				}
			}
			
			// write option text
			g.setColor(Color.BLACK);
			count = 0;

			OUTER_OPTION_LOOP:
			for (int yPos : this.optionY) {
				for (int xPos : this.optionX) {
					String optionString = getOptionString(count);
					g.setFont(fontOptions);
					List<String> parts = splitText(optionString,optionSpace,fontOptions);
					// try out a smaller font if the text is too long
					if (parts.size() > 1) {
						g.setFont(fontOptionsSmaller);
						parts = splitText(optionString,optionSpace,fontOptionsSmaller);
					}
						
					int p = 0;
					for (String s : parts) {
						int optAlt = 0;
						if (parts.size() == 2) {
							if (p == 0) {
								optAlt = -optionAlt2;
							} else {
								optAlt = optionAlt2;
							}	
						} else if (parts.size() >= 3) {
							if (p == 0) {
								optAlt = -optionAlt3;
							} else if (p == 2) {
								optAlt = optionAlt3;
							}
						}
						g.drawString(s, altX + xPos, altY + yPos + optAlt);
						p++;
						if (p == 3) break; // more than 3 parts are not allowed - string will just be cut :(
					}	
					count++;
					if (count >= optionsCount) break OUTER_OPTION_LOOP;
				}
			}
		}
		
		if (showEscapeDialog) {
			g.setColor(Color.GRAY);
			g.fillRect(altX+escapeDialogX, altY+escapeDialogY, escapeDialogWidth, escapeDialogHeight);
			g.setColor(Color.WHITE);
			g.setFont(fontEscapeDialog);
			g.drawString("Do you really want to", altX+escapeDialogX+escapeDialogPadding, altY+escapeDialogY+escapeDialogPadding+escapeDialogTextDistance);
			g.drawString("leave the game?", altX+escapeDialogX+escapeDialogPadding, altY+escapeDialogY+escapeDialogPadding+escapeDialogTextDistance*2);
			g.drawString("[Y] Yes     [N] No", altX+escapeDialogX+escapeDialogPadding, altY+escapeDialogY+escapeDialogPadding+escapeDialogTextDistance*4);
		}
    }
}
