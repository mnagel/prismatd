package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.enemy.BlueEnemy;
import com.avona.games.towerdefence.enemy.GreenEnemy;
import com.avona.games.towerdefence.enemy.PurpleEnemy;
import com.avona.games.towerdefence.enemy.RedEnemy;
import com.avona.games.towerdefence.tower.BlueTower;
import com.avona.games.towerdefence.tower.RedTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Mixing Colors")
public class _030_Mixing_Colors extends Mission {

	private static final long serialVersionUID = -2376503319147078452L;

	public _030_Mixing_Colors(final Game game) {
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
				new RedTower(game.timedCodeManager, 1),
				new BlueTower(game.timedCodeManager, 1)
		};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.7f),
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.7f),
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.7f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new BlueEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new GreenEnemy(this, 1), 0.35f), // MUHAHA cannot be killed ... take that 100% gamers!
				},
		};
	}
}
