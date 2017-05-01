package com.avona.games.towerdefence.input;

public abstract class MenuButton {
	public MenuButtonLook look;
	private String name;
	MenuButton(String name, MenuButtonLook look) {
		this.name = name;
		this.look = look;
	}

	public abstract void onClick();

	public abstract Object getRenderExtra();

	@Override
	public String toString() {
		return name;
	}

	enum MenuButtonLook {
		BUILD_TOWER,
		UPGRADE_TOWER,
		NEXT_WAVE
	}
}
