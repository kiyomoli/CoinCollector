package coinGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;
import javax.swing.Timer;

import coinGame.GameState;
import coinGame.Sprite;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
	
	final Color BACKGROUND_COLOUR = Color.BLACK;
	//Black or Dark grey??
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
			}	
		}
		coins.clear();
		for (int i=0; i<10; i++) {
			coins.add(new Coin(PANEL_WIDTH, PANEL_HEIGHT));
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
				//g.setColor(Color.RED);
				lastCollisionTime = currentTime;
				playerLives--;
				System.out.println("Lives Remaining: " + playerLives);
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
	            System.out.println("Score: " + playerScore + " | Coins remaining: " + coins.size());
	        }
	    }
		if (coins.isEmpty()) {
			gameState = GameState.GameWon;
		}
		}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent event) {
		//press space to pause?
		//reset game/new round?
		if(event.getKeyCode() == KeyEvent.VK_ENTER){
			if (gameState == GameState.Initialising) {
				gameState = GameState.Playing;
			}
			else if (gameState == GameState.GameWon || gameState == GameState.GameOver) {
		        createObjects(); // Re-create/reset ball, paddles, and scores
		        gameState = GameState.Playing;
		        if (!timer.isRunning()) {
		            timer.start(); // Restart timer if it was stopped in GameOver
		        }
		}
		}
		//|| gameState == GameState.GameWon || gameState == GameState.GameOver)
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
	
	private void paintSprite(Graphics g, Sprite sprite) {
	     g.setColor(sprite.getColour());
	     //circle looks nicer?
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
		Font scoreFont = new Font("Serif", Font.BOLD, fontSize);
		String score = "PLAYER SCORE: " + Integer.toString(playerScore);
		g.setFont(scoreFont);
		g.setColor(Color.WHITE);
		g.drawString(score, getWidth()-4*xPadding, getHeight()- yPadding);
		}
	
	private void paintLives(Graphics g) {
		int xPadding = 50;
		int yPadding = 50;
		int fontSize = 20;
		Font scoreFont = new Font("Serif", Font.BOLD, fontSize);
		g.setColor(Color.WHITE);
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
	
	private void paintEntryScreen(Graphics g) {
		int xPadding = getWidth()/8;
		int yPadding = getHeight()/2;
		int fontSize = 50;
		Font scoreFont = new Font("Serif", Font.BOLD, fontSize);
		g.setColor(Color.WHITE);
		//String enterGame = "**PRESS ENTER TO START**";
		String enterGame = "**Press ENTER to START**";
		String test = "Test message here";
		if (gameState == GameState.Initialising) {
			g.setFont(scoreFont);
			g.drawString(enterGame, xPadding, yPadding);
			fontSize = 30; //doesn't change the font size??
			g.drawString(test, xPadding + 75, yPadding + 50);
		}
	}
	
	private void paintGameWon(Graphics g) {
		int xPadding = getWidth()/8;
		int yPadding = getHeight()/2;
		int fontSize = 75;
		Font scoreFont = new Font("Serif", Font.BOLD, fontSize);
		g.setColor(Color.WHITE);
		String gameWon = "**ROUND WON**";
		String continueGame = "Press ENTER to CONTINUE";
		if (gameState == GameState.GameWon) {
			g.setFont(scoreFont);
			g.drawString(gameWon, xPadding, yPadding);
			fontSize = 30;
			g.drawString(continueGame, xPadding + 75, yPadding + 50);
		}

		}
	
	private void paintGameOver(Graphics g) {
		int xPadding = getWidth()/8;
		int yPadding = getHeight()/2;
		int fontSize = 75;
		Font scoreFont = new Font("Serif", Font.BOLD, fontSize);
		g.setColor(Color.WHITE);
		String gameOver = "**GAME OVER**";
		String restart = "Press ENTER to RESTART";
		if (gameState == GameState.GameOver) {
			g.setFont(scoreFont);
			g.drawString(gameOver, xPadding, yPadding);
			fontSize = 30;
			g.drawString(restart, xPadding + 75, yPadding + 50);
		}

		}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintEntryScreen(g);
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
	     paintScore(g);
	     paintLives(g);
	     paintGameWon(g);
	     paintGameOver(g);
		}

}
