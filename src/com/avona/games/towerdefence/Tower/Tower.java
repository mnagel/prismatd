package com.avona.games.towerdefence.Tower;

import java.util.List;

import com.avona.games.towerdefence.LocationObject;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.RechargeTimer;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.Enemy.Enemy;
import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public abstract class Tower extends LocationObject {
	private static final long serialVersionUID = 1L;

	public float range;
	protected RechargeTimer timer;
	public EnemySelectionPolicy enemySelectionPolicy;
	public ParticleCollidorPolicy enemyParticleCollidorPolicy;
	public int price;
	public int level;

	public RGB strength;

	// FIXME one ctor should call the other
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

	// // FIXME will fail
	// public Tower copy() {
	// return new EmeraldPrisma(this);
	// }

	public abstract Particle makeParticle(Enemy e);

	public Particle shootTowards(Enemy e) {
		if (timer.ready) {
			timer.rearm();
			return makeParticle(e);
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
