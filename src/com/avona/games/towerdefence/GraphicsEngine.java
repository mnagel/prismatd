package com.avona.games.towerdefence;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.Point2d;

public class GraphicsEngine {
	
	protected Game g;
	
	public GraphicsEngine(Game g) {
		this.g = g;
	}
	
	public void render(GLAutoDrawable glDrawable) {
		renderMouse(glDrawable);
	}
	
	public void renderMouse(GLAutoDrawable glDrawable) {
		final GL gl = glDrawable.getGL();
		gl.glColor3d(1.0, 0.0, 1.0);
		
		Point2d p = g.mouse.location;
		final double width = 0.04;
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(p.x - width/2, p.y - width/2);
		gl.glVertex2d(p.x + width/2, p.y - width/2);
		gl.glVertex2d(p.x + width/2, p.y + width/2);
		gl.glVertex2d(p.x - width/2, p.y + width/2);
		gl.glEnd();
	}
}
