package com.avona.games.towerdefence;

import javax.vecmath.Point2d;

public class InputActor {
	private PortableMainLoop ml;
	private Game game;

	public InputActor(PortableMainLoop mainLoop, Game game) {
		this.ml = mainLoop;
		this.game = game;
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
	public void pressedMouseBtn1At(Point2d location) {
		game.addTowerAt(location);
		checkMouseOverTower(game.mouse.location);
	}
	public void pressedMouseBtn2At(Point2d location) {
		game.addEnemyAt(location);
	}
	public void mouseEntered() {
		game.mouse.onScreen = true;
	}
	public void mouseExited() {
		game.mouse.onScreen = false;		
	}

	public void checkMouseOverTower(Point2d location) {
		Tower t = game.closestTowerWithinRadius(location,
				PortableGraphicsEngine.TOWER_WIDTH);
		game.showTowersRange(t);
	}
	public void mouseMovedTo(Point2d location) {
		game.mouse.location = location;
		checkMouseOverTower(location);
	}
	public void mouseDraggedTo(Point2d location) {
		game.mouse.location = location;
	}
}
