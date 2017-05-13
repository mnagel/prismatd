package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.mission.GridCell;

class MenuButtonLayer extends Layer {
	MenuButtonLayer(String name, int depth, Layer parent) {
		super(name, depth, parent, null, null, new V2(GridCell.size, GridCell.size));
		this.parent = parent;
	}

	void adjustSize(V2 offset, V2 region) {
		this.offset = offset;
		this.region = region;
	}
}
