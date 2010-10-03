package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;

public class MenuInputActor extends EmptyInputActor {
	private Game game;

	public MenuInputActor(Game game) {
		this.game = game;
	}

	@Override
	public void mouseBtn1DownAt(V2 location) {
		

		Util.log("menu clicked at raw: " + location.toString());
		final int max = 480; // FIXME get this from somewhere
		location.y = max - location.y;
		
		final int numbtn = 4;
		
		final int btn = (int)(Math.floor(numbtn * location.y / max));
		Util.log("menu clicked at: " + location.toString() + " that is btn#" + btn);
		
		switch (btn) {
		case 0:
		case 1:
		case 2:
			game.selectedBuildTower = game.level.listBuildableTowers()[btn];
			break;
		default:
			game.startWave();
			break;
		}
	}

}
