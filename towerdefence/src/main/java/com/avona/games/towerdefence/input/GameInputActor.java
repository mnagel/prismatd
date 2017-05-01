package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.mission.MissionList;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.Util;

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
							"Start another mission...",
							"Cheat...",
							"Quit the game."
					},
					new IAsyncInput.MyRunnable() {
						@Override
						public void run(int selectedOption) {
							switch (selectedOption) {
								default:
								case 0:
									break;
								case 1:
									loadMissionInteractive();
									break;
								case 2:
									pressedOtherKey('c');
									break;
								case 3:
									game.isTerminated = true;
									break;
							}
						}
					}
			);
		}
		if (keyCode == 'c') {
			AsyncInput.runnableChooser(
					"Pick your cheat:",
					new String[]{
							"Go Back to my game.",
							"Log Debug Info.",
							"Kill All Enemies.",
					},
					new IAsyncInput.MyRunnable() {
						@Override
						public void run(int selectedOption) {
							switch (selectedOption) {
								default:
								case 0:
									break;
								case 1:
									pressedOtherKey('d');
									break;
								case 2:
									pressedOtherKey('k');
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
		} else {
			game.selectedObject = game.getEnemyWithinRadius(location, mouse.radius);
		}
		game.eventListener.onMenuRebuild();
	}

	@Override
	public void mouseMovedTo(V2 location) {
		// checkMouseOverTower(location);
	}
}
