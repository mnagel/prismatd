package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.level.Level;

public class LimeLizardEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public LimeLizardEnemy(Level world, int level) {
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
