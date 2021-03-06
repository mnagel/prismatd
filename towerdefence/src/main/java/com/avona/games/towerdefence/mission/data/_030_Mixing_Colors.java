package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.BlueEnemy;
import com.avona.games.towerdefence.enemy.data.PurpleEnemy;
import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Mixing Colors")
public class _030_Mixing_Colors extends Mission {

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
		l += "...1............\n"; // 0
		l += "...x............\n"; // 1
		l += "...x.4x5........\n"; // 2
		l += "...x.x.x........\n"; // 3
		l += "...x.x.x........\n"; // 4
		l += "...x.x.x........\n"; // 5
		l += "...x.x.x........\n"; // 6
		l += "...x.x.x........\n"; // 7
		l += "...x.x.x........\n"; // 8
		l += "...2x3.x........\n"; // 9
		l += ".......x........\n"; // 0
		l += ".......6........\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(8, 1, "Purple Pixels must be hit Red"),
				new MissionStatementText(8, 2, "and"),
				new MissionStatementText(8, 3, "Purple Pixels must be hit Blue"),
				new MissionStatementText(8, 4, "because Purple = Red + Blue"),
				new MissionStatementText(8, 6, "Hitting them Red makes them less Red = more Blue"),
				new MissionStatementText(8, 7, "Hitting them Blue makes them less Blue = more Red"),
				new MissionStatementText(8, 10, "Stop the Pixels from escaping!"),
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
				WaveEnemyConfig.create(10, 0.35f, new PurpleEnemy(++level)),
				WaveEnemyConfig.create(10, 0.35f, new RedEnemy(++level), new BlueEnemy(level), new PurpleEnemy(level)),
		};
	}
}
