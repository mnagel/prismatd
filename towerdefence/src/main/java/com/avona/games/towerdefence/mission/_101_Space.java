package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.enemy.LimeLizardEnemy;
import com.avona.games.towerdefence.enemy.VioletViperEnemy;
import com.avona.games.towerdefence.tower.EmeraldPrismaTower;
import com.avona.games.towerdefence.tower.RubyPrismaTower;
import com.avona.games.towerdefence.tower.SapphirePrismaTower;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

@MissionName(value = "Space")
public class _101_Space extends Mission {

	private static final long serialVersionUID = 7889543946996644326L;

	public _101_Space(final Game game) {
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
	protected String getMissionDefinitionString() {
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
		return new Tower[]{new RubyPrismaTower(game.timedCodeManager, 1),
				new EmeraldPrismaTower(game.timedCodeManager, 1),
				new SapphirePrismaTower(game.timedCodeManager, 1)};
	}

	@Override
	protected WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
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
								0.35f)},
				new WaveEnemyConfig[]{
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
								0.35f)},
				new WaveEnemyConfig[]{
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
								0.35f)},
				new WaveEnemyConfig[]{
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
								0.35f)}};
	}

	@Override
	protected String getGameBackgroundName() {
		return "space_l";
	}

	@Override
	protected String getMenuBackgroundName() {
		return "space_r";
	}

	@Override
	protected String getOverlayBackgroundName() {
		return "no_briefing";
	}
}
