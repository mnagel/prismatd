package com.avona.games.towerdefence;


public abstract class MovingObject extends StationaryObject {
	protected VelocityVector velocity = new VelocityVector();
	protected World world;
	
	public VelocityVector getVelocity() {
		return velocity;
	}

	public void setVelocity(VelocityVector velocity) {
		this.velocity = velocity;
	}
	
	public abstract void step(long dt);
}
