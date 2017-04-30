package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.enemy.data.*;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.GreenTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "More Colors")
public class _031_More_Colors extends Mission {

	private static final long serialVersionUID = -2376503319147078452L;

	public _031_More_Colors(final Game game) {
		super(game);

		waveTracker.waveBegunListeners
				.add(new GrantInterestPerWave(game, 0.10f));
	}

	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 1000;
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
				new MissionStatementText(8, 1, "Yellow Pixels are FAST"),
				new MissionStatementText(8, 2, "also, they are Red and Green"),
				new MissionStatementText(8, 10, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(game.timedCodeManager, 1),
				new GreenTower(game.timedCodeManager, 1),
				new BlueTower(game.timedCodeManager, 1)
		};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 2.0f),

						new WaveEnemyConfig(new GreenEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 2.0f),

						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new WhiteEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new WhiteEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new WhiteEnemy(this, 1), 1.0f),
				},
				new WaveEnemyConfig[]{

						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new WhiteEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new WhiteEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new YellowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new WhiteEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 2.0f),

						new WaveEnemyConfig(new RainbowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RainbowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RainbowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RainbowEnemy(this, 1), 1.0f),
						new WaveEnemyConfig(new RainbowEnemy(this, 1), 1.0f),
				},
		};
	}
}
