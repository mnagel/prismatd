package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.wave.Wave;
import com.avona.games.towerdefence.wave.WaveEnemyConfig;
import com.avona.games.towerdefence.wave.WaveSender;
import com.avona.games.towerdefence.wave.WaveTracker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class Level implements Serializable, WaveSender {
	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;
	private static final long serialVersionUID = 1L;
	public final float WAYPOINT_WIDTH = 4.0f;
	public GridCell[] waypoints;
	public final Tower[] buildableTowers;
	private final WaveEnemyConfig[][] enemyWaves;
	public String gameBackgroundName;
	public String menuBackgroundName;
	public String overlayBackgroundName;
	public boolean showOverlay = true;
	public String levelName;
	public WaveTracker waveTracker = new WaveTracker(this);
	public boolean completed = false;
	public GridCell[][] gridCells2d;
	public GridCell[] gridCells;
	public int gridCellCountX;
	public int gridCellCountY;

	protected Game game;

	public void parseLevelDefinition(String levelDefinition) {
		gridCellCountX = 16;
		gridCellCountY = 12;
		gridCells2d = new GridCell[gridCellCountX][gridCellCountY];
		gridCells = new GridCell[gridCellCountX * gridCellCountY];
		for (int x = 0; x < gridCellCountX; x++) {
			for (int y = 0; y < gridCellCountY; y++) {
				GridCell c = new GridCell(x, y, new V2((x+0.5f)*GridCell.width, (y+0.5f)*GridCell.heigth), CellState.FREE);
				gridCells2d[x][y] = c;
				gridCells[x* gridCellCountY +y] = c;
			}
		}

		HashMap<Integer, GridCell> wp = new HashMap<>();

		int yy = -1;

		for (String row: levelDefinition.split("\n")) {
			yy++;
			int xx = -1;
			for (char c: row.toCharArray()) {
				xx++;

				int x = xx; // gridCellCountX - xx - 1;
				int y = gridCellCountY - yy - 1;

				if (c == '.') {
					gridCells2d[x][y].state = CellState.FREE;
				} else {
					gridCells2d[x][y].state = CellState.WAY;
				}

				if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
					wp.put((int)c, gridCells2d[x][y]);
				}
			}
		}

		List<Integer> is = new ArrayList<>(wp.keySet());
		Collections.sort(is);
		this.waypoints = new GridCell[is.size()];
		for (int i = 0; i < is.size(); i++) {
			this.waypoints[i] = wp.get(is.get(i));
		}
	}

	public GridCell getCellAt(V2 location) {
		int x = (int)Math.floor(location.x / GridCell.width);
		int y = (int)Math.floor(location.y / GridCell.heigth);
		if (x >= 0 && x < gridCellCountX && y >= 0 && y < gridCellCountY) {
			return gridCells2d[x][y];
		} else {
			return null;
		}
	}

	public Level(final Game game) {
		String l = this.getLevelDefinitionString();
		this.parseLevelDefinition(l);

		this.game = game;
		this.gameBackgroundName = getGameBackgroundName();
		this.menuBackgroundName = getMenuBackgroundName();
		this.overlayBackgroundName = getOverlayBackgroundName();
		this.levelName = getLevelName();
		this.enemyWaves = loadEnemyWaves();
		this.buildableTowers = loadBuildableTowers();

		// very sad to have this here, but loadBuildableTowers() is user-defined per level :(
		for (int i = 0; i < buildableTowers.length; i++) {
			V2 menuPos = new V2(1, i);
			Tower t = buildableTowers[i];
			t.location = menuPos;
		}
	}

	@Override
	public Wave sendWave(final int waveNum) {
		if (waveNum >= enemyWaves.length)
			return null;

		return new Wave(waveNum, game, this, game.timedCodeManager,
				enemyWaves[waveNum]);
	}

	@Override
	public void onAllWavesCompleted() {
		Util.log("All waves completed -> level completed");
		completed = true;
		game.eventListener.onLevelCompleted(this);
	}

	@Override
	public int getNumWaves() {
		return enemyWaves.length;
	}

	public Collection<Enemy> getEnemyPreview(int waveId) { // TODO: move to wave class once possible
		if (waveId >= getNumWaves()) {
			return new ArrayList<>();
		}

		LinkedHashMap<String, Enemy> col = new LinkedHashMap<>(); // keep insertion order
		for (WaveEnemyConfig we: this.enemyWaves[waveId]) {
			col.put(we.enemy.getClass().getSimpleName(), we.enemy);
		}
		return col.values();
	}

	protected abstract String getLevelDefinitionString();

	protected abstract WaveEnemyConfig[][] loadEnemyWaves();

	/**
	 * @return A list of towers that can be built in this level.
	 */
	protected abstract Tower[] loadBuildableTowers();

	protected abstract String getLevelName();

	protected abstract String getGameBackgroundName();

	protected abstract String getMenuBackgroundName();

	protected abstract String getOverlayBackgroundName();

	/**
	 * @return The amount of money the player starts with.
	 */
	public abstract int getStartMoney();

	/**
	 * @return The number of lives the player starts with.
	 */
	public abstract int getStartLives();
}
