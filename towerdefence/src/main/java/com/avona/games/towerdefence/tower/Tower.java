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

	// The color the tower will be drawn with.
	public RGB color;

	public Tower(TimedCodeManager timedCodeManager,
			ParticleCollidorPolicy enemyParticleCollidorPolicy, int level) {
		super();
		this.enemySelectionPolicy = getPolicyForLevel(level);
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
		timer = t.timer.clone();
		range = t.range;
		price = t.price;
		color = t.color;
	}

	abstract public String getName();

	@Override
	public abstract Tower clone();

	public abstract Particle makeParticle(Enemy e);

	public void setLevel(int level) {
		this.level = level;
		this.enemySelectionPolicy = getPolicyForLevel(level);
	}

	protected abstract EnemySelectionPolicy getPolicyForLevel(int level);

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
