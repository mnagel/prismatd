package com.avona.games.towerdefence.input;

public abstract class MenuButton extends EmptyInputActor {
	public MenuButtonLook look;
	private String name;

	MenuButton(String name, MenuButtonLook look) {
		this.name = name;
		this.look = look;
	}

	public Object getRenderExtra() {
		return null;
	}

	@Override
	public String toString() {
		return name;
	}
}
