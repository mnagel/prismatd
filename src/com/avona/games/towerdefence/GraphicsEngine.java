package com.avona.games.towerdefence;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.Point2d;

/**
 * The GraphicsEngine object currently incorporates all drawing operations.
 * It will iterate over all in-game objects and call (possibly overloaded)
 * class methods to perform the GL calls.  It will not touch any in-game
 * state, though.
 */
public class GraphicsEngine {
	protected Game game;
	
	public GraphicsEngine(Game game) {
		this.game = game;
	}
	
	public void render(GLAutoDrawable glDrawable) {
		for(Enemy e : game.enemies) {
			renderEnemy(glDrawable, e);
		}
		for(Tower t : game.towers) {
			renderTower(glDrawable, t);
		}
		for(Particle p : game.particles) {
			renderParticle(glDrawable, p);
		}
		renderMouse(glDrawable);
	}
	
	public static void renderEnemy(final GLAutoDrawable glDrawable, final Enemy e) {
		if(e.isDead())
			return;

		final double width = 0.04;
		final Point2d location = e.location;

		final GL gl = glDrawable.getGL();
		gl.glColor3d(0.0, 0.0, 1.0);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(location.x - width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y + width/2);
		gl.glVertex2d(location.x - width/2, location.y + width/2);
		gl.glEnd();
	}
	
	public static void renderTower(final GLAutoDrawable glDrawable, final Tower t) {
		final double width = 0.03;
		final Point2d location = t.location;
		
		final GL gl = glDrawable.getGL();
		gl.glColor3d(1.0, 0.0, 0.0);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(location.x - width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y + width/2);
		gl.glVertex2d(location.x - width/2, location.y + width/2);
		gl.glEnd();
	}
	
	public static void renderParticle(final GLAutoDrawable glDrawable, final Particle p) {
		if(p.isDead())
			return;

		final double width = 0.02;
		final Point2d location = p.location;
		
		final GL gl = glDrawable.getGL();
		gl.glColor3d(1.0, 1.0, 1.0);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(location.x - width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y + width/2);
		gl.glVertex2d(location.x - width/2, location.y + width/2);
		gl.glEnd();
	}
	
	public void renderMouse(GLAutoDrawable glDrawable) {
		final GL gl = glDrawable.getGL();
		gl.glColor3d(1.0, 0.0, 1.0);
		
		final Point2d p = game.mouse.location;
		final double width = 0.04;
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(p.x - width/2, p.y - width/2);
		gl.glVertex2d(p.x + width/2, p.y - width/2);
		gl.glVertex2d(p.x + width/2, p.y + width/2);
		gl.glVertex2d(p.x - width/2, p.y + width/2);
		gl.glEnd();
	}
}
