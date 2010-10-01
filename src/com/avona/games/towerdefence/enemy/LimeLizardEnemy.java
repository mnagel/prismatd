package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.world.World;

public class LimeLizardEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public LimeLizardEnemy(World world, int level) {
		super(world, level);
	}

	public LimeLizardEnemy(LimeLizardEnemy other) {
		super(other);
	}

	@Override
	public RGB getMaxLife() {
		return new RGB(0, 50 * level + 10, 0);
	}

	@Override
	public Enemy copy() {
		return new LimeLizardEnemy(this);
	}
}
