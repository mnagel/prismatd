package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MenuLayer extends Layer {
	public List<MenuButton> buttons = new ArrayList<>();
	Game game;

	public MenuLayer(String name, Game game) {
		super(
				name,
				1,
				null,
				new V2(),
				new V2(),
				new V2(125, 480)
		);
		this.game = game;
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
				0,
				this,
				new V2(this.offset.x, this.offset.y + this.region.y - (index + 1) * ysize),
				new V2(this.region.x, ysize),
				new V2(GridCell.size, GridCell.size)
		);
		return res;
	}
	
	public void buildEntries() {


		// TODO handle these states in the menu
		if (!(game.selectedObject instanceof Tower)) {
			for (int i = 0; i < game.mission.buildableTowers.length; i++) {
				final Tower t = game.mission.buildableTowers[i];

				addButton(new MenuButton("tower" + i, MenuButton.MenuButtonLook.BUILD_TOWER) {
					@Override
					public void onClick() {
						Util.log("klicked tower button");
						game.selectedBuildTower = t;
					}

					@Override
					public Object getRenderExtra() {
						return t;
					}
				});
			}
		}

		if (game.selectedObject instanceof Tower) {
			addButton(new MenuButton("levelup", MenuButton.MenuButtonLook.UPGRADE_TOWER) {
				@Override
				public void onClick() {
					Util.log("klicked lvlup button");
					if (game.selectedObject instanceof Tower) {
						final Tower t = (Tower) game.selectedObject;
						game.levelUpTower(t);
					}
				}

				@Override
				public Object getRenderExtra() {
					return null;
				}
			});
		}

		addButton(new MenuButton("wave", MenuButton.MenuButtonLook.NEXT_WAVE) {

			@Override
			public void onClick() {
				//parent.pressedOtherKey(' ');
				Util.log("klicked wave button");
			}

			@Override
			public Object getRenderExtra() {
				return null;
			}
		});


	}



}
