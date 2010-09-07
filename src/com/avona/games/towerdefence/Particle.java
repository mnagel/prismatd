package com.avona.games.towerdefence;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

public class Particle extends MovingObject {
	protected int strength = 10;
	protected double range = 0.001;
	protected double timeAlive = 4.0 / TimeBase.BASE;
	
	protected boolean dead = false;
	protected Enemy target;
	protected double counter = 0.0;
	
	public Particle(Point2d location, Enemy target) {
		this.location = new Point2d(location);
		this.target = target;
		
		velocity.speed = 0.1;
		
		recalculateTargetVector();
		Util.dumpPoint(String.format("Particle creation (dx=%f, dy=%f):", velocity.vector.x, velocity.vector.y), location);
	}
	
	public void recalculateTargetVector() {
		Vector2d vec = new Vector2d(target.location);
		vec.sub(location);
		this.velocity.setVector(vec);
	}
	
	public boolean inRange(Enemy e) {
		return location.distance(e.location) < range;
	}

	/**
	 * Attack the enemy using a static damage value.  The particle destroys
	 * itself after destroying an enemy.  Further attack calls are then
	 * ignored.
	 * @param e Enemy to attack
	 */
	public void attack(Enemy e) {
		if(isDead()) {
			return;
		}
		
		e.inflictDamage(strength);
		if(e.isDead()) {
			dead = true;
		}
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void step(long dt) {
		if(target.isDead())
			dead = true;
		
		if(isDead())
			return;
		
		/*counter += dt;
		if(counter >= timeAlive) {
			System.exit(0);
			dead = true;
			return;
		}*/
		
		recalculateTargetVector();
		velocity.translate(location, TimeBase.fractionOfSecond(dt));
	}

	@Override
	public void display(GLAutoDrawable glDrawable) {
		if(dead)
			return;
		
		final double width = 0.02;
		
		final GL gl = glDrawable.getGL();
		gl.glColor3d(1.0, 1.0, 1.0);
		
		gl.glBegin(GL.GL_QUADS);
		gl.glVertex2d(location.x - width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y - width/2);
		gl.glVertex2d(location.x + width/2, location.y + width/2);
		gl.glVertex2d(location.x - width/2, location.y + width/2);
		gl.glEnd();
	}
}
