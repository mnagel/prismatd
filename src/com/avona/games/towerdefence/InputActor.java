package com.avona.games.towerdefence;

public class InputActor {
	private PortableMainLoop ml;
	private Game game;
	private PortableGraphicsEngine ge;

	public InputActor(PortableMainLoop mainLoop, Game game, PortableGraphicsEngine ge) {
		this.ml = mainLoop;
		this.game = game;
		this.ge = ge;
	}

	public void pressedEscapeKey() {
		ml.exit();
	}
	public void onPause() {
		ml.pauseGame();
	}
	public void onResume() {
		ml.unpauseGame();
	}
	public void pressedSpaceKey() {
		ml.toggleGamePause();
	}
	public void pressedMouseBtn1At(V2 location) {
		Layer layer = ge.layerHerder.findLayerWithinPoint(location);
		Util.log("clicked on layer " + layer.name + ", position: " + location);
		if (layer.name == "game") {
			location = layer.convertToVirtual(location);
			game.addTowerAt(location);
			checkMouseOverTower(game.mouse.location);
		}
	}
	public void pressedMouseBtn2At(V2 location) {
		Layer layer = ge.layerHerder.findLayerWithinPoint(location);
		if (layer.name == "game") {
			location = layer.convertToVirtual(location);
			game.addEnemyAt(location);
		}
	}
	public void mouseEntered() {
		game.mouse.onScreen = true;
	}
	public void mouseExited() {
		game.mouse.onScreen = false;		
	}

	public void checkMouseOverTower(V2 location) {
		Tower t = game.closestTowerWithinRadius(location,
				PortableGraphicsEngine.TOWER_WIDTH);
		game.showTowersRange(t);
	}
	public void mouseMovedTo(V2 location) {
		game.mouse.location = location;
		Layer layer = ge.layerHerder.findLayerWithinPoint(location);
		if (layer.name == "game") {
			location = layer.convertToVirtual(location);
			checkMouseOverTower(location);
		}
	}
	public void mouseDraggedTo(V2 location) {
		game.mouse.location = location;
	}
}
