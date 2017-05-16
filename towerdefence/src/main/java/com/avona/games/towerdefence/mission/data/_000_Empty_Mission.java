package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Empty Mission")
public class _000_Empty_Mission extends Mission {

	public _000_Empty_Mission(final Game game) {
		super(game);
	}

	@Override
	public int getStartLives() {
		return 1;
	}

	@Override
	public int getStartMoney() {
		return 0;
	}

	@Override
	protected String getMissionDefinitionString() {
		String l = "";
		//////0123456789012345
		l += "................\n"; // 0
		l += "................\n"; // 1
		l += "................\n"; // 2
		l += "................\n"; // 3
		l += "................\n"; // 4
		l += "................\n"; // 5
		l += "................\n"; // 6
		l += "................\n"; // 7
		l += "................\n"; // 8
		l += "................\n"; // 9
		l += "................\n"; // 0
		l += "................\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(1, 1, "Welcome to PrismaTD. Please load a Mission!")
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(game.timedCodeManager, 1)
		};
	}

	@Override
	public WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{};
	}
}
