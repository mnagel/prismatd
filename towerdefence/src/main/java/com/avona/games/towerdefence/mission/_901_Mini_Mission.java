package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.enemy.RedEnemy;
import com.avona.games.towerdefence.tower.RedTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.Wave;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.WaveListener;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Mini Mission")
public class _901_Mini_Mission extends Mission {

	public _901_Mini_Mission(final Game game) {
		super(game);

		waveTracker.waveFullyDeployedListeners.add(new WaveListener() {
			@Override
			public void onWave(Wave wave) {
				Util.log("autostarting next wave");
				waveTracker.startNextWave();
			}
		});
	}

	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 1000;
	}

	@Override
	protected String getMissionDefinitionString() {
		String l = "";
		//////0123456789012345
		l += "................\n"; // 0
		l += "................\n"; // 1
		l += "................\n"; // 2
		l += "................\n"; // 3
		l += "................\n"; // 4
		l += "........1.......\n"; // 5
		l += "........2.......\n"; // 6
		l += "................\n"; // 7
		l += "................\n"; // 8
		l += "................\n"; // 9
		l += "................\n"; // 0
		l += "................\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(game.timedCodeManager, 1)
		};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				}
		};
	}
}
