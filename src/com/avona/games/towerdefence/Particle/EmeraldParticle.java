package com.avona.games.towerdefence.Particle;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particleCollidors.ParticleCollidorPolicy;

public class EmeraldParticle extends Particle {

	private static final long serialVersionUID = 5591841836118929513L;

	public EmeraldParticle(int level, V2 location, Enemy target,
			ParticleCollidorPolicy collidorPolicy) {

		super(level, location, target, collidorPolicy);
		this.strength = new RGB(0, 10 + 2 * (level - 1), 0);
	}

}
