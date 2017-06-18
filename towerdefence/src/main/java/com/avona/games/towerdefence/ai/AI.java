package com.avona.games.towerdefence.ai;


import com.avona.games.towerdefence.core.RGB;
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

	public Tuple<Tower, GridCell> findOptimalPosition(Mission m, Tower... ts) {
		Tuple<Tower, GridCell> best = null;
		RGB bestRat = new RGB(0, 0, 0);

		for (Tower t : ts) {
			for (GridCell c : m.gridCells) {
				if (c.state != CellState.FREE) {
					continue;
				}

				RGB rat = dpsBasedScoring(m, c, t);
				if (rat.length() > bestRat.length()) {
					bestRat = rat;
					best = new Tuple<>(t, c);
				}
			}
		}

		return best;
	}

	public void buildAtBestPosition(Game g) {
		Tuple<Tower, GridCell> best = findOptimalPosition(g.mission, g.mission.buildableTowers);
		if (best != null) {
			g.addTowerAt(best.a.clone2(), best.b);
		}
	}


}
