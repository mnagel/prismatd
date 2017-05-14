package com.avona.games.towerdefence.input;

public abstract class MenuButton extends EmptyInputActor {
	public MenuButtonLook look;
	private String name;
	float weight;

	MenuButton(String name, MenuButtonLook look, float weight) {
		this.name = name;
		this.look = look;
		this.weight = weight;
	}

	MenuButton(String name, MenuButtonLook look) {
		this(name, look, 1);
	}

	public Object getRenderExtra() {
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
