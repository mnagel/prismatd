package com.avona.games.towerdefence;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.Point2d;



public class Tower extends StationaryObject {
	protected double range = 0.8;
	protected RechargeTimer timer = new RechargeTimer(4.0);

	public Tower(Point2d location) {
		this.location = location;
	}
	
	public boolean inRange(Enemy e) {
		return location.distance(e.location) < range;
	}

	public Particle shootTowards(Enemy e, final double dt) {
		if(timer.isReady()) {
			timer.rearm();
			return new Particle(location, e);
		} else {
			return null;
		}
	}

	public void display(GLAutoDrawable glDrawable) {
		final double width = 0.03;
		
		final GL gl = glDrawable.getGL();
		gl.glColor3d(1.0, 0.0, 0.0);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(location.x - width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y + width/2);
		gl.glVertex2d(location.x - width/2, location.y + width/2);
		gl.glEnd();
	}

	@Override
	public void step(double dt) {
		timer.step(dt);
	}
}
