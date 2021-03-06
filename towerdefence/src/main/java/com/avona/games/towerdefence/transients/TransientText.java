package com.avona.games.towerdefence.transients;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.gfx.PortableDisplay;
import com.avona.games.towerdefence.input.Layer;

public class TransientText implements Transient {
	private String text;
	private float lifetime;
	private V2 location;
	private RGB color;
	private float alpha;

	public TransientText(String text, float lifetime, V2 location, RGB color, float alpha) {
		this.text = text;
		this.lifetime = lifetime;
		this.location = location;
		this.color = color;
		this.alpha = alpha;
	}

	@Override
	public V2 getLocation() {
		return location;
	}

	@Override
	public void step(float dt) {
		this.lifetime -= dt;
	}

	@Override
	public boolean isDead() {
		return this.lifetime < 0;
	}

	@Override
	public void draw(PortableDisplay d, Layer layer) {
		d.drawText(layer, text, true, location, color, alpha);
	}
}
