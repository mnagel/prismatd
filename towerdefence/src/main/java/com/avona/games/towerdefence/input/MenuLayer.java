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
		MenuButtonLayer result = new MenuButtonLayer(
				"forButton" + button.toString(),
				0,
				this
		);
		return result;
	}

	private void buildEntries() {
		resetEntries();

		addButton(
				new MenuButton(
						"Game Info",
						MenuButtonLook.GAME_INFO,
						0.33f
				) {
				});

		if (FeatureFlags.SHOW_CONSOLE) {
			addButton(new MenuButton("Debug Info", MenuButtonLook.DEBUG_INFO, 0.166f) {
			});
		}

		if (game.missionStatus == MissionStatus.ACTIVE) {
			if (!(game.selectedObject instanceof Tower)) {
				for (int i = 0; i < game.mission.buildableTowers.length; i++) {
					final int ii = i;
					final Tower t = game.mission.buildableTowers[ii];

					addButton(new MenuButton("Build Tower " + i, MenuButtonLook.BUILD_TOWER) {
						@Override
						public void mouseBtn1DownAt(V2 location) {
							layerHerder.menuLayer.rootInputActor.pressedOtherKey(Character.forDigit(ii + 1, 10));
						}

						@Override
						public Object getRenderExtra() {
							return t;
						}
					});
				}
			} else {
				final Tower t = (Tower) game.selectedObject;
				if (t.canUpgrade()) {
					addButton(new MenuButton("Tower Upgrade", MenuButtonLook.TOWER_UPGRADE) {
						@Override
						public void mouseBtn1DownAt(V2 location) {
							layerHerder.menuLayer.rootInputActor.pressedOtherKey('+');
						}
					});
				}

				addButton(new MenuButton("Tower Info", MenuButtonLook.TOWER_INFO) {
				});

				addButton(new MenuButton("Sell Tower", MenuButtonLook.TOWER_SELL) {
					@Override
					public void mouseBtn1DownAt(V2 location) {
						layerHerder.menuLayer.rootInputActor.pressedOtherKey('s');
					}
				});
			}
		}

		addButton(new MenuButton("Next Wave", MenuButtonLook.NEXT_WAVE) {
			@Override
			public void mouseBtn1DownAt(V2 location) {
				layerHerder.menuLayer.rootInputActor.pressedOtherKey(' ');
			}
		});

		for (MenuButton button : buttons) {
			MenuButtonLayer layer = makeButtonLayer(button);
			resizeButton(button, layer);
			buttonLayers.put(button, layer);
			layerHerder.addLayer(layer);
			rootInputActor.inputLayerMap.put(layer, button);
		}
	}

	void resizeChildren() {
		for (MenuButton button : buttons) {
			resizeButton(button, getButtonLayer(button));
		}
	}

	private void resizeButton(MenuButton button, MenuButtonLayer layer) {
		float totalWeight = 0;
		for (MenuButton b : buttons) {
			totalWeight += b.weight;
		}
		float prequelWeight = 0;
		for (MenuButton b : buttons) {
			prequelWeight += b.weight;
			if (b == button) {
				break;
			}
		}
		prequelWeight = totalWeight - prequelWeight;

		float height = region.y * button.weight / totalWeight;
		V2 off = new V2(offset.x, offset.y + region.y * (prequelWeight / totalWeight));
		V2 size = new V2(region.x, height);
		layer.adjustSize(off, size);
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
