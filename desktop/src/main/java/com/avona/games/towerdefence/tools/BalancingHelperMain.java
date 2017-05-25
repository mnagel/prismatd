package com.avona.games.towerdefence.tools;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.enemy.data.*;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.mission.data._900_Benchmark;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.tower.data.*;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BalancingHelperMain {
	public static void main(String... args) {
		BalancingHelperMain m = new BalancingHelperMain();
		m.rateTowers();
		System.out.println();
		m.rateEnemies();
		System.out.println();
		m.rateMissions();
		System.out.println();
		m.rateWaves();
		System.out.println();
	}

	private void rateTowers() {
		System.out.println("Name\tLevel\tPrice\tRange\tDamage\tDamage Combined\tReload Time\tDPS\tDPS/k$\tDMG to single Red20");

		for (Class tc : new Class[]{
				RedTower.class,
				GreenTower.class,
				BlueTower.class,
				PaintRedTower.class,
				SlowDownTower.class
		}) {
			for (int level : new int[]{1, 2, 3}) {
				try {
					Constructor<? extends Tower> ctor = tc.getConstructor(int.class);
					Tower t = ctor.newInstance(level);
					rateTower(t);
				} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
					throw new RuntimeException("died horribly in tower list hackery", e);
				}
			}
		}
	}

	private void rateTower(Tower t) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%d\t%.1f\t%s\t%.1f\t%.1f\t%s\t%s\t%s",
						t.getName(),
						t.level,
						t.getPrice(),
						t.getRange(),
						t.getDamage(),
						t.getDamage().length(),
						t.getReloadTime(),
						t.getDamage().scaled(1.0f / t.getReloadTime()),
						t.getDamage().scaled(1.0f / t.getReloadTime() / t.getPrice() * 1000),
						t.getDamage().scaled(1.0f / t.getReloadTime() * t.getRange() / new RedEnemy(20).getVelocity().length())
				)
		);
	}

	private void rateEnemies() {
		System.out.println("Name\tLevel\tWorth\tHealth\tHealth Combined\tSpeed\tDPS Required by Red3");


		for (Class<Enemy> ec : new Class[]{
				RedEnemy.class,
				GreenEnemy.class,
				BlueEnemy.class,
				PurpleEnemy.class,
				YellowEnemy.class,
				WhiteEnemy.class,
				RainbowEnemy.class,
		}) {
			for (int level = 1; level <= 20; level++) {
				try {
					Constructor<? extends Enemy> ctor = ec.getConstructor(int.class);
					Enemy e = ctor.newInstance(level);
					rateEnemy(e);
				} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
					throw new RuntimeException("died horribly in enemy list hackery", e);
				}
			}
		}
	}

	private void rateEnemy(Enemy e) {
		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%d\t%s\t%.1f\t%.1f\t%s",
						e.getClass().getSimpleName(), // TODO getter
						e.level,
						e.worth,
						e.maxLife,
						e.maxLife.length(),
						e.getVelocity().length(),
						e.maxLife.scaled(1.0f / (new RedTower(3).getRange() / e.getVelocity().length()))
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
		for (Class<Mission> mc : MissionList.availableMissions) {
			try {
				Constructor<? extends Mission> ctor = mc.getConstructor();
				Mission mission = ctor.newInstance();
				if (mission.getClass() == _900_Benchmark.class) {
					continue;
				}
				rateWaves(mission);
			} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
				throw new RuntimeException("died horribly in mission list hackery", e);
			}
		}
	}

	private void rateWaves(Mission m) {
		System.out.println("Mission\twaveNr\tEnemy Count\tHP\t$\tsum($)\tHP/$\tDuration\tDPS required\tDPS req / sum(k$)\tDPS req / sum(k$)\tEnemies");
		float money = m.getStartMoney();
		for (int waveNr = 0; waveNr < m.getWaveCount(); waveNr++) {
			WaveEnemyConfig[] wave = m.loadEnemyWaves()[waveNr];
			rateWave(m.missionName, waveNr, wave, money);

			double worth = Stream.of(wave).mapToDouble(waveEnemyConfig -> waveEnemyConfig.enemy.worth).sum();
			money += worth;
		}
		System.out.println();
	}


	private void rateWave(String mission, int waveNr, WaveEnemyConfig[] wave, float money) {
		double life = Stream.of(wave).mapToDouble(waveEnemyConfig -> waveEnemyConfig.enemy.life.length()).sum();
		RGB life2 = Stream.of(wave).collect(
				() -> new RGB(0, 0, 0), // create new acc's
				(acc, next) -> acc.add(next.enemy.life), // consume next value into acc
				(acc1, acc2) -> acc1.add(acc2) // combine two acc's (parallelism, ...)
		);

		//.mapToDouble(waveEnemyConfig -> waveEnemyConfig.enemy.life.length()).sum();
		float worth = (float) Stream.of(wave).mapToDouble(waveEnemyConfig -> waveEnemyConfig.enemy.worth).sum();
		// duration after last enemy is ignored
		float duration = (float) Stream.of(wave).limit(wave.length - 1).mapToDouble(waveEnemyConfig -> waveEnemyConfig.delayAfter).sum();
		// add amount of delay last enemy will be enemy will still be shootable
		// (e.g. think of the case when there is only 1 enemy)
		duration += new RedTower(1).getRange() / new RedEnemy(1).getVelocity().length();

		String enemies = Stream.of(wave)
				.map(waveEnemyConfig -> waveEnemyConfig.enemy.getClass().getSimpleName())
				.distinct()
				.collect(Collectors.joining(", ")
				);

		System.out.println(
				String.format(
						Locale.US,
						"%s\t%d\t%d\t%s\t%.1f\t%.1f\t%s\t%.1f\t%s\t%s\t%.1f\t%s",
						mission,
						waveNr + 1,
						wave.length,
						life2,
						worth,
						money,
						life2.scaled(1.0f / worth),
						duration,
						life2.scaled(1.0f / duration),
						life2.scaled(1.0f / duration / money * 1000),
						life / duration / money * 1000,
						enemies
				)
		);
	}
}
