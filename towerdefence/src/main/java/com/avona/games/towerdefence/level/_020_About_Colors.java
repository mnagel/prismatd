package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.RedRaptorEnemy;
import com.avona.games.towerdefence.tower.EmeraldPrismaTower;
import com.avona.games.towerdefence.tower.RubyPrismaTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

public class _020_About_Colors extends Level {

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
		return 100;
	}

	@Override
	protected String getLevelDefinitionString() {
		String l = "";
		//////0123456789012345
		l += "......0.........\n"; // 0
		l += "......x.........\n"; // 1
		l += "......1xxxxxxx2.\n"; // 2
		l += "..............x.\n"; // 3
		l += "..6xxxxxxxxx5.x.\n"; // 4
		l += "..x.........x.x.\n"; // 5
		l += "..x.........x.x.\n"; // 6
		l += "..x.........x.x.\n"; // 7
		l += "..7xxxxxxx8.4x3.\n"; // 8
		l += "..........x.....\n"; // 9
		l += "..........x.....\n"; // 0
		l += "..........9.....\n"; // 1

		return l;
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[] { 
				new RubyPrismaTower(game.timedCodeManager, 1),
				new EmeraldPrismaTower(game.timedCodeManager, 1)
		};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][] {
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
				}
		};
	}

	@Override
	public String getLevelName() {
		return "About Color";
	}

	@Override
	protected String getGameBackgroundName() {
		return "space_l";
	}

	@Override
	protected String getMenuBackgroundName() {
		return "tutorial_r2";
	}

	@Override
	protected String getOverlayBackgroundName() {
		return "tutorial_x2";
	}
}
