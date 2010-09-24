package com.avona.games.towerdefence;

public class Wave extends TimedCode {
	private static final long serialVersionUID = 1L;

	private int level;
	private boolean completed = false;
	private Game game;
	private TimedCodeManager timedCodeManager;
	private int totalEnemies;
	private int numEnemies = 0;

	public Wave(Game game, TimedCodeManager timedCodeManager, int level) {
		this.game = game;
		this.timedCodeManager = timedCodeManager;
		this.level = level;

		totalEnemies = 2 * level + 2;
		spawnEnemy();
	}

	public int getLevel() {
		return level;
	}

	public boolean isCompleted() {
		return completed;
	}

	private void spawnEnemy() {
		++numEnemies;
		game.spawnEnemy(level);
		timedCodeManager.addCode(0.4f, this);
	}

	@Override
	public void execute() {
		if (numEnemies < totalEnemies) {
			spawnEnemy();
		} else {
			completed = true;
			game.onWaveCompleted(level);
		}
	}

}
