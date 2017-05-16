package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.BlueEnemy;
import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.RedTower;
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
		l += "###1############\n"; // 0
		l += "###x############\n"; // 1
		l += "###x############\n"; // 2
		l += "###x############\n"; // 3
		l += "##.x############\n"; // 4
		l += "###x############\n"; // 5
		l += "###x.###########\n"; // 6
		l += "###x############\n"; // 7
		l += "###x############\n"; // 8
		l += "###x############\n"; // 9
		l += "###x############\n"; // 0
		l += "###2############\n"; // 1
		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(9, 1, "Red Prismas hit Red Pixels"),
				new MissionStatementText(9, 3, "Blue Prismas hit Blue Pixels"),
				new MissionStatementText(4, 5, "Select a Prisma to upgrade it!"),
				new MissionStatementText(7, 8, "Prismas cannot be placed on blue walls"),
				new MissionStatementText(4, 11, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(game.timedCodeManager, 1),
				new BlueTower(game.timedCodeManager, 1)
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
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
