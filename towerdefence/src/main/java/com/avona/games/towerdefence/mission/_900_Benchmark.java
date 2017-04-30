package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.util.Util;
import com.avona.games.towerdefence.enemy.*;
import com.avona.games.towerdefence.tower.*;
import com.avona.games.towerdefence.wave.Wave;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.WaveListener;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "Benchmark!")
public class _900_Benchmark extends Mission {

	private static final long serialVersionUID = -3454126790549945539L;

	public _900_Benchmark(final Game game) {
		super(game);

		waveTracker.waveFullyDeployedListeners.add(new WaveListener() {
			@Override
			public void onWave(Wave wave) {
				Util.log("autostarting next wave");
				waveTracker.startNextWave();
			}
		});
		waveTracker.waveBegunListeners.add(new WaveListener() {
			@Override
			public void onWave(Wave wave) {
				if (wave.waveNum == 0) {
					Util.log("building towers");
					setupTowers();
				}
			}
		});
	}

	private void setupTowers() {
		Tower[] protoype = new Tower[]{
				new RedTower(game.timedCodeManager, 1),
				new GreenTower(game.timedCodeManager, 1),
				new BlueTower(game.timedCodeManager, 1),
				new PaintRedTower(game.timedCodeManager, 1),
				new SlowDownTower(game.timedCodeManager, 1),
		};

		for (int i = 0; i < gridCells.length; i++) {
			GridCell c = gridCells[i];
			if (c.state == CellState.FREE) {
				game.addTowerAt(protoype[i % protoype.length].clone2(), c);
			}
		}
	}

	@Override
	public int getStartLives() {
		return 10;
	}

	@Override
	public int getStartMoney() {
		return 100;
	}

	@Override
	protected String getMissionDefinitionString() {
		String l = "";
		//////0123456789012345
		l += ".0..............\n"; // 0
		l += ".x.4xxxxxxxxxx3.\n"; // 1
		l += ".x.x..........x.\n"; // 2
		l += ".x.x.8xxxxxx7.x.\n"; // 3
		l += ".x.x..x.....x.x.\n"; // 4
		l += ".x.x...x....x.x.\n"; // 5
		l += ".x.x....9...x.x.\n"; // 6
		l += ".x.x........x.x.\n"; // 7
		l += ".x.5xxxxxxxx6.x.\n"; // 8
		l += ".x............x.\n"; // 9
		l += ".1xxxxxxxxxxxx2.\n"; // 0
		l += "................\n"; // 1

		return l;
	}

	@Override
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(3, 0, "This is Benchmark!"),
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
		int lvl = 10000;
		Enemy[] prototype = new Enemy[]{
				new RedEnemy(this, lvl),
				new GreenEnemy(this, lvl),
				new BlueEnemy(this, lvl),
				new PurpleEnemy(this, lvl),
				new YellowEnemy(this, lvl),
				new WhiteEnemy(this, lvl),
				new RainbowEnemy(this, lvl),
		};

		for (Enemy e : prototype) {
			e.getVelocity().setLength(50);
		}

		WaveEnemyConfig[][] wec = new WaveEnemyConfig[100][100];

		for (int wave = 0; wave < wec.length; wave++) {
			for (int enemy = 0; enemy < wec[0].length; enemy++) {
				Enemy e = prototype[enemy % prototype.length].clone2();
				wec[wave][enemy] = new WaveEnemyConfig(e, 0.5f);
			}
		}

		return wec;
	}
}