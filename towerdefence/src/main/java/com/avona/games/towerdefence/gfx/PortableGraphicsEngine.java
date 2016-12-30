package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.LayerHerder;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.TickRater;
import com.avona.games.towerdefence.TimeTrack;
import com.avona.games.towerdefence.Transient;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.wave.WaveTracker;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.tower.Tower;

import java.util.Collection;
import java.util.Locale;

public class PortableGraphicsEngine implements DisplayEventListener {

	public static final int DEFAULT_HEIGHT = 480;
	public static final int DEFAULT_WIDTH = 675;

	private Layer menuLayer;
	private Layer gameLayer;

	private TimeTrack graphicsTime = new TimeTrack();
	private TickRater graphicsTickRater = new TickRater(graphicsTime);
	private Display display;
	private Game game;
	private Mouse mouse;

	private VertexArray[] levelVertices;
	private VertexArray[] menuVertices;
	private VertexArray[] overlayVertices;

	public PortableGraphicsEngine(Display display, Game game, Mouse mouse,
			LayerHerder layerHerder, PortableMainLoop ml) {
		this.display = display;
		this.game = game;
		this.mouse = mouse;

		gameLayer = layerHerder
				.findLayerByName(PortableMainLoop.GAME_LAYER_NAME);
		menuLayer = layerHerder
				.findLayerByName(PortableMainLoop.MENU_LAYER_NAME);
		ml.eventListener.listeners.add(new ReloadOnLevelSwitch(this));
	}

	@Override
	public void onNewScreenContext() {
		// Make sure that the VertexArrays are cleared on a screen context
		// reset. Otherwise, any preloaded texture wouldn't be reloaded again.
		freeLevelVertices();
		freeMenuVertices();
		freeOverlayVertices();
	}

	public void render(final float dt) {
		graphicsTime.updateTick(dt);
		graphicsTickRater.updateTickRate();

		display.prepareScreen();

		display.prepareTransformationForLayer(gameLayer);
		renderLevel();

		for (Enemy e : game.enemies) {
			renderEnemy(e);
		}
		for (Tower t : game.towers) {
			renderTower(t, null, gameLayer);
		}
		for (Particle p : game.particles) {
			renderParticle(p);
		}
		for (Transient t : game.transients) {
			renderTransient(t, gameLayer);
		}
		if (game.selectedObject != null) {
			if (game.selectedObject instanceof Tower) {
				final Tower t = (Tower) game.selectedObject;
				drawCircle(t.location.x, t.location.y, t.range, 1.0f, 1.0f,
						1.0f, 1.0f);
			}
		}
		if (game.draggingTower && game.selectedBuildTower != null) {
			final V2 l = gameLayer.convertToVirtual(mouse.location);
			final Tower t = game.selectedBuildTower;
			drawCircle(l.x, l.y, t.range, 1.0f, 1.0f, 1.0f, 1.0f);
		}

		if (game.level.showOverlay) {
			renderOverlay();
		}
		display.resetTransformation();

		renderMenu();

		if (!game.gameTime.isRunning()) {
			renderPauseOverlay();
		}

		renderStats();
		renderMouse();
	}

	private void renderTransient(final Transient t, Layer layer) {
		if (t.isDead())
			return;
		t.draw(display, layer);
	}

	private void renderPauseOverlay() {
		final V2 size = display.getSize();
		final VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;
		va.reserveBuffers();
		GeometryHelper.boxVerticesAsTriangleStrip(0, 0, size.x, size.y, va);
		GeometryHelper.boxColoursAsTriangleStrip(0.0f, 0.0f, 0.0f, 0.4f, va);
		display.drawVertexArray(va);
		va.freeBuffers();

		final String pauseText = "Game paused";
		final V2 s = display.getTextBounds(pauseText);
		display.drawText(pauseText, (size.x * 0.5f) - (s.x * 0.5f), (size.y * 0.5f)
				+ (s.y * 0.5f), 1.0f, 1.0f, 1.0f, 1.0f);
	}

	private VertexArray createSimpleTextureBox(final float x, final float y,
											   final float width, final float height, final String textureName) {
		VertexArray va = new VertexArray();
		va.hasTexture = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		va.reserveBuffers();
		GeometryHelper.boxVerticesAsTriangleStrip(x, y, width, height, va);

		va.texture = display.allocateTexture();
		va.texture.loadImage(textureName);

		GeometryHelper.boxTextureAsTriangleStrip(va);
		return va;
	}

	private void createLevel() {
		freeLevelVertices();
		levelVertices = new VertexArray[1];

		levelVertices[0] = createSimpleTextureBox(0.0f, 0.0f,
				gameLayer.virtualRegion.x, gameLayer.virtualRegion.y,
				game.level.gameBackgroundName);
	}

	void freeLevelVertices() {
		if (levelVertices != null) {
			// In case we're recreating the world, allow re-using of the
			// buffers.
			for (VertexArray va : levelVertices) {
				va.freeBuffers();
			}
			levelVertices = null;
		}
	}

