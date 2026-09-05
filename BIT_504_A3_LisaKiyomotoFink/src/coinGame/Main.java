package coinGame;

import javax.swing.JFrame;

public class Main extends JFrame {
	
	public Main() {
		
		final String WINDOW_TITLE = "Coin Game";
    	setTitle(WINDOW_TITLE);
    	setResizable(false);
    	GamePanel panel = new GamePanel();
    	add(panel);
    	pack();
    	setVisible(true);//false used if an application opens a window that the end user shouldn't see
    	setLocationRelativeTo(null); //centres it on screen
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
}

	public static void main(String[] args) {

		//need to do the below so the code runs predictably and safely
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
        	 
            public void run() {
   
                   new Main();
   
            }
   
          });
        
	}

}