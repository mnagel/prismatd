package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.ai.AI;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.engine.MissionStatus;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.avona.games.towerdefence.util.Util;

import java.util.LinkedHashMap;

public class GameInputActor extends EmptyInputActor {
	private Game game;
	private Mouse mouse;

	public GameInputActor(Game game, Mouse mouse) {
		this.game = game;
		this.mouse = mouse;
	}

	@Override
	public void mouseBtn1DownAt(V2 location) {
		if (game.selectedBuildTower == null) {
			checkMouseOverTower(location);
		} else {
			game.draggingTower = true;
		}
	}

	@Override
	public void mouseBtn1UpAt(V2 location) {
		if (game.draggingTower) {
			if (game.canBuildTowerAt(game.mission.getCellAt(location))) {
				game.addTowerAt(game.mission.getCellAt(location));
			}
		}

		game.selectedBuildTower = null;
		game.draggingTower = false;
	}

	@Override
	public void mouseBtn2DownAt(V2 location) {
		pressForwardButton();
	}

	private void pressForwardButton() {
		if (game.missionStatus == MissionStatus.ACTIVE) {
			game.waveTracker.startNextWave();
		} else if (game.missionStatus == MissionStatus.WON) {
			loadMissionInteractive();
		} else {
			LinkedHashMap<String, Runnable> options = new LinkedHashMap<>();
			options.put("Go Back to my game.", new Runnable() {
				@Override
				public void run() {
				}
			});
			options.put("Restart the current mission.", new Runnable() {
				@Override
				public void run() {
					game.reloadMission();
				}
			});
			options.put("Start another mission...", new Runnable() {
				@Override
				public void run() {
					loadMissionInteractive();
				}
			});
			AsyncInput.chooseNamedRunnable("What do you want to do?", true, options);
		}
	}

	private void loadMissionInteractive() {
		Util.log("loadMissionInteractive");
		String[] missions = MissionList.getAvailableMissionNames();
		AsyncInput.chooseRunnable("Load Mission", missions, true, new IAsyncInput.MyRunnable() {
			@Override
			public void run(int selectedOption) {
				Util.log("run");
				if (selectedOption != -1) {
					game.loadMission(selectedOption);
				}
			}
		});
	}

	@Override
	public void pressedOtherKey(char keyCode) {
		// SPACE <<< yeah, search function works now
		if (keyCode == ' ') {
			pressForwardButton();
		}
		if (keyCode == '+') {
			if (game.selectedObject instanceof Tower) {
				final Tower t = (Tower) game.selectedObject;
				game.levelUpTower(t);
			}
		}
		if (keyCode == 'a') {
			new AI().buildAtBestPosition(game);
		}
		if (keyCode == 'b') {
			LinkedHashMap<String, Runnable> options = new LinkedHashMap<>();
			options.put("Go Back to my game.", new Runnable() {
				@Override
				public void run() {
				}
			});
			options.put("Restart the current mission.", new Runnable() {
				@Override
				public void run() {
					game.reloadMission();
				}
			});
			options.put("Start another mission...", new Runnable() {
				@Override
				public void run() {
					loadMissionInteractive();
				}
			});
			options.put("Cheat...", new Runnable() {
				@Override
				public void run() {
					pressedOtherKey('c');
				}
			});
			options.put("Quit the game.", new Runnable() {
				@Override
				public void run() {
					game.isTerminated = true;
				}
			});
			AsyncInput.chooseNamedRunnable("What do you want to do?", true, options);
		}
		if (keyCode == 'c') {
			LinkedHashMap<String, Runnable> options = new LinkedHashMap<>();
			options.put("Go Back to my game.", new Runnable() {
				@Override
				public void run() {
				}
			});
			options.put("Log Debug Info.", new Runnable() {
				@Override
				public void run() {
					pressedOtherKey('d');
				}
			});
			options.put("Kill All Enemies.", new Runnable() {
				@Override
				public void run() {
					pressedOtherKey('k');
				}
			});
			options.put("Get 10000 Money.", new Runnable() {
				@Override
				public void run() {
					game.money = 10000;
				}
			});
			AsyncInput.chooseNamedRunnable("Pick your cheat:", true, options);
		}
		if (keyCode == 'd') {
			FeatureFlags.SHOW_CONSOLE = !FeatureFlags.SHOW_CONSOLE;
			game.eventDistributor.onMenuRebuild();
			game.logDebugInfo();
		}
		if (keyCode == 'k') {
			game.killAllEnemies();
		}
		if (keyCode == 'l') {
			loadMissionInteractive();
		}
		if (keyCode == 's') {
			if (game.selectedObject instanceof Tower) {
				final Tower t = (Tower) game.selectedObject;
				game.sellTower(t);
			}
		}
		if (keyCode >= '1' && keyCode <= '9') {
			int i = Integer.parseInt(String.valueOf(keyCode));
			if (i > game.mission.buildableTowers.length) {
				return;
			}

			Tower buttoned = game.mission.buildableTowers[i - 1];

			if (game.selectedObject instanceof Tower) {
				Tower selected = (Tower) game.selectedObject;
				if (buttoned.getClass() == selected.getClass()) {
					game.levelUpTower(selected);
				}
			} else {
				if (buttoned != game.selectedBuildTower) {
					game.selectedBuildTower = buttoned;
				} else {
					game.selectedBuildTower = null;
				}
			}
		}
	}

	private void checkMouseOverTower(V2 location) {
		final Tower t = game.getTowerWithinRadius(location, mouse.radius);
		if (t != null) {
			game.selectedObject = t;
		} else {
			game.selectedObject = game.getEnemyWithinRadius(location, mouse.radius);
		}
		game.eventDistributor.onMenuRebuild();
	}

	@Override
	public void mouseMovedTo(V2 location) {
		// checkMouseOverTower(location);
	}

	@Override
	public String toString() {
		return "GameInputActor";
	}
}
