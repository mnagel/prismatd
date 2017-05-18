package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.RedEnemy;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.tower.data.SlowDownTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Slow it down")
public class _060_SpecialTower_SlowDown extends Mission {

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
	public WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(1), 0.35f),
				},
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(1),
				new SlowDownTower(1),
		};
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(8, 1, "Check out the Slow Down Tower..."),
				new MissionStatementText(8, 10, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 1000;
	}
}
