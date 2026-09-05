package coinGame;

import java.awt.Color;

public class Player extends Sprite {
	
	final static Color PLAYER_COLOUR = Color.BLUE;
	final static int PLAYER_WIDTH = 30;
	final static int PLAYER_HEIGHT = 30;
	final static int PLAYER_SPEED = 2;

	public Player(int panelWidth, int panelHeight) {
		this.setColour(PLAYER_COLOUR);
		this.setWidth(PLAYER_WIDTH);
		this.setHeight(PLAYER_HEIGHT);
		//set starting in the middle
		setInitialPosition(panelWidth/2 - (getWidth()/2), panelHeight/2 - (getHeight()/2));
		resetToInitialPosition();

	}

}
