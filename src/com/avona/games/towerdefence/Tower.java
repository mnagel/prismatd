package com.avona.games.towerdefence;

import java.util.List;

public class Tower extends LocationObject {
	public float range;
	protected RechargeTimer timer;
	public EnemySelectionPolicy enemySelectionPolicy = new NearestEnemyPolicy();
	public int price;
	public int level;

	public Tower(int level) {
		super();
		this.level = level;
		timer = new RechargeTimer(0.3);
		range = 75 + 2 * level;
		price = 100 + 2 * level;
		radius = 16;
	}

	public Tower(final Tower t) {
		super(t);
		timer = new RechargeTimer(t.timer);
		level = t.level;
		range = t.range;
		price = t.price;
		radius = t.radius;
	}

	public Tower copy() {
		return new Tower(this);
	}

	public Particle shootTowards(Enemy e) {
		if (timer.isReady()) {
			timer.rearm();
			return new Particle(level, location, e);
		} else {
			return null;
		}
	}

	@Override
	public void step(float dt) {
		timer.step(dt);
	}

	public Enemy findSuitableEnemy(List<Enemy> enemies) {
		return enemySelectionPolicy.findSuitableEnemy(this, enemies);
	}
}
