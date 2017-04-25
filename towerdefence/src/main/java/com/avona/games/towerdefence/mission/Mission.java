package com.avona.games.towerdefence.mission;

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
import java.util.*;

public abstract class Mission implements Serializable, WaveSender {
	public final static float ORIGIN_X = 0;
	public final static float WIDTH = 675;
	public final static float ORIGIN_Y = 0;
	public final static float HEIGHT = 480;
	private static final long serialVersionUID = 1L;
	public final float WAYPOINT_WIDTH = 4.0f;
	public final Tower[] buildableTowers;
	private final WaveEnemyConfig[][] enemyWaves;
	public GridCell[] waypoints;
	public String missionName;
	public MissionStatementText[] missionStatementTexts;
	public WaveTracker waveTracker = new WaveTracker(this);
	public boolean completed = false;
	public GridCell[][] gridCells2d;
	public GridCell[] gridCells;
	public int gridCellCountX;
	public int gridCellCountY;

	protected Game game;

	public Mission(final Game game) {
		String l = this.getMissionDefinitionString();
		this.parseMissionDefinition(l);

		this.game = game;
		this.missionStatementTexts = getMissionStatementTexts();
		for (MissionStatementText t : this.missionStatementTexts) {
			t.y = gridCellCountY - t.y - 1;
		}

		this.missionName = getMissionName();
		this.enemyWaves = loadEnemyWaves();
		this.buildableTowers = loadBuildableTowers();

		// very sad to have this here, but loadBuildableTowers() is user-defined per mission :(
		for (int i = 0; i < buildableTowers.length; i++) {
			V2 menuPos = new V2(1, i);
			Tower t = buildableTowers[i];
			t.location = menuPos;
		}
	}

	public void parseMissionDefinition(String missionDefinition) {
		gridCellCountX = 16;
		gridCellCountY = 12;
		gridCells2d = new GridCell[gridCellCountX][gridCellCountY];
		gridCells = new GridCell[gridCellCountX * gridCellCountY];
		for (int x = 0; x < gridCellCountX; x++) {
			for (int y = 0; y < gridCellCountY; y++) {
				GridCell c = new GridCell(x, y, new V2((x + 0.5f) * GridCell.size, (y + 0.5f) * GridCell.size), CellState.FREE);
				gridCells2d[x][y] = c;
				gridCells[x * gridCellCountY + y] = c;
			}
		}

		HashMap<Integer, GridCell> wp = new HashMap<>();

		int yy = -1;

		for (String row : missionDefinition.split("\n")) {
			yy++;
			int xx = -1;
			for (char c : row.toCharArray()) {
				xx++;

				int x = xx; // gridCellCountX - xx - 1;
				int y = gridCellCountY - yy - 1;

				if (c == '.') {
					gridCells2d[x][y].state = CellState.FREE;
				} else if (c == '#') {
					gridCells2d[x][y].state = CellState.WALL;
				} else {
					gridCells2d[x][y].state = CellState.WAY;
				}

				if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')) {
					wp.put((int) c, gridCells2d[x][y]);
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
		int x = (int) Math.floor(location.x / GridCell.size);
		int y = (int) Math.floor(location.y / GridCell.size);
		if (x >= 0 && x < gridCellCountX && y >= 0 && y < gridCellCountY) {
			return gridCells2d[x][y];
		} else {
			return null;
		}
	}

	@Override
	public Wave sendWave(final int waveNumber) {
		if (waveNumber >= enemyWaves.length)
			return null;

		return new Wave(waveNumber, game, this, game.timedCodeManager,
				enemyWaves[waveNumber]);
	}

	@Override
	public void onAllWavesCompleted() {
		Util.log("All waves completed -> mission completed");
		completed = true;
		game.eventListener.onMissionCompleted(this);
	}

	@Override
	public int getWaveCount() {
		return enemyWaves.length;
	}

	public Collection<Enemy> getEnemyPreview(int waveId) { // TODO: move to wave class once possible
		if (waveId >= getWaveCount()) {
			return new ArrayList<>();
		}

		LinkedHashMap<String, Enemy> col = new LinkedHashMap<>(); // keep insertion order
		for (WaveEnemyConfig we : this.enemyWaves[waveId]) {
			col.put(we.enemy.getClass().getSimpleName(), we.enemy);
		}
		return col.values();
	}

	protected abstract String getMissionDefinitionString();

	protected abstract WaveEnemyConfig[][] loadEnemyWaves();

	/**
	 * @return A list of towers that can be built in this mission.
	 */
	protected abstract Tower[] loadBuildableTowers();

	protected String getMissionName() {
		return this.getClass().getAnnotation(MissionName.class).value();
	}

	protected abstract MissionStatementText[] getMissionStatementTexts();

	/**
	 * @return The amount of money the player starts with.
	 */
	public abstract int getStartMoney();

	/**
	 * @return The number of lives the player starts with.
	 */
	public abstract int getStartLives();
}
