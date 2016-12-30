package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.RedRaptorEnemy;
import com.avona.games.towerdefence.enemy.VioletViperEnemy;
import com.avona.games.towerdefence.tower.EmeraldPrismaTower;
import com.avona.games.towerdefence.tower.RubyPrismaTower;
import com.avona.games.towerdefence.tower.SapphirePrismaTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

public class _030_Mixing_Colors extends Level {

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
		return 100;
	}

	@Override
	protected V2[] loadWaypoints() {
		return new V2[] { new V2(238.f, 480.f), new V2(238.f, 384.f),
				new V2(592.f, 384.f), new V2(592.f, 132.f),
				new V2(476.f, 132.f), new V2(476.f, 290.f),
				new V2(94.f, 290.f), new V2(94.f, 132.f), new V2(405.f, 131.f),
				new V2(405.f, 0.f) };
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
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
				},
				new WaveEnemyConfig[] {
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new RedRaptorEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new VioletViperEnemy(this, 1), 0.35f),
						new WaveEnemyConfig(new LimeLizardEnemy(this, 1), 0.35f),
				},
		};
	}

	@Override
	public String getLevelName() {
		return "Mixing Colors";
	}

	@Override
	protected String getGameBackgroundName() {
		return "tutorial_l";
	}

	@Override
	protected String getMenuBackgroundName() {
		return "tutorial_r3";
	}

	@Override
	protected String getOverlayBackgroundName() {
		return "tutorial_x3";
	}
}
