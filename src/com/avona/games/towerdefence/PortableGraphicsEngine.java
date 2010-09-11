package com.avona.games.towerdefence;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public abstract class PortableGraphicsEngine {

	public static final int DEFAULT_HEIGHT = 480;
	public static final int DEFAULT_WIDTH = 675;

	public static final double TOWER_WIDTH = 16;

	public V2 size;

	public LayerHerder layerHerder = new LayerHerder();
	public Layer menuLayer;
	public Layer gameLayer;

	protected TimeTrack graphicsTime = new TimeTrack();
	protected TickRater graphicsTickRater = new TickRater(graphicsTime);
	protected Game game;

	private FloatBuffer squareVertexBuffer;
	private FloatBuffer squareColorBuffer;

	public PortableGraphicsEngine(Game game) {
		this.game = game;

		gameLayer = new Layer();
		gameLayer.virtualRegion.x = World.WIDTH;
		gameLayer.virtualRegion.y = World.HEIGHT;
		gameLayer.level = 0;
		gameLayer.name = "game";
		layerHerder.layers.add(gameLayer);

		menuLayer = new Layer();
		menuLayer.virtualRegion.x = 125;
		menuLayer.virtualRegion.y = 480;
		menuLayer.level = 0;
		menuLayer.name = "menu";
		layerHerder.layers.add(menuLayer);

		squareVertexBuffer = allocateFloatBuffer(4 * 2);
		squareColorBuffer = allocateFloatBuffer(4 * 4);
	}

	public abstract void prepareTransformationForLayer(Layer layer);

	public abstract void resetTransformation();

	protected abstract void prepareScreen();

	protected abstract void drawTriangleStrip(final int vertices,
			final FloatBuffer vertexBuffer, final FloatBuffer colorBuffer);

	protected abstract void drawLine(final int vertices,
			final FloatBuffer vertexBuffer, final FloatBuffer colorBuffer);

	protected abstract void drawTriangleFan(final int vertices,
			final FloatBuffer vertexBuffer, final FloatBuffer colorBuffer);

	public abstract void drawText(final String text, final double x,
			final double y, final float colR, final float colG,
			final float colB, final float colA);

	public abstract V2 getTextBounds(final String text);

	public void render(float gameDelta, float graphicsDelta) {
		graphicsTime.updateTick(graphicsDelta);
		graphicsTickRater.updateTickRate();

		prepareScreen();

		prepareTransformationForLayer(gameLayer);
		renderWorld();

		for (Enemy e : game.enemies) {
			renderEnemy(e);
		}
		for (Tower t : game.towers) {
			renderTower(t);
		}
		for (Particle p : game.particles) {
			renderParticle(p);
		}
		resetTransformation();

		prepareTransformationForLayer(menuLayer);
		renderMenu();
		resetTransformation();

		renderStats();
		renderMouse();
	}

	protected void renderWorld() {
		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (gameLayer.virtualRegion.x));
		squareVertexBuffer.put((float) (gameLayer.virtualRegion.y));
		squareColorBuffer.put(new float[] { 0.35f, 0.82f, 0.90f, 1.0f });

		squareVertexBuffer.put((float) (gameLayer.virtualRegion.x));
		squareVertexBuffer.put(0.0f);
		squareColorBuffer.put(new float[] { 0.60f, 0.83f, 0.91f, 1.0f });

		squareVertexBuffer.put(0.0f);
		squareVertexBuffer.put((float) (gameLayer.virtualRegion.y));
		squareColorBuffer.put(new float[] { 0.34f, 0.81f, 0.89f, 1.0f });

		squareVertexBuffer.put(0.0f);
		squareVertexBuffer.put(0.0f);
		squareColorBuffer.put(new float[] { 0.37f, 0.84f, 0.92f, 1.0f });

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		drawTriangleStrip(4, squareVertexBuffer, squareColorBuffer);
		
		// Draw the waypoints... top first...
		FloatBuffer vertexBuffer = allocateFloatBuffer(game.world.waypoints.size() * 2);
		FloatBuffer colorBuffer = allocateFloatBuffer(game.world.waypoints.size() * 4);
		for(V2 p : game.world.waypoints) {
			vertexBuffer.put(p.x + 4.0f);
			vertexBuffer.put(p.y + 4.0f);
			colorBuffer.put(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
		}
		vertexBuffer.position(0);
		colorBuffer.position(0);
		
		drawLine(game.world.waypoints.size(), vertexBuffer, colorBuffer);
		
		// ...then bottom...
		vertexBuffer.position(0);
		colorBuffer.position(0);
		for(V2 p : game.world.waypoints) {
			vertexBuffer.put(p.x - 4.0f);
			vertexBuffer.put(p.y - 4.0f);
			colorBuffer.put(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });
		}
		
		vertexBuffer.position(0);
		colorBuffer.position(0);
		
		drawLine(game.world.waypoints.size(), vertexBuffer, colorBuffer);
	}

	protected void renderMenu() {
		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (menuLayer.virtualRegion.x));
		squareVertexBuffer.put((float) (menuLayer.virtualRegion.y));
		squareColorBuffer.put(new float[] { 0.9f, 1.0f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (menuLayer.virtualRegion.x));
		squareVertexBuffer.put((float) (0));
		squareColorBuffer.put(new float[] { 0.9f, 1.0f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (0));
		squareVertexBuffer.put((float) (menuLayer.virtualRegion.y));
		squareColorBuffer.put(new float[] { 0.9f, 1.0f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (0));
		squareVertexBuffer.put((float) (0));
		squareColorBuffer.put(new float[] { 0.9f, 1.0f, 0.0f, 1.0f });

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		drawTriangleStrip(4, squareVertexBuffer, squareColorBuffer);
	}

	protected FloatBuffer allocateFloatBuffer(final int entries) {
		final ByteBuffer byteBuf = ByteBuffer.allocateDirect(entries * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		return byteBuf.asFloatBuffer();
	}

	public void renderEnemy(final Enemy e) {
		if (e.isDead())
			return;

		final float pg = 0.01f * (float) e.health;
		final float pr = 1.0f - pg;

		final double width = 12;
		final V2 location = e.location;

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { pr * 1.0f, pg * 0.9f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { pr * 0.8f, pg * 0.6f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { pr * 0.6f, pg * 0.8f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { pr * 0.9f, pg * 1.0f, 0.0f, 1.0f });

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		drawTriangleStrip(4, squareVertexBuffer, squareColorBuffer);
	}

	public void renderStats() {
		final String fpsString = String.format("fps %.2f",
				graphicsTickRater.tickRate);
		final V2 bounds = getTextBounds(fpsString);
		final double width = bounds.x + 4;
		final double height = bounds.y + 2;

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (width));
		squareVertexBuffer.put((float) (height));
		squareColorBuffer.put(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });

		squareVertexBuffer.put((float) (width));
		squareVertexBuffer.put((float) (0));
		squareColorBuffer.put(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });

		squareVertexBuffer.put((float) (0));
		squareVertexBuffer.put((float) (height));
		squareColorBuffer.put(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });

		squareVertexBuffer.put((float) (0));
		squareVertexBuffer.put((float) (0));
		squareColorBuffer.put(new float[] { 0.1f, 0.1f, 0.1f, 1.0f });

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		drawTriangleStrip(4, squareVertexBuffer, squareColorBuffer);

		drawText(fpsString, 2, 4, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void renderTower(final Tower t) {
		final double width = TOWER_WIDTH;
		final V2 location = t.location;

		if (t.showRange) {
			drawCircle(t.location.x, t.location.y, t.range, 1.0f, 1.0f, 1.0f,
					1.0f);
		}

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { 0.0f, 0.0f, 0.9f, 1.0f });

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { 0.0f, 0.0f, 0.6f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { 0.0f, 0.0f, 0.8f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { 0.0f, 0.0f, 1.0f, 1.0f });

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		drawTriangleStrip(4, squareVertexBuffer, squareColorBuffer);
	}

	public void renderParticle(final Particle p) {
		if (p.isDead())
			return;

		final double width = 10;
		final V2 location = p.location;

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { 0.9f, 0.6f, 0.2f, 1.0f });

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { 0.6f, 0.9f, 0.2f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { 0.8f, 1.0f, 0.2f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { 1.0f, 0.8f, 0.2f, 1.0f });

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		drawTriangleStrip(4, squareVertexBuffer, squareColorBuffer);
	}

	public void renderMouse() {
		if (!game.mouse.onScreen)
			return;
		final V2 p = game.mouse.location;
		final double col = 0.4 + 0.6 * Math.abs(Math
				.sin(2 * graphicsTime.clock));
		drawFilledCircle(p.x, p.y, col, col, col, 1.0, game.mouse.radius);
	}

	protected void onReshapeScreen() {
		final float gameFieldPercentage = gameLayer.virtualRegion.x
				/ (gameLayer.virtualRegion.x + menuLayer.virtualRegion.x);

		final float gameRatio = gameLayer.virtualRegion.x
				/ gameLayer.virtualRegion.y;

		gameLayer.region.y = size.y;
		gameLayer.region.x = gameRatio * gameLayer.region.y;
		if (gameLayer.region.x / size.x > gameFieldPercentage) {
			// Too wide, screen width is the limit.
			gameLayer.region.x = size.x * gameFieldPercentage;
			gameLayer.region.y = gameLayer.region.x / gameRatio;
		}
		gameLayer.offset.x = 0;
		gameLayer.offset.y = (size.y - gameLayer.region.y) * 0.5f;

		final float menuRatio = menuLayer.virtualRegion.x
				/ menuLayer.virtualRegion.y;

		final V2 remainingSize = new V2(size.x - gameLayer.offset.x
				- gameLayer.region.x, size.y);

		menuLayer.region.y = remainingSize.y;
		menuLayer.region.x = menuRatio * menuLayer.region.y;
		if (menuLayer.region.x > remainingSize.x) {
			// Too wide, screen width is the limit.
			menuLayer.region.x = remainingSize.x;
			menuLayer.region.y = menuLayer.region.x / menuRatio;
		}

		menuLayer.offset.y = (remainingSize.y - menuLayer.region.y) * .5f;
		menuLayer.offset.x = gameLayer.offset.x + gameLayer.region.x
				+ remainingSize.x - menuLayer.region.x;
	}
}