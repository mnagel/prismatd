package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.*;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.input.Layer;
import com.avona.games.towerdefence.input.LayerHerder;
import com.avona.games.towerdefence.input.Mouse;
import com.avona.games.towerdefence.mission.CellState;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.time.TickRater;
import com.avona.games.towerdefence.time.TimeTrack;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;

import java.util.Collection;
import java.util.Locale;

public class PortableGraphicsEngine implements DisplayEventListener {

	public static final int DEFAULT_HEIGHT = 480;
	public static final int DEFAULT_WIDTH = 675;
	public static final int MENU_BUTTON_COUNT = 5;
	private static final String LEVEL_UP_TEXT = "Level up Tower";
	private Layer menuLayer;
	private Layer gameLayer;

	private TimeTrack graphicsTime = new TimeTrack();
	private TickRater graphicsTickRater = new TickRater(graphicsTime);
	private Display display;
	private Game game;
	private Mouse mouse;

	private VertexArray[] missionVertices;
	private VertexArray[] menuVertices;
	private Shader towerShader;
	private Shader enemyShader;
	private Shader particleShader;
	private Shader gridcellShader;
	private float textSize;

	public PortableGraphicsEngine(
			Display display,
			Game game,
			Mouse mouse,
			LayerHerder layerHerder,
			PortableMainLoop ml
	) {
		this.display = display;
		this.game = game;
		this.mouse = mouse;

		gameLayer = layerHerder.findLayerByName(PortableMainLoop.GAME_LAYER_NAME);
		menuLayer = layerHerder.findLayerByName(PortableMainLoop.MENU_LAYER_NAME);
		ml.eventListener.listeners.add(new ReloadOnMissionSwitch(this));
	}

	synchronized public void setTowerShader(Shader towerShader) {
		this.towerShader = towerShader;
	}

	@Override
	synchronized public void onNewScreenContext() {
		// Make sure that the VertexArrays are cleared on a screen context reset.
		// Otherwise, any preloaded texture wouldn't be reloaded again.
		freeMissionVertices();
		freeMenuVertices();
	}

	synchronized public void render(final float dt) {
		graphicsTime.updateTick(dt);
		graphicsTickRater.updateTickRate();

		textSize = display.getTextBounds("#XM|p").y;

		display.prepareScreen();

		display.prepareTransformationForLayer(gameLayer);
		renderMission();
		renderMissionStatement();

		for (Enemy e : game.enemies) {
			renderEnemy(e, gameLayer);
		}
		for (Tower t : game.towers) {
			renderTower(t, null, gameLayer, t == game.selectedObject);
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
				drawCircle(t.location.x, t.location.y, t.getRange(), 1.0f, 1.0f, 1.0f, 1.0f);
			}
		}
		if (game.draggingTower && game.selectedBuildTower != null) {
			final V2 l = gameLayer.convertToVirtual(mouse.location);
			final Tower t = game.selectedBuildTower;
			drawCircle(l.x, l.y, t.getRange(), 1.0f, 1.0f, 1.0f, 1.0f);
		}

		display.resetTransformation();

		renderMenu();

		if (!game.gameTime.isRunning()) {
			renderPauseOverlay();
		}

		if (FeatureFlags.SHOW_CONSOLE) {
			renderStats();
		}
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

