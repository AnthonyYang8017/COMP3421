/*package ass2;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Zombie extends GameObject {

	private CubeVBO myHead;
	private CubeVBO myArmleft;
	private CubeVBO myArmright;
	private CubeVBO myTorso;
	private CubeVBO mylegleft;
	private CubeVBO mylegright;
	
	private double forward = 1;
	
	private static float vertices[] = 
		{
				1, -1, 1, 
				1, 1, 1, 
				1, 1, -1, 
				1, -1, -1, 
				-1, -1, 1, 
				-1, 1, 1, 
				-1, 1, -1, 
				-1, -1, -1
			};

	private float colors[] =  
		{
			1,0,0, 
			0,1,0,
			1,1,1,
			0,0,0,
			0,0,1, 
			1,1,0,
			1,0,0, 
			0,1,0,
			
		}; 
	
		// Vertex indices of each box side, 6 groups of 4.
	private static short quadIndices[] = 
		{
		    3, 2, 1, 0, //right face
		    7, 6, 2, 3, //back face
		    4, 5, 6, 7, //left face
			0, 1, 5, 4, //front face
			4, 7, 3, 0, //bottom face
			6, 5, 1, 2  //top face
		};
		
	
	//These are not vertex buffer objects, they are just java containers
		//These are not vertex buffer objects, they are just java containers
	private FloatBuffer  posData= Buffers.newDirectFloatBuffer(vertices);
	private FloatBuffer colorData = Buffers.newDirectFloatBuffer(colors);
	private ShortBuffer indexData = Buffers.newDirectShortBuffer(quadIndices);
		
		//We will be using 2 vertex buffer objects
	private int bufferIds[] = new int[2];
			
	
	public Zombie(GameObject parent) {
		super(parent);
		setMyHead(new CubeVBO(this));
		double[] scale = new double[]{0.4,0.4,0.4};
		double[] rotation = new double[]{0,0,0};
		double[] Position = new double[]{0,1.8,0};
		getMyHead().setPosition(Position);
		getMyHead().setScale(scale);
		getMyHead().setRotation(rotation);
		
		myTorso = new CubeVBO(this);
		scale = new double[]{0.4,0.6,0.2};
		rotation = new double[]{0,0,0};
		Position = new double[]{0,1,0};
		myTorso.setPosition(Position);
		myTorso.setScale(scale);
		myTorso.setRotation(rotation);
		
		
		//myArm
		myArmleft = new CubeVBO(this);
		scale = new double[]{0.2,0.6,0.2};
		rotation = new double[]{90,0,0};
		Position = new double[]{0.6,1,0};
		myArmleft.setPosition(Position);
		myArmleft.setScale(scale);
		myArmleft.setRotation(rotation);
		
		myArmright = new CubeVBO(this);
		scale = new double[]{0.2,0.6,0.2};
		rotation = new double[]{90,0,0};
		Position = new double[]{-0.6,1,0};
		myArmright.setPosition(Position);
		myArmright.setScale(scale);
		myArmright.setRotation(rotation);
		
		
		//myleg
		mylegleft = new CubeVBO(this);
		scale = new double[]{0.2,0.6,0.19};
		rotation = new double[]{0,0,0};
		Position = new double[]{0.2,0,0};
		mylegleft.setPosition(Position);
		mylegleft.setScale(scale);
		mylegleft.setRotation(rotation);
		
		mylegright = new CubeVBO(this);
		scale = new double[]{0.2,0.6,0.19};
		rotation = new double[]{0,0,0};
		Position = new double[]{-0.2,0,0};
		mylegright.setPosition(Position);
		mylegright.setScale(scale);
		mylegright.setRotation(rotation);
		
	}
	
	
	public void vboInit(GL2 gl) {
   	 
    	// gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
    	    	 
    	 //Generate 2 VBO buffer and get their IDs
         gl.glGenBuffers(2,bufferIds,0);
        
    	 //This buffer is now the current array buffer
         //array buffers hold vertex attribute data
         gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);
      
         //This is just setting aside enough empty space
         //for all our data
         gl.glBufferData(GL2.GL_ARRAY_BUFFER,    //Type of buffer  
        	        vertices.length * Float.BYTES +  colors.length* Float.BYTES, //size needed
        	        null,    //We are not actually loading data here yet
        	        GL2.GL_STATIC_DRAW); //We expect once we load this data we will not modify it


         //Actually load the positions data
         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, //From byte offset 0
        		 vertices.length*Float.BYTES,posData);

         //Actually load the color data
         gl.glBufferSubData(GL2.GL_ARRAY_BUFFER,
        		 vertices.length*Float.BYTES,  //Load after the position data
        		 colors.length*Float.BYTES,colorData);
         
         
         //Now for the element array
         //Element arrays hold indexes to an array buffer
         gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);

         //We can load it all at once this time since there are not
         //two separate parts like there was with color and position.
         gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER,      
     	        quadIndices.length *Short.BYTES,
     	        indexData, GL2.GL_STATIC_DRAW);
    }
	
	
	public void drawSelf(GL2 gl) {
		update(0);
		float[] ambient = {0f, 0.1f, 0.1f, 1.0f};
	    float[] diffuse = {0f, 0.2f, 0.2f, 1.0f};
	    float[] specular = {0.0f, 0.1f, 0.1f, 1.0f};
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
	    gl.glTranslated(0, -0.6, 0);
	    
	    
	    //do VBO binding stuff
	    
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER,bufferIds[0]);

    	// Enable two vertex arrays: coordinates and color.
    	//To tell the graphics pipeline that we want it to use our vertex position and color data
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_COLOR_ARRAY);

   	   // This tells OpenGL the locations for the co-ordinates and color arrays.
   	   gl.glVertexPointer(3, //3 coordinates per vertex 
   			              GL.GL_FLOAT, //each co-ordinate is a float 
   			              0, //There are no gaps in data between co-ordinates 
   			              0); //Co-ordinates are at the start of the current array buffer
   	   gl.glColorPointer(3, GL.GL_FLOAT, 0, 
   			             vertices.length*Float.BYTES); //colors are found after the position
   	   												    //co-ordinates in the current array buffer
    	
   	   
   	   //Also need to bind the current element array buffer
   	   gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, bufferIds[1]);
   	
	    
	}
	
	@Override
    public void update(double dt) {
		double[] angle1 = new double[]{0,0,0};
		double[] angle2 = new double[]{0,0,0};
		double[] angle3 = new double[]{0,0,0};
		double[] angle4 = new double[]{0,0,0};
	   
    	double tem = myArmleft.getRotation()[0];
    	if(tem>110 || tem<70){
    		forward = -forward;
    	}
		angle1 = new double[]{1*forward*Game.speed*30,0,0};
		angle2 = new double[]{-1*forward*Game.speed*30,0,0};
		myArmleft.rotate(angle1);
    	myArmright.rotate(angle2);
    	angle3 = new double[]{1*forward*Game.speed*80,0,0};
    	angle4 = new double[]{-1*forward*Game.speed*80,0,0};
    	mylegright.rotate(angle3);
    	mylegleft.rotate(angle4);
	}

	public CubeVBO getMyHead() {
		return myHead;
	}

	public void setMyHead(CubeVBO myHead) {
		this.myHead = myHead;
	}

}*/
