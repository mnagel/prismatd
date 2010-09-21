package com.avona.games.towerdefence;

public class GameInputActor implements InputActor {
	private Game game;
	private Mouse mouse;

	public GameInputActor(Game game, Mouse mouse) {
		this.game = game;
		this.mouse = mouse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#pressedEscapeKey()
	 */
	public void pressedEscapeKey() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#onPause()
	 */
	public void onPause() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#onResume()
	 */
	public void onResume() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#pressedSpaceKey()
	 */
	public void pressedSpaceKey() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#pressedMouseBtn1At(com.avona.
	 * games.towerdefence.V2)
	 */
	public void pressedMouseBtn1At(V2 location) {
		Tower t = game.closestTowerWithinRadius(location, mouse.radius);
		if (t == null) {
			if (game.canBuildTowerAt(location)) {
				game.addTowerAt(location);
			}
		}
		checkMouseOverTower(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#pressedMouseBtn2At(com.avona.
	 * games.towerdefence.V2)
	 */
	public void pressedMouseBtn2At(V2 location) {
		game.startWave();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#mouseEntered()
	 */
	public void mouseEntered() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#mouseExited()
	 */
	public void mouseExited() {
	}

	public void checkMouseOverTower(V2 location) {
		final Tower t = game.closestTowerWithinRadius(location, mouse.radius);
		if (t != null) {
			game.selectedObject = t;
			return;
		}
		final Enemy e = game.closestEnemyWithinRadius(location, mouse.radius);
		game.selectedObject = e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#mouseMovedTo(com.avona.games.
	 * towerdefence.V2)
	 */
	public void mouseMovedTo(V2 location) {
		checkMouseOverTower(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#mouseDraggedTo(com.avona.games
	 * .towerdefence.V2)
	 */
	public void mouseDraggedTo(V2 location) {
	}
}
