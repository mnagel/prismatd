package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.level.Level;

public class LimeLizardEnemy extends Enemy {
	private static final long serialVersionUID = 3102974967310386039L;

	public LimeLizardEnemy(Level level, int levelNum) {
		super(level, levelNum, 3 + (levelNum - 1), new RGB(0,
				50 * levelNum + 10, 0), 80 + 3 * (levelNum - 1), 12);
	}

	public LimeLizardEnemy(LimeLizardEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone() {
		return new LimeLizardEnemy(this);
	}
}
