package com.avona.games.towerdefence;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

public class Enemy extends MovingObject {
	protected World world;
	protected int health = 100;

	public Enemy(World world) {
		this.world = world;
		location = world.getInitialLocation();
		velocity = world.getDirection(location);
		System.out.println(velocity);
		System.out.println(location);
	}
	
	@Override
	public void step(double dt) {
		if(isDead())
			return;
		
		velocity.translate(location, dt);
		//System.out.println(velocity);
		//System.out.println(location);
	}

	public boolean isDead() {
		return health <= 0;
	}
	
	public void inflictDamage(int damage) {
		health -= damage;
		System.out.println(health);
	}

	public void display(GLAutoDrawable glDrawable) {
		if(isDead())
			return;

		final double width = 0.04;
		
		final GL gl = glDrawable.getGL();
		gl.glColor3d(0.0, 0.0, 1.0);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(location.x - width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y + width/2);
		gl.glVertex2d(location.x - width/2, location.y + width/2);
		gl.glEnd();
	}
}
