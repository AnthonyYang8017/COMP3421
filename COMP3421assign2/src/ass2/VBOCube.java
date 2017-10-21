package ass2;

import com.jogamp.opengl.GL2;

public class VBOCube extends GameObject {
	
	private OtherVBO vbo; //the actual VBO, and method for drawing

	public VBOCube(GameObject parent) {
		super(parent);
	}
	
	public void initVBO(OtherVBO vbo){
		this.vbo = vbo;
	}
	
	public void drawSelf(GL2 gl) {
		gl.glPushMatrix();
		vbo.drawVOB(gl);
		gl.glPopMatrix();
	}
}
