package com.avona.games.towerdefence.engine;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.events.EventDistributor;
import com.avona.games.towerdefence.gfx.Display;
import com.avona.games.towerdefence.gfx.DisplayEventDistributor;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.input.*;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.time.TimeTrack;
import com.avona.games.towerdefence.util.FeatureFlags;

import java.io.Serializable;

public abstract class PortableMainLoop implements Serializable {
	public static final String GAME_LAYER_NAME = "game";
	public static final String MENU_LAYER_NAME = "menu";
	private static final long serialVersionUID = 1L;
	private static final float FIXED_TICK = 0.04f;
	public Game game;
	public Display display;
	public PortableGraphicsEngine ge;
	public LayeredInputActor rootInputActor;
	public Mouse mouse = new Mouse();
	public LayerHerder layerHerder = new LayerHerder();
	public EventDistributor eventDistributor = new EventDistributor();
	public DisplayEventDistributor displayEventListener = new DisplayEventDistributor();
	protected TimeTrack wallTime = new TimeTrack();
	private float gameTicks = 0;

	public static double getWallClock() {
		return System.nanoTime() * Math.pow(10, -9);
	}

	protected void initWithGame() {
		Layer gameLayer = new Layer(
				GAME_LAYER_NAME,
				1,
				null,
				new V2(),
				new V2(),
				new V2(Mission.WIDTH, Mission.HEIGHT)
		);
		layerHerder.addLayer(gameLayer);

		Layer menuLayer = new MenuLayer(MENU_LAYER_NAME, game);
		layerHerder.addLayer(menuLayer);

		setupInputActors();

		//noinspection ConstantConditions
		if (FeatureFlags.AUTOSTART_MISSION != -1) {
			game.loadMission(FeatureFlags.AUTOSTART_MISSION - 1); // array 0-based
		} else {
			rootInputActor.pressedOtherKey('l');
		}
	}

	public void setupInputActors() {
		LayeredInputActor lia = new LayeredInputActor(this, mouse, layerHerder);
		rootInputActor = lia;

		Layer gameLayer = layerHerder.findLayerByName(GAME_LAYER_NAME);
		GameInputActor gameInputActor = new GameInputActor(game, mouse);
		lia.inputLayerMap.put(gameLayer, gameInputActor);

		Layer menuLayer = layerHerder.findLayerByName(MENU_LAYER_NAME);
		MenuInputActor menuInputActor = new MenuInputActor(rootInputActor, game, menuLayer);
		lia.inputLayerMap.put(menuLayer, menuInputActor);
	}

	public void performIteration() {
		if (game.isTerminated) {
			exit();
		}

		final double wallClock = getWallClock();
		wallTime.update(wallClock);

		// Updating of inputs is done asynchronously.
		// Update the world with a fixed rate.
		gameTicks += wallTime.tick;
		while (gameTicks >= FIXED_TICK) {
			game.updateWorld(FIXED_TICK);
			gameTicks -= FIXED_TICK;
		}

		// Show the world.
		ge.render(wallTime.tick);
		if (display != null) {
			display.checkGLError("after frame render");
		}
	}

	public void pauseGame() {
		game.pause();
	}

	public void unpauseGame() {
		game.unpause();
	}

	public void toggleGamePause() {
		if (game.isPaused()) {
			unpauseGame();
		} else {
			pauseGame();
		}
	}

	public abstract void exit();

	public void serialize() {
		// TODO: Make this abstract.
	}
}
