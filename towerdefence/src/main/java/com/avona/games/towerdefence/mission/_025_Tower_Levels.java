package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.enemy.BlueEnemy;
import com.avona.games.towerdefence.enemy.RedEnemy;
import com.avona.games.towerdefence.tower.BlueTower;
import com.avona.games.towerdefence.tower.RedTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Tower Levels")
public class _025_Tower_Levels extends Mission {

	private static final long serialVersionUID = -2476503319147078452L;

	public _025_Tower_Levels(final Game game) {
		super(game);

		waveTracker.waveBegunListeners
				.add(new GrantInterestPerWave(game, 0.10f));
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
		l += "___1____________\n"; // 0
		l += "___x____________\n"; // 1
		l += "___x____________\n"; // 2
		l += "___x____________\n"; // 3
		l += "___x____________\n"; // 4
		l += "__.x____________\n"; // 5
		l += "___x.___________\n"; // 6
		l += "___x____________\n"; // 7
		l += "___x____________\n"; // 8
		l += "___x____________\n"; // 9
		l += "___x____________\n"; // 0
		l += "___2____________\n"; // 1
		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(9, 1, "Red Prismas hit Red Pixels"),
				new MissionStatementText(9, 3, "Blue Prismas hit Blue Pixels"),
				new MissionStatementText(4, 10, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(game.timedCodeManager, 1),
				new RedTower(game.timedCodeManager, 3),
				new BlueTower(game.timedCodeManager, 1)
		};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
				}
		};
	}
}
