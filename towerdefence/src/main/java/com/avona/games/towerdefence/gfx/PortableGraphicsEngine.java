package com.avona.games.towerdefence.gfx;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.enemy.Enemy;
import com.avona.games.towerdefence.engine.Game;
import com.avona.games.towerdefence.events.EmptyEventListener;
import com.avona.games.towerdefence.input.*;
import com.avona.games.towerdefence.mission.CellState;
import com.avona.games.towerdefence.mission.GridCell;
import com.avona.games.towerdefence.mission.Mission;
import com.avona.games.towerdefence.mission.MissionStatementText;
import com.avona.games.towerdefence.particle.Particle;
import com.avona.games.towerdefence.time.TickRater;
import com.avona.games.towerdefence.time.TimeTrack;
import com.avona.games.towerdefence.tower.Tower;
import com.avona.games.towerdefence.transients.Transient;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.avona.games.towerdefence.util.Util;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Locale;

public class PortableGraphicsEngine {
	private TimeTrack graphicsTime = new TimeTrack();
	private TickRater graphicsTickRater = new TickRater(graphicsTime);

	private PortableDisplay display;
	private Game game;
	private Mouse mouse;
	private boolean showMouse;

	private LayerHerder layerHerder;

	private Shader towerShader;
	private Shader enemyShader;
	private Shader particleShader;
	private Shader gridcellShader;
	private VertexArray missionVertices = new VertexArray();
	private boolean missionSwitched = true;

	public PortableGraphicsEngine(
			PortableDisplay display,
			Game game,
			Mouse mouse,
			boolean showMouse,
			LayerHerder layerHerder
	) {
		this.display = display;
		this.game = game;
		this.mouse = mouse;
		this.showMouse = showMouse;
		this.layerHerder = layerHerder;

		game.eventDistributor.listeners.add(new EmptyEventListener() {
			@Override
			public void onMissionSwitched(Mission mission) {
				missionSwitched = true;
			}
		});
	}

	synchronized public void setTowerShader(Shader towerShader) {
		this.towerShader = towerShader;
	}

	synchronized public void render(final float dt) {
		Layer gameLayer = layerHerder.gameLayer;
		graphicsTime.updateTick(dt);
		graphicsTickRater.updateTickRate();

		display.prepareScreen();

		prepareTransformationForLayer(gameLayer);
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
			final Tower t = game.selectedBuildTower;
			final V2 location = gameLayer.convertToVirtual(mouse.physicalLocation);
			renderTower(t, location, gameLayer, false);
			drawCircle(location.x, location.y, t.getRange(), 1.0f, 1.0f, 1.0f, 1.0f);
		}

		renderMenu();

		if (!game.gameTime.isRunning()) {
			renderPauseOverlay();
		}

		if (FeatureFlags.SHOW_CONSOLE) {
			renderStats();
		}

