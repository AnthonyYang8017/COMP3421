package ass2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

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

	static Terrain myTerrain;
    
    static double angle = 0;
	static double angle2 = 0;
	
	static double positionX = 0;
	static double positionZ = 0;
	static double speed = 0.05;
	
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	
	static boolean pressW = false;
	static boolean pressS = false;
	static boolean pressA = false;
	static boolean pressD = false;
	
	static boolean FPcamera = false;
	static boolean cameramode = false;
	
	private float a = 0.3f; // Ambient white light intensity.
	private float d = 0.8f; // Diffuse white light intensity
	private float s = 0.3f; // Specular white light intensity.
	private float g = 0.2f; // Global Ambient intensity.
	
	private double counter = 0; //counter

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
    	// initialisation
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);
        
        //Terrain terrain = LevelIO.load(new File(args[0]));
        Terrain terrain = LevelIO.load(new File("test.txt"));
        Game game = new Game(terrain);
        positionX = positionZ = Terrain.getMySize().width/2;
        
        panel.addGLEventListener(game);
        panel.addKeyListener(game);
        panel.setFocusable(true);        

        // Add an animator to call 'display' at 60fps        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();  
        
        // put it in a JFrame
        final JFrame jframe = new JFrame("ass2");
        jframe.setSize(1920, 1080);
        jframe.add(panel);
        jframe.setVisible(true); 
        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		float factor = (float)((Math.sin(3.14*(counter/180))+1)/2.3);
		gl.glClearColor((float) (factor*0.65),(float) (factor*0.9),(float) (factor*1), 0.0f);

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        cameraSetting(drawable);
        setGlobalLighting(drawable);
        setTorchLighting(drawable);
        
        Key();
        
		GameObject.ROOT.draw(gl);
	}
	public void setGlobalLighting(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_LIGHT0);
		
		float factor = (float)((Math.sin(3.14*(counter/180))+1)/2.3);
		a = (float) ((float) (factor*a)+0.1);
		d = (float) ((float) (factor*d)+0.1);
		s = (float) ((float) (factor*s)+0.1);
		//System.out.println(factor);
		float dusk;
		if(counter>90&&counter<270){
			dusk = (float) ((90-Math.abs(counter-180))*0.01 + a);
		}else{
			dusk = a;
		}
		
		float lightAmb[] = { dusk, a, a, 1.0f };
        float lightDif0[] = { d, d, d, 1.0f };
        float lightSpec0[] = { s, s, s, 1.0f };
        
        // Light0 properties.
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif0,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec0,0);        
        
        float lightPos0[] = {30.0f, 0.0f, 0.0f, 1.0f};
        
    	if(counter>360)counter=0;
    	counter = counter+0.25;
    	gl.glPushMatrix();{
    		gl.glRotated(counter, 0.0, 0.0, 1.0);
    		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0,0);
    			//sun
    			gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
    			float[] ambient = {1.0f, 1.0f, 1.0f, 1.0f};
    			float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    			float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};
    			float[] emmL = {1.0f, 1.0f, 1.0f, 1.0f};
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emmL,0);
    			GLUT glut = new GLUT();
    			glut.glutSolidSphere(5, 20, 20);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, specular,0);
		}gl.glPopMatrix();
	}
	public void setTorchLighting(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if(counter > 180){
			gl.glEnable(GL2.GL_LIGHT1);
		}else{
			gl.glDisable(GL2.GL_LIGHT1);
		}
				
		float lightAmb[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float lightDif0[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float lightSpec0[] = {0.0f, 0.0f, 0.0f, 1.0f};
        
        // LIGHT1 properties.
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDif0,0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightSpec0,0);        
        
        float lightPos0[] = {0.0f, 0.0f, 0.0f, 1.0f};
        float spotDirection[] = {0.0f, 0.0f, -1.0f};
        float spotAngle = 45.0f;
        
    	gl.glPushMatrix();{
    		gl.glTranslated(0, Terrain.myAvatar.getPosition()[1], 0);
    		gl.glRotated(Terrain.myAvatar.getRotation()[1], 0.0, 1.0, 0.0);
    		gl.glTranslated(0, 0, -0.1);
    		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos0,0);
    		gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, spotAngle);
    		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, spotDirection,0);
    			//sun
    			gl.glTranslatef(lightPos0[0], lightPos0[1], lightPos0[2]);
    			float[] ambient = {1.0f, 1.0f, 1.0f, 1.0f};
    			float[] diffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    			float[] specular = {0.0f, 0.0f, 0.0f, 1.0f};
    			float[] emmL = {1.0f, 1.0f, 1.0f, 1.0f};
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emmL,0);
    			GLUT glut = new GLUT();
    			glut.glutSolidSphere(0.1, 20, 20);
    			gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, specular,0);
		}gl.glPopMatrix();
	}
	@Override
	public void dispose(GLAutoDrawable drawable) {		
	}
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// gl.glClearColor(0.1f, 0.1f, 1.0f, 0.0f);
		
		// enable depth testing
    	gl.glEnable(GL2.GL_DEPTH_TEST);
    	// enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        // normalise normals (!)
        // this is necessary to make lighting work properly
        gl.glEnable(GL2.GL_NORMALIZE);
        float globAmb[] = { g, g, g, 1.0f };
        // Global light properties
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, GL2.GL_TRUE); // Enable local viewpoint.
        
        
        //Objects initialisation
        myTerrain.addAvatar();
       
        //Initalise Zombie VBOs
        myTerrain.othersVBOInit(gl);
        
        //myTerrain.addZombie(gl);
        //myTerrain.addZombie(gl);
        //myTerrain.addZombie(gl);
        
        
        myTerrain.addPortal();
       
        initialiseTextures(gl);
        
        gl.glEnable(GL2.GL_TEXTURE_2D); 
	}
	
	public void initialiseTextures(GL2 gl ){
	      //Texture initialisation 
		//List<MyTexture> textures;
		
        String groundTextureFileName = "src/ass2/grass.bmp";
        MyTexture groundTexture = new MyTexture(gl,groundTextureFileName,"bmp",true);
        String treeTopTextureFileName = "src/ass2/leaves.jpg";
        MyTexture treeTopTexture = new MyTexture(gl,treeTopTextureFileName,"jpg",true);
        String treeTrunkTextureFileName = "src/ass2/bark.jpg";
        MyTexture treeTrunkTexture = new MyTexture(gl,treeTrunkTextureFileName,"jpg",true);
        String roadTextureFileName = "src/ass2/yellowRock.jpg";
        MyTexture roadTexture = new MyTexture(gl,roadTextureFileName,"jpg",true);
        
        //Avatar textures
        String headTexFileName = "src/ass2/hair.bmp";
        MyTexture headTex = new MyTexture(gl, headTexFileName,"bmp",true);
        String faceTexFileName = "src/ass2/avatarFace.bmp";
        MyTexture faceTex = new MyTexture(gl,faceTexFileName,"bmp",true);
        String bodyTexFileName = "src/ass2/avatarBody.bmp";
        MyTexture bodyTex = new MyTexture(gl,bodyTexFileName,"bmp",true);
        /*
        
        //Zombie Textures
        String ZheadTexFileName = "src/ass2/zombieBody.bmp";
        MyTexture ZheadTex = new MyTexture(gl,ZheadTexFileName,"bmp",true);
        String ZFaceTexFileName = "src/ass2/zombieFace.bmp";
        MyTexture ZFaceTex = new MyTexture(gl,ZFaceTexFileName,"bmp",true);
        String ZBodyTexFileName = "src/ass2/zombieBody.bmp";
        MyTexture ZBodyTex = new MyTexture(gl,ZBodyTexFileName,"bmp",true);
       // System.out.println(ZtorsoTex.getTextureId());
        */
        
        //Portal Textures
        String ATextureFileName = "src/ass2/rock_norm.bmp";
        MyTexture ATex = new MyTexture(gl,ATextureFileName,"bmp",true);
        String BTextureFileName = "src/ass2/rock_norm.bmp";
        MyTexture BTex = new MyTexture(gl,BTextureFileName,"bmp",true);
        
        myTerrain.setTextures(groundTexture, treeTopTexture, treeTrunkTexture, roadTexture,faceTex, 
        		headTex, bodyTex, ATex, BTex );
        
	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        if(FPcamera){
            gl.glFrustum(-0.2, 0.2, -0.1, 0.1, 0.1, 60);
        }else{
            gl.glFrustum(-0.15, 0.15, -0.075, 0.075, 0.3, 60);
        }
        gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
	public void cameraSetting(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if(FPcamera!=cameramode){
        	cameramode(drawable);
        	cameramode=FPcamera;
        }
        
        if(FPcamera){
        	gl.glRotated(angle,1,0,0);
        	gl.glRotated(angle2,0,1,0);
        	gl.glTranslated(0, -(Terrain.altitude(myTerrain.getsize().width-Game.positionX,myTerrain.getsize().height-Game.positionZ)+1), 0);
        }else{
        	gl.glTranslated(0, 0, -20);
        	gl.glRotated(angle+45,1,0,0);
        	gl.glRotated(angle2,0,1,0);
        	//gl.glTranslated(0, -(Terrain.altitude(10-Game.positionX,10-Game.positionZ)), 0);
        	gl.glTranslated(0, -2, 0);
        }
	}
	public void cameramode(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        if(FPcamera){
            gl.glFrustum(-0.2, 0.2, -0.1, 0.1, 0.1, 60);
        }else{
            gl.glFrustum(-0.15, 0.15, -0.075, 0.075, 0.3, 60);
        }
        gl.glMatrixMode(GL2.GL_MODELVIEW);
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
		
		myTerrain.setPosition(Game.positionX-myTerrain.getsize().width, 0, Game.positionZ-myTerrain.getsize().height);
		}
}
