package com.avona.games.towerdefence.gfx;

public class ShaderSource {
	public String source;

	public ShaderSource(String sourceString) {
		fromString(sourceString);
	}

	public void fromString(String sourceString) {
		this.source = sourceString;
	}

	@Override
	public String toString() {
		return source;
	}
}
