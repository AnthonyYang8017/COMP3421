package ass2;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import ass2.MathUtil;



/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain extends GameObject {

    private Dimension mySize;
    private static double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private Avatar myAvatar;
    private float[] mySunlight;

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
        return myAltitude[x][z];
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
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }
    
    public void addAvatar() {
    	myAvatar = new Avatar(this);
    	myAvatar.setPosition(0,altitude(0,0),0);
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
	    
	    myAvatar.setPosition(10-Game.positionX, altitude(10-Game.positionX,10-Game.positionZ), 10-Game.positionZ);
	    
	}
	
	public void update(double dt) {
        // do nothing
		//myAvatar.setPosition(Game.positionX-10, 0, Game.positionZ-10);
		
    }
	
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
}
