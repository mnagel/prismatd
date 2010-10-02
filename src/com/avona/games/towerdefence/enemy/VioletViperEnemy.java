package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.level.Level;

public class VioletViperEnemy extends Enemy {

	private static final long serialVersionUID = 93877621275472018L;

	public VioletViperEnemy(Level level, int levelNum) {
		super(level, levelNum, 3 + (levelNum - 1), new RGB(50 * levelNum + 10,
				0, 50 * levelNum + 10), 80 + 3 * (levelNum - 1));
	}

	public VioletViperEnemy(VioletViperEnemy other) {
		super(other);
	}

	@Override
	public Enemy copy() {
		return new VioletViperEnemy(this);
	}
}
