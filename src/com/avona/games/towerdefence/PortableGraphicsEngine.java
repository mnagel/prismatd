package com.avona.games.towerdefence;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.vecmath.Point2d;

public abstract class PortableGraphicsEngine {

	public static final int DEFAULT_HEIGHT = 480;
	public static final int DEFAULT_WIDTH = 675;

	public static final double TOWER_WIDTH = 16;

	public Point2d size;

	protected TimeTrack graphicsTime = new TimeTrack();
	protected Game game;

	private FloatBuffer squareVertexBuffer;
	private FloatBuffer squareColorBuffer;

	public PortableGraphicsEngine(Game game) {
		this.game = game;

		squareVertexBuffer = allocateFloatBuffer(4 * 2);
		squareColorBuffer = allocateFloatBuffer(4 * 4);
	}

	public void render(double gameDelta, double graphicsDelta) {
		graphicsTime.updateTick(graphicsDelta);

		prepareScreen();

		for (Enemy e : game.enemies) {
			renderEnemy(e);
		}
		for (Tower t : game.towers) {
			renderTower(t);
		}
		for (Particle p : game.particles) {
			renderParticle(p);
		}
		renderStats();
		renderMouse();
	}

	protected abstract void prepareScreen();

	public abstract void drawCircle(final double x, final double y,
			final double colR, final double colG, final double colB,
			final double colA, final double radius, final int segments,
			final int mode);

	public abstract void drawFilledCircle(final double x, final double y,
			final double colR, final double colG, final double colB, final double colA, final double radius);

	public abstract void drawCircle(final double x, final double y, final double colR,
			final double colG, final double colB, final double colA, final double radius);

	protected FloatBuffer allocateFloatBuffer(final int entries) {
		final ByteBuffer byteBuf = ByteBuffer.allocateDirect(entries * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		return byteBuf.asFloatBuffer();
	}

	protected abstract void drawColorVertexArray(final int vertices, final FloatBuffer vertexBuffer,
			final FloatBuffer colourBuffer);

	public void renderEnemy(final Enemy e) {
		if (e.isDead())
			return;
	
		final float pg = 0.01f * (float) e.health;
		final float pr = 1.0f - pg;
	
		final double width = 12;
		final Point2d location = e.location;
	
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
	
		drawColorVertexArray(4, squareVertexBuffer, squareColorBuffer);
	}

	public abstract void drawText(final String text, final double x, final double y,
			final float colR, final float colG, final float colB, final float colA);

	public abstract Point2d getTextBounds(final String text);

	public void renderStats() {
		final String fpsString = String.format("fps %.2f",
				graphicsTime.tickrate);
	
		final Point2d bounds = getTextBounds(fpsString);
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

		drawColorVertexArray(4, squareVertexBuffer, squareColorBuffer);
		
		drawText(fpsString, 2, 4, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void renderTower(final Tower t) {
		final double width = TOWER_WIDTH;
		final Point2d location = t.location;
	
		if (t.showRange) {
			drawCircle(t.location.x, t.location.y, t.range, 1.0, 1.0, 1.0, 1.0);
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

		drawColorVertexArray(4, squareVertexBuffer, squareColorBuffer);
	}

	public void renderParticle(final Particle p) {
		if (p.isDead())
			return;
	
		final double width = 10;
		final Point2d location = p.location;
	
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

		drawColorVertexArray(4, squareVertexBuffer, squareColorBuffer);
	}

	public void renderMouse() {
		if (!game.mouse.onScreen)
			return;
		final Point2d p = game.mouse.location;
		final double col = 0.4 + 0.6 * Math.abs(Math
				.sin(2 * graphicsTime.clock));
		drawFilledCircle(p.x, p.y, col, col, col, 1.0, game.mouse.radius);
	}
}