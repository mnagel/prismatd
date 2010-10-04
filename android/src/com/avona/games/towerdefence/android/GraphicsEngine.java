package com.avona.games.towerdefence.android;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Paint;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.LayerHerder;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.gfx.Texture;
import com.avona.games.towerdefence.gfx.VertexArray;
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

	public GraphicsEngine(Game game, Mouse mouse, LayerHerder layerHerder) {
		super(game, mouse, layerHerder);

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

		onNewScreenContext();
	}

	protected void prepareScreen() {
		// Paint background, clearing previous drawings.
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
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
	protected void drawVertexArray(final VertexArray array) {
		assert array.coordBuffer != null;
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		array.coordBuffer.position(0);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, array.coordBuffer);

		if (array.hasColour) {
			assert array.colourBuffer != null;
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			array.colourBuffer.position(0);
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, array.colourBuffer);
		}
		if (array.hasTexture) {
			assert array.textureBuffer != null;
			assert array.texture != null;
			array.textureBuffer.position(0);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, array.texture.textureId);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, array.textureBuffer);
		}

		if (array.mode == VertexArray.Mode.TRIANGLE_FAN) {
			gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, array.numCoords);
		} else if (array.mode == VertexArray.Mode.TRIANGLE_STRIP) {
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, array.numCoords);
		} else if (array.mode == VertexArray.Mode.TRIANGLES) {
			assert array.indexBuffer != null;
			array.indexBuffer.position(0);
			gl.glDrawElements(GL10.GL_TRIANGLES, array.numIndexes,
					GL10.GL_UNSIGNED_SHORT, array.indexBuffer);
		} else if (array.mode == VertexArray.Mode.LINE_STRIP) {
			gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, array.numCoords);
		}

		if (array.hasTexture) {
			gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		if (array.hasColour) {
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	@Override
	public Texture allocateTexture() {
		assert gl != null;

		Texture texture = new AndroidTexture(gl);

		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		texture.textureId = textures[0];

		assert gl.glGetError() == 0;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture.textureId);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);

		gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_REPLACE);

		Util.log("glGetError after create texture: " + gl.glGetError());
		assert gl.glGetError() == 0;
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

		return texture;
	}
}
