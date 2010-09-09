package com.avona.games.towerdefence.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Particle;
import com.avona.games.towerdefence.TimeTrack;
import com.avona.games.towerdefence.Tower;
import com.avona.games.towerdefence.Util;
import com.sun.opengl.util.j2d.TextRenderer;

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
public class GraphicsEngine implements GLEventListener {
	final static public int DEFAULT_HEIGHT = 750;
	final static public int DEFAULT_WIDTH = 900;

	public Frame frame;
	public GLCanvas canvas;
	public Point2d size;

	TimeTrack graphicsTime;
	GL gl;
	GLU glu;
	Game game;
	MainLoop main;
	TextRenderer renderer;

	private FloatBuffer squareVertexBuffer;
	private FloatBuffer squareColorBuffer;
	
	public GraphicsEngine(MainLoop main, Game game) {
		this.main = main;
		this.game = game;

		graphicsTime = new TimeTrack();
		renderer = new TextRenderer(new Font("Deja Vu Sans", Font.PLAIN, 12),
				true, true);
		glu = new GLU();

		squareVertexBuffer = allocateFloatBuffer(4 * 2);
		squareColorBuffer = allocateFloatBuffer(4 * 4);
		
		setupGlCanvas();

		frame = new Frame("Towerdefence");
		frame.add(canvas);
		frame.setSize(GraphicsEngine.DEFAULT_WIDTH,
				GraphicsEngine.DEFAULT_HEIGHT);
		frame.setBackground(Color.WHITE);
		frame.setCursor(java.awt.Toolkit.getDefaultToolkit()
				.createCustomCursor(
						new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR),
						new java.awt.Point(0, 0), "NOCURSOR"));
		frame.setVisible(true);
	}
	
	private FloatBuffer allocateFloatBuffer(final int entries) {
		final ByteBuffer byteBuf = ByteBuffer.allocateDirect(entries * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		return byteBuf.asFloatBuffer();
	}

	private void setupGlCanvas() {
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setDoubleBuffered(true);

		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.setAutoSwapBufferMode(true);
	}

	public void render(double gameDelta, double graphicsDelta) {
		graphicsTime.updateTick(graphicsDelta);

		// Paint background, clearing previous drawings.
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

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

	public void renderStats() {
		gl.glBegin(GL.GL_QUADS);

		final String fpsString = String.format("fps %.2f", graphicsTime.tickrate);
		Rectangle2D bounds = renderer.getBounds(fpsString);
		final double width = (bounds.getWidth() / size.x) * 2 + 0.01;
		final double height = (bounds.getHeight() / size.y) * 2 + 0.01;

		gl.glColor3d(0.1, 0.1, 0.1);
		gl.glVertex2d(-1, -1);
		gl.glVertex2d(-1, -1 + height);
		gl.glVertex2d(-1 + width, -1 + height);
		gl.glVertex2d(-1 + width, -1);
		gl.glEnd();
		drawText(fpsString, 2, 4, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void drawText(final String text, final double x, final double y,
			final float colR, final float colG, final float colB,
			final float colA) {
		renderer.beginRendering((int) size.x, (int) size.y);
		renderer.setColor(colR, colG, colB, colA);
		renderer.draw(text, (int) x, (int) y);
		renderer.endRendering();
	}

	public void renderEnemy(final Enemy e) {
		if (e.isDead())
			return;
		
		final float pg = 0.01f * (float)e.health;
		final float pr = 1.0f - pg;

		final double width = 0.04;
		final Point2d location = e.location;

		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { pr*1.0f, pg*0.9f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (location.x + width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { pr*0.8f, pg*0.6f, 0.0f, 1.0f });
		
		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y + width / 2));
		squareColorBuffer.put(new float[] { pr*0.6f, pg*0.8f, 0.0f, 1.0f });

		squareVertexBuffer.put((float) (location.x - width / 2));
		squareVertexBuffer.put((float) (location.y - width / 2));
		squareColorBuffer.put(new float[] { pr*0.9f, pg*1.0f, 0.0f, 1.0f });
		
		squareVertexBuffer.position(0);
		squareColorBuffer.position(0);
		
		gl.glVertexPointer(2, GL.GL_FLOAT, 0, squareVertexBuffer);
		gl.glColorPointer(4, GL.GL_FLOAT, 0, squareColorBuffer);
		
		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);
		
		gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
	}

	public void renderTower(final Tower t) {
		final double width = 0.03;
		final Point2d location = t.location;

		if (t.showRange) {
			gl.glColor3d(1.0, 1.0, 1.0);
			drawCircle(t.location.x, t.location.y, t.range);
		}

		gl.glBegin(GL.GL_QUADS);
		gl.glColor3d(0.0, 0.0, 1.0);
		gl.glVertex2d(location.x - width / 2, location.y - width / 2);
		gl.glColor3d(0.0, 0.0, 0.6);
		gl.glVertex2d(location.x + width / 2, location.y - width / 2);
		gl.glColor3d(0.0, 0.0, 0.9);
		gl.glVertex2d(location.x + width / 2, location.y + width / 2);
		gl.glColor3d(0.0, 0.0, 0.8);
		gl.glVertex2d(location.x - width / 2, location.y + width / 2);
		gl.glEnd();
	}

	public void renderParticle(final Particle p) {
		if (p.isDead())
			return;

		final double width = 0.02;
		final Point2d location = p.location;

		gl.glBegin(GL.GL_QUADS);
		gl.glColor3d(1.0, 0.8, 0.2);
		gl.glVertex2d(location.x - width / 2, location.y - width / 2);
		gl.glColor3d(0.6, 0.9, 0.2);
		gl.glVertex2d(location.x + width / 2, location.y - width / 2);
		gl.glColor3d(0.9, 0.6, 0.2);
		gl.glVertex2d(location.x + width / 2, location.y + width / 2);
		gl.glColor3d(0.8, 1.0, 0.2);
		gl.glVertex2d(location.x - width / 2, location.y + width / 2);
		gl.glEnd();
	}

	public void drawCircle(final double x, final double y, final double radius) {
		drawCircle(x, y, radius, 100, GL.GL_LINE_LOOP);
	}

	public void drawFilledCircle(final double x, final double y,
			final double radius) {
		drawCircle(x, y, radius, 100, GL.GL_POLYGON);
	}

	public void drawCircle(final double x, final double y, final double radius,
			final int segments, final int mode) {
		final double angleStep = 2 * Math.PI / segments;
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glLineWidth(1.0f);

		gl.glBegin(mode);
		for (int i = 0; i < segments; ++i) {
			final double angle = i * angleStep;
			gl.glVertex2d(x + (Math.cos(angle) * radius), y
					+ (Math.sin(angle) * radius));
		}
		gl.glEnd();
		gl.glPopMatrix();
	}

	public void renderMouse() {
		if (!game.mouse.onScreen)
			return;
		final Point2d p = game.mouse.location;
		final double col = 0.4 + 0.6 * Math.abs(Math
				.sin(2 * graphicsTime.clock));
		gl.glColor3d(col, col, col);
		drawFilledCircle(p.x, p.y, game.mouse.radius);
	}

	@Override
	public void display(GLAutoDrawable drawable) {
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// Not implemented by JOGL.
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// We have a fresh GL context, retrieve reference.
		gl = canvas.getGL();

		// ... and initialise.
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-1.0f, 1.0f, -1.0f, 1.0f); // drawing square
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// The canvas has been updated.
		gl.glViewport(0, 0, width, height);
		size = new Point2d(width, height);
	}
}
