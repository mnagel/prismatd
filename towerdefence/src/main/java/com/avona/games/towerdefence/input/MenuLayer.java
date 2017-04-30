package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.mission.GridCell;

import java.util.ArrayList;
import java.util.List;

public class MenuLayer extends Layer {
	public List<MenuButton> buttons = new ArrayList<>();

	public MenuLayer(String name) {
		super(
				name,
				null,
				new V2(),
				new V2(),
				new V2(125, 480)
		);
	}

	public void addButton(MenuButton b) {
		buttons.add(b);
	}

	public void removeButton(MenuButton b) {
		buttons.remove(b);
	}

	public void resetButtons() {
		buttons = new ArrayList<>();
	}

	public Layer getButtonLayer(MenuButton b) {
		int index = buttons.indexOf(b);

		float ysize = this.region.y / buttons.size();

		Layer res = new Layer(
				"forButton" + b.toString(),
				this,
				new V2(this.offset.x, this.offset.y + this.region.y - (index + 1) * ysize),
				new V2(this.region.x, ysize),
				new V2(GridCell.size, GridCell.size)
		);
		return res;
	}
}
