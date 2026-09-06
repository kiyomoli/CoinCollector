package coinGame;

import java.awt.Color;

public class Enemy extends Sprite {
	
	final static Color ENEMY_COLOUR = Color.RED;
	final static int ENEMY_WIDTH = 30;
	final static int ENEMY_HEIGHT = 30;
	public final static int ENEMY_SPEED = 1;

	public Enemy(int panelWidth, int panelHeight) {
		this.setColour(ENEMY_COLOUR);
		this.setWidth(ENEMY_WIDTH);
		this.setHeight(ENEMY_HEIGHT);
		this.setxVelocity(ENEMY_SPEED);
		this.setyVelocity(ENEMY_SPEED);
		setRandomEnemyPosition(panelWidth, panelHeight);
		resetToInitialPosition();
		//try and make not overlapping??
	}
	
	public void setRandomEnemyPosition(int panelWidth, int panelHeight) {
        int randomXPosition = random.nextInt(panelWidth - getWidth());
        int randomYPosition = random.nextInt(panelHeight - getHeight());
        setInitialPosition(randomXPosition, randomYPosition);
    }

}
