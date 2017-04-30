package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.mission.Mission;

public class RainbowEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;
	private float localAge = 0;

	public RainbowEnemy(Mission mission, int levelNum) {
		super(
				mission,
				levelNum,
				3 + (levelNum - 1),
				new RGB(
						50 * levelNum + 10,
						0,
						0
				),
				80 + 3 * (levelNum - 1)
		);
	}

	public RainbowEnemy(RainbowEnemy other) {
		super(other);
	}

	@Override
	public Enemy clone2() {
		return new RainbowEnemy(this);
	}

	@Override
	public void step(float dt) {
		super.step(dt);
		localAge += dt;
		if (localAge > 1) {
			life.rotate();
			localAge -= 1;
		}
	}
}
