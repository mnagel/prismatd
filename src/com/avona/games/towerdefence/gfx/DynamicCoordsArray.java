package com.avona.games.towerdefence.gfx;

import java.util.ArrayList;

import com.avona.games.towerdefence.V2;

public class DynamicCoordsArray {
	public ArrayList<V2> coords = new ArrayList<V2>();

	public void addCoord(final float x, final float y) {
		coords.add(new V2(x, y));
	}

	public void addCoord(final double x, final double y) {
		coords.add(new V2((float) x, (float) y));
	}

	public void addCoord(final V2 p) {
		coords.add(p);
	}

	public void loadIntoVertexArray(final VertexArray va) {
		va.numCoords = coords.size();
		va.reserveBuffers();
		for (V2 coord : coords) {
			va.addCoord(coord);
		}
	}
}
