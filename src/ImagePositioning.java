import java.awt.Image;


public class ImagePositioning {

	private Image image;
	private int x;
	private int y;
	
	public ImagePositioning(Image objImage, int valX, int valY) {
		this.image = objImage;
		this.x = valX;
		this.y = valY;
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public int getValX() {
		return this.x;
	}
	
	public int getValY() {
		return this.y;
	}
	
}
