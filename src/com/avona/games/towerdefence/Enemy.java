package com.avona.games.towerdefence;

import java.util.LinkedList;
import java.util.List;

import com.avona.games.towerdefence.enemyEventListeners.EnemyEventListener;

public class Enemy extends MovingObject {
	private static final long serialVersionUID = 1L;

	public V2 target;
	// public int health;
	// public int maxHealth;

	public int lifeR;
	public int lifeG;
	public int lifeB;

	public int lifeMaxR;
	public int lifeMaxG;
	public int lifeMaxB;

	public int level;
	public int waypointId = 1;
	public boolean escaped = false;
	public int worth;
	public List<EnemyEventListener> eventListeners = new LinkedList<EnemyEventListener>();

	public Enemy(World world, V2 location, int level) {
		this.level = level;
		this.lifeMaxR = this.lifeMaxG = this.lifeMaxB = 50 + 20 * (level - 1);
		this.lifeR = this.lifeG = this.lifeB = this.lifeMaxR;
		this.worth = 3 + (level - 1);
		this.world = world;
		this.location = location;
		this.velocity.setLength(80 + 3 * (level - 1));
		setWPID(1);
	}

	public void setWPID(int i) {
		if (waypointId + 1 < world.waypoints.size()) {
			waypointId = i;
			target = world.waypoints.get(waypointId);
			velocity.setDirection(location, target);
		} else {
			escape();
		}
	}

	@Override
	public void step(float dt) {
		if (isDead())
			return;

		location.addWeighted(velocity, dt);
	}

	public boolean isDead() {
		return lifeR <= 0 && lifeG <= 0 && lifeB <= 0;
	}

	public void inflictDamage(int damageR, int damageG, int damageB) {
		lifeR -= damageR;
		lifeG -= damageG;
		lifeB -= damageB;

		lifeR = Math.max(lifeR, 0);
		lifeG = Math.max(lifeG, 0);
		lifeB = Math.max(lifeB, 0);

		if (isDead()) {
			die();
		}
	}

	public void die() {
		for (EnemyEventListener l : eventListeners) {
			l.onDeathEvent(this);
		}
	}

	public void escape() {
		escaped = true;
		for (EnemyEventListener l : eventListeners) {
			l.onEscapeEvent(this);
		}
	}
}
