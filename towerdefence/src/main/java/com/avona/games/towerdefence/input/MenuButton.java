package com.avona.games.towerdefence.input;

public abstract class MenuButton {
	private String name;
	public MenuButtonLook look;

	public MenuButton(String name, MenuButtonLook look) {
		this.name = name;
		this.look = look;
	}

	public abstract void onClick();

	public abstract Object getRenderExtra();

	public enum MenuButtonLook {
		BUILD_TOWER,
		UPGRADE_TOWER,
		NEXT_WAVE
	}

	@Override
	public String toString() {
		return name;
	}
}
