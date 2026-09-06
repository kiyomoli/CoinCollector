package coinGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	
	final Color BACKGROUND_COLOUR = Color.BLACK;
	private final static int TIMER_DELAY = 5;
	public final static int PANEL_WIDTH = 800;
	public final static int PANEL_HEIGHT = 600;
	public GameState gameState = GameState.Initialising;
	Timer timer = new Timer(TIMER_DELAY, this);
	
	public Player player;
	ArrayList<Enemy> enemies = new ArrayList<>();
	ArrayList<Coin> coins = new ArrayList<>();
	int playerScore = 0;
	int playerLives = 5;
	private long lastCollisionTime = 0;
	private final long IMMUNITY_DURATION = 3000;
	
	public final static int CENTRE_ZONE_WIDTH = 150;
	public final static int CENTRE_ZONE_HEIGHT = 150;
	public int centreZoneX = (PANEL_WIDTH/2)-(CENTRE_ZONE_WIDTH/2);
	public int centreZoneY = (PANEL_HEIGHT/2) - (CENTRE_ZONE_HEIGHT/2);
	Rectangle centreZone = new Rectangle(centreZoneX, centreZoneY, CENTRE_ZONE_WIDTH, CENTRE_ZONE_HEIGHT);

	public GamePanel() {
		setBackground(BACKGROUND_COLOUR);
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		createObjects();
		timer.start();
		//the below means that the panel can focus on the keys being pressed
		addKeyListener(this);
		setFocusable(true);
	}
	
	private void update() {
		switch(gameState) {
		case Initialising:{
			break;
		}
		case Playing: {
			for (Enemy enemy: enemies) {
				moveObject(enemy);
			}
			moveObject(player);
			checkWallBounce();
			collectCoin();
			enemyCollision();
			break;		
		}
		case Paused: {
			break;
		}
		case GameOver: {
			timer.stop();
			break;
		}
		case GameWon: {
			timer.stop();
			break;
		}
		}
	}
	
	public void createObjects() {
		player =  new Player(PANEL_WIDTH, PANEL_HEIGHT);
		enemies.clear();
		for (int i=0; i<10; i++) {
			enemies.add(new Enemy(PANEL_WIDTH, PANEL_HEIGHT));
			for (Enemy enemy: enemies) {
				enemy.setxVelocity(ThreadLocalRandom.current().nextBoolean() ? 1 : -1);
				enemy.setyVelocity(ThreadLocalRandom.current().nextBoolean() ? 1 : -1);
				int attempts = 0; //500 attempts cap to stop it lagging on start up/getting stuck in the while loop forever
				while (enemy.getRectangle().intersects(centreZone) && attempts < 500) {
					enemy.setRandomEnemyPosition(PANEL_WIDTH, PANEL_HEIGHT);
					attempts ++;
				}
			}	
		}
		coins.clear();
		for (int i=0; i<10; i++) {
			coins.add(new Coin(PANEL_WIDTH, PANEL_HEIGHT));
			for (Coin coin: coins) {
				int attempts = 0;
				while (coin.getRectangle().intersects(centreZone) && attempts < 500){
					coin.setRandomCoinPosition(PANEL_WIDTH, PANEL_HEIGHT);
					attempts ++;
				}
			}
		}
		
	}
	
	public void moveObject(Sprite sprite) {
		int newXPosition = sprite.getxPosition() + sprite.getxVelocity();
		sprite.setxPosition(newXPosition, getWidth());
		int newYPosition = sprite.getyPosition() + sprite.getyVelocity();
		sprite.setyPosition(newYPosition, getHeight());
	}
	
	public void checkWallBounce(){
		for (Enemy enemy: enemies) {
			if (enemy.getxPosition() >= (getWidth() - enemy.getWidth()) || enemy.getxPosition() <= 0){
				enemy.setxVelocity(-(enemy.getxVelocity()));
			}
			if (enemy.getyPosition() <= 0 || enemy.getyPosition() >= getHeight() - enemy.getHeight()) {
	        enemy.setyVelocity(-enemy.getyVelocity());
			}
	      }
	}
	
	public void enemyCollision() {
		long currentTime = System.currentTimeMillis();
		//immune if within 3 seconds of being hit
		if (currentTime - lastCollisionTime <= IMMUNITY_DURATION) {
			return;
		}
		for (Enemy enemy: enemies) {
			if(player.getRectangle().intersects(enemy.getRectangle())){
				player.setColour(Color.MAGENTA);
				lastCollisionTime = currentTime;
				playerLives--;
				//System.out.println("Lives Remaining: " + playerLives);
				break;
			}
		}
		if (currentTime - lastCollisionTime > IMMUNITY_DURATION) {
			player.setColour(Color.BLUE);
		}
		if (playerLives <= 0) {
			gameState = GameState.GameOver;
			//System.out.println("Game over!");
		}
		}
	
	public void collectCoin() {
		for (int i = coins.size() - 1; i >= 0; i--) {
	        Coin coin = coins.get(i);
	        if (player.getRectangle().intersects(coin.getRectangle())) {
	            coins.remove(i);       
	            playerScore++; 
	            //System.out.println("Score: " + playerScore + " | Coins remaining: " + coins.size());
	        }
	    }
		if (coins.isEmpty()) {
			gameState = GameState.GameWon;
		}
		}

	@Override
	public void keyTyped(KeyEvent e) {
		//not used
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_ENTER){
			if (gameState == GameState.Initialising) {
				gameState = GameState.Playing;
			}
			else if (gameState == GameState.GameWon || gameState == GameState.GameOver) {
				createObjects();
				playerScore = 0;
				playerLives = 5;
				//resets player score and lives
				//without the reset, could play it longer with each game becoming a 'round'
				gameState = GameState.Initialising;
		        if (!timer.isRunning()) {
		            timer.start(); // Restart timer if it was stopped in GameOver	
			}
			}
		}
		if(event.getKeyCode() == KeyEvent.VK_SPACE) {
			if (gameState == GameState.Playing) {
				gameState = GameState.Paused;
				timer.stop();
				repaint();
			}
			else if (gameState == GameState.Paused) {
				gameState = GameState.Playing;
				timer.start();
				repaint();
			}
		}
		if(event.getKeyCode() == KeyEvent.VK_UP) {
			player.setyVelocity(-2);
		}
		else if(event.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setyVelocity(2);
		}
		else if(event.getKeyCode() == KeyEvent.VK_LEFT) {
			player.setxVelocity(-2);
		}
		else if(event.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.setxVelocity(2);
		}
		else if(event.getKeyCode() == KeyEvent.VK_LEFT && event.getKeyCode() == KeyEvent.VK_UP) {
			player.setxVelocity(-2);
			player.setyVelocity(-2);
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT && event.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setxVelocity(-2);
			player.setyVelocity(2);
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT && event.getKeyCode() == KeyEvent.VK_UP) {
			player.setxVelocity(2);
			player.setyVelocity(-2);
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT && event.getKeyCode() == KeyEvent.VK_DOWN) {
			player.setxVelocity(2);
			player.setyVelocity(2);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN ||
				event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.setxVelocity(0);
			player.setyVelocity(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		update();
		repaint();	
	}
	
	//painting methods
	
	// for testing
	/*private void paintCentreZone(Graphics g, Rectangle rectangle) {
		g.setColor(Color.GRAY);
		g.fillRect(centreZoneX, centreZoneY, CENTRE_ZONE_WIDTH, CENTRE_ZONE_HEIGHT);
	}*/
	
	private void paintSprite(Graphics g, Sprite sprite) {
	     g.setColor(sprite.getColour());
	     //circle looks nicer - keep separate from coin painter in case want to change back to rectangle
	     g.fillOval(sprite.getxPosition(), sprite.getyPosition(), sprite.getWidth(), sprite.getHeight());
	}
	
	private void paintCoin(Graphics g, Sprite sprite) {
	     g.setColor(sprite.getColour());
	     g.fillOval(sprite.getxPosition(), sprite.getyPosition(), sprite.getWidth(), sprite.getHeight());
	}
	
	private void paintScore(Graphics g) {
		int xPadding = 50;
		int yPadding = 50;
		int fontSize = 20; 
		Font scoreFont = new Font("SansSerif", Font.BOLD, fontSize);
		String score = "PLAYER SCORE: " + Integer.toString(playerScore);
		g.setFont(scoreFont);
		g.setColor(Color.WHITE);
		g.drawString(score, getWidth()-4*xPadding, getHeight()- yPadding);
		}
	
	private void paintLives(Graphics g) {
		int xPadding = 50;
		int yPadding = 50;
		int fontSize = 20;
		Font scoreFont = new Font("SansSerif", Font.BOLD, fontSize);
		
		long currentTime = System.currentTimeMillis();
	    if (currentTime - lastCollisionTime <= IMMUNITY_DURATION) {
	        g.setColor(Color.RED); // Hit and lost a life
	    } else {
	        g.setColor(Color.WHITE);
	    }
		String lives = null;
		switch (playerLives) {
		case 0: lives = "PLAYER LIVES: "; break;
		case 1: lives = "PLAYER LIVES: " + "\u2661 "; break;
		case 2: lives = "PLAYER LIVES: " + "\u2661 " + "\u2661 "; break;
		case 3: lives = "PLAYER LIVES: " + "\u2661 " + "\u2661 " + "\u2661 "; break;
		case 4: lives = "PLAYER LIVES: " + "\u2661 " + "\u2661 " + "\u2661 " + "\u2661 "; break;
		case 5: lives = "PLAYER LIVES: " + "\u2661 " + "\u2661 " + "\u2661 " + "\u2661 " + "\u2661"; break;
		}
		g.setFont(scoreFont);
		g.drawString(lives, xPadding, getHeight()- yPadding);
	}
	
	private void paintMainMessage(Graphics g) {
		int xPadding = getWidth()/8;
		int yPadding = getHeight()/2;
		int fontSize = 50;
		Font scoreFont = new Font("SansSerif", Font.BOLD, fontSize);
		g.setColor(Color.WHITE);
		g.setFont(scoreFont);
		String message = "";
		switch (gameState) {
		case Initialising: { message = "**Press ENTER to START**";
		break;
		}
		case Paused: { message = "**GAME PAUSED**";
		break;
		}
		case GameWon: { message = "**GAME WON**";
		xPadding = getWidth()/8 + 75;
		break;
		}
		case GameOver: { message = "**GAME OVER**";
		xPadding = getWidth()/8 + 75;
		break;
		}
		default: 
			return;
		}
		g.drawString(message, xPadding, yPadding);
	}
	
	private void paintSubmessage(Graphics g) {
		int xPadding = (getWidth()/8 + 75);
		int yPadding = (getHeight()/2 + 50);
		int fontSize = 30;
		Font scoreFont = new Font("SansSerif", Font.ITALIC, fontSize);
		g.setColor(Color.WHITE);
		g.setFont(scoreFont);
		String pauseInstruction = "Press SPACE to PAUSE";
		String unpauseInstruction = "Press SPACE to PLAY";
		String restart = "Press ENTER to RESTART";
		if (gameState == GameState.Initialising) {
			g.drawString(pauseInstruction, xPadding, yPadding);
		}
		else if (gameState == GameState.Paused) {
			g.drawString(unpauseInstruction, xPadding, yPadding);
		}
		else if (gameState == GameState.GameOver || gameState == GameState.GameWon) {
			g.setFont(scoreFont);
			g.drawString(restart, xPadding, yPadding);
		}
	}
	
	public void paintInstructions(Graphics g) {
		int xPadding = (50);
		int yPadding = (50);
		int fontSize = 20;
		Font scoreFont = new Font("SansSerif", Font.PLAIN, fontSize);
		g.setColor(Color.WHITE);
		g.setFont(scoreFont);
		String gameInstructions = "  COIN COLLECTOR GAME";
		String gameInstructions1 = "* Move using the arrow keys";
		String gameInstructions2 = "* Collect all yellow coins to win";
		String gameInstructions3 = "* Avoid the red enemies";
		String gameInstructions4 = "* Recieve 3 seconds immunity after an enemy hit";
		if (gameState == GameState.Initialising) {
			g.drawString(gameInstructions, xPadding, yPadding);
			g.drawString(gameInstructions1, xPadding, yPadding + 30);
			g.drawString(gameInstructions2, xPadding, yPadding + 60);
			g.drawString(gameInstructions3, xPadding, yPadding + 90);
			g.drawString(gameInstructions4, xPadding, yPadding + 120);
		}
		else if (gameState == GameState.Paused) {
			g.drawString(gameInstructions, xPadding, yPadding);
			g.drawString(gameInstructions1, xPadding, yPadding + 30);
			g.drawString(gameInstructions2, xPadding, yPadding + 60);
			g.drawString(gameInstructions3, xPadding, yPadding + 90);
			g.drawString(gameInstructions4, xPadding, yPadding + 120);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	     if(gameState != GameState.Initialising) {
	    	 //objects from background to foreground as follows
	    	 if (coins!= null) {
	    		 for (Coin coin : coins) {
	    			 paintCoin(g, coin);
	    			 }
	    	 }
	    	 if (enemies!= null) {
	    		 for (Enemy enemy: enemies) {
	    			 paintSprite(g, enemy); 
	    		 }
	    	 }
	         if (player!=null) {
	        	paintSprite(g, player); 
	         }
	     }
	     paintInstructions(g);
	     paintScore(g);
	     paintLives(g);
	     paintMainMessage(g);
	     paintSubmessage(g);
	     //TEST paintCentreZone(g, centreZone);
		}

}
