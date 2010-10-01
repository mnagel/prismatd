package com.avona.games.towerdefence.enemy;

import java.util.LinkedList;
import java.util.List;

import com.avona.games.towerdefence.MovingObject;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemyEventListeners.EnemyEventListener;
import com.avona.games.towerdefence.world.World;

public abstract class Enemy extends MovingObject {
	private static final long serialVersionUID = 1L;

	public World world;
	public int level;
	public int waypointId = 1;
	public boolean escaped = false;
	public int worth;
	public List<EnemyEventListener> eventListeners = new LinkedList<EnemyEventListener>();
	public V2 target;
	public RGB life;
	public RGB maxLife;

	public Enemy(World world, int level) {
		super();
		this.world = world;
		this.level = level;
		this.worth = 3 + (level - 1);
		this.maxLife = this.getMaxLife();
		this.life = new RGB(this.maxLife);
	}

	public Enemy(Enemy other) {
		super(other);
		world = other.world;
		level = other.level;
		waypointId = other.waypointId;
		escaped = other.escaped;
		worth = other.worth;
		target = other.target;
		life = other.life;
		maxLife = other.maxLife;
	}

	public void setInitialLocation(V2 location) {
		this.location = location;
		this.velocity.setLength(80 + 3 * (level - 1));
		setWPID(1);
	}

	public abstract Enemy copy();

	public abstract RGB getMaxLife();

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
		return life.R <= 0 && life.G <= 0 && life.B <= 0;
	}

	public void inflictDamage(RGB dmg) {
		life.sub(dmg, 0.0f);

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
