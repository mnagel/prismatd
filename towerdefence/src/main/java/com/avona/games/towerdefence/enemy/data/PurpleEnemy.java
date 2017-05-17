package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class PurpleEnemy extends Enemy {

	private static final long serialVersionUID = 93877621275472018L;

	public PurpleEnemy(int level) {
		super(
				level,
				3 + (level - 1),
				new RGB(
						50 * level + 10,
						0,
						50 * level + 10
				),
				80 + 3 * (level - 1)
		);
	}

	public PurpleEnemy(PurpleEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new PurpleEnemy(this);
	}
}
