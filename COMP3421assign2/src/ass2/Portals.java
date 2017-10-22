package ass2;

import com.jogamp.opengl.GL2;

public class Portals extends GameObject {
	public Cube PortalA;
	public Cube PortalB;

	// TODO Remove testing code
	public String testInit() {
		return "can use MyPortals";
	}

	public Portals(GameObject parent) {
		super(parent);
		PortalA = new Cube(this);
		double[] scale = new double[] { 0.1, 1, 0.5 };
		double[] rotation = new double[] { 0, 0, 0 };
		double[] Position = new double[] { 1, 0, 0 };
		PortalA.setPosition(Position);
		PortalA.setScale(scale);
		PortalA.setRotation(rotation);

		PortalB = new Cube(this);
		scale = new double[] { 0.1, 1, 0.5 };
		rotation = new double[] { 0, 0, 0 };
		Position = new double[] { -1, 0, 0 };
		PortalB.setPosition(Position);
		PortalB.setScale(scale);
		PortalB.setRotation(rotation);

	}

	public void setTextures(MyTexture texA, MyTexture texB) {
		PortalA.setTextures(texA, texA);
		PortalB.setTextures(texB, texB);
	}

	public void drawSelf(GL2 gl) {
		gl.glTranslated(0, -7.4, 0);
		float[] ambient = { 0.6f, 0.5f, 0.5f, 1.0f };
		float[] diffuse = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] specular = { 0.6f, 0.5f, 0.5f, 1.0f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
	}

	@Override
	public void update(double dt) {

	}
}
