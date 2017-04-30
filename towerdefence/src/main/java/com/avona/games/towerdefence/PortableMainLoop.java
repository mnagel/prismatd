package com.avona.games.towerdefence;

import com.avona.games.towerdefence.gfx.Display;
import com.avona.games.towerdefence.gfx.DisplayEventDistributor;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.input.*;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.time.TimeTrack;

import java.io.Serializable;

public abstract class PortableMainLoop implements Serializable {
	public static final String GAME_LAYER_NAME = "game";
	public static final String MENU_LAYER_NAME = "menu";
	private static final long serialVersionUID = 1L;
	private static final float FIXED_TICK = 0.04f;
	public Game game;
	public Display display;
	public PortableGraphicsEngine ge;
	public InputActor rootInputActor;
	public Mouse mouse = new Mouse();
	public LayerHerder layerHerder = new LayerHerder();
	public EventDistributor eventListener = new EventDistributor();
	public DisplayEventDistributor displayEventListener = new DisplayEventDistributor();
	protected TimeTrack wallTime = new TimeTrack();
	private float gameTicks = 0;

	public static double getWallClock() {
		return System.nanoTime() * Math.pow(10, -9);
	}

	protected void initWithGame() {
		Layer gameLayer = new Layer();
		gameLayer.virtualRegion.x = Mission.WIDTH;
		gameLayer.virtualRegion.y = Mission.HEIGHT;
		gameLayer.name = GAME_LAYER_NAME;
		layerHerder.addLayer(gameLayer);

		Layer menuLayer = new Layer();
		menuLayer.virtualRegion.x = 125;
		menuLayer.virtualRegion.y = 480;
		menuLayer.name = MENU_LAYER_NAME;
		layerHerder.addLayer(menuLayer);
	}

	public void setupInputActors() {

		GameInputActor gameInputActor = new GameInputActor(game, mouse);
		LayeredInputActor lia = new LayeredInputActor(this, mouse, layerHerder);
		rootInputActor = lia;
		lia.inputLayerMap.put(layerHerder.findLayerByName(GAME_LAYER_NAME), gameInputActor);
		final Layer menuLayer = layerHerder.findLayerByName(MENU_LAYER_NAME);
		lia.inputLayerMap.put(menuLayer, new MenuInputActor(rootInputActor, game, menuLayer));
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
