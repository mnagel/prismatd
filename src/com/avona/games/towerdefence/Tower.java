package com.avona.games.towerdefence;

import java.util.List;

import com.avona.games.towerdefence.enemySelection.EnemySelectionPolicy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class Tower extends LocationObject {
	private static final long serialVersionUID = 1L;
	
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
		

		
		if (cnt % 3 == 0) {
			colors = 0;
		}
		else if (cnt % 3 == 1) {
			colors = 1;
		}
		else {
			colors = 2;
		}		cnt++;
	}
	
	public int colors;
	public static int cnt = 0;

	public Tower copy() {
		return new Tower(this);
	}

	public Particle shootTowards(Enemy e) {
		if (timer.ready) {
			timer.rearm();
			Particle p = new Particle(level, location, e,
					enemyParticleCollidorPolicy);
			
			if (this.colors == 0) {
				p.strengthR *= 3;
				p.strengthG = p.strengthB = 0;
			}
			else if (this.colors == 1) {
				p.strengthG *= 3;
				p.strengthR = p.strengthB = 0;
			} 
			else {
				p.strengthB *= 3;
				p.strengthR = p.strengthG = 0;
			}
			
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
