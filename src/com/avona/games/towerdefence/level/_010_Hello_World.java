package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Wave;
import com.avona.games.towerdefence.WaveEnemy;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.VioletViperEnemy;
import com.avona.games.towerdefence.waveListeners.GrantInterestPerWave;

public class _010_Hello_World extends Level {

	private static final long serialVersionUID = -2476503319147078452L;

	public _010_Hello_World(final Game game) {
		super(game);

		gameBackgroundName = "back_l";
		menuBackgroundName = "back_r";
		waveTracker.waveBegunListeners.add(new GrantInterestPerWave(game, 0.10f));
	}

	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 100;
	}

	@Override
	public void initWaypoints() {
		addWaypoint(30, 480);
		addWaypoint(30, 400);
		addWaypoint(600, 400);
		addWaypoint(600, 300);
		addWaypoint(500, 300);
		addWaypoint(500, 350);
		addWaypoint(30, 350);
		addWaypoint(30, 200);
		addWaypoint(300, 200);
		addWaypoint(300, 0);
	}

	@Override
	public Object listBuildableTowers() {
		return null;
	}

	@Override
	public Wave sendWave(final int wave) {
		final float delay = 0.35f;
		final int level = 1;
		return new Wave(
				game,
				this,
				game.timedCodeManager,
				new WaveEnemy[] {
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay),
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay),
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay),
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay),
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay),
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay),
						new WaveEnemy(new LimeLizardEnemy(this, level), delay),
						new WaveEnemy(new VioletViperEnemy(this, level), delay) });
	}
}
