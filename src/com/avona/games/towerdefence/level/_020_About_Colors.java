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

public class _020_About_Colors extends Level {

	private static final long serialVersionUID = -2476503319147078452L;

	public _020_About_Colors(final Game game) {
		super(game);
		gameBackgroundName = "back_l";
		menuBackgroundName = "back_r";
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

	protected V2[] loadWaypoints() {
		return new V2[] { new V2(500, 300), new V2(500, 350), new V2(30, 350),
				new V2(30, 200), new V2(300, 200), new V2(300, 0),
				new V2(30, 480), new V2(30, 400), new V2(600, 400),
				new V2(600, 300) };
	}

	@Override
	protected Tower[] loadBuildableTowers() {
		return new Tower[] { new RubyPrismaTower(game.timedCodeManager, 1),
				new EmeraldPrismaTower(game.timedCodeManager, 1),
				new SapphirePrismaTower(game.timedCodeManager, 1) };
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		final float delay = 0.4f;
		final int level = 2;
		return new WaveEnemyConfig[][] { new WaveEnemyConfig[] {
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay),
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay),
				new WaveEnemyConfig(new VioletViperEnemy(this, level), delay),
				new WaveEnemyConfig(new VioletViperEnemy(this, level), delay),
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay),
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay),
				new WaveEnemyConfig(new VioletViperEnemy(this, level), delay),
				new WaveEnemyConfig(new VioletViperEnemy(this, level), delay),
				new WaveEnemyConfig(new VioletViperEnemy(this, level), delay),
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay),
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay),
				new WaveEnemyConfig(new LimeLizardEnemy(this, level), delay) } };
	}
	
	@Override
	public String getLevelName() {
		return "About Colors";
	}
}