		if (showMouse) {
			renderMouse();
		}
	}

	private void prepareTransformationForLayer(Layer layer) {
		display.prepareTransformationForLayer(layer);

		if (FeatureFlags.OPENGL_DEBUG_LAYERS) {
			RGB gfxcol = new RGB(layer.hashCode() % 3 / 3.0f, layer.hashCode() % 7 / 7.0f, layer.hashCode() % 11 / 11.0f);
			renderFilledRect(new V2(0, 0), layer.virtualRegion, gfxcol); // 0,0 because already transformed
		}
	}

	private void renderTransient(final Transient t, Layer layer) {
		if (t.isDead()) {
			return;
		}
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

	private void createMissionVertices() {
		missionVertices = new VertexArray();
		final VertexArray va = missionVertices;
		va.mode = VertexArray.Mode.TRIANGLES;
		va.hasColour = true;
		va.numCoords = GeometryHelper.COORD_COUNT_BOX_VERTICES_AS_TRIANGLES * game.mission.gridCells.length;
		va.numIndexes = GeometryHelper.INDEX_COUNT_BOX_VERTICES_AS_TRIANGLES * game.mission.gridCells.length;
		va.reserveBuffers();

		if (gridcellShader == null) {
			gridcellShader = display.allocateShader("gridcell");
			gridcellShader.loadShaderProgramFromFile("default");
		}
		va.shader = gridcellShader;
		va.hasShader = true;

		for (int i = 0; i < game.mission.gridCells.length; i++) {
			GridCell c = game.mission.gridCells[i];

			final float padding = 0.05f;
			final float cellScaling = 1.0f - 2.0f * padding;

			GeometryHelper.boxVerticesAsTriangles(
					(c.x + padding) * GridCell.size,
					(c.y + padding) * GridCell.size,
					GridCell.size * cellScaling,
					GridCell.size * cellScaling,
					va
			);

			if (c.state == CellState.WALL) {
				GeometryHelper.boxColoursAsTriangles(0.1f, 0.1f, 0.2f, 1.0f, va);
			} else if (c.state == CellState.WAY) {
				if (c == game.mission.waypoints[0]) {
					GeometryHelper.boxColoursAsTriangles(0.0f, 0.4f, 0.1f, 1.0f, va);
				} else if (c == game.mission.waypoints[game.mission.waypoints.length - 1]) {
					GeometryHelper.boxColoursAsTriangles(0.4f, 0.1f, 0.0f, 1.0f, va);
				} else {
					GeometryHelper.boxColoursAsTriangles(0.0f, 0.0f, 0.0f, 1.0f, va);
				}
			} else { // FREE/TOWER
				GeometryHelper.boxColoursAsTriangles(0.1f, 0.1f, 0.1f, 1.0f, va);
			}
		}
	}

	private void renderMission() {
		if (missionSwitched) {
			missionSwitched = false;
			createMissionVertices();
		}
		display.drawVertexArray(missionVertices);
	}

	private void renderMenu() {
		MenuLayer menuLayer = layerHerder.menuLayer;
		prepareTransformationForLayer(menuLayer);

		for (MenuButton b : Lists.reverse(menuLayer.buttons)) {
			Layer layer = menuLayer.getButtonLayer(b);
			if (layer == null) {
				Util.log("fix this race condition about removing buttons/layers"); // TODO
				continue;
			}
			prepareTransformationForLayer(layer);

			switch (b.look) {
				case BUILD_TOWER:
					V2 location = layer.virtualRegion.clone2().mult(0.5f);
					Tower t5 = (Tower) b.getRenderExtra();
					float seed2 = t5.seed;
					Tower t = t5.clone2();
					t.seed = seed2;
					if (game.money < t.getPrice()) {
						t.color = new RGB(1, 1, 1);
					}
					renderTower(t, location, layer, t5 == game.selectedBuildTower);
					String label = String.format(Locale.US, "$%d", t.getPrice());
					display.drawText(layer, label, true, location, RGB.WHITE, 1.0f);
					break;

				case TOWER_UPGRADE:
					Tower t2 = (Tower) game.selectedObject;
					V2 location2 = layer.virtualRegion.clone2().mult(0.5f);
					renderTower(t2, location2, layer, true);
					display.drawText(
							layer,
							"Upgrade Prisma",
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.6f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					display.drawText(
							layer,
							"$" + t2.getUpgradePrice(),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.4f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					break;

				case TOWER_INFO:
					Tower t3 = (Tower) game.selectedObject;
					display.drawText(
							layer,
							t3.getName(),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.9f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					display.drawText(
							layer,
							"Level " + t3.level,
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.7f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					display.drawText(
							layer,
							"D: " + t3.getDamage(),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.5f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					display.drawText(
							layer,
							String.format(Locale.US, "Range: %.1f", +t3.getRange() / GridCell.size),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.3f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					break;

				case TOWER_SELL:
					Tower t4 = (Tower) game.selectedObject;
					display.drawText(
							layer,
							"Sell Prisma",
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.6f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					display.drawText(
							layer,
							"$" + t4.getSellPrice(),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.4f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					break;

				case DEBUG_INFO:
					display.drawText(
							layer,
							String.format(Locale.US, "%.1f fps", graphicsTickRater.tickRate),
							true,
							new V2(GridCell.size / 2, GridCell.size / 2),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					break;

				case GAME_INFO:
					display.drawText(
							layer,
							String.format(Locale.US, "$%d", game.money),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.25f),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					RGB lifeColor = game.lives > 3 ? new RGB(1.0f, 1.0f, 1.0f) : new RGB(1.0f, 0.0f, 0.0f);
					display.drawText(
							layer,
							String.format(Locale.US, "%d lives", game.lives),
							true,
							new V2(GridCell.size / 2, GridCell.size * 0.75f),
							lifeColor,
							1.0f
					);
					break;

				case NEXT_WAVE:
					int wavenr = game.waveTracker.currentWaveNum();
					Collection<Enemy> es = game.mission.getEnemyPreview(wavenr + 1);
					int enemyCount = es.size();
					int i = 0;
					for (Enemy e : es) {
						float seed = e.seed;
						e = e.clone2();
						e.seed = seed;
						e.location = new V2(
								GridCell.size / enemyCount * (i + 0.5f),
								3 * GridCell.size / 5
						);
						if (game.waveTracker.currentWave != null && !game.waveTracker.currentWave.isFullyDeployed()) {
							e.life.subUpto(e.life, 1);
							e.maxLife = e.life.clone2();
						}
						renderEnemy(e, layer);
						i++;
					}

					String waveButtonText;
					switch (game.missionStatus) {
						default:
						case ALLOCATED:
							waveButtonText = "fishy attempt to render mission that is only in allocated status";
							Util.log(waveButtonText);
							break;
						case ACTIVE:
							waveButtonText = String.format(Locale.US, "> Wave %d/%d", +(wavenr + 2), game.mission.getWaveCount());
							if (wavenr + 1 >= game.mission.getWaveCount()) {
								waveButtonText = "Final Wave!";
							}
							break;
						case WON:
							renderFilledRect(new V2(0, 0), layer.virtualRegion, new RGB(0, 1, 0));
							waveButtonText = "Mission Won!";
							break;
						case LOST:
							renderFilledRect(new V2(0, 0), layer.virtualRegion, new RGB(1, 0, 0));
							waveButtonText = "Mission Lost!";
							break;
					}

					display.drawText(
							layer,
							waveButtonText,
							true,
							new V2(GridCell.size / 2, GridCell.size / 5),
							new RGB(1.0f, 1.0f, 1.0f),
							1.0f
					);
					break;
			}

		}
	}

	private void renderMissionStatement() {
		for (MissionStatementText t : game.mission.missionStatementTexts) {
			display.drawText(
					layerHerder.gameLayer,
					t.text,
					false,
					true,
					game.mission.gridCells2d[t.x][t.y].center,
					new RGB(1, 1, 1),
					1
			);
		}
	}

	private void renderEnemy(final Enemy e, Layer layer) {
		if (e.isDead()) {
			return;
		}

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
			enemyShader.loadShaderProgramFromFile("enemy");
		}

		enemyShader.setUniform("selected", false);
		enemyShader.setUniform("level", e.level);
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
								e.level, e.life.R, e.life.G, e.life.B,
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
			towerShader.loadShaderProgramFromFile("tower");
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
	}

	private void renderParticle(final Particle p) {
		if (p.isDead()) {
			return;
		}

		RGB gfxcol = p.strength.normalized();

		final float radius = p.radius;
		final V2 location = p.location;

		final VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;

		if (particleShader == null) {
			particleShader = display.allocateShader("particle");
			particleShader.loadShaderProgramFromFile("particle");
		}

		particleShader.setUniform("selected", false);
		particleShader.setUniform("level", 1);
		particleShader.setUniform("clock", graphicsTime.clock + p.seed);
		particleShader.setUniform("virtualLocation", location);
		particleShader.setUniform("physicalLocation", layerHerder.gameLayer.convertToPhysical(location));
		particleShader.setUniform("physicalRadius", layerHerder.gameLayer.scaleToPhysical(radius));

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
		Layer l = layerHerder.gameLayer;
		prepareTransformationForLayer(l);
		final V2 p = l.convertToVirtual(mouse.physicalLocation);
		final float col = 0.5f + 0.3f * (float) Math.abs(Math.sin(4 * graphicsTime.clock));
		drawFilledCircle(p.x, p.y, mouse.radius, 1.0f, 1.0f, 1.0f, col);
	}

	private VertexArray createCircleVa(
			final float x, final float y, final float radius,
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

	@SuppressWarnings("SameParameterValue")
	private void drawCircle(
			final float x, final float y, final float radius,
			final float colR, final float colG, final float colB, final float colA) {
		final int segments = 100;
		VertexArray va = createCircleVa(x, y, radius, colR, colG, colB, colA, segments);
		va.mode = VertexArray.Mode.LINE_STRIP;

		display.drawVertexArray(va);
		va.freeBuffers();
	}

	@SuppressWarnings("SameParameterValue")
	private void drawFilledCircle(
			final float x, final float y, final float radius,
			final float colR, final float colG, final float colB, final float colA) {
		final int segments = 100;
		VertexArray va = createCircleVa(x, y, radius, colR, colG, colB, colA, segments);
		va.mode = VertexArray.Mode.TRIANGLE_FAN;

		va.addCoord(x, y);
		va.addColour(colR, colG, colB, colA);

		display.drawVertexArray(va);
		va.freeBuffers();
	}

	private void renderFilledRect(V2 offset, V2 size, RGB gfxcol) {
		final VertexArray va = new VertexArray();
		va.hasColour = true;
		va.numCoords = 4;
		va.mode = VertexArray.Mode.TRIANGLE_STRIP;
		va.hasShader = false;
		va.reserveBuffers();

		GeometryHelper.boxVerticesAsTriangleStrip(offset.x, offset.y, size.x, size.y, va);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 0.7f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 0.7f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 0.7f);
		va.addColour(gfxcol.R, gfxcol.G, gfxcol.B, 0.7f);

		display.drawVertexArray(va);
		va.freeBuffers();
	}
}
