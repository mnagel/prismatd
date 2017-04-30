package com.avona.games.towerdefence.transients;

import com.avona.games.towerdefence.gfx.Display;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.input.Layer;

public interface Transient {
	void step(float dt);

	boolean isDead();

	void draw(Display d, Layer l);

	V2 getLocation();
}
