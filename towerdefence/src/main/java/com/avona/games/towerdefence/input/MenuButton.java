package com.avona.games.towerdefence.input;

public abstract class MenuButton {
	private String name;

	public MenuButton(String name) {
		this.name = name;
	}

	public abstract void render();

	@Override
	public String toString() {
		return name;
	}
}
