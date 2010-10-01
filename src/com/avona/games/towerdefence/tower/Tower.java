package com.avona.games.towerdefence.tower;

import java.util.List;

import com.avona.games.towerdefence.LocationObject;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.RechargeTimer;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public abstract class Tower extends LocationObject {
	private static final long serialVersionUID = 1L;

	public EnemySelectionPolicy enemySelectionPolicy;
	public ParticleCollidorPolicy enemyParticleCollidorPolicy;
	public int level;
	protected RechargeTimer timer;
	public float range;
	public int price;
	public RGB strength;

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
		level = t.level;
		timer = t.timer.copy();
		range = t.range;
		price = t.price;
		strength = t.strength;
	}

	public abstract Tower copy();

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
