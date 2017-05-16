package com.avona.games.towerdefence.util;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.data.*;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.events.EventDistributor;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.*;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public class BalancingHelperMain {
	public static void main(String... args) {
		new BalancingHelperMain().rateTowers();
		new BalancingHelperMain().rateEnemies();
		new BalancingHelperMain().rateMissions();
		new BalancingHelperMain().rateWaves();
	}

	public void rateTowers() {
		System.out.println("Name\tLevel\tPrice\tRange\tDamage\tDamage Combined\tReload Time\tDPS\tSingle Enemy Damage\tDPSP$");

		for (int level : new int[]{1, 2, 3}) {
			for (Tower t : new Tower[]{
					new RedTower(null, level),
					new GreenTower(null, level),
					new BlueTower(null, level),
					new PaintRedTower(null, level),
					new SlowDownTower(null, level)
			}) {
				rateTower(t);
			}
		}
	}

	public void rateTower(Tower t) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%d\t%.1f\t%s\t%.1f\t%.1f\t%.1f\t%.1f\t%.1f",
						t.getName(),
						t.level,
						t.getPrice(),
						t.getRange(),
						t.getDamage(),
						t.getDamage().length(),
						t.getReloadTime(),
						t.getDamage().length() / t.getReloadTime(),
						t.getDamage().length() / t.getReloadTime() * t.getRange(),
						t.getDamage().length() / t.getReloadTime() / t.getPrice()
				)
		);
	}

	public void rateEnemies() {
		System.out.println("Name\tLevel\tPrice\tDamage\tDamage Combined\tSpeed\tDPS Required\tDPSP$");

		for (int level : new int[]{1, 2, 3}) {
			for (Enemy e : new Enemy[]{
					new RedEnemy(null, level),
					new GreenEnemy(null, level),
					new BlueEnemy(null, level),
					new PurpleEnemy(null, level),
					new YellowEnemy(null, level),
					new WhiteEnemy(null, level),
					new RainbowEnemy(null, level),
			}) {
				rateEnemy(e);
			}
		}
	}

	public void rateEnemy(Enemy e) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%d\t%s\t%.1f\t%.1f\t%.1f\t%.1f",
						e.getClass().getSimpleName(), // TODO getter
						e.levelNum, // TODO level
						e.worth,
						e.maxLife,
						e.maxLife.length(),
						e.getVelocity().length(),
						e.maxLife.length() / e.getVelocity().length(),
						e.maxLife.length() / e.getVelocity().length() / e.worth
				)
		);
	}

	public void rateMissions() {
		System.out.println("Name\tWave Count\tLength of Path\tWayFields/TowerField for relevant fields");

		for (Class<Mission> mc : MissionList.availableMissions) {
			try {
				Constructor<? extends Mission> ctor = mc.getConstructor(Game.class);
				Game game = new Game(new EventDistributor());
				Mission mission = ctor.newInstance(game);
				rateMission(mission);
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				throw new RuntimeException("died horribly in mission list hackery", e);
			}
		}
	}

	public void rateMission(Mission m) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%s\t%s",
						m.missionName,
						m.getWaveCount(),
						"xxx",
						"yyy"
				)
		);
	}

	public void rateWaves() {
		System.out.println("Mission\twaveNr\tEnemy Count\tEnemy HP\tEnemy Worth\tEnemy HP/Worth\tDuration\tHP/s\tHP/s/$");

		for (Class<Mission> mc : MissionList.availableMissions) {
			try {
				Constructor<? extends Mission> ctor = mc.getConstructor(Game.class);
				Game game = new Game(new EventDistributor());
				Mission mission = ctor.newInstance(game);
				rateWaves(mission);
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				throw new RuntimeException("died horribly in mission list hackery", e);
			}
		}
	}

	public void rateWaves(Mission m) {
		for (int waveNr = 0; waveNr < m.getWaveCount(); waveNr++) {
			WaveEnemyConfig[] wave = m.loadEnemyWaves()[waveNr];
			rateWave(m.missionName, waveNr, wave);
		}
	}


	public void rateWave(String mission, int waveNr, WaveEnemyConfig[] wave) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\tTBD",
						mission,
						waveNr
						// TODO
				)
		);
	}
}