		display.drawText(null, "Game paused", true,
				new V2(size.x * 0.5f, size.y * 0.5f),
				new RGB(1.0f, 1.0f, 1.0f), 1.0f);
	}

	private void createMission() {
		freeMissionVertices();
		missionVertices = new VertexArray[game.mission.gridCells.length];

		for (int i = 0; i < game.mission.gridCells.length; i++) {
			GridCell c = game.mission.gridCells[i];
			VertexArray va = new VertexArray();
			missionVertices[i] = va;

			va.mode = VertexArray.Mode.TRIANGLE_STRIP;
			va.hasColour = true;
			va.numCoords = 4;
			va.reserveBuffers();

			if (i == 0) {
				// TODO this is kinda fundamentally broken b/c there is only one binding of uniforms for all gridcells
				if (gridcellShader == null) {
					gridcellShader = display.allocateShader("gridcell");
					gridcellShader.loadShaderProgramsByName("default.vert", "default.frag");
				}

//				gridcellShader.setUniform("selected", false);
//				gridcellShader.setUniform("level", 1);
//				gridcellShader.setUniform("clock", graphicsTime.clock);
//				gridcellShader.setUniform("virtualLocation", c.center);
//				gridcellShader.setUniform("physicalLocation", gameLayer.convertToPhysical(c.center));
//				gridcellShader.setUniform("physicalRadius", gameLayer.scaleToPhysical(GridCell.size));
			}

			va.shader = gridcellShader;
			va.hasShader = true;

			final float padding = 0.05f;
			final float cellScaling = 1.0f - 2.0f * padding;

			GeometryHelper.boxVerticesAsTriangleStrip(
					(c.x + padding) * GridCell.size,
					(c.y + padding) * GridCell.size,
					GridCell.size * cellScaling,
					GridCell.size * cellScaling,
					va
			);

			if (c.state == CellState.FREE) {
				GeometryHelper.boxColoursAsTriangleStrip(0.2f, 0.2f, 0.2f, 1.0f, va);
			} else if (c.state == CellState.WALL) {
				GeometryHelper.boxColoursAsTriangleStrip(0.1f, 0.1f, 0.2f, 1.0f, va);
			} else if (c.state == CellState.WAY) {
				GeometryHelper.boxColoursAsTriangleStrip(0.0f, 0.0f, 0.0f, 1.0f, va);
			}

		}
	}

	synchronized void freeMissionVertices() {
		if (missionVertices != null) {
			// In case we're recreating the world, allow re-using of the buffers.
			for (VertexArray va : missionVertices) {
				va.freeBuffers();
			}
			missionVertices = null;
		}
	}

	private void renderMission() {
		if (missionVertices == null) {
			createMission();
		}

		for (VertexArray va : missionVertices) {
			display.drawVertexArray(va);
		}
	}

	private void buildMenu() {
		freeMenuVertices();
		menuVertices = new VertexArray[1];

		final VertexArray va = new VertexArray();
		menuVertices[0] = va;
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;
		va.reserveBuffers();
		GeometryHelper.boxVerticesAsTriangleStrip(0.0f, 0.0f, menuLayer.virtualRegion.x, menuLayer.virtualRegion.y, va);
		GeometryHelper.boxColoursAsTriangleStrip(0.2f, 0.2f, 0.2f, 0.4f, va);
	}

	synchronized void freeMenuVertices() {
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

		for (int i = 0; i < game.mission.buildableTowers.length; i++) {
			Tower t = game.mission.buildableTowers[i];

			V2 location = new V2(
					menuLayer.virtualRegion.x * 0.5f,
					menuLayer.virtualRegion.y * (1.0f - (i + 0.5f) / MENU_BUTTON_COUNT));

			renderTower(t, location, menuLayer, t == game.selectedBuildTower);
		}

		if (game.selectedObject instanceof Tower) {
			display.drawText(menuLayer, LEVEL_UP_TEXT, true,
					new V2(menuLayer.virtualRegion.x * 0.5f,
							menuLayer.virtualRegion.y * (1.0f - (MENU_BUTTON_COUNT - 2 + 0.5f) / MENU_BUTTON_COUNT)),
					new RGB(1.0f, 1.0f, 1.0f), 1.0f);
		}

		int wavenr = game.mission.waveTracker.currentWaveNum();
		Collection<Enemy> es = game.mission.getEnemyPreview(wavenr + 1);
		int enemyCount = es.size();
		int i = 0;
		for (Enemy e : es) {
			e.location = new V2(
					(i + 1) * menuLayer.virtualRegion.x / (enemyCount + 1),
					menuLayer.virtualRegion.y / 2.0f / MENU_BUTTON_COUNT
			);
			renderEnemy(e, menuLayer);
			i++;
		}

		String waveButtonText = "Send Wave #" + (wavenr + 2);
		if (wavenr + 1 >= game.mission.getWaveCount()) {
			waveButtonText = "Final Wave!";
		}
		if (game.mission.completed) {
			waveButtonText = "Mission Done!";
		}

		display.drawText(
				menuLayer,
				waveButtonText,
				true,
				new V2(
						menuLayer.virtualRegion.x / 2,
						menuLayer.virtualRegion.y / 2.0f / MENU_BUTTON_COUNT - textSize - Enemy.ENEMY_RADIUS
				),
				new RGB(1.0f, 1.0f, 1.0f),
				1.0f
		);
		display.resetTransformation();
	}

	private void renderMissionStatement() {
		for (MissionStatementText t : game.mission.missionStatementTexts) {
			display.drawText(
					gameLayer,
					t.text,
					false,
					game.mission.gridCells2d[t.x][t.y].center.clone2().sub(0, textSize / 2),
					new RGB(1, 1, 1),
					1
			);
		}
	}

	private void renderEnemy(final Enemy e, Layer layer) {
		if (e.isDead())
			return;

		RGB gfxcol = e.life.normalized();
		final float life = e.life.length() / e.maxLife.length();

		// Scale the enemy based on the remaining life to a minimum
		// of 25% of its original radius if it's almost dead.
		final float radius = e.radius * (life * 0.75f + 0.25f);
		final V2 location = e.location;

		final VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		if (enemyShader == null) {
			enemyShader = display.allocateShader("enemy");
			enemyShader.loadShaderProgramsByName("enemy.vert", "enemy.frag");
		}

		enemyShader.setUniform("selected", false);
		enemyShader.setUniform("level", e.levelNum);
		enemyShader.setUniform("clock", graphicsTime.clock + e.seed);
		enemyShader.setUniform("virtualLocation", location);
		enemyShader.setUniform("physicalLocation", layer.convertToPhysical(location));
		enemyShader.setUniform("physicalRadius", layer.scaleToPhysical(radius));

		va.shader = enemyShader;
		va.hasShader = true;

		va.reserveBuffers();

		GeometryHelper.boxVerticesAsTriangleStrip(location.x - radius, location.y - radius, radius * 2, radius * 2, va);

		// TODO: check if color is still needed like this or if shader can do
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);

		display.drawVertexArray(va);

		va.freeBuffers();
	}

	private void renderStats() {
		String towerString = "";
		if (game.selectedObject != null) {
			if (game.selectedObject instanceof Tower) {
				final Tower t = (Tower) game.selectedObject;
				towerString = String.format(Locale.US, "%s L%d %s | ",
						t.getName(), t.level, t.enemySelectionPolicy.getName());
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

		final String fpsString = String.format(
				Locale.US,
				"%d lives | $%d | %s | %sfps %.2f",
				game.lives,
				game.money,
				game.mission.missionName,
				towerString,
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

		GeometryHelper.boxVerticesAsTriangleStrip(0.0f, 0.0f, width, height, va);
		GeometryHelper.boxColoursAsTriangleStrip(0.0f, 0.0f, 0.0f, 0.2f, va);

		display.drawVertexArray(va);

		va.freeBuffers();

		display.drawText(null, fpsString, false, new V2(2.0f, 4.0f), new RGB(1.0f, 1.0f, 1.0f), 1.0f);
	}

	private void renderTower(final Tower t, V2 overrideLocation, Layer layer, boolean selected) {
		float radius = t.radius;
		final V2 location = overrideLocation != null ? overrideLocation : t.location;

		RGB gfxcol = t.color.normalized();

		VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		va.reserveBuffers();

		if (towerShader == null) {
			towerShader = display.allocateShader("tower");
			towerShader.loadShaderProgramsByName("tower.vert", "tower.frag");
		}

		towerShader.setUniform("level", t.level);
		towerShader.setUniform("selected", selected);
		towerShader.setUniform("clock", graphicsTime.clock + t.seed);
		towerShader.setUniform("virtualLocation", location);
		towerShader.setUniform("physicalLocation", layer.convertToPhysical(location));
		towerShader.setUniform("physicalRadius", layer.scaleToPhysical(radius));

		va.shader = towerShader;
		va.hasShader = true;

		GeometryHelper.boxVerticesAsTriangleStrip(location.x - radius, location.y - radius, radius * 2, radius * 2, va);

		// TODO: check if color is still needed like this or if shader can do
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);

		display.drawVertexArray(va);

		va.freeBuffers();

		if (overrideLocation != null) {
			float y_off = GridCell.size / 2 + textSize / 2;
			final String label = String.format(Locale.US, "$%d", t.getPrice());
			display.drawText(layer, label, true, location.clone2().sub(0, y_off), RGB.WHITE, 1.0f);
		}
	}

	private void renderParticle(final Particle p) {
		if (p.isDead())
			return;

		RGB gfxcol = p.strength.normalized();

		final float radius = p.radius;
		final V2 location = p.location;

		final VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		if (particleShader == null) {
			particleShader = display.allocateShader("particle");
			particleShader.loadShaderProgramsByName("particle.vert", "particle.frag");
		}

		particleShader.setUniform("selected", false);
		particleShader.setUniform("level", 1);
		particleShader.setUniform("clock", graphicsTime.clock + p.seed);
		particleShader.setUniform("virtualLocation", location);
		particleShader.setUniform("physicalLocation", gameLayer.convertToPhysical(location));
		particleShader.setUniform("physicalRadius", gameLayer.scaleToPhysical(radius));

		va.shader = particleShader;
		va.hasShader = true;

		va.reserveBuffers();

		GeometryHelper.boxVerticesAsTriangleStrip(location.x - radius, location.y - radius, radius * 2, radius * 2, va);

		// TODO: check if color is still needed like this or if shader can do
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 1.0f);

		display.drawVertexArray(va);

		va.freeBuffers();
	}

	private void renderMouse() {
		if (!mouse.onScreen)
			return;
		final V2 p = mouse.location;
		final float col = 0.5f + 0.3f * (float) Math.abs(Math.sin(4 * graphicsTime.clock));
		drawFilledCircle(p.x, p.y, mouse.radius, 1.0f, 1.0f, 1.0f, col);
	}

	@Override
	synchronized public void onReshapeScreen() {
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
		va.numCoords = segments + 1;
		va.reserveBuffers();

		final double angleStep = 2 * Math.PI / segments;
		final float[] cols = new float[]{colR, colG, colB, colA};

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
