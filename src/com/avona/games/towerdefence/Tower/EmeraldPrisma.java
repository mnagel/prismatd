package com.avona.games.towerdefence.Tower;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.RechargeTimer;
import com.avona.games.towerdefence.TimedCodeManager;
import com.avona.games.towerdefence.Enemy.Enemy;
import com.avona.games.towerdefence.Particle.EmeraldParticle;
import com.avona.games.towerdefence.Particle.Particle;
import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class EmeraldPrisma extends Tower {

	private static final long serialVersionUID = -3211451538363567663L;

	@Override
	public Particle makeParticle(Enemy e) {
		return new EmeraldParticle(level, location, e, this.enemyParticleCollidorPolicy);
	}
	
	
	// FIXME dont copy and paste base class...
	public EmeraldPrisma(TimedCodeManager timedCodeManager,
			EnemySelectionPolicy enemySelectionPolicy,
			ParticleCollidorPolicy enemyParticleCollidorPolicy, int level) {
		super(timedCodeManager, enemySelectionPolicy, enemyParticleCollidorPolicy, level);
		this.enemySelectionPolicy = enemySelectionPolicy;
		this.enemyParticleCollidorPolicy = enemyParticleCollidorPolicy;
		this.level = level;
		timer = new RechargeTimer(timedCodeManager, 0.3f);
		range = 75 + 2 * (level - 1);
		price = 10 + 2 * (level - 1);
		radius = 16;
		
		this.strength = new RGB(0, level * 10 + 10, 0);
	}

	// FIXME dont copy and paste from base class
	public EmeraldPrisma(final Tower t) {
		super(t);
		enemySelectionPolicy = t.enemySelectionPolicy;
		enemyParticleCollidorPolicy = t.enemyParticleCollidorPolicy;
		timer = t.timer.copy();
		level = t.level;
		range = t.range;
		price = t.price;
		radius = t.radius;
		
		this.strength = new RGB(0, level * 10 + 10, 0);
	}

	public EmeraldPrisma copy() {
		return new EmeraldPrisma(this);
	}
	
	

}
