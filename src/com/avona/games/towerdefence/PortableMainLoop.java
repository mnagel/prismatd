package com.avona.games.towerdefence;

import java.io.Serializable;

import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.inputActors.GameInputActor;
import com.avona.games.towerdefence.inputActors.LayeredInputActor;
import com.avona.games.towerdefence.inputActors.MenuInputActor;
import com.avona.games.towerdefence.waveListeners.GrantInterestPerWave;
import com.avona.games.towerdefence.world.World;

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
	protected TimeTrack gameTime = new TimeTrack();
	protected TimeTrack graphicsTime = new TimeTrack();
	protected TimedCodeManager timedCodeManager = new TimedCodeManager();
	private float gameTicks = 0;

	public PortableMainLoop() {
		game = new Game(gameTime, timedCodeManager, eventListener);

		Layer gameLayer = new Layer();
		gameLayer.virtualRegion.x = World.WIDTH;
		gameLayer.virtualRegion.y = World.HEIGHT;
		gameLayer.name = GAME_LAYER_NAME;
		layerHerder.addLayer(gameLayer);

		Layer menuLayer = new Layer();
		menuLayer.virtualRegion.x = 125;
		menuLayer.virtualRegion.y = 480;
		menuLayer.name = MENU_LAYER_NAME;
		layerHerder.addLayer(menuLayer);

		game.waveBegunListeners.add(new GrantInterestPerWave(game, 0.10f));
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
		graphicsTime.update(wallClock);
		gameTime.update(wallClock);
		timedCodeManager.update(gameTime.tick);

		// Updating of inputs is done asynchronously.

		// Update the world with a fixed rate.
		gameTicks += gameTime.tick;
		while (gameTicks >= FIXED_TICK) {
			game.updateWorld(FIXED_TICK);
			gameTicks -= FIXED_TICK;
		}

		// Show the world.
		ge.render(gameTime.tick, graphicsTime.tick);
	}

	public void pauseGame() {
		gameTime.stopClock();
	}

	public void unpauseGame() {
		gameTime.startClock();
	}

	public void toggleGamePause() {
		if (gameTime.isRunning()) {
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
