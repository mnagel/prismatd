package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.engine.MissionStatus;
import com.avona.games.towerdefence.events.EmptyEventListener;
import com.avona.games.towerdefence.events.EventDistributor;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.FeatureFlags;

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
		ed.listeners.add(new EmptyEventListener() {
			@Override
			public void onMenuRebuild() {
				buildEntries();
			}

			@Override
			public void onMissionSwitched(Mission mission) {
				buildEntries();
			}
		});

	}

	private void addButton(MenuButton button) {
		buttons.add(button);
	}

	public MenuButtonLayer getButtonLayer(MenuButton button) {
		return buttonLayers.get(button);
	}

	private MenuButtonLayer makeButtonLayer(MenuButton button) {
		int index = buttons.indexOf(button);

		MenuButtonLayer result = new MenuButtonLayer(
				"forButton" + button.toString(),
				0,
				this,
				index,
				buttons.size()
		);
		return result;
	}

	private void buildEntries() {
		resetEntries();

		if (game.missionStatus == MissionStatus.ACTIVE) {
			if (!(game.selectedObject instanceof Tower)) {
				for (int i = 0; i < game.mission.buildableTowers.length; i++) {
					final Tower t = game.mission.buildableTowers[i];

					addButton(new MenuButton("Build Tower " + i, MenuButtonLook.BUILD_TOWER) {
						@Override
						public void mouseBtn1DownAt(V2 location) {
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
				addButton(new MenuButton("Selected Tower Info", MenuButtonLook.TOWER_INFO) {
				});

				addButton(new MenuButton("Level Up Tower", MenuButtonLook.TOWER_UPGRADE) {
					@Override
					public void mouseBtn1DownAt(V2 location) {
						if (game.selectedObject instanceof Tower) {
							final Tower t = (Tower) game.selectedObject;
							game.levelUpTower(t);
						}
					}
				});
			}
		}

		if (FeatureFlags.SHOW_CONSOLE) {
			addButton(new MenuButton("Debug Info", MenuButtonLook.DEBUG_INFO) {
			});
		}

		addButton(new MenuButton("Game Info", MenuButtonLook.GAME_INFO) {
		});

		addButton(new MenuButton("Next Wave", MenuButtonLook.NEXT_WAVE) {
			@Override
			public void mouseBtn1DownAt(V2 location) {
				layerHerder.menuLayer.rootInputActor.pressedOtherKey(' ');
			}
		});


		for (MenuButton button : buttons) {
			MenuButtonLayer layer = makeButtonLayer(button);
			buttonLayers.put(button, layer);
			layerHerder.addLayer(layer);
			rootInputActor.inputLayerMap.put(layer, button);
		}
	}

	void resizeChildren() {
		for (MenuButton button : buttons) {
			getButtonLayer(button).adjustSize();
		}
	}

	private void resetEntries() {
		for (MenuButton button : buttons) {
			Layer layer = getButtonLayer(button);
			layerHerder.removeLayer(layer);
			rootInputActor.inputLayerMap.remove(layer);
		}
		buttonLayers = new HashMap<>();
		buttons = new ArrayList<>();
	}

}
