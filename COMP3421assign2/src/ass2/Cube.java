package ass2;

import com.jogamp.opengl.GL2;

public class Cube extends GameObject {

	private static double vertices[] = 
		{
			1.0, -1.0, 1.0, 
			1.0, 1.0, 1.0, 
			1.0, 1.0, -1.0, 
			1.0, -1.0, -1.0, 
			-1.0, -1.0, 1.0, 
			-1.0, 1.0, 1.0, 
			-1.0, 1.0, -1.0, 
			-1.0, -1.0, -1.0
		};

		// Vertex indices of each box side, 6 groups of 4.
		private static int quadIndices[] = 
		{
		    3, 2, 1, 0, //right face
		    7, 6, 2, 3, //back face
		    4, 5, 6, 7, //left face
			0, 1, 5, 4, //front face
			4, 7, 3, 0, //bottom face
			6, 5, 1, 2  //top face
		};

		
		//Face normals = normalized unit vector pointing in direction of face
		static double faceNormals[] = {
			 1, 0, 0,  //right face
			 0, 0,-1,  //back face
			-1, 0, 0,  //left face
			 0, 0, 1,  //front face
			 0,-1, 0,  //bottom face
		     0, 1, 0   //top face
		};
	
	public Cube(GameObject parent) {
		super(parent);
	}
	
	public void drawSelf(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(0, -0.7, 0);
	    
	    gl.glBegin(GL2.GL_QUADS);{
         	//Draw 6 faces of box (each with 4 vertices)
         	for(int i=0; i< 24; i++){
         		int index = quadIndices[i];  
         		//The same normal is used for all 4 vertices
         		//in a face
         		gl.glNormal3dv(faceNormals,(i/4)*3);
         		gl.glVertex3dv(vertices,index*3);
         	}
         }gl.glEnd();
	    
	    gl.glPopMatrix();
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		
	}
}
