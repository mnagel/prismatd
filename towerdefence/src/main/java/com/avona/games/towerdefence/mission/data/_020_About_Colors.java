package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.BlueEnemy;
import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "About Colors")
public class _020_About_Colors extends Mission {

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
		l += "..1.6...........\n"; // 0
		l += "..x.x...........\n"; // 1
		l += "..x.x...........\n"; // 2
		l += "..x.x...........\n"; // 3
		l += "..x.x...........\n"; // 4
		l += "..x.x...........\n"; // 5
		l += "..x.x...........\n"; // 6
		l += "..x.5xxxxxxxx4..\n"; // 7
		l += "..x..........x..\n"; // 8
		l += "..2xxxxxxxxxx3..\n"; // 9
		l += "................\n"; // 0
		l += "................\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(9, 2, "Red Prismas hit Red Pixels"),
				new MissionStatementText(9, 5, "Blue Prismas hit Blue Pixels"),
				new MissionStatementText(6, 9, "Note the preview of Pixels in the next Wave."),
				new MissionStatementText(4, 11, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(1),
				new BlueTower(1)
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		int level = 0;
		return new WaveEnemyConfig[][]{
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new BlueEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new BlueEnemy(level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new BlueEnemy(level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new BlueEnemy(level)),
		};
	}
}
