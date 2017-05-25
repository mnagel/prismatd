package com.avona.games.towerdefence.enemy.data;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;

public class RainbowEnemy extends Enemy {
	private static final long serialVersionUID = 3102974973240386039L;
	private float localAge = 0;

	public RainbowEnemy(int level) {
		super(
				level,
				40 + level,
				new RGB(50.0f * (float) Math.pow(1.3, level), 0, 0),
				80 + 3 * (float) Math.pow(1.1, level)
		);
	}

	public RainbowEnemy(RainbowEnemy other) {
		super(other);
		other.localAge = this.localAge;
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
