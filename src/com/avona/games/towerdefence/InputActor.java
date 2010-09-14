package com.avona.games.towerdefence;

public class InputActor {
	private PortableMainLoop ml;
	private Game game;
	private Mouse mouse;
	private LayerHerder layerHerder;
	private Layer gameLayer;
	private Layer menuLayer;

	public InputActor(PortableMainLoop mainLoop, Game game, Mouse mouse,
			LayerHerder layerHerder) {
		this.ml = mainLoop;
		this.game = game;
		this.mouse = mouse;
		this.layerHerder = layerHerder;

		gameLayer = layerHerder
				.findLayerByName(PortableMainLoop.GAME_LAYER_NAME);
		menuLayer = layerHerder
				.findLayerByName(PortableMainLoop.MENU_LAYER_NAME);
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
		Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == gameLayer) {
			location = layer.convertToVirtual(location);
			Tower t = game.closestTowerWithinRadius(location, mouse.radius);
			if (t == null) {
				game.addTowerAt(location);
			}
			checkMouseOverTower(location);
		} else if (layer == menuLayer) {
			game.spawnEnemy();
		}
	}

	public void pressedMouseBtn2At(V2 location) {
		Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == gameLayer) {
			game.spawnEnemy();
		}
	}

	public void mouseEntered() {
		mouse.onScreen = true;
	}

	public void mouseExited() {
		mouse.onScreen = false;
	}

	public void checkMouseOverTower(V2 location) {
		Tower t = game.closestTowerWithinRadius(location, mouse.radius);
		game.showTowersRange(t);
	}

	public void mouseMovedTo(V2 location) {
		mouse.location = location;
		Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == gameLayer) {
			location = layer.convertToVirtual(location);
			checkMouseOverTower(location);
		}
	}

	public void mouseDraggedTo(V2 location) {
		mouse.location = location;
	}
}
