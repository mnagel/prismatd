package com.avona.games.towerdefence.mission.data;

import com.avona.games.towerdefence.enemy.data.GreenEnemy;
import com.avona.games.towerdefence.enemy.data.PurpleEnemy;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionName;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.BlueTower;
import com.avona.games.towerdefence.tower.data.GreenTower;
import com.avona.games.towerdefence.tower.data.RedTower;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.waveListeners.GrantInterestPerWave;

@SuppressWarnings("WeakerAccess")
@MissionName(value = "There shall be light!")
public class _100_Mission extends Mission {

	private static final long serialVersionUID = -3454126790549945539L;

	public _100_Mission(final Game game) {
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
	protected MissionStatementText[] getMissionStatementTexts() {
		return new MissionStatementText[]{
				new MissionStatementText(0, 0, "Kill all Pixels!"),
				new MissionStatementText(0, 1, "Thank you for playing PrismaTD"),
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
	public WaveEnemyConfig[][] loadEnemyWaves() {
		return new WaveEnemyConfig[][]{
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(1), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(1),
								0.35f)},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(2), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(2),
								0.35f)},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(3), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(3),
								0.35f)},
				new WaveEnemyConfig[]{
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f),
						new WaveEnemyConfig(new GreenEnemy(4), 0.35f),
						new WaveEnemyConfig(new PurpleEnemy(4),
								0.35f)}};
	}
}
