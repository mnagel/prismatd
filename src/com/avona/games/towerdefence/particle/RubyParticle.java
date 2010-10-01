package com.avona.games.towerdefence.particle;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class RubyParticle extends Particle {

	private static final long serialVersionUID = 8441019002619600846L;

	public RubyParticle(int level, V2 location, Enemy target,
			ParticleCollidorPolicy collidorPolicy) {

		super(level, location, target, collidorPolicy);
		this.strength = new RGB(10 + 2 * (level - 1), 0, 0);
	}

}
