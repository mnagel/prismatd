package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.util.Util;

public class MenuButtonInputActor extends EmptyInputActor {

	MenuButton button;

	public MenuButtonInputActor(MenuButton button) {
		this.button = button;
	}

	@Override
	public void mouseBtn1DownAt(final V2 virtualLocation) {
		button.onClick();
	}
}
