package com.avona.games.towerdefence.particle;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particleCollidors.ParticleColliderPolicy;

/**
 * PaintballParticle has a color that it applies to the target enemy as attack damage.
 * It then measures how much damage it inflicted and redistributes the damage according
 * to mask back as colored health onto the target enemy. This can be used to convert
 * damage in one color as health in another, effectively repainting the enemy.
 */

public class PaintballParticle extends Particle {
	public RGB mask;

	public PaintballParticle(V2 location, Enemy target, ParticleColliderPolicy colliderPolicy, int velocity, RGB strength, RGB mask) {
		super(location, target, colliderPolicy, velocity, strength);
		this.mask = mask;
	}

	@Override
	public void attack(Enemy e) {
		if (isDead()) {
			return;
		}

		final RGB oldLife = e.life.clone2();
		final RGB newLife = oldLife.clone2().subUpto(strength, 0.0f);
		final float length = oldLife.length() - newLife.length();
		final RGB damage = new RGB(-mask.R * length + strength.R, -mask.G * length + strength.G, -mask.B * length + strength.B);

		e.inflictDamage(damage);

		dead = true;
	}
}
