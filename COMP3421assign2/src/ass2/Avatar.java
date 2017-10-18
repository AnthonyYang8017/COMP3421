package ass2;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar extends GameObject {

	public Avatar(GameObject parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	
	public void drawSelf(GL2 gl) {
		gl.glPushMatrix();
		float[] ambient = {0.2f, 0.4f, 1f, 1.0f};
	    float[] diffuse = {0.2f, 0.4f, 1f, 1.0f};
	    float[] specular = {0.0f, 0.1f, 1f, 1.0f};
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
		
		
		GLUT glut = new GLUT();
        glut.glutSolidSphere(1.0, 20, 20);
        
        gl.glPopMatrix();
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

}
