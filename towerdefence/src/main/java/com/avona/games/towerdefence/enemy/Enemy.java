package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.MovingObject;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemyEventListeners.EnemyEventListener;
import com.avona.games.towerdefence.mission.Mission;

import java.util.LinkedList;
import java.util.List;

public abstract class Enemy extends MovingObject {
	private static final long serialVersionUID = 1L;

	public Mission mission;
	public int levelNum;
	public int waypointId = 1;
	public boolean escaped = false;
	public int worth;
	public List<EnemyEventListener> eventListeners = new LinkedList<>();
	public V2 target;
	public RGB life;
	public RGB maxLife;

	public Enemy(Mission mission, int levelNum, int worth, RGB maxLife, int speed, float radius) {
		super();
		this.mission = mission;
		this.levelNum = levelNum;
		this.worth = worth;
		this.maxLife = maxLife;
		this.getVelocity().setLength(speed);
		this.radius = radius;
		life = maxLife.clone();
	}

	public Enemy(Enemy other) {
		super(other);
		mission = other.mission;
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

	public void setWPID(int waypointId) {
		this.waypointId = waypointId;
		if (waypointId < mission.waypoints.length) {
			target = mission.waypoints[waypointId].center;
			getVelocity().setDirection(location, target);
		} else {
			escape();
		}
	}

	@Override
	public void step(float dt) {
		if (isDead())
			return;

		location.addWeighted(getVelocity(), dt);
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
