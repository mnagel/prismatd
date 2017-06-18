package com.avona.games.towerdefence.ai;


import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.CellState;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

public class AI {
	public float rateCellForTower(Mission m, GridCell c, Tower t) {
		if (c.state != CellState.FREE) {
			return -1;
		}

		float res = 0;
		for (GridCell o: m.gridCells) {
			if (o.state == CellState.WAY) {
				float dist = c.center.dist(o.center);
				if (dist < t.getRange()) {
					res += 1;
				}
			}
		}

		return res;
	}

	public GridCell findOptimalPosition(Mission m, Tower t) {
		GridCell best = null;
		float bestRat = -1;

		for (GridCell c: m.gridCells) {
			if (c.state != CellState.FREE) {
				continue;
			}

			float rat = rateCellForTower(m, c, t);
			if (rat > bestRat) {
				bestRat = rat;
				best = c;
			}
		}

		return best;
	}

	public void buildAtBestPosition(Game g, Tower t) {
		GridCell bestGridCell = findOptimalPosition(g.mission, t);
		if (bestGridCell != null) {
			g.addTowerAt(t, bestGridCell);
		}
	}


}
