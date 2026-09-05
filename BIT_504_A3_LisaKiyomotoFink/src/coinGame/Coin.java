package coinGame;

import java.awt.Color;

public class Coin extends Sprite {
	
	final static Color COIN_COLOUR = Color.YELLOW;
	final static int COIN_WIDTH = 25;
	final static int COIN_HEIGHT = 25;

	public Coin(int panelWidth, int panelHeight) {
		this.setColour(COIN_COLOUR);
		this.setWidth(COIN_WIDTH);
		this.setHeight(COIN_HEIGHT);
		this.setxVelocity(0);
		this.setyVelocity(0);
		setRandomPosition(panelWidth, panelHeight);
		resetToInitialPosition();
		//try and make not overlapping??
		//location not too close to the centre
	}

}
