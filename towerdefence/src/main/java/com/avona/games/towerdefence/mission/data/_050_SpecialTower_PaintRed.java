package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.GreenEnemy;
import com.avona.games.towerdefence.enemy.data.RainbowEnemy;
import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.enemy.data.YellowEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.PaintRedTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.tower.data.SlowDownTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Paint it Red")
public class _050_SpecialTower_PaintRed extends Mission {

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
		l += "...6xxxxxxxxxx5.\n"; // 1
		l += "...x...3xxxxxx4.\n"; // 2
		l += "...x...x........\n"; // 3
		l += "...x...x........\n"; // 4
		l += "...x...x........\n"; // 5
		l += "...x...x........\n"; // 6
		l += "...x...x........\n"; // 7
		l += "...x...x........\n"; // 8
		l += "...x...x........\n"; // 9
		l += "...x...2xxxxxxx1\n"; // 0
		l += "...7............\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(8, 1, "Check out the Paint Red Tower..."),
				new MissionStatementText(8, 10, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(1),
				new PaintRedTower(1),
				new SlowDownTower(1),
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		int level = 0;
		return new WaveEnemyConfig[][]{
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new GreenEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new YellowEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new GreenEnemy(level), new YellowEnemy(level)),
				WaveEnemyConfig.create(10, 0.35f, new RainbowEnemy(++level)),
		};
	}
}
