package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.WaveEnemyConfig;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.VioletViperEnemy;
import com.avona.games.towerdefence.tower.EmeraldPrismaTower;
import com.avona.games.towerdefence.tower.RubyPrismaTower;
import com.avona.games.towerdefence.tower.SapphirePrismaTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.waveListeners.GrantInterestPerWave;

public class _101_Space extends Level {

	private static final long serialVersionUID = 7889543946996644326L;

	public _101_Space(final Game game) {
		super(game);

		gameBackgroundName = "space-l";
		menuBackgroundName = "space-r";
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
		return new V2[] { 
				new V2(238.f, 480.f), 
				new V2(238.f, 384.f), 
				new V2(592.f, 384.f), 
				new V2(592.f, 132.f), 
				new V2(476.f, 132.f), 
				new V2(476.f, 290.f), 
				new V2( 94.f, 290.f), 
				new V2( 94.f, 132.f), 
				new V2(405.f, 131.f), 
				new V2(405.f,   0.f)
		};
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[] { 
				new RubyPrismaTower(game.timedCodeManager, 1),
				new EmeraldPrismaTower(game.timedCodeManager, 1),
				new SapphirePrismaTower(game.timedCodeManager, 1) 
				};
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
}
