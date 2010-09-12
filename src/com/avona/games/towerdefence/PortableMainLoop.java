package com.avona.games.towerdefence;

public abstract class PortableMainLoop {

	private static final float FIXED_TICK = 0.04f;
	
	public static final String GAME_LAYER_NAME = "game";
	public static final String MENU_LAYER_NAME = "menu";

	public Game game = new Game();
	public PortableGraphicsEngine ge;
	public InputActor inputActor;
	public Mouse mouse = new Mouse();
	public LayerHerder layerHerder = new LayerHerder();
	protected TimeTrack gameTime = new TimeTrack();;
	protected TimeTrack graphicsTime = new TimeTrack();
	private float gameTicks = 0;
	
	public PortableMainLoop() {
		Layer gameLayer = new Layer();
		gameLayer.virtualRegion.x = World.WIDTH;
		gameLayer.virtualRegion.y = World.HEIGHT;
		gameLayer.name = GAME_LAYER_NAME;
		layerHerder.layers.add(gameLayer);

		Layer menuLayer = new Layer();
		menuLayer.virtualRegion.x = 125;
		menuLayer.virtualRegion.y = 480;
		menuLayer.name = MENU_LAYER_NAME;
		layerHerder.layers.add(menuLayer);
	}

	public static double getWallClock() {
		return System.nanoTime() * Math.pow(10, -9);
	}

	public void performLoop() {
		final double wallClock = getWallClock();
		graphicsTime.update(wallClock);
		gameTime.update(wallClock);

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

}