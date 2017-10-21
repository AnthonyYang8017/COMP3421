package ass2;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import ass2.MathUtil;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain extends GameObject {

    private static Dimension mySize;
    private static double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    static Avatar myAvatar;
    //private List<Zombie> myZombies;
    private Other myOther;
    private Portals myPortals;
    private Integer PortalA;
    private Integer PortalB;
    private static double myAvatarY = 0.75;
    private float[] mySunlight;
    private boolean forwardPortal = true;
    
    
    private MyTexture groundTexture;
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(GameObject parent) {
		super(parent);
	}
    public Terrain(int width, int depth) {
    	this(GameObject.ROOT);
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
      //  myZombies = new ArrayList<Zombie>();
        mySunlight = new float[3];
        
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }
    
    public void setGroundTexture (MyTexture texture){
        	groundTexture = texture;
    }

    public Dimension getsize() {
        return mySize;
    }

    public List<Tree> gettrees() {
        return myTrees;
    }

    public List<Road> getroads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public static double getGridAltitude(int x, int z) {
    	if(x+1<=mySize.width&&z+1<=mySize.height&&x>=0&&z>=0){
    		return myAltitude[x][z];
    	}
        return 0.0;
    }
    
    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public static double altitude(double x, double z) {
        double altitude = 0;
        double i = (double)(int) x;
        double j = (double)(int) z;
        double heightXZ = getGridAltitude((int)i,(int)j);
        double heightX1Z = getGridAltitude((int)i+1,(int)j);
        double heightXZ1 = getGridAltitude((int)i,(int)j+1);
        double heightX1Z1 = getGridAltitude((int)i+1,(int)j+1);
        double temi = x-i;
        double temj = z-j;
        if (temi + temj <= 1){
        	altitude = (heightXZ + (heightX1Z - heightXZ)*temi)
        			+((heightXZ1 + (heightX1Z - heightXZ1)*temi)
        			-(heightXZ + (heightX1Z - heightXZ)*temi))
        			*(temj/Math.sqrt(2));
        }else{
        	altitude =(heightXZ1 + (heightX1Z - heightXZ1)*temi)
        			+((heightXZ1 + (heightX1Z1 - heightXZ1)*temi)
        			-(heightXZ1 + (heightX1Z - heightXZ1)*temi))
        			*(temj/Math.sqrt(2));
        }
        
        return altitude;
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(this);
        tree.setPosition(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
    	Road road = new Road(this, width, spine, altitude(spine[0], spine[1]));
        myRoads.add(road);        
    }
    
    public void addAvatar() {
    	myAvatar = new Avatar(this);
    	myAvatar.setPosition(0,altitude(0,0)+getMyAvatarY(),0);
    	double[] scale = new double[]{0.5,0.45,0.5};
    	myAvatar.setScale(scale);
	}
    
    /*
    public void addZombie(GL2 gl) {
    	Zombie myZombie = new Zombie(this);
    	double randomNum = ThreadLocalRandom.current().nextDouble(0,1);
    	double randomNumX = (randomNum * (mySize.width-1));
    	randomNum = ThreadLocalRandom.current().nextDouble(0,1);
    	double randomNumY = (randomNum * (mySize.height-1));
    	myZombie.setPosition(randomNumX,altitude(randomNumX,randomNumY)+getMyAvatarY(),randomNumY);
    	randomNum = ThreadLocalRandom.current().nextDouble(0,1);

    	double random = randomNum/10;
    	double[] scale = new double[]{0.5+random,0.45+random,0.5+random};
    	myZombie.setScale(scale);
    	
    	//initialise VBO stuff
    	
    	myZombies.add(myZombie);
	}*/
    
    public void addOther(GL2 gl){
    	myOther = new Other(this);
    	myOther.initVBO(gl);
    	
    }
    
    public void addPortal() {
    	myPortals = new Portals(this);
    	System.out.println(myPortals.testInit());
    	PortalA = ThreadLocalRandom.current().nextInt(0,mySize.height-1);
    	double[] position = new double[]{0,altitude(0,PortalA)+9.1,PortalA};
    	myPortals.PortalA.setPosition(position);
    	
    	PortalB = ThreadLocalRandom.current().nextInt(0,mySize.height-1);
    	position = new double[]{mySize.width-1,altitude(mySize.width-1,PortalB)+9.1,PortalB};
    	myPortals.PortalB.setPosition(position);
    	System.out.println("Added portals "+ myPortals.testInit());
    	
    }

	public void drawSelf(GL2 gl) {
		
		gl.glPushMatrix();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		//gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		//gl.glColor4d(0, 0, 0, 0);
		gl.glLineWidth(10);
		
		float[] ambient = {0.1f, 0.5f, 0.1f, 1.0f};
	    float[] diffuse = {0.1f, 0.5f, 0.1f, 1.0f};
	    float[] specular = {0.1f, 0.1f, 0.1f, 1.0f};
	  
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
	    
	    gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT); 
        gl.glBindTexture(GL2.GL_TEXTURE_2D, groundTexture.getTextureId());
	 
        System.out.println("textureSize" + mySize.width + " " +mySize.height);
	    for (int z = 0; z < mySize.height -1; z++) {
	        for (int x = 0; x < mySize.width -1; x++) {
	        	double[] v;
	            gl.glBegin(GL2.GL_TRIANGLES);
	            {
	              
	              v = NormalProcesser(x, getGridAltitude(x, z), z);
	              gl.glNormal3d(v[0],v[1],v[2]);
	              gl.glTexCoord2d(0, 0);
	              gl.glVertex3d(x, getGridAltitude(x, z), z);
	              
	              v = NormalProcesser(x, getGridAltitude(x, z + 1), z + 1);
	              gl.glNormal3d(v[0],v[1],v[2]);
	              gl.glTexCoord2d(1, 0.5);
	              gl.glVertex3d(x, getGridAltitude(x, z + 1), z + 1);
	              
	              v = NormalProcesser(x + 1, getGridAltitude(x + 1, z), z);
	              gl.glNormal3d(v[0],v[1],v[2]);
	              gl.glTexCoord2d(0, 1);
	              gl.glVertex3d(x + 1, getGridAltitude(x + 1, z), z);
	            }
	            gl.glEnd();
	            
	            gl.glBegin(GL2.GL_TRIANGLES);
	            {
	              
	              v = NormalProcesser(x + 1, getGridAltitude(x + 1, z), z);
	              gl.glNormal3d(v[0],v[1],v[2]);
	              gl.glTexCoord2d(0, 0);
	              gl.glVertex3d(x + 1, getGridAltitude(x + 1, z), z);
	              
	              v = NormalProcesser(x, getGridAltitude(x, z + 1), z + 1);
	              gl.glNormal3d(v[0],v[1],v[2]);
	              gl.glTexCoord2d(1, 0.5);
	              gl.glVertex3d(x, getGridAltitude(x, z + 1), z + 1);
	              
	              v = NormalProcesser(x + 1, getGridAltitude(x + 1, z + 1), z + 1);
	              gl.glNormal3d(v[0],v[1],v[2]);
	              gl.glTexCoord2d(0, 1);
	              gl.glVertex3d(x + 1, getGridAltitude(x + 1, z + 1), z + 1);
	            }
	            gl.glEnd();
	        }
	    }
	    
	    gl.glPopMatrix();
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	    
	    update(0);
	}
	
	public void update(double dt) {
	    //should put in update but it's not working!!!
		if(!forwardPortal){
			if(myAvatar.getPosition()[0] < 0 && Math.abs(myAvatar.getPosition()[2]-PortalA) < 1){
				Game.positionX = 11-mySize.width;
				Game.positionZ = mySize.height-PortalB;
			}else if(myAvatar.getPosition()[0] > mySize.width-1 && Math.abs(myAvatar.getPosition()[2]-PortalB) < 1){
				Game.positionX = mySize.width;
				Game.positionZ = mySize.height-PortalA;
			}
			forwardPortal = !forwardPortal;
		}
		
	    myAvatar.setPosition(mySize.width-Game.positionX, altitude(mySize.width-Game.positionX,mySize.height-Game.positionZ)+getMyAvatarY(), mySize.height-Game.positionZ);
	    if((myAvatar.getPosition()[0] < 0 && Math.abs(myAvatar.getPosition()[2]-PortalA) < 1)||
	    		(myAvatar.getPosition()[0] > mySize.width-1 && Math.abs(myAvatar.getPosition()[2]-PortalB) < 1))
	    	forwardPortal = !forwardPortal;
	    
	    double[] rotation ;
	    if(Game.pressW||Game.pressS||Game.pressA||Game.pressD){
	    	rotation = new double[]{0,-Game.angle2,0};
	    	myAvatar.setRotation(rotation);
	    	rotation = new double[]{2,Game.angle2,0};
	    	myAvatar.getMyHead().setRotation(rotation);
	    }
	    	rotation = new double[]{2,-Game.angle2-myAvatar.getRotation()[1],0};
	    	myAvatar.getMyHead().setRotation(rotation);

	    if(Game.FPcamera){
	    	myAvatar.show(false);
	    }else{
	    	myAvatar.show(true);
	    }
		
	    if(Game.pressA&&Game.pressW){
	    	rotation = new double[]{0,45,0};
	    	myAvatar.rotate(rotation);
	    }else if(Game.pressA&&Game.pressS){
	    	rotation = new double[]{0,135,0};
	    	myAvatar.rotate(rotation);
	    }else if(Game.pressW&&Game.pressD){
	    	rotation = new double[]{0,-45,0};
	    	myAvatar.rotate(rotation);
	    }else if(Game.pressS&&Game.pressD){
	    	rotation = new double[]{0,-135,0};
	    	myAvatar.rotate(rotation);
	    }else if(Game.pressA){
	    	rotation = new double[]{0,90,0};
	    	myAvatar.rotate(rotation);
	    }else if(Game.pressD){
	    	rotation = new double[]{0,-90,0};
	    	myAvatar.rotate(rotation);
	    }else if(Game.pressS){
	    	rotation = new double[]{0,-180,0};
	    	myAvatar.rotate(rotation);
	    }
	   
	}
	    /*for (Zombie element : myZombies) {
	    	rotation = element.getRotation();
	    	rotation = new double[]{-Game.speed*Math.sin(rotation[1]/180*Math.PI)*0.5,0,-Game.speed*Math.cos(rotation[1]/180*Math.PI)*0.5};
	    	element.translate(rotation);
	    	rotation = element.getPosition();
	    	element.setPosition(rotation[0], altitude(rotation[0],rotation[2])+myAvatarY, rotation[2]);
	    	
	    	double[] position = myAvatar.getPosition();
	    	double ans = (((position[0]-rotation[0])
	    			/Math.abs(position[0]-rotation[0]))+2)*90
	    			+((Math.atan((rotation[2]-position[2])/(position[0]-rotation[0]))
	    			*(180/Math.PI)));
	    	ans = ((ans + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;

	    	
	    	if(((position[0]-rotation[0])*(position[0]-rotation[0]))+((rotation[2]-position[2])*(rotation[2]-position[2])) <= 30){
	    		if((ans-element.getRotation()[1]<=180&&ans-element.getRotation()[1]>0)||ans-element.getRotation()[1]<-180){
			    	rotation = new double[]{0,1,0};

		    	}else if(ans-element.getRotation()[1]<0||ans-element.getRotation()[1]>180){
			    	rotation = new double[]{0,-1,0};

		    	}else{
			    	rotation = new double[]{0,0,0};
		    	}
	    	}else{
	    		position = new double[]{mySize.width,0,mySize.height};
	    		ans = (((position[0]-rotation[0])
		    			/Math.abs(position[0]-rotation[0]))+2)*90
		    			+((Math.atan((rotation[2]-position[2])/(position[0]-rotation[0]))
		    			*(180/Math.PI)));
		    	ans = ((ans + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
	    		if((ans-element.getRotation()[1]<=180&&ans-element.getRotation()[1]>0)||ans-element.getRotation()[1]<-180){
			    	rotation = new double[]{0,1,0};

		    	}else if(ans-element.getRotation()[1]<0||ans-element.getRotation()[1]>180){
			    	rotation = new double[]{0,-1,0};

		    	}else{
			    	rotation = new double[]{0,0,0};
		    	}
	    	}
	    	element.rotate(rotation);
	    }
    }*/
	
	public double[] NormalProcesser(int x, double y, int z) {
		double[] normal;
		if( x > 0 && x < mySize.width-1 && z > 0 && z < mySize.height-1){
			double[] v1 = new double[]{x-1, getGridAltitude(x-1, z), z};
			double[] v2 = new double[]{x+1, getGridAltitude(x+1, z), z};
			double[] v3 = new double[]{x, getGridAltitude(x, z-1), z-1};
			double[] v4 = new double[]{x, getGridAltitude(x, z+1), z+1};
			normal = MathUtil.getNormal(v1, v2, v3, v4);
		}else{
			normal = new double[]{0, 1, 0};
		}
		return normal;
        
	}
	public static double getMyAvatarY() {
		return myAvatarY;
	}
	public void setMyAvatarY(double myAvatarY) {
		this.myAvatarY = myAvatarY;
	}
	
	//set textures for all Gameobjects
    public void setTextures (MyTexture ground, MyTexture treeTop, MyTexture treeTrunk, MyTexture road, MyTexture avFace,  MyTexture headTex,
    		MyTexture bodyTex, MyTexture ATex, MyTexture BTex ){
		groundTexture = ground;
		for(Tree t: myTrees){
			t.setTexture(treeTop, treeTrunk);
		}
		for(Road r: myRoads){
			r.setTexture(road);
		}
		/*
		for(Zombie z: myZombies){
			z.setTextures(zFace,ZheadTex, ZBodyTex);
		}*/
		
    	myAvatar.setTextures(avFace, headTex,  bodyTex);
    	//System.out.println(myPortals.testInit()); //
    	//System.out.println(ATex.getTextureId() + " " + BTex.getTextureId());
		myPortals.setTextures(ATex, BTex);

    	
    }
}
