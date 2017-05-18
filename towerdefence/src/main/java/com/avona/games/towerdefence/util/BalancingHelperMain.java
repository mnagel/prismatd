package com.avona.games.towerdefence.util;

import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.data.*;
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
		BalancingHelperMain m = new BalancingHelperMain();
		m.rateTowers();
		m.rateEnemies();
		m.rateMissions();
		m.rateWaves();
	}

	private void rateTowers() {
		System.out.println("Name\tLevel\tPrice\tRange\tDamage\tDamage Combined\tReload Time\tDPS\tSingle Enemy Damage\tDPSP$");

		for (Tower t : new Tower[]{
				new RedTower(0),
				new GreenTower(0),
				new BlueTower(0),
				new PaintRedTower(0),
				new SlowDownTower(0)
		}) {
			for (int level : new int[]{1, 2, 3}) {
				t.setLevel(level);
				rateTower(t);
			}
		}
	}

	private void rateTower(Tower t) {
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

	private void rateEnemies() {
		System.out.println("Name\tLevel\tPrice\tDamage\tDamage Combined\tSpeed\tDPS Required\tDPSP$");


		for (Enemy e : new Enemy[]{
				new RedEnemy(0),
				new GreenEnemy(0),
				new BlueEnemy(0),
				new PurpleEnemy(0),
				new YellowEnemy(0),
				new WhiteEnemy(0),
				new RainbowEnemy(0),
		}) {
			for (int level : new int[]{1, 2, 3}) {
				e.level = level;
				rateEnemy(e);
			}
		}
	}

	private void rateEnemy(Enemy e) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%d\t%s\t%.1f\t%.1f\t%.1f\t%.1f",
						e.getClass().getSimpleName(), // TODO getter
						e.level,
						e.worth,
						e.maxLife,
						e.maxLife.length(),
						e.getVelocity().length(),
						e.maxLife.length() / e.getVelocity().length(),
						e.maxLife.length() / e.getVelocity().length() / e.worth
				)
		);
	}

	private void rateMissions() {
		System.out.println("Name\tWave Count\tLength of Path\tWayFields/TowerField for relevant fields");

		for (Class<Mission> mc : MissionList.availableMissions) {
			try {
				Constructor<? extends Mission> ctor = mc.getConstructor();
				Mission mission = ctor.newInstance();
				rateMission(mission);
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				throw new RuntimeException("died horribly in mission list hackery", e);
			}
		}
	}

	private void rateMission(Mission m) {
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

	private void rateWaves() {
		System.out.println("Mission\twaveNr\tEnemy Count\tEnemy HP\tEnemy Worth\tEnemy HP/Worth\tDuration\tHP/s\tHP/s/$");

		for (Class<Mission> mc : MissionList.availableMissions) {
			try {
				Constructor<? extends Mission> ctor = mc.getConstructor();
				Mission mission = ctor.newInstance();
				rateWaves(mission);
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				throw new RuntimeException("died horribly in mission list hackery", e);
			}
		}
	}

	private void rateWaves(Mission m) {
		for (int waveNr = 0; waveNr < m.getWaveCount(); waveNr++) {
			WaveEnemyConfig[] wave = m.loadEnemyWaves()[waveNr];
			rateWave(m.missionName, waveNr, wave);
		}
	}


	private void rateWave(String mission, int waveNr, WaveEnemyConfig[] wave) {
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
