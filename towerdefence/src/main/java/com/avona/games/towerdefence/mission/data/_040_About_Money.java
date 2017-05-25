package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.BlueEnemy;
import com.avona.games.towerdefence.enemy.data.GreenEnemy;
import com.avona.games.towerdefence.enemy.data.PurpleEnemy;
import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.GreenTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "About Money")
public class _040_About_Money extends Mission {

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
		l += ".1............\n"; // 0
		l += ".x............\n"; // 1
		l += ".x............\n"; // 2
		l += ".x............\n"; // 3
		l += ".x............\n"; // 4
		l += ".2xxxxxxxxxx3.\n"; // 5
		l += "............x.\n"; // 6
		l += "7xx6........x.\n"; // 7
		l += "...x........x.\n"; // 8
		l += "...x........x.\n"; // 9
		l += "...5xxxxxxxx4.\n"; // 0
		l += "..............\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(8, 1, "Towers cost Money."),
				new MissionStatementText(8, 2, "You need to decide when to buy them."),
				new MissionStatementText(8, 10, "Stop the Pixels from escaping!"),
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
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new PurpleEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new GreenEnemy(level), new BlueEnemy(level)),
				WaveEnemyConfig.create(10, 0.35f, new PurpleEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new GreenEnemy(level), new BlueEnemy(level)),
		};
	}
}
