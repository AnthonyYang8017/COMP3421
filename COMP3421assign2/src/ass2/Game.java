package ass2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;

import ass2.LevelIO;
import ass2.Terrain;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener , KeyListener{

    private Terrain myTerrain;
    
	private double angle = 45;
	private double angle2 = 0;
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
    }
    
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        //Terrain terrain = LevelIO.load(new File(args[0]));
        Terrain terrain = LevelIO.load(new File("test.txt"));
        Game game = new Game(terrain);
        
        // initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);
        
        // put it in a JFrame
        final JFrame jframe = new JFrame("ass2");
        jframe.setSize(1920, 1080);
        jframe.add(panel);
        jframe.setVisible(true);

        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        panel.addGLEventListener(game);
        panel.addKeyListener(game);
        panel.setFocusable(true);

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();   
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslated(0, 0, -20);
        gl.glRotated(angle,1,0,0);
        gl.glRotated(angle2,0,1,0);
        
		myTerrain.drawTerrain(gl);
		Key();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
		//enable depth testing
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	
    	 // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        // turn on a light. Use default settings.
        gl.glEnable(GL2.GL_LIGHT0);
        
        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);
    	
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustum(-2, 2, -1, 1, 1, 40);
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		  
		 case KeyEvent.VK_SPACE:
		     break;
								  
		 case KeyEvent.VK_DOWN:
			  down = true;
			  break;
	     case KeyEvent.VK_UP:
	    	  up = true;
			  break;
			  
		 case KeyEvent.VK_RIGHT:
			  right = true;
			  break;
	     case KeyEvent.VK_LEFT:
	    	  left = true;
			  break;	
		
		 default:
			 break;
		 }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		  
		 case KeyEvent.VK_SPACE:
		     break;
								  
		 case KeyEvent.VK_DOWN:
			  down = false;
			  break;
	     case KeyEvent.VK_UP:
	    	  up = false;
			  break;
			  
		 case KeyEvent.VK_RIGHT:
			  right = false;
			  break;
	     case KeyEvent.VK_LEFT:
	    	  left = false;
			  break;	
		
		 default:
			 break;
		 }
	}
	
	public void Key(){
		if(up){
			angle = (angle + 1) % 360;
		}
		if(down){
			angle = (angle - 1) % 360;
		}
		if(left){
			angle2 = (angle2 - 3) % 360;
		}
		if(right){
			angle2 = (angle2 + 3) % 360;
		}
	}
}
