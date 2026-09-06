package coinGame;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class Sprite {

	public Sprite() {
	}
	
	private int xPosition;
	private int yPosition;
	private int initialXPosition;
	private int initialYPosition;
	private int xVelocity;
	private int yVelocity;
	private int width;
	private int height;
	private Color colour;
	Random random = new Random();
	int randomXPosition;
	int randomYPosition;
	
	//getter methods
	
	public int getxPosition() {
		return xPosition;
	}
	public int getyPosition() {
		return yPosition;
	}
	public int getxVelocity() {
		return xVelocity;
	}
	public int getyVelocity() {
		return yVelocity;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public Color getColour() {
		return colour;
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(getxPosition(), getyPosition(), getWidth(), getHeight());
		}

	//setter methods
	
	public void setInitialPosition(int initialXPosition, int initialYPosition) {
		this.initialXPosition = initialXPosition;
		this.initialYPosition = initialYPosition;
	}
	
	public void resetToInitialPosition(){
		setxPosition(initialXPosition);
		setyPosition(initialYPosition);
	}
	
	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}
	
	public void setxPosition(int newX, int panelWidth) {
		xPosition = newX;
		if (newX + width > panelWidth) {
			xPosition = panelWidth - width;
		}
		else if (newX < 0){
			xPosition = 0;
		}
	}
	
	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}
	
	public void setyPosition(int newY, int panelHeight) {
		yPosition = newY;
		if (newY + height > panelHeight) {
			yPosition = panelHeight - height;
		}
		else if (newY < 0){
			yPosition = 0;
		}
	}
	
	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}
	

}
