package ass2;

import com.jogamp.opengl.GL2;

public class Cylinder extends GameObject {

	private double radius = 0.2;
	private double height = 2;
	private int slices = 16;
	private MyTexture texture; 
	
	public Cylinder(GameObject parent) {
		super(parent);
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public void setTexture(MyTexture trunk){
		texture = trunk;
	}
	
	public void drawSelf(GL2 gl) {
		gl.glPushMatrix();
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT); 
	    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId());
		
		float[] ambient = {0.4f, 0.4f, 0.2f, 1.0f};
	    float[] diffuse = {0.4f, 0.4f, 0.2f, 1.0f};
	    float[] specular = {0.0f, 0.1f, 0.1f, 1.0f};
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
    	//gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
    	//gl.glColor3f(1,0,0);
		gl.glRotated(-90,1,0,0);
    	double z1 = 0;
    	double z2 = height;
    	
//    	//Front circle
//    	gl.glBegin(GL2.GL_TRIANGLE_FAN);{
//    	
//    		 gl.glNormal3d(0,0,1);
//    		 gl.glVertex3d(0,0,z1);
//    		 double angleStep = 2*Math.PI/slices;
//             for (int i = 0; i <= slices ; i++){
//                 double a0 = i * angleStep;
//                 double x0 = radius*Math.cos(a0);
//                 double y0 = radius*Math.sin(a0);
//
//                gl.glVertex3d(x0,y0,z1);
//              
//             }                
//    	}gl.glEnd();
//    	
//    	//Back circle
//    	gl.glBegin(GL2.GL_TRIANGLE_FAN);{
//       
//   		 gl.glNormal3d(0,0,-1);
//   		 gl.glVertex3d(0,0,z2);
//   		 double angleStep = 2*Math.PI/slices;
//            for (int i = 0; i <= slices ; i++){
//            	double a0 = 2*Math.PI - i * angleStep;
//                            
//                double x0 = radius*Math.cos(a0);
//                double y0 = radius*Math.sin(a0);
//
//                gl.glVertex3d(x0,y0,z2);
//            }
//                
//                
//    	}gl.glEnd();
    	  
    	//Sides of the cylinder
    	gl.glBegin(GL2.GL_QUADS);
        {
            double angleStep = 2*Math.PI/slices;
            for (int i = 0; i <= slices ; i++){
                double a0 = i * angleStep;
                double a1 = ((i+1) % slices) * angleStep;
                
                //Calculate vertices for the quad
                double x0 = radius*Math.cos(a0);
                double y0 = radius*Math.sin(a0);

                double x1 = radius*Math.cos(a1);
                double y1 = radius*Math.sin(a1);
                
                double s1 = (1/(double)slices)*i;
                double s2 = (1/(double)slices)*(1+i);
                //Calculation for face normal for each quad
                //                     (x0,y0,z2)
                //                     ^
                //                     |  u = (0,0,z2-z1) 
                //                     |
                //                     | 
                //(x1,y1,z1)<--------(x0,y0,z1)
                //v = (x1-x0,y1-y0,0)  
                //                     
                //                     
                //                       
                //                    
                //
                // u x v gives us the un-normalised normal
                // u = (0,     0,   z2-z1)
                // v = (x1-x0,y1-y0,0) 
                
                
                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                gl.glNormal3d(x0, y0, 0);
                gl.glTexCoord2d(s1,1);                     
                gl.glVertex3d(x0, y0, z1);
                gl.glTexCoord2d(s1,0);
                gl.glVertex3d(x0, y0, z2);  
                
                //If we want it to be smooth like a cylinder
                //use different normals for each different x and y
                gl.glNormal3d(x1, y1, 0);
                gl.glTexCoord2d(s2,0);
                gl.glVertex3d(x1, y1, z2);
                gl.glTexCoord2d(s2,1);
                gl.glVertex3d(x1, y1, z1);               
                         
            }

        }
        gl.glEnd();
        gl.glPopMatrix();
	    gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    }


}
