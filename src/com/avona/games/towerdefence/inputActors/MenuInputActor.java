package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;

public class MenuInputActor extends EmptyInputActor {
	private Game game;

	public MenuInputActor(Game game) {
		this.game = game;
	}

	@Override
	public void mouseBtn1DownAt(V2 location) {
		game.startWave();
	}

}
