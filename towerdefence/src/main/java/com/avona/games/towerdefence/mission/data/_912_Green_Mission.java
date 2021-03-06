package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.GreenEnemy;
import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.GreenTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Green Mission")
public class _912_Green_Mission extends Mission {
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
		l += "......0.........\n"; // 0
		l += "......x.........\n"; // 1
		l += "......1xxxxxxx2.\n"; // 2
		l += "..............x.\n"; // 3
		l += "..6xxxxxxxxx5.x.\n"; // 4
		l += "..x.........x.x.\n"; // 5
		l += "..x.........x.x.\n"; // 6
		l += "..x.........x.x.\n"; // 7
		l += "..7xxxxxxx8.4x3.\n"; // 8
		l += "..........x.....\n"; // 9
		l += "..........x.....\n"; // 0
		l += "..........9.....\n"; // 1

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
				new GreenTower(1),
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		int level = 0;
		return new WaveEnemyConfig[][]{
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(20, 0.35f, new GreenEnemy(++level)),
		};
	}
}
