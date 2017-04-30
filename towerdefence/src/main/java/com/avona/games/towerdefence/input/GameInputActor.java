package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.*;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.tower.Tower;

public class GameInputActor extends EmptyInputActor {
	private Game game;
	private Mouse mouse;

	public GameInputActor(Game game, Mouse mouse) {
		this.game = game;
		this.mouse = mouse;
	}

	@Override
	public void mouseBtn1DownAt(V2 location) {
		if (game.selectedBuildTower == null)
			return;
		game.draggingTower = true;
	}

	@Override
	public void mouseBtn1UpAt(V2 location) {
		if (!game.draggingTower)
			return;
		game.draggingTower = false;

		if (game.canBuildTowerAt(game.mission.getCellAt(location))) {
			game.addTowerAt(game.mission.getCellAt(location));
		}
		checkMouseOverTower(location);
	}

	@Override
	public void mouseBtn2DownAt(V2 location) {
		if (game.mission.completed) {
			loadMissionInteractive();
		} else {
			game.mission.waveTracker.startNextWave();
		}
	}

	private void pressForwardButton() {
		if (game.mission.completed) {
			loadMissionInteractive();
		} else {
			game.mission.waveTracker.startNextWave();
		}
	}

	private void loadMissionInteractive() {
		Util.log("loadMissionInteractive");
		String[] missions = MissionList.getAvailableMissionNames();
		AsyncInput.runnableChooser("Load Mission", missions, new IAsyncInput.MyRunnable() {
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
		if (keyCode == 'b') {
			//pause();
			AsyncInput.runnableChooser(
					"What do you want to do?",
					new String[]{
							"Go Back to my game.",
							"Start another mission.",
							"Quit the game."
					},
					new IAsyncInput.MyRunnable() {
						@Override
						public void run(int selectedOption) {
							switch (selectedOption) {
								case 1:
									loadMissionInteractive();
								default:
								case 0:
									//resume();
									break;
								case 2:
									game.isTerminated = true;
									break;
							}
						}
					}
			);
		}
		if (keyCode == 'd') {
			game.logDebugInfo();
		}
		if (keyCode == 'k') {
			game.killAllEnemies();
		}
		if (keyCode == 'l') {
			loadMissionInteractive();
		}
	}

	@Override
	public void mouseExited() {
		game.draggingTower = false;
	}

	private void checkMouseOverTower(V2 location) {
		final Tower t = game.getTowerWithinRadius(location, mouse.radius);
		if (t != null) {
			game.selectedObject = t;
			return;
		}
		game.selectedObject = game.getEnemyWithinRadius(location, mouse.radius);
	}

	@Override
	public void mouseMovedTo(V2 location) {
		checkMouseOverTower(location);
	}
}
