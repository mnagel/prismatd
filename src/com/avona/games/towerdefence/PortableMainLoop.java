package com.avona.games.towerdefence;

import java.io.Serializable;

import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.inputActors.GameInputActor;
import com.avona.games.towerdefence.inputActors.LayeredInputActor;
import com.avona.games.towerdefence.inputActors.MenuInputActor;
import com.avona.games.towerdefence.level.Level;

public abstract class PortableMainLoop implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final float FIXED_TICK = 0.04f;

	public static final String GAME_LAYER_NAME = "game";
	public static final String MENU_LAYER_NAME = "menu";

	public Game game;
	public PortableGraphicsEngine ge;
	public LayeredInputActor inputActor;
	public Mouse mouse = new Mouse();
	public LayerHerder layerHerder = new LayerHerder();
	public EventDistributor eventListener = new EventDistributor();
	protected TimeTrack graphicsTime = new TimeTrack();
	protected TimeTrack fullGameTime = new TimeTrack();
	private float gameTicks = 0;

	public PortableMainLoop() {
		game = new Game(eventListener);

		Layer gameLayer = new Layer();
		gameLayer.virtualRegion.x = Level.WIDTH;
		gameLayer.virtualRegion.y = Level.HEIGHT;
		gameLayer.name = GAME_LAYER_NAME;
		layerHerder.addLayer(gameLayer);

		Layer menuLayer = new Layer();
		menuLayer.virtualRegion.x = 125;
		menuLayer.virtualRegion.y = 480;
		menuLayer.name = MENU_LAYER_NAME;
		layerHerder.addLayer(menuLayer);
	}

	public void setupInputActors() {
		inputActor = new LayeredInputActor(this, mouse, layerHerder);
		inputActor.inputLayerMap.put(layerHerder
				.findLayerByName(GAME_LAYER_NAME), new GameInputActor(game,
				mouse));
		inputActor.inputLayerMap.put(layerHerder
				.findLayerByName(MENU_LAYER_NAME), new MenuInputActor(game));
	}

	public static double getWallClock() {
		return System.nanoTime() * Math.pow(10, -9);
	}

	public void performLoop() {
		final double wallClock = getWallClock();
		fullGameTime.update(wallClock);
		graphicsTime.update(wallClock);

		// Updating of inputs is done asynchronously.
		// Update the world with a fixed rate.
		gameTicks += fullGameTime.tick;
		while (gameTicks >= FIXED_TICK) {
			game.updateWorld(FIXED_TICK);
			gameTicks -= FIXED_TICK;
		}

		// Show the world.
		ge.render(game.gameTime.tick, graphicsTime.tick);
	}

	public void pauseGame() {
		fullGameTime.stopClock();
		game.pause();
	}

	public void unpauseGame() {
		fullGameTime.startClock();
		game.unpause();
	}

	public void toggleGamePause() {
		if (fullGameTime.isRunning()) {
			pauseGame();
		} else {
			unpauseGame();
		}
	}

	public abstract void exit();

	public void serialize() {
		// TODO: Make this abstract.
	}
}
