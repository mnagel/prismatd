package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.V2;

public class GridCell {
	public static final float size = 40.0f;

	public int x;
	public int y;
	public CellState state;
	public V2 center;

	public GridCell(int x, int y, V2 center, CellState state) {
		this.x = x;
		this.y = y;
		this.center = center;
		this.state = state;
	}
}
