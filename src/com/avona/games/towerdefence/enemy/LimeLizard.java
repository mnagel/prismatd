package com.avona.games.towerdefence.enemy;

import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.World.World;

public class LimeLizard extends Enemy {

	private static final long serialVersionUID = 3102974967310386039L;

	public LimeLizard(World world, V2 location, int level) {
		super(world, location, level);
		this.life = new RGB(0, 50 * level + 10, 0);
		this.maxLife = new RGB(0, 50 * level + 10, 0);
	}

}
