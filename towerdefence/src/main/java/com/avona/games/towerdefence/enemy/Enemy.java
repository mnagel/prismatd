package com.avona.games.towerdefence.enemy;

import java.util.LinkedList;
import java.util.List;

import com.avona.games.towerdefence.MovingObject;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemyEventListeners.EnemyEventListener;
import com.avona.games.towerdefence.level.Level;

public abstract class Enemy extends MovingObject {
	private static final long serialVersionUID = 1L;

	public Level level;
	public int levelNum;
	public int waypointId = 1;
	public boolean escaped = false;
	public int worth;
	public List<EnemyEventListener> eventListeners = new LinkedList<EnemyEventListener>();
	public V2 target;
	public RGB life;
	public RGB maxLife;

	public Enemy(Level level, int levelNum, int worth, RGB maxLife, int speed,
			float radius) {
		super();
		this.level = level;
		this.levelNum = levelNum;
		this.worth = worth;
		this.maxLife = maxLife;
		this.velocity.setLength(speed);
		this.radius = radius;
		life = maxLife.clone();
	}

	public Enemy(Enemy other) {
		super(other);
		level = other.level;
		levelNum = other.levelNum;
		waypointId = other.waypointId;
		escaped = other.escaped;
		worth = other.worth;
		target = other.target;
		life = other.life;
		maxLife = other.maxLife;
	}

	public void setInitialLocation(V2 location) {
		this.location = location.clone();
		setWPID(1);
	}

	@Override
	public abstract Enemy clone();

	public void setWPID(int i) {
		if (waypointId + 1 < level.waypoints.length) {
			waypointId = i;
			target = level.waypoints[waypointId];
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
		return life.R <= 0 && life.G <= 0 && life.B <= 0;
	}

	public void inflictDamage(RGB dmg) {
		life.subUpto(dmg, 0.0f);

		if (isDead()) {
			die();
		}
	}

	/**
	 * Remove all/any life from the enemy.
	 */
	public void kill() {
		life.R = 0;
		life.G = 0;
		life.B = 0;
		die();
	}

	private void die() {
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

	public String toString() {
		String s = String.format("%s at %s with %s/%s",
				this.getClass().getSimpleName(),
				this.location.toString(2),
				this.life,
				this.maxLife
		);
		return s;
	}
}
