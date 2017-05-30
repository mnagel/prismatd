package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.*;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.GreenTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "La Pirouette")
public class _120_La_Pirouette extends Mission {
	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 2 * 20 * new RedEnemy(1).worth;
	}

	@Override
	protected String getMissionDefinitionString() {
		String l = "";
		//////0123456789012345
		l += ".............0..\n"; // 0
		l += ".............x..\n"; // 1
		l += "..3xxxxxxx4..x..\n"; // 2
		l += "..x.......x..x..\n"; // 3
		l += "..x.......x..x..\n"; // 4
		l += "..x..7x8..x..x..\n"; // 5
		l += "..x..x....x..x..\n"; // 6
		l += "..x..6xxxx5..x..\n"; // 7
		l += "..x..........x..\n"; // 8
		l += "..2xxxxxxxxxx1..\n"; // 9
		l += "................\n"; // 0
		l += "................\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(0, 0, "Kill all Pixels!"),
				new MissionStatementText(0, 1, "Thank you for playing PrismaTD"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(1),
				new GreenTower(1),
				new BlueTower(1)
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		int level = 0;
		return new WaveEnemyConfig[][]{
				WaveEnemyConfig.create(20, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new BlueEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new PurpleEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new RedEnemy(++level), new BlueEnemy(level)),
				WaveEnemyConfig.create(20, 0.35f, new YellowEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new BlueEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new PurpleEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new RedEnemy(++level), new BlueEnemy(level)),
				WaveEnemyConfig.create(20, 0.35f, new YellowEnemy(++level)),
				WaveEnemyConfig.create(5, 2.00f, new WhiteEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new BlueEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new PurpleEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new RedEnemy(++level), new BlueEnemy(level)),
				WaveEnemyConfig.create(20, 0.35f, new YellowEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new RainbowEnemy(++level)),

		};
	}
}
