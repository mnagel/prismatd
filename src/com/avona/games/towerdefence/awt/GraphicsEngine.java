package com.avona.games.towerdefence.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.LayerHerder;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.PortableGraphicsEngine;
import com.avona.games.towerdefence.V2;
import com.sun.opengl.util.j2d.TextRenderer;

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
public class GraphicsEngine extends PortableGraphicsEngine implements
		GLEventListener {
	public Frame frame;
	public GLCanvas canvas;
	public GL gl;
	GLU glu;
	TextRenderer renderer;

	public FloatBuffer squareVertexBuffer;
	public FloatBuffer squareColorBuffer;

	public GraphicsEngine(Game game, Mouse mouse, LayerHerder layerHerder) {
		super(game, mouse, layerHerder);

		renderer = new TextRenderer(new Font("Deja Vu Sans", Font.PLAIN, 12),
				true, true);
		glu = new GLU();

		setupGlCanvas();
		setupFrame();
	}

	private void setupGlCanvas() {
		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setDoubleBuffered(true);

		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.setAutoSwapBufferMode(true);
	}

	private void setupFrame() {
		frame = new Frame("Towerdefence");
		frame.add(canvas);
		frame.setSize(PortableGraphicsEngine.DEFAULT_WIDTH,
				PortableGraphicsEngine.DEFAULT_HEIGHT);
		frame.setBackground(Color.WHITE);
		frame.setCursor(java.awt.Toolkit.getDefaultToolkit()
				.createCustomCursor(
						new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR),
						new java.awt.Point(0, 0), "NOCURSOR"));
		frame.setVisible(true);
	}

	@Override
	public V2 getTextBounds(final String text) {
		Rectangle2D bounds = renderer.getBounds(text);
		return new V2((float) bounds.getWidth(), (float) bounds.getHeight());
	}

	@Override
	public void drawText(final String text, final double x, final double y,
			final float colR, final float colG, final float colB,
			final float colA) {
		renderer.beginRendering((int) size.x, (int) size.y);
		renderer.setColor(colR, colG, colB, colA);
		renderer.draw(text, (int) x, (int) y);
		renderer.endRendering();
	}

	@Override
	public void drawTriangleStrip(final int vertices,
			final FloatBuffer vertexBuffer, final FloatBuffer colorBuffer) {
		gl.glVertexPointer(2, GL.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL.GL_FLOAT, 0, colorBuffer);

		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL.GL_TRIANGLE_STRIP, 0, vertices);

		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
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

		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		size = new V2(width, height);

		gl.glViewport(0, 0, (int) size.x, (int) size.y);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, width, 0, height, -1, 1);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		onReshapeScreen();
	}

	@Override
	protected void prepareScreen() {
		// Paint background, clearing previous drawings.
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void prepareTransformationForLayer(Layer layer) {
		gl.glPushMatrix();
		gl.glTranslatef(layer.offset.x, layer.offset.y, 0);
		gl.glScalef(layer.region.x / layer.virtualRegion.x, layer.region.y
				/ layer.virtualRegion.y, 1);
	}

	@Override
	public void resetTransformation() {
		gl.glPopMatrix();
	}

	@Override
	protected void drawLine(int vertices, FloatBuffer vertexBuffer,
			FloatBuffer colorBuffer) {
		gl.glVertexPointer(2, GL.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL.GL_FLOAT, 0, colorBuffer);

		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL.GL_LINE_STRIP, 0, vertices);

		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
	}

	@Override
	protected void drawTriangleFan(int vertices, FloatBuffer vertexBuffer,
			FloatBuffer colorBuffer) {
		gl.glVertexPointer(2, GL.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL.GL_FLOAT, 0, colorBuffer);

		gl.glEnableClientState(GL.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL.GL_TRIANGLE_FAN, 0, vertices);

		gl.glDisableClientState(GL.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL.GL_COLOR_ARRAY);
	}
}