	private void renderLevel() {
		if (levelVertices == null) {
			createLevel();
		}

		for (VertexArray va : levelVertices) {
			display.drawVertexArray(va);
		}
	}

	private void buildMenu() {
		// deprecated -- do not use a background file any more
		freeMenuVertices();
		menuVertices = new VertexArray[0];
	}

	void freeMenuVertices() {
		if (menuVertices != null) {
			// In case we're recreating the world, allow re-using of the
			// buffers.
			for (VertexArray va : menuVertices) {
				va.freeBuffers();
			}
			menuVertices = null;
		}
	}

	private void renderMenu() {
		display.prepareTransformationForLayer(menuLayer);
		if (menuVertices == null) {
			buildMenu();
		}

		for (VertexArray va : menuVertices) {
			display.drawVertexArray(va);
		}

		for (int i = 0; i < game.level.buildableTowers.length; i++) {
			Tower t = game.level.buildableTowers[i];
			float buttonCount = 4;
			V2 location = new V2(
					menuLayer.virtualRegion.x * 0.5f,
					menuLayer.virtualRegion.y * (1.0f - (i + 0.5f) / buttonCount));
			renderTower(t, location, menuLayer);
		}

		int wavenr = game.level.waveTracker.currentWaveNum();
		Collection<Enemy> es = game.level.getEnemyPreview(wavenr+1);
		int i = 0;
		float buttonCount = 4;
		for (Enemy e: es) {
			i++;
			V2 location = new V2(
					i * menuLayer.virtualRegion.x / 4,
					menuLayer.virtualRegion.y / (2.0f* buttonCount)
			);
			e.location = location;
			renderEnemy(e);

		}
		display.resetTransformation();
	}

	private void buildOverlay() {
		freeOverlayVertices();
		overlayVertices = new VertexArray[1];

		overlayVertices[0] = createSimpleTextureBox(0.0f, 0.0f,
				gameLayer.virtualRegion.x, gameLayer.virtualRegion.y,
				game.level.overlayBackgroundName);
	}

	void freeOverlayVertices() {
		if (overlayVertices != null) {
			// In case we're recreating the world, allow re-using of the
			// buffers.
			for (VertexArray va : overlayVertices) {
				va.freeBuffers();
			}
			overlayVertices = null;
		}
	}

	private void renderOverlay() {
		if (overlayVertices == null) {
			buildOverlay();
		}

		for (VertexArray va : overlayVertices) {
			display.drawVertexArray(va);
		}
	}

	private void renderEnemy(final Enemy e) {
		if (e.isDead())
			return;

		RGB gfxcol = e.life.normalized();

		final float radius = e.radius;
		final V2 location = e.location;

		final VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		va.reserveBuffers();

		GeometryHelper.boxVerticesAsTriangleStrip(location.x - radius,
				location.y - radius, radius * 2, radius * 2, va);

		// Top right
		va.addColour(gfxcol.R * 1.0f, gfxcol.G * 0.9f, gfxcol.B * 0.9f, 1.0f);
		// Bottom right
		va.addColour(gfxcol.R * 0.8f, gfxcol.G * 0.6f, gfxcol.B * 0.6f, 1.0f);
		// Top left
		va.addColour(gfxcol.R * 0.6f, gfxcol.G * 0.8f, gfxcol.B * 0.8f, 1.0f);
		// Bottom left
		va.addColour(gfxcol.R * 0.9f, gfxcol.G * 1.0f, gfxcol.B * 1.0f, 1.0f);
		va.rotate(location, 45);

		display.drawVertexArray(va);

		va.freeBuffers();
	}

