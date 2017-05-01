package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.events.EmptyEventListener;
import com.avona.games.towerdefence.events.EventDistributor;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.tower.Tower;

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

		if (!(game.selectedObject instanceof Tower)) {
			for (int i = 0; i < game.mission.buildableTowers.length; i++) {
				final Tower t = game.mission.buildableTowers[i];

				addButton(new MenuButton("Build Tower " + i, MenuButton.MenuButtonLook.BUILD_TOWER) {
					@Override
					public void onClick() {
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
			addButton(new MenuButton("Level Up Tower", MenuButton.MenuButtonLook.UPGRADE_TOWER) {
				@Override
				public void onClick() {
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

		addButton(new MenuButton("Next Wave", MenuButton.MenuButtonLook.NEXT_WAVE) {
			@Override
			public void onClick() {
				layerHerder.menuLayer.rootInputActor.pressedOtherKey(' ');
			}

			@Override
			public Object getRenderExtra() {
				return null;
			}
		});


		for (MenuButton button : buttons) {
			MenuButtonLayer layer = makeButtonLayer(button);
			buttonLayers.put(button, layer);
			layerHerder.addLayer(layer);
			MenuButtonInputActor inputActor = new MenuButtonInputActor(button);
			rootInputActor.inputLayerMap.put(layer, inputActor);
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
