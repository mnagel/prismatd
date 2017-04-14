package com.avona.games.towerdefence;

import com.avona.games.towerdefence.gfx.Display;

public interface Transient {
	void step(float dt);

	boolean isDead();

	void draw(Display d, Layer l);

	V2 getLocation();
}
