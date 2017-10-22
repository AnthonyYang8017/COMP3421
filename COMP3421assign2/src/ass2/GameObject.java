package ass2;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * A GameObject is an object that can move around in the game world.
 * 
 * GameObjects form a scene tree. The root of the tree is the special ROOT
 * object.
 * 
 * Each GameObject is offset from its parent by a rotation, a translation and a
 * scale factor.
 * 
 */
public class GameObject {

	// the list of all GameObjects in the scene tree
	public final static List<GameObject> ALL_OBJECTS = new ArrayList<GameObject>();

	// the root of the scene tree
	public final static GameObject ROOT = new GameObject();

	// the links in the scene tree
	private GameObject myParent;
	private List<GameObject> myChildren;

	// the local transformation
	// myRotation should be normalised to the range [-180..180)
	private double[] myRotation;
	private double[] myScale;
	private double[] myTranslation;

	// is this part of the tree showing?
	private boolean amShowing;

	/**
	 * Special private constructor for creating the root node. Do not use
	 * otherwise.
	 */
	private GameObject() {
		myParent = null;
		myChildren = new ArrayList<GameObject>();

		myRotation = new double[3];
		myRotation[0] = 0;
		myRotation[1] = 0;
		myRotation[2] = 0;
		myScale = new double[3];
		myScale[0] = 1;
		myScale[1] = 1;
		myScale[2] = 1;
		myTranslation = new double[3];
		myTranslation[0] = 0;
		myTranslation[1] = 0;
		myTranslation[2] = 0;

		amShowing = true;

		ALL_OBJECTS.add(this);
	}

	/**
	 * Public constructor for creating GameObjects, connected to a parent
	 * (possibly the ROOT).
	 * 
	 * New objects are created at the same location, Rotation and scale as the
	 * parent.
	 *
	 * @param parent
	 */
	public GameObject(GameObject parent) {
		myParent = parent;
		myChildren = new ArrayList<GameObject>();

		parent.myChildren.add(this);

		myRotation = new double[3];
		myRotation[0] = 0;
		myRotation[1] = 0;
		myRotation[2] = 0;
		myScale = new double[3];
		myScale[0] = 1;
		myScale[1] = 1;
		myScale[2] = 1;
		myTranslation = new double[3];
		myTranslation[0] = 0;
		myTranslation[1] = 0;
		myTranslation[2] = 0;

		// initially showing
		amShowing = true;

		ALL_OBJECTS.add(this);
	}

	/**
	 * Remove an object and all its children from the scene tree.
	 */
	public void destroy() {
		List<GameObject> childrenList = new ArrayList<GameObject>(myChildren);
		for (GameObject child : childrenList) {
			child.destroy();
		}
		if (myParent != null)
			myParent.myChildren.remove(this);
		ALL_OBJECTS.remove(this);
	}

	/**
	 * Get the parent of this game object
	 * 
	 * @return
	 */
	public GameObject getParent() {
		return myParent;
	}

	/**
	 * Get the children of this object
	 * 
	 * @return
	 */
	public List<GameObject> getChildren() {
		return myChildren;
	}

	/**
	 * Get the local rotation (in degrees)
	 * 
	 * @return
	 */
	public double[] getRotation() {
		return myRotation;
	}

	/**
	 * Set the local rotation (in degrees)
	 * 
	 * @return
	 */
	public void setRotation(double[] rotation) {
		myRotation = MathUtil.normaliseAngle(rotation);
	}

	/**
	 * Rotate the object by the given angle (in degrees)
	 * 
	 * @param angle
	 */
	public void rotate(double[] angle) {
		myRotation[0] += angle[0];
		myRotation[1] += angle[1];
		myRotation[2] += angle[2];
		myRotation = MathUtil.normaliseAngle(myRotation);
	}

	/**
	 * Get the local scale
	 * 
	 * @return
	 */
	public double[] getScale() {
		return myScale;
	}

	/**
	 * Set the local scale
	 * 
	 * @param scale
	 */
	public void setScale(double[] scale) {
		myScale = scale;
	}

	/**
	 * Multiply the scale of the object by the given factor
	 * 
	 * @param factor
	 */
	public void scale(double[] factor) {
		myScale[0] *= factor[0];
		myScale[1] *= factor[1];
		myScale[2] *= factor[2];
	}

	/**
	 * Get the local position of the object
	 * 
	 * @return
	 */
	public double[] getPosition() {
		return myTranslation;
	}

	/**
	 * Set the local position of the object
	 * 
	 * @param position
	 */
	public void setPosition(double[] position) {
		myTranslation = position;
	}

