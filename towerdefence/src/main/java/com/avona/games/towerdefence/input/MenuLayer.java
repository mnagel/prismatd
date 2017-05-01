package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.events.EventDistributor;
import com.avona.games.towerdefence.events.IEventListener;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuLayer extends Layer {
	private final Game game;
	private final LayerHerder layerHerder;
	public List<MenuButton> buttons = new ArrayList<>();
	public LayeredInputActor rootInputActor;
	private Map<MenuButton, MenuButtonLayer> buttonLayers = new HashMap<>();

	public MenuLayer(String name, Game game, LayerHerder layerHerder, EventDistributor ed) {
		super(
				name,
				1,
				null,
				new V2(),
				new V2(),
				new V2(125, 480)
		);
		this.game = game;
		this.layerHerder = layerHerder;
		ed.listeners.add(new IEventListener() {
			@Override
			public void onBuildTower(Tower t) {

			}

			@Override
			public void onMenuRebuild() {
				Util.log("rebuilding menu");
				buildEntries();
			}

			@Override
			public void onMissionSwitched(Mission mission) {
				Util.log("missionswitch menulayer");
				buildEntries();
			}

			@Override
			public void onMissionCompleted(Mission mission) {

			}

			@Override
			public void onGameCompleted(Game g) {

			}

			@Override
			public void onGameOver(Game g) {

			}
		});

	}

	public void addButton(MenuButton b) {
		buttons.add(b);
	}

	public MenuButtonLayer getButtonLayer(MenuButton b) {
		return buttonLayers.get(b);
	}


	private MenuButtonLayer makeButtonLayer(MenuButton b) {
		int index = buttons.indexOf(b);


		MenuButtonLayer res = new MenuButtonLayer(
				"forButton" + b.toString(),
				0,
				this,
				index,
				buttons.size()
		);
		return res;
	}

	public void buildEntries() {
		Util.log("build entries");
		killEntries();


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
				layerHerder.menuLayer.
						rootInputActor.pressedOtherKey(' ');
				Util.log("klicked wave button");
			}

			@Override
			public Object getRenderExtra() {
				return null;
			}
		});


		for (MenuButton b : buttons) {
			MenuButtonLayer x = makeButtonLayer(b);
			Util.log(b.toString());
			Util.log(x.toString());
			buttonLayers.put(b, x);
			layerHerder.addLayer(x);
			//Util.log("telling root inp");
			MenuButtonInputActor y = new MenuButtonInputActor(b);
			Util.log("add input mapping from " + x + " to " + y);
			rootInputActor.inputLayerMap.put(x, y);
		}
	}

	public void pushDownResize() {
		for (MenuButton b : buttons) {
			getButtonLayer(b).adjustSize();
		}
	}

	public void killEntries() {
		for (MenuButton b : buttons) {
			Layer x = getButtonLayer(b);
			//buttonLayers.add(x);
			layerHerder.removeLayer(x);
			rootInputActor.inputLayerMap.remove(x);
		}
		buttonLayers = new HashMap<>();
		buttons = new ArrayList<>();
	}

}
