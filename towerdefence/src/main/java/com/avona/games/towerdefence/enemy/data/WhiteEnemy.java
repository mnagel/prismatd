package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class WhiteEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;

	public WhiteEnemy(int level) {
		super(
				level,
				3 + (level - 1),
				new RGB(
						5 * 50 * level + 10,
						5 * 50 * level + 10,
						5 * 50 * level + 10
				),
				(80 + 3 * (level - 1)) / 3
		);
	}

	public WhiteEnemy(WhiteEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new WhiteEnemy(this);
	}
}