	private void renderStats() {
		String towerString = "";
		if (game.selectedObject != null) {
			if (game.selectedObject instanceof Tower) {
				final Tower t = (Tower) game.selectedObject;
				towerString = String.format(Locale.US, "Tower Lev%d | ", t.level);
			} else if (game.selectedObject instanceof Enemy) {
				final Enemy e = (Enemy) game.selectedObject;
				towerString = String
						.format(
								Locale.US,
								"enemy lvl %d, health R%.0f G%.0f B%.0f  /  R%.0f G%.0f B%.0f | ",
								e.levelNum, e.life.R, e.life.G, e.life.B,
								e.maxLife.R, e.maxLife.G, e.maxLife.B);
			}
		}
		String waveString = "";
		if (game.level != null) {
			final WaveTracker wt = game.level.waveTracker;
			if (wt.hasWaveStarted() || wt.hasWaveEnded()) {
				waveString = String.format(Locale.US, "Wave %d | ", wt.currentWaveNum());
			}
		}
		final String fpsString = String.format(
				Locale.US,
				"%s%s%d killed | %d lives | $%d | level %d | %s | fps %.2f",
				towerString,
				waveString,
				game.killed,
				game.lives,
				game.money,
				game.curLevelIdx + 1,
				game.level.levelName,
				graphicsTickRater.tickRate
		);
		final V2 bounds = display.getTextBounds(fpsString);
		final float width = bounds.x + 4;
		final float height = bounds.y + 2;

		VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		va.reserveBuffers();

		GeometryHelper
				.boxVerticesAsTriangleStrip(0.0f, 0.0f, width, height, va);
		GeometryHelper.boxColoursAsTriangleStrip(0.0f, 0.0f, 0.0f, 0.2f, va);

		display.drawVertexArray(va);

		va.freeBuffers();

		display.drawText(fpsString, 2, 4, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	private void renderTower(final Tower t, V2 overrideLocation, Layer layer) {
		final float radius = t.radius;
		final V2 location = overrideLocation != null ? overrideLocation : t.location;

		RGB gfxcol = t.strength.normalized();

		VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		va.reserveBuffers();

		GeometryHelper.boxVerticesAsTriangleStrip(location.x - radius,
				location.y - radius, radius * 2, radius * 2, va);

		// Top right
		va.addColour(gfxcol.R * 1.0f, gfxcol.G * 0.9f, gfxcol.B * 0.9f, 1.0f);
		// Bottom right
		va.addColour(gfxcol.R * 0.8f, gfxcol.G * 0.6f, gfxcol.B * 0.6f, 1.0f);
		// Top left
		va.addColour(gfxcol.R * 0.6f, gfxcol.G * 0.8f, gfxcol.B * 0.8f, 1.0f);
		// Bottom left
		va.addColour(gfxcol.R * 0.9f, gfxcol.G * 1.0f, gfxcol.B * 1.0f, 1.0f);

		display.drawVertexArray(va);

		va.freeBuffers();

		if (overrideLocation != null) {
			final String label = String.format("$%d", t.price);
			final V2 textBounds = display.getTextBounds(label);
			display.drawText(layer, label, location.x - textBounds.x / 2, location.y - textBounds.y / 2, 1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	private void renderParticle(final Particle p) {
		if (p.isDead())
			return;

		RGB gfxcol = p.strength.normalized();

		final float width = 10;
		final float height = 10;
		final V2 location = p.location;

		VertexArray va = new VertexArray();

		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		va.reserveBuffers();

		GeometryHelper.boxVerticesAsTriangleStrip(
				location.x - width / 2,
				location.y - height / 2,
				width,
				height,
				va
		);

		// Top right
		va.addColour(gfxcol.R * 1.0f, gfxcol.G * 0.9f, gfxcol.B * 0.9f, 1.0f);
		// Bottom right
		va.addColour(gfxcol.R * 0.8f, gfxcol.G * 0.6f, gfxcol.B * 0.6f, 1.0f);
		// Top left
		va.addColour(gfxcol.R * 0.6f, gfxcol.G * 0.8f, gfxcol.B * 0.8f, 1.0f);
		// Bottom left
		va.addColour(gfxcol.R * 0.9f, gfxcol.G * 1.0f, gfxcol.B * 1.0f, 1.0f);

		display.drawVertexArray(va);

		va.freeBuffers();
	}

	private void renderMouse() {
		if (!mouse.onScreen)
			return;
		final V2 p = mouse.location;
		final float col = 0.5f + 0.3f * (float) Math.abs(Math
				.sin(4 * graphicsTime.clock));
		drawFilledCircle(p.x, p.y, mouse.radius, 1.0f, 1.0f, 1.0f, col);
	}

	@Override
	public void onReshapeScreen() {
		final V2 size = display.getSize();

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

		gameLayer.offset.x += (remainingSize.x - menuLayer.region.x) * .5f;

		menuLayer.offset.y = gameLayer.offset.y;
		menuLayer.offset.x = gameLayer.offset.x + gameLayer.region.x;
	}

	private VertexArray createCircleVa(final float x, final float y, final float radius,
									   final float colR, final float colG, final float colB,
									   final float colA, final int segments) {

		VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = segments+1;
		va.reserveBuffers();

		final double angleStep = 2 * Math.PI / segments;
		final float[] cols = new float[] { colR, colG, colB, colA };

		for (int i = 0; i < segments; ++i) {
			final double angle = i * angleStep;
			va.addCoord(x + (Math.cos(angle) * radius), y + (Math.sin(angle) * radius));
			va.addColour(cols);
		}
		// Close the circle.
		va.addCoord(x + (Math.cos(0) * radius), y + (Math.sin(0) * radius));
		va.addColour(cols);

		return va;
	}

	private void drawCircle(final float x, final float y, final float radius,
							final float colR, final float colG, final float colB, final float colA) {

		final int segments = 100;
		VertexArray va = createCircleVa(x, y, radius, colR, colG, colB, colA, segments);
		va.mode = VertexArray.Mode.LINE_STRIP;

		display.drawVertexArray(va);
		va.freeBuffers();
	}

	private void drawFilledCircle(final float x, final float y, final float radius,
								  final float colR, final float colG, final float colB, final float colA) {

		final int segments = 100;
		VertexArray va = createCircleVa(x, y, radius, colR, colG, colB, colA, segments);
		va.mode = VertexArray.Mode.TRIANGLE_FAN;

		va.addCoord(x, y);
		va.addColour(colR, colG, colB, colA);

		display.drawVertexArray(va);
		va.freeBuffers();
	}
}
