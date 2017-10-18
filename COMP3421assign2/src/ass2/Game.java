package ass2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import ass2.LevelIO;
import ass2.Terrain;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener , KeyListener{

    private Terrain myTerrain;
    private Sphere mySphere;
    
	private double angle = 45;
	private double angle2 = 0;
	
	static double positionX = 0;
	static double positionZ = 0;
	private double speed = 0.05;
	
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	
	private boolean pressW = false;
	private boolean pressS = false;
	private boolean pressA = false;
	private boolean pressD = false;
	
    private boolean FPcamera = false;


	public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        //mySphere = new Sphere(GameObject.ROOT);
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
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        
        if(FPcamera){
        	gl.glRotated(angle-45,1,0,0);
        	gl.glRotated(angle2,0,1,0);
        	gl.glTranslated(0, -(Terrain.altitude(10-Game.positionX,10-Game.positionZ)+1.5), 0);
        }else{
        	gl.glTranslated(0, 0, -20);
        	gl.glRotated(angle,1,0,0);
        	gl.glRotated(angle2,0,1,0);
        	//gl.glTranslated(0, -(Terrain.altitude(10-Game.positionX,10-Game.positionZ)), 0);
        }
        
		GameObject.ROOT.draw(gl);
		
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
        
        //Texture initialisation
        String groundTextureFileName1 = "src/ass2/grass.bmp";
        MyTexture groundTexture = new MyTexture(gl,groundTextureFileName1,"bmp",true);
        myTerrain.setGroundTexture(groundTexture);
        gl.glEnable(GL2.GL_TEXTURE_2D); 
        
        myTerrain.addAvatar();
    	
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        gl.glFrustum(-0.1, 0.1, -0.05, 0.05, 0.1, 40);
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
	     case KeyEvent.VK_W:
			  pressW = true;
			  break;
	     case KeyEvent.VK_S:
			  pressS = true;
			  break;
	     case KeyEvent.VK_A:
			  pressA = true;
			  break;
	     case KeyEvent.VK_D:
			  pressD = true;
			  break;
	     case KeyEvent.VK_ENTER:
	    	  FPcamera = !FPcamera;
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
	     case KeyEvent.VK_W:
			  pressW = false;
			  break;
	     case KeyEvent.VK_S:
			  pressS = false;
			  break;
	     case KeyEvent.VK_A:
			  pressA = false;
			  break;
	     case KeyEvent.VK_D:
			  pressD = false;
			  break;
		
		 default:
			 break;
		 }
	}
	
	public void Key(){
		if(up){
			angle = (angle - 1) % 360;
		}
		if(down){
			angle = (angle + 1) % 360;
		}
		if(left){
			angle2 = ((angle2 + 177.0) % 360.0 + 360.0) % 360.0 - 180.0;
		}
		if(right){
			angle2 = ((angle2 + 183.0) % 360.0 + 360.0) % 360.0 - 180.0;
		}
		
		double finalspeed;
		if((pressW&&pressA)||(pressW&&pressD)||(pressA&&pressS)||(pressS&&pressD)){
			finalspeed = speed/Math.sqrt(2);
		}else{
			finalspeed = speed;
		}
		
		
		if(pressW){
			positionX = positionX-finalspeed*Math.sin(angle2/180*Math.PI);
			positionZ = positionZ+finalspeed*Math.cos(angle2/180*Math.PI);
		}
		if(pressS){
			positionX = positionX+finalspeed*Math.sin(angle2/180*Math.PI);
			positionZ = positionZ-finalspeed*Math.cos(angle2/180*Math.PI);
		}
		if(pressA){
			positionX = positionX+finalspeed*Math.cos(angle2/180*Math.PI);
			positionZ = positionZ+finalspeed*Math.sin(angle2/180*Math.PI);
		}
		if(pressD){
			positionX = positionX-finalspeed*Math.cos(angle2/180*Math.PI);
			positionZ = positionZ-finalspeed*Math.sin(angle2/180*Math.PI);
		}
		
		myTerrain.setPosition(Game.positionX-10, 0, Game.positionZ-10);
		
		//System.out.print(" !!!"+  speed*Math.cos(angle2/180*Math.PI) + " !!!\n");
	}
}
