package com.avona.games.towerdefence.android;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Paint;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.LayerHerder;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.PortableGraphicsEngine;
import com.avona.games.towerdefence.TimeTrack;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;
import com.example.google.LabelMaker;

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
public class GraphicsEngine extends PortableGraphicsEngine implements Renderer {
	private GL10 gl;
	private Paint labelPaint;
	private LabelMaker labels;

	public GraphicsEngine(Game game, Mouse mouse, LayerHerder layerHerder,
			TimeTrack graphicsTime) {
		super(game, mouse, layerHerder, graphicsTime);

		labelPaint = new Paint();
		labelPaint.setTextSize(16);
		labelPaint.setAntiAlias(true);
		labelPaint.setARGB(0xff, 0x00, 0x00, 0x00);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		size = new V2(width, height);

		gl.glViewport(0, 0, (int) size.x, (int) size.y);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, 0, width, 0, height);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		if (labels != null) {
			labels.shutdown(gl);
		}
		labels = new LabelMaker(true, Util.roundUpPower2(width), Util
				.roundUpPower2(64));
		labels.initialize(gl);

		onReshapeScreen();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		this.gl = gl;

		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

	protected void prepareScreen() {
		// Paint background, clearing previous drawings.
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
	}

	protected void drawTriangleStrip(final int vertices,
			final FloatBuffer vertexBuffer, final FloatBuffer colourBuffer) {
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colourBuffer);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	public void drawText(final String text, final double x, final double y,
			final float colR, final float colG, final float colB,
			final float colA) {
		labels.beginAdding(gl);
		final int l = labels.add(gl, text, labelPaint);
		labels.endAdding(gl);

		labels.beginDrawing(gl);
		labels.draw(gl, (int) x, (int) y, l);
		labels.endDrawing(gl);
	}

	public V2 getTextBounds(final String text) {
		final int ascent = (int) Math.ceil(-labelPaint.ascent());
		final int descent = (int) Math.ceil(labelPaint.descent());
		final int measuredTextWidth = (int) Math.ceil(labelPaint
				.measureText(text));
		final int textHeight = ascent + descent;
		return new V2(measuredTextWidth, textHeight);
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
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, vertices);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}

	@Override
	protected void drawTriangleFan(int vertices, FloatBuffer vertexBuffer,
			FloatBuffer colorBuffer) {
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, vertices);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
	}
}
