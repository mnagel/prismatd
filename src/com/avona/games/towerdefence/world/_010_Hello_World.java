package com.avona.games.towerdefence.world;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Wave;
import com.avona.games.towerdefence.WaveEnemyConfig;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.VioletViperEnemy;

public class _010_Hello_World extends World {

	private static final long serialVersionUID = -2476503319147078452L;

	public _010_Hello_World() {
		super();
		gameBackgroundName = "back_l";
		menuBackgroundName = "back_r";
	}

	@Override
	public int getStartLifes() {
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
	public void onWaveCompleted(int wave) {
		return;
	}

	@Override
	public Wave sendWave(int wave, Game g) {
		final float delay = 0.35f;
		final int level = 1;
		return new Wave(g, this, g.timedCodeManager, wave,
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay),
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay),
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay),
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay),
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay),
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay),
						new WaveEnemyConfig(new LimeLizardEnemy(this, level),
								delay),
						new WaveEnemyConfig(new VioletViperEnemy(this, level),
								delay) });
	}
}
