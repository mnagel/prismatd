package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.VioletViperEnemy;
import com.avona.games.towerdefence.tower.EmeraldPrismaTower;
import com.avona.games.towerdefence.tower.RubyPrismaTower;
import com.avona.games.towerdefence.tower.SapphirePrismaTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

public class _100_Grass extends Level {

	private static final long serialVersionUID = -3454126790549945539L;

	public _100_Grass(final Game game) {
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
	protected V2[] loadWaypoints() {
		return new V2[] { new V2(30, 480), new V2(30, 400), new V2(600, 400),
				new V2(600, 300), new V2(500, 300), new V2(500, 350),
				new V2(30, 350), new V2(30, 200), new V2(300, 200),
				new V2(300, 0) };
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[] { new RubyPrismaTower(game.timedCodeManager, 1),
				new EmeraldPrismaTower(game.timedCodeManager, 1),
				new SapphirePrismaTower(game.timedCodeManager, 1) };
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][] {
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1),
								0.35f) },
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 2), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 2),
								0.35f) },
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 3), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 3),
								0.35f) },
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 4), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 4),
								0.35f) } };
	}
	
	@Override
	public String getLevelName() {
		return "Grass";
	}

	@Override
	protected String getGameBackgroundName() {
		return "back_l";
	}

	@Override
	protected String getMenuBackgroundName() {
		return "back_r";
	}

	@Override
	protected String getOverlayBackgroundName() {
		return "no_briefing";
	}
}
