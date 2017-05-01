package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;

class MenuButtonInputActor extends EmptyInputActor {

	private MenuButton button;

	MenuButtonInputActor(MenuButton button) {
		this.button = button;
	}

	@Override
	public void mouseBtn1DownAt(final V2 virtualLocation) {
		button.onClick();
	}
}
