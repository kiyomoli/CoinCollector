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
		setRandomCoinPosition(panelWidth, panelHeight);
		resetToInitialPosition();
	}
	
	// position not right next to the edges of the screen
	public void setRandomCoinPosition(int panelWidth, int panelHeight) {
        int randomXPosition = random.nextInt(30, panelWidth - getWidth()-30);
        int randomYPosition = random.nextInt(30, panelHeight - getHeight()-30);
        setInitialPosition(randomXPosition, randomYPosition);
	}
		
}
