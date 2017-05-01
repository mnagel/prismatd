package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.mission.GridCell;

class MenuButtonLayer extends Layer {
	private final int buttonCount;
	private final int myIndex;

	MenuButtonLayer(String name, int depth, Layer parent, int myIndex, int buttonCount) {
		super(
				name,
				depth,
				parent,
				new V2(parent.offset.x, parent.offset.y + parent.region.y - (myIndex + 1) * (parent.region.y / buttonCount)),
				new V2(parent.region.x, (parent.region.y / buttonCount)),
				new V2(GridCell.size, GridCell.size)
		);
		this.parent = parent;
		this.myIndex = myIndex;
		this.buttonCount = buttonCount;
	}

	void adjustSize() {
		this.offset = new V2(parent.offset.x, parent.offset.y + parent.region.y - (myIndex + 1) * (parent.region.y / buttonCount));
		this.region = new V2(parent.region.x, (parent.region.y / buttonCount));
	}
}
