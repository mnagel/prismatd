package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.Enemy.Enemy;
import com.avona.games.towerdefence.Tower.Tower;

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
	public void mouseBtn1DownAt(V2 location) {
		if (game.selectedBuildTower == null)
			return;
		game.draggingTower = true;
	}

	@Override
	public void mouseBtn1UpAt(V2 location) {
		if (!game.draggingTower)
			return;
		game.draggingTower = false;

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
	public void mouseBtn2DownAt(V2 location) {
		game.startWave();
	}

	@Override
	public void mouseBtn2UpAt(V2 location) {
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
		game.draggingTower = false;
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
