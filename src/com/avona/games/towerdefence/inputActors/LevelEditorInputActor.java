package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;

public class LevelEditorInputActor extends GameInputActor {

	public LevelEditorInputActor(Game game, Mouse mouse) {
		super(game, mouse);
	}
	
	@Override
	public void mouseBtn1UpAt(V2 location) {
		super.mouseBtn1UpAt(location);
		
		Util.log("new V2(" + location.x + "f, " + location.y + "f), ");
	}

}
