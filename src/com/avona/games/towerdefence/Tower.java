package com.avona.games.towerdefence;

import java.util.List;

public class Tower extends StationaryObject {
	public float range = 75;
	protected RechargeTimer timer = new RechargeTimer(0.3);
	public boolean showRange = false;
	public EnemySelectionPolicy enemySelectionPolicy = new NearestEnemyPolicy();
	public int price = 100;

	public Tower() {
		super();
		radius = 16;
	}

	public Tower(final Tower t) {
		super(t);
		range = t.range;
		price = t.price;
		timer = new RechargeTimer(t.timer);
	}

	public Particle shootTowards(Enemy e) {
		if (timer.isReady()) {
			timer.rearm();
			return new Particle(location, e);
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
