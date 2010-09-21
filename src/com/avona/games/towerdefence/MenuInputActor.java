package com.avona.games.towerdefence;

public class MenuInputActor implements InputActor {
	private Game game;

	public MenuInputActor(Game game) {
		this.game = game;
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
		game.spawnWave(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#pressedMouseBtn2At(com.avona.
	 * games.towerdefence.V2)
	 */
	public void pressedMouseBtn2At(V2 location) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#mouseMovedTo(com.avona.games.
	 * towerdefence.V2)
	 */
	public void mouseMovedTo(V2 location) {
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
