package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.Util;

public class MenuButtonInputActor extends EmptyInputActor {
	private InputActor parent;
	private Game game;
	private Layer layer;

	public MenuButtonInputActor(final InputActor parent, final Game game, final Layer layer) {
		this.parent = parent;
		this.game = game;
		this.layer = layer;
	}

	@Override
	public void mouseBtn1DownAt(final V2 virtualLocation) {
		Util.log("heeeeeeeeeeeeeeello");
	}
}
