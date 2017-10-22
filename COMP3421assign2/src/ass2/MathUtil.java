package ass2;

/**
 * A collection of useful math methods
 *
 * TODO: The methods you need to complete are at the bottom of the class
 *
 * @author malcolmr
 */
public class MathUtil {

	/**
	 * Normalise an angle to the range [-180, 180)
	 * 
	 * @param angle
	 * @return
	 */
	static public double[] normaliseAngle(double[] old) {
		double[] angle = new double[3];
		angle[0] = ((old[0] + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
		angle[1] = ((old[1] + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
		angle[2] = ((old[2] + 180.0) % 360.0 + 360.0) % 360.0 - 180.0;
		return angle;
	}

	/**
	 * Clamp a value to the given range
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */

	public static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	/**
	 * Multiply two matrices
	 * 
	 * @param p
	 *            A 4x4 matrix
	 * @param q
	 *            A 4x4 matrix
	 * @return
	 */
	public static double[][] multiply(double[][] p, double[][] q) {

		double[][] m = new double[4][4];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				m[i][j] = 0;
				for (int k = 0; k < 4; k++) {
					m[i][j] += p[i][k] * q[k][j];
				}
			}
		}

		return m;
	}

	/**
	 * Multiply a vector by a matrix
	 * 
	 * @param m
	 *            A 4x4 matrix
	 * @param v
	 *            A 4x1 vector
	 * @return
	 */
	public static double[] multiply(double[][] m, double[] v) {

		double[] u = new double[4];

		for (int i = 0; i < 4; i++) {
			u[i] = 0;
			for (int j = 0; j < 4; j++) {
				u[i] += m[i][j] * v[j];
			}
		}

		return u;
	}

	/**
	 * A 3D translation matrix for the given offset vector
	 * 
	 * @param pos
	 * @return
	 */
	public static double[][] translationMatrix(double[] v) {

		double[][] m = new double[4][4];

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i == j) {
					m[i][j] = 1;
				} else {
					m[i][j] = 0;
				}
			}
		}

		m[0][3] += v[0];
		m[1][3] += v[1];
		m[2][3] += v[2];

		return m;
	}

	/**
	 * A 3D rotation matrix for the given angle
	 * 
	 * @param angle
	 *            in degrees
	 * @return
	 */
	public static double[][][] rotationMatrix(double[] angle) {

		double[] r = new double[3];
		r[0] = Math.toRadians(angle[0]);
		r[1] = Math.toRadians(angle[1]);
		r[2] = Math.toRadians(angle[2]);

		double[][][] m = new double[3][4][4];

		m[0] = new double[][] { { 1, 0, 0, 0 }, { 0, Math.cos(r[0]), -Math.sin(r[0]), 0 },
				{ 0, Math.sin(r[0]), Math.cos(r[0]), 0 }, { 0, 0, 0, 1 } };
		m[1] = new double[][] { { Math.cos(r[1]), 0, Math.sin(r[0]), 0 }, { 0, 1, 0, 0 },
				{ -Math.sin(r[0]), 0, Math.cos(r[0]), 0 }, { 0, 0, 0, 1 } };
		m[2] = new double[][] { { Math.cos(r[0]), -Math.sin(r[0]), 0, 0 }, { Math.sin(r[0]), Math.cos(r[0]), 0, 0 },
				{ 0, 0, 1, 0 }, { 0, 0, 0, 1 } };

		return m;
	}

	/**
	 * A 3D scale matrix that scales both axes by the same factor
	 * 
	 * @param scale
	 * @return
	 */
	public static double[][] scaleMatrix(double[] scale) {

		double[] r = scale;

		double[][] m = { { r[0], 0, 0, 0 }, { 0, r[1], 0, 0 }, { 0, 0, r[2], 0 }, { 0, 0, 0, 1 } };

		return m;

	}

	public static double[] getNormal(double[] p0, double[] p1, double[] p2) {
		double u[] = { p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2] };
		double v[] = { p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2] };

		return crossProduct(u, v);
	}

	public static double[] getNormal(double[] p0, double[] p1, double[] p2, double[] p3) {
		double u[] = { -2, -(p1[1] - p0[1]), 0 };
		double v[] = { 0, (p3[1] - p2[1]), 2 };

		return crossProduct(u, v);
	}

	public static double[] crossProduct(double u[], double v[]) {
		double crossProduct[] = new double[3];
		crossProduct[0] = u[1] * v[2] - u[2] * v[1];
		crossProduct[1] = u[2] * v[0] - u[0] * v[2];
		crossProduct[2] = u[0] * v[1] - u[1] * v[0];

		return crossProduct;
	}

}
