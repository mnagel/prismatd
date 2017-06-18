package com.avona.games.towerdefence.ai;


import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.CellState;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.Tuple;

public class AI {
	public RGB dpsBasedScoring(Mission m, GridCell c, Tower t) {
		RGB res = new RGB(0, 0, 0);

		for (GridCell o : m.gridCells) {
			if (o.state == CellState.WAY) {
				float dist = c.center.dist(o.center);
				if (dist < t.getRange()) {
					res.add(t.getDps());
				}
			}
		}

		return res;
	}

	public Tuple<Tower, GridCell> findOptimalPosition(Mission m, RGB enemyHealth, RGB fleetDamage, Tower... ts) {
		float shortestFight = Float.POSITIVE_INFINITY;
		Tower bestTower = null;

		// 1 color
		for (Tower t : ts) {
			// calculate fight duration with this tower
			RGB newFleetDps = fleetDamage.added(t.getDps());
			RGB ratio = enemyHealth.compDived(newFleetDps);
			float duration = ratio.maxComp();
			// use tower with shortest fight duration
			if (duration < shortestFight) {
				shortestFight = duration;
				bestTower = t;
			}
		}


		// 2 position
		Tuple<Tower, GridCell> best = null;
		RGB bestRat = new RGB(0, 0, 0);

		for (GridCell c : m.gridCells) {
			if (c.state != CellState.FREE) {
				continue;
			}

			RGB rat = dpsBasedScoring(m, c, bestTower);
			if (rat.length() > bestRat.length()) {
				bestRat = rat;
				best = new Tuple<>(bestTower, c);
			}
		}

		return best;
	}

	public RGB getEnemyHealth(Enemy... es) {
		RGB res = new RGB(0, 0, 0);
		for (Enemy e : es) {
			res.add(e.maxLife);
		}
		return res;
	}

	public RGB getFleetDamage(Tower... ts) {
		RGB res = new RGB(0, 0, 0);
		for (Tower t : ts) {
			res.add(t.getDps());
		}
		return res;
	}

	public void buildAtBestPosition(Game g) {
		Tuple<Tower, GridCell> best = findOptimalPosition(
				g.mission,
				getEnemyHealth(g.mission.getEnemiesForWave(g.waveTracker.currentWaveNum() + 1).toArray(new Enemy[]{})),
				getFleetDamage(g.towers.toArray(new Tower[]{})),
				g.mission.buildableTowers
		);
		if (best != null) {
			g.addTowerAt(best.a.clone2(), best.b);
		}
	}


}
