package ass2;

import com.jogamp.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Sphere extends GameObject {

	private double radius;
	private MyTexture texture;

	public Sphere(GameObject parent) {
		super(parent);
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setTexture(MyTexture top) {
		texture = top;
	}

	public void drawSelf(GL2 gl) {
		gl.glPushMatrix();
		// gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		// gl.glColor3f(1,0,0);
		gl.glEnable(GL2.GL_TEXTURE_2D);

		gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
		gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
		gl.glEnable(GL2.GL_TEXTURE_GEN_S);
		gl.glEnable(GL2.GL_TEXTURE_GEN_T);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId());

		float[] ambient = { 0.2f, 0.3f, 0.1f, 1.0f };
		float[] diffuse = { 0.2f, 0.3f, 0.1f, 1.0f };
		float[] specular = { 0.0f, 0.1f, 0.1f, 1.0f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);

		GLUT glut = new GLUT();
		// radius-The radius of the sphere.
		// slices-The number of subdivisions around the Z axis (similar to lines
		// of longitude).
		// stacks-The number of subdivisions along the Z axis (similar to lines
		// of latitude).
		glut.glutSolidSphere(1.0, 20, 20);
		gl.glDisable(GL2.GL_TEXTURE_GEN_S);
		gl.glDisable(GL2.GL_TEXTURE_GEN_T);
		gl.glPopMatrix();
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
	}

}
