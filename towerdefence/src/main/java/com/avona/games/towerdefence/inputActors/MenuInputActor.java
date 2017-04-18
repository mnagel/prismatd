package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.tower.Tower;

public class MenuInputActor extends EmptyInputActor {
	private Game game;
	private Layer layer;

	public MenuInputActor(final Game game, final Layer layer) {
		this.game = game;
		this.layer = layer;
	}

	@Override
	public void mouseBtn1DownAt(final V2 virtualLocation) {
		if (game.isPaused())
			return;

		final float height = layer.virtualRegion.y;
		final float fromTop = height - virtualLocation.y;
		final int buttonCount = PortableGraphicsEngine.MENU_BUTTON_COUNT;
		final int btn = (int) (Math.floor(buttonCount * fromTop / height));

		if (btn == PortableGraphicsEngine.MENU_BUTTON_COUNT - 1) {
			// Send wave button is always last.
			game.pressForwardButton();
			return;
		}

		if (btn == PortableGraphicsEngine.MENU_BUTTON_COUNT - 2) {
			if (game.selectedObject instanceof Tower) {
				final Tower t = (Tower) game.selectedObject;
				game.levelUpTower(t);
			}
		}

		if (btn < game.mission.buildableTowers.length) {
			game.selectedBuildTower = game.mission.buildableTowers[btn];
		}
	}

}
