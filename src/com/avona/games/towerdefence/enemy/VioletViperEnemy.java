package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.level.Level;

public class VioletViperEnemy extends Enemy {

	private static final long serialVersionUID = 93877621275472018L;

	public VioletViperEnemy(Level world, int level) {
		super(world, level);
	}

	public VioletViperEnemy(VioletViperEnemy other) {
		super(other);
	}

	@Override
	public RGB getMaxLife() {
		return new RGB(50 * level + 10, 0, 50 * level + 10);
	}

	@Override
	public Enemy copy() {
		return new VioletViperEnemy(this);
	}
}
