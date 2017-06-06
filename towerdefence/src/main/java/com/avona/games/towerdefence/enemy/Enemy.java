package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.eventListeners.EnemyEventListener;
import com.avona.games.towerdefence.engine.MovingObject;
import com.avona.games.towerdefence.mission.GridCell;

import java.util.LinkedList;
import java.util.List;

public abstract class Enemy extends MovingObject {
	private static final float ENEMY_RADIUS = GridCell.size / 2;

	public int level;

	public V2 target;
	public int waypointId = 1;
	public boolean escaped = false;

	public RGB life;
	public RGB maxLife;
	public int worth;

	public List<EnemyEventListener> eventListeners = new LinkedList<>();

	public Enemy(int level, int worth, RGB maxLife, float speed) {
		super(null, ENEMY_RADIUS);
		this.level = level;
		this.worth = worth;
		this.maxLife = maxLife;
		this.getVelocity().setLength(speed);
		life = maxLife.clone2();
	}

	public Enemy(Enemy other) {
		super(other);
		level = other.level;
		waypointId = other.waypointId;
		escaped = other.escaped;
		worth = other.worth;
		target = other.target == null ? null : other.target.clone2();
		life = other.life == null ? null : other.life.clone2();
		maxLife = other.maxLife == null ? null : other.maxLife.clone2();
	}

	public void setInitialLocation(V2 location, V2 target) {
		this.location = location.clone2();
		setTargetWaypoint(1, target);
	}

	public abstract Enemy clone2();

	public void setTargetWaypoint(int waypointId, V2 waypoint) {
		this.waypointId = waypointId;
		this.target = waypoint;
		getVelocity().setDirection(location, waypoint);
	}

	@Override
	public void step(float dt) {
		if (isDead()) {
			return;
		}

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
				this.location == null ? "null" : this.location.toString(2),
				this.life,
				this.maxLife
		);
		return s;
	}
}
