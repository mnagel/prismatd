package com.avona.games.towerdefence;

import java.util.List;

public class Tower extends LocationObject {
	public float range;
	protected RechargeTimer timer;
	public EnemySelectionPolicy enemySelectionPolicy;
	public ParticleCollidorPolicy enemyParticleCollidorPolicy;
	public int price;
	public int level;

	public Tower(TimedCodeManager timedCodeManager,
			EnemySelectionPolicy enemySelectionPolicy,
			ParticleCollidorPolicy enemyParticleCollidorPolicy, int level) {
		super();
		this.enemySelectionPolicy = enemySelectionPolicy;
		this.enemyParticleCollidorPolicy = enemyParticleCollidorPolicy;
		this.level = level;
		timer = new RechargeTimer(timedCodeManager, 0.3f);
		range = 75 + 2 * (level - 1);
		price = 10 + 2 * (level - 1);
		radius = 16;
	}

	public Tower(final Tower t) {
		super(t);
		enemySelectionPolicy = t.enemySelectionPolicy;
		enemyParticleCollidorPolicy = t.enemyParticleCollidorPolicy;
		timer = t.timer.copy();
		level = t.level;
		range = t.range;
		price = t.price;
		radius = t.radius;
	}

	public Tower copy() {
		return new Tower(this);
	}

	public Particle shootTowards(Enemy e) {
		if (timer.ready) {
			timer.rearm();
			Particle p = new Particle(level, location, e,
					enemyParticleCollidorPolicy);
			return p;
		} else {
			return null;
		}
	}

	@Override
	public void step(float dt) {
	}

	public Enemy findSuitableEnemy(List<Enemy> enemies) {
		return enemySelectionPolicy.findSuitableEnemy(this, enemies);
	}
}
