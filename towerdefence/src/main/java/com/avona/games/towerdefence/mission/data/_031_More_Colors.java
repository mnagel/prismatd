package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.*;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.GreenTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.tower.data.SlowDownTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "More Colors")
public class _031_More_Colors extends Mission {

	private static final long serialVersionUID = -2376503319147078452L;

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
		l += "................\n"; // 0
		l += "................\n"; // 1
		l += ".....3x4........\n"; // 2
		l += ".....x.x........\n"; // 3
		l += ".....x#x........\n"; // 4
		l += ".....x.x........\n"; // 5
		l += "1xxxx2#x........\n"; // 6
		l += ".......x........\n"; // 7
		l += ".......5xxxxxx6.\n"; // 8
		l += "..............x.\n"; // 9
		l += "8xxxxxxxxxxxxx7.\n"; // 0
		l += "................\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(8, 1, "Yellow Pixels are FAST"),
				new MissionStatementText(8, 2, "also, they are Red and Green"),
				new MissionStatementText(8, 6, "Have a look at the Slowdown Tower"),
				new MissionStatementText(8, 10, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(1),
				new GreenTower(1),
				new BlueTower(1),
				new SlowDownTower(1)
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		int level = 0;
		return new WaveEnemyConfig[][]{
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new GreenEnemy(level)),
				WaveEnemyConfig.create(10, 0.35f, new YellowEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f,
						new RedEnemy(++level),
						new GreenEnemy(level),
						new WhiteEnemy(1)
				),
				WaveEnemyConfig.create(10, 0.35f,
						new RedEnemy(++level),
						new GreenEnemy(level),
						new BlueEnemy(level),
						new YellowEnemy(level),
						new WhiteEnemy(1),
						new RainbowEnemy(level)
				),
		};
	}
}
