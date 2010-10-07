package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.V2;

public class MenuInputActor extends EmptyInputActor {
	private Game game;
	private Layer layer;

	public MenuInputActor(final Game game, final Layer layer) {
		this.game = game;
		this.layer = layer;
	}

	@Override
	public void mouseBtn1DownAt(final V2 location) {
		final float height = layer.region.y;
		final float fromTop = height - location.y;
		final int buttonCount = 4;
		final int btn = (int) (Math.floor(buttonCount * fromTop / height));

		switch (btn) {
		case 0:
		case 1:
		case 2:
			if (btn < game.level.buildableTowers.length) {
				game.selectedBuildTower = game.level.buildableTowers[btn];
			}
			break;
		default:
			game.startWave();
			break;
		}
	}

}
