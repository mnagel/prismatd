package com.avona.games.towerdefence.level;

import com.avona.games.towerdefence.V2;

public class GridCell {
	public static final float width = 40.0f;
	public static final float heigth = 40.0f;

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