	/**
	 * Set the local position of the object
	 * 
	 * @param position
	 */
	public void setPosition(double x, double y, double z) {
		double[] position = new double[] { x, y, z };
		setPosition(position);
	}

	/**
	 * Move the object by the specified offset in local coordinates
	 * 
	 * @param translation
	 */
	public void translate(double[] translation) {
		myTranslation[0] += translation[0];
		myTranslation[1] += translation[1];
		myTranslation[2] += translation[2];
	}

	/**
	 * Test if the object is visible
	 * 
	 * @return
	 */
	public boolean isShowing() {
		return amShowing;
	}

	/**
	 * Set the showing flag to make the object visible (true) or invisible
	 * (false). This flag should also apply to all descendents of this object.
	 * 
	 * @param showing
	 */
	public void show(boolean showing) {
		amShowing = showing;
	}

	/**
	 * Update the object. This method is called once per frame.
	 * 
	 * This does nothing in the base GameObject class. Override this in
	 * subclasses.
	 * 
	 * @param dt
	 *            The amount of time since the last update (in seconds)
	 */
	public void update(double dt) {
		// do nothing
	}

	/**
	 * Draw the object (but not any descendants)
	 * 
	 * This does nothing in the base GameObject class. Override this in
	 * subclasses.
	 * 
	 * @param gl
	 */
	public void drawSelf(GL2 gl) {
		// do nothing
	}

	/**
	 * Draw the object and all of its descendants recursively.
	 * 
	 * Complete this method
	 * 
	 * @param gl
	 */
	public void draw(GL2 gl) {
		double[] p = getPosition();
		double[] r = getRotation();
		double[] s = getScale();

		// don't draw if it is not showing
		if (!amShowing) {
			return;
		} else {
			// setting the model transform appropriately
			gl.glColor3d(0.0, 0.0, 0.0);
			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glPushMatrix();
			{
				gl.glTranslated(p[0], p[1], p[2]);
				gl.glRotated(r[0], 1, 0, 0);
				gl.glRotated(r[1], 0, 1, 0);
				gl.glRotated(r[2], 0, 0, 1);
				gl.glScaled(s[0], s[1], s[2]);

				// draw the object (Call drawSelf() to draw the object itself)
				drawSelf(gl);
				// Draw the object and all of its descendants recursively.
				for (GameObject gameObject : getChildren()) {
					gameObject.draw(gl);
				}
			}
			gl.glPopMatrix();
		}
	}

	/**
	 * Compute the object's position in world coordinates
	 * 
	 * Write this method
	 * 
	 * @return a point in world coordinats in [x,y] form
	 */
	public double[] getGlobalPosition() {
		double[] ini = { 0, 0, 0, 1 };
		double[] pos = new double[3];
		double[][] p;
		double[][][] r;
		double[][] s;
		double[][] t;
		GameObject ct = this;

		while (ct != null) {
			p = MathUtil.translationMatrix(ct.myTranslation);
			r = MathUtil.rotationMatrix(ct.myRotation);
			s = MathUtil.scaleMatrix(ct.myScale);
			t = (MathUtil.multiply(MathUtil.multiply(MathUtil.multiply(MathUtil.multiply(r[1], r[0]), r[2]), p), s));
			ini = MathUtil.multiply(t, ini);

			ct = ct.getParent();
		}
		pos[0] = ini[0];
		pos[1] = ini[1];
		pos[2] = ini[2];

		return pos;
	}

	/**
	 * Compute the object's rotation in the global coordinate frame
	 * 
	 * Write this method
	 * 
	 * @return the global rotation of the object (in degrees) and normalized to
	 *         the range (-180, 180) degrees.
	 */
	public double[] getGlobalRotation() {
		double[] r = new double[3];
		r[0] = r[1] = r[2] = 0;
		GameObject ct = this;

		while (ct != null) {
			r[0] += ct.myRotation[0];
			r[1] += ct.myRotation[1];
			r[2] += ct.myRotation[2];
			ct = ct.getParent();
		}

		return MathUtil.normaliseAngle(r);
	}

	/**
	 * Compute the object's scale in global terms
	 * 
	 * Write this method
	 * 
	 * @return the global scale of the object
	 */
	public double[] getGlobalScale() {
		double[] s = new double[3];
		s[0] = s[1] = s[2] = 1;
		GameObject ct = this;

		while (ct != null) {
			s[0] *= ct.myScale[0];
			s[1] *= ct.myScale[1];
			s[2] *= ct.myScale[2];
			ct = ct.getParent();
		}
		return s;
	}
}
