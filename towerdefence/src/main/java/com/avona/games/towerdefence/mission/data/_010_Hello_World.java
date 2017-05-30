package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Hello World")
public class _010_Hello_World extends Mission {

	private static final long serialVersionUID = -2476503319147078452L;

	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 10 * new RedTower(1).getPrice();
	}

	@Override
	protected String getMissionDefinitionString() {
		String l = "";
		//////0123456789012345
		l += ".1..............\n"; // 0
		l += ".x..............\n"; // 1
		l += ".x..............\n"; // 2
		l += ".x..............\n"; // 3
		l += ".x..............\n"; // 4
		l += ".x..............\n"; // 5
		l += ".x..............\n"; // 6
		l += ".x..............\n"; // 7
		l += ".x..............\n"; // 8
		l += ".x..............\n"; // 9
		l += ".x..............\n"; // 0
		l += ".2..............\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(2, 0, "1. Pixels spawn here!"),
				new MissionStatementText(2, 11, "2. Pixels escape here!"),
				new MissionStatementText(3, 3, "3. Stop them by building Prismas here!"),
				new MissionStatementText(3, 4, "... and here ..."),
				new MissionStatementText(3, 5, "... and here ..."),
				new MissionStatementText(3, 6, "... and here!"),

				new MissionStatementText(15, 4, ">>>"),
				new MissionStatementText(10, 5, "Select Prisma to build"),
				new MissionStatementText(10, 7, "Send Pixels"),
				new MissionStatementText(15, 8, ">>>"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(1)
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		int level = 0;
		return new WaveEnemyConfig[][]{
				WaveEnemyConfig.create(3, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(6, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(9, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(12, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(15, 0.35f, new RedEnemy(++level)),
		};
	}
}
