package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.enemy.BlueEnemy;
import com.avona.games.towerdefence.enemy.RedEnemy;
import com.avona.games.towerdefence.tower.BlueTower;
import com.avona.games.towerdefence.tower.PaintRedTower;
import com.avona.games.towerdefence.tower.RedTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "About Colors")
public class _020_About_Colors extends Mission {

	private static final long serialVersionUID = -2476503319147078452L;

	public _020_About_Colors(final Game game) {
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
		l += "...x............\n"; // 2
		l += "...x............\n"; // 3
		l += "...x............\n"; // 4
		l += "...x............\n"; // 5
		l += "...x............\n"; // 6
		l += "...x............\n"; // 7
		l += "...x............\n"; // 8
		l += "...x............\n"; // 9
		l += "...x............\n"; // 0
		l += "...2............\n"; // 1
		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(9, 1, "Red Prismas hit Red Pixels"),
				new MissionStatementText(9, 3, "Blue Prismas hit Blue Pixels"),
				new MissionStatementText(4, 10, "Stop the Pixels from escaping!"),
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[]{
				new RedTower(game.timedCodeManager, 2),
				new BlueTower(game.timedCodeManager, 2)
		};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
				}
		};
	}
}
