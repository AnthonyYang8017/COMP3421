package ass2;

import com.jogamp.opengl.GL2;
import ass2.GameObject;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree extends GameObject {

	private Cylinder myCylinder;
	private Sphere mySphere;
    
    
    public Tree(GameObject parent) {
    	super(parent);
    	myCylinder = new Cylinder(this);
    	myCylinder.setPosition(0, -0.5, 0);
    	mySphere = new Sphere(this);
    	mySphere.setPosition(0, 2, 0);
    }
    
    public void setTexture(MyTexture top, MyTexture trunk){
    	mySphere.setTexture(top);
    	myCylinder.setTexture(trunk);
    }
}
