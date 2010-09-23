package com.avona.games.towerdefence;

import java.util.List;

public class Tower extends LocationObject {
	public float range;
	protected RechargeTimer timer;
	public EnemySelectionPolicy enemySelectionPolicy;
	public EnemyParticleCollidor enemyParticleCollidor;
	public int price;
	public int level;

	public Tower(TimedCodeManager timedCodeManager,
			EnemySelectionPolicy enemySelectionPolicy,
			EnemyParticleCollidor enemyParticleCollidor, int level) {
		super();
		this.enemySelectionPolicy = enemySelectionPolicy;
		this.enemyParticleCollidor = enemyParticleCollidor;
		this.level = level;
		timer = new RechargeTimer(timedCodeManager, 0.3f);
		range = 75 + 2 * (level - 1);
		price = 10 + 2 * (level - 1);
		radius = 16;
	}

	public Tower(final Tower t) {
		super(t);
		enemySelectionPolicy = t.enemySelectionPolicy;
		enemyParticleCollidor = t.enemyParticleCollidor;
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
			Particle p = new Particle(level, location, e);
			enemyParticleCollidor.registerEnemyVsParticle(e, p);
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
