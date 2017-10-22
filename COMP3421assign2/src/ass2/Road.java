package ass2;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road extends GameObject {

    private List<Double> myPoints;
    private double myWidth;
    private List<Double> roadPoints;
    private List<Double> altitudes;
    private double startingAltitude;
    private MyTexture texture;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(GameObject parent, double width, double x0, double y0, double altitude) {
        super(parent);
    	myWidth = width;
        myPoints = new ArrayList<Double>();
        roadPoints = new ArrayList<Double>();
        altitudes = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
        
        generateRoad();
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(GameObject parent,double width, double[] spine, double altitude) {
    	super(parent);
        myWidth = width;
        myPoints = new ArrayList<Double>();
        altitudes = new ArrayList<Double>();
        roadPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
        
        generateRoad();
    }
    
    /**
     * Generate points along the side of the road
     */
    private void generateRoad(){	
    	double increment = (size()/(double)20);
    	//List<Double> otherSide = new ArrayList<Double>();  ;;
    	for(double t = increment; t<(size() -increment) ; t= t+increment ){
    		double[] p_before = point(t-increment);
    		double[] p = point(t);
    		double[] p_after = point(t+increment);
    		double[]tangent =  new double[2];
    		tangent[0]  = p[0] - p_before[0];
    		tangent[1] = p[1] - p_before[1];
    		
    		//get unscaled normals
    		double[] normal1 = new double[2];
    		normal1[0] = tangent[1];
    		normal1[1] = -tangent[0];
    		
    		//scale normals
    		double normalMagnitude =  Math.sqrt( normal1[0]*normal1[0] + normal1[1]*normal1[1] ); //magnitude should be same for both    		
    		normal1[0] /= normalMagnitude;
    		normal1[0] *= width()/2;
    		normal1[1] /= normalMagnitude;
    		normal1[1] *= width()/2;
    		//System.out.println("normal length: "+ Math.sqrt( normal1[0]*normal1[0] + normal1[1]*normal1[1] ) / width());
    		
    		//add points for spline point t
    		//System.out.println(roadPoints.size());
    		roadPoints.add(p[0] - normal1[0] );
    		roadPoints.add(p[1] - normal1[1] ); 
    		
    		roadPoints.add(p[0] + normal1[0] );
    		roadPoints.add(p[1] + normal1[1] );
    		
    		//keep track of altitude
    		altitudes.add(Terrain.altitude(p[0], p[1]));
    	}
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }
    
    /**
     * 
     * @param texture
     */
    public void setTexture(MyTexture texture){
    	this.texture = texture;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }
    
    
    public void drawSelf(GL2 gl) {
    	gl.glPushMatrix();
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
		gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId());
		
		//gl.glColor4d(1, 0, 0, 0);
		
//		gl.glLineWidth((float) 0.2);
		
    	//System.out.println("road");
		/*
    	gl.glBegin(GL2.GL_LINES);{
    		for(int i = 0; i<myPoints.size()-1;i+=2){
    			gl.glVertex3d(myPoints.get(i), 1, roadPoints.get(i+1));
    		}
    	}
    	gl.glEnd();*/
		gl.glBegin(GL2.GL_QUADS);
        {
        	int altitudeIndex = 0;
	        for(int i = 0; i<roadPoints.size()-8;i+=4){ //draw one road segment
	        	//System.out.println(roadPoints.get(i) + "  " +roadPoints.get(i+1) );
	        	gl.glTexCoord2d(1, 1);
	        	//System.out.println( "alt " + (Terrain.altitude(roadPoints.get(i),roadPoints.get(i+1)) + 0.1 ));
	        	//System.out.println( "alt1 " + Terrain.altitude(roadPoints.get(i+2),roadPoints.get(i+3)) + 0.1 ));
	        	//System.out.println( "alt2 " + Terrain.altitude(roadPoints.get(i+6),roadPoints.get(i+7) + 0.2 ));
	        	//System.out.println( "alt3 " + Terrain.altitude(roadPoints.get(i+4),roadPoints.get(i+5) + 0.2 ));
	        	gl.glVertex3d(roadPoints.get(i), Terrain.altitude(roadPoints.get(i),roadPoints.get(i+1)) + 0.1, roadPoints.get(i+1));
	        	gl.glTexCoord2d(1, 0);
	        	gl.glVertex3d(roadPoints.get(i+2),Terrain.altitude(roadPoints.get(i+2),roadPoints.get(i+3))+0.1, roadPoints.get(i+3));
	        	gl.glTexCoord2d(0, 0);
	        	gl.glVertex3d(roadPoints.get(i+6),Terrain.altitude(roadPoints.get(i+6),roadPoints.get(i+7))+0.1, roadPoints.get(i+7));
	        	gl.glTexCoord2d(0, 1);
	        	gl.glVertex3d(roadPoints.get(i+4),Terrain.altitude(roadPoints.get(i+4),roadPoints.get(i+5))+0.1, roadPoints.get(i+5));
	        	altitudeIndex++;
	        	
	        } 
        }
        gl.glEnd();
		
		
		gl.glPopMatrix();
    }
    

}
