package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.RGB;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.gfx.DisplayEventListener;
import com.avona.games.towerdefence.gfx.PortableDisplay;
import com.avona.games.towerdefence.gfx.PortableGraphicsEngine;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.Texture;
import com.avona.games.towerdefence.gfx.VertexArray;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import static com.jogamp.opengl.GL2.GL_BLEND;
import static com.jogamp.opengl.GL2.GL_CLAMP_TO_EDGE;
import static com.jogamp.opengl.GL2.GL_COLOR_ARRAY;
import static com.jogamp.opengl.GL2.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL2.GL_FLOAT;
import static com.jogamp.opengl.GL2.GL_LINEAR;
import static com.jogamp.opengl.GL2.GL_LINE_STRIP;
import static com.jogamp.opengl.GL2.GL_MODELVIEW;
import static com.jogamp.opengl.GL2.GL_NEAREST;
import static com.jogamp.opengl.GL2.GL_ONE_MINUS_SRC_ALPHA;
import static com.jogamp.opengl.GL2.GL_PROJECTION;
import static com.jogamp.opengl.GL2.GL_REPLACE;
import static com.jogamp.opengl.GL2.GL_SRC_ALPHA;
import static com.jogamp.opengl.GL2.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL2.GL_TEXTURE_COORD_ARRAY;
import static com.jogamp.opengl.GL2.GL_TEXTURE_ENV;
import static com.jogamp.opengl.GL2.GL_TEXTURE_ENV_MODE;
import static com.jogamp.opengl.GL2.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL2.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL2.GL_TEXTURE_WRAP_S;
import static com.jogamp.opengl.GL2.GL_TEXTURE_WRAP_T;
import static com.jogamp.opengl.GL2.GL_TRIANGLES;
import static com.jogamp.opengl.GL2.GL_TRIANGLE_FAN;
import static com.jogamp.opengl.GL2.GL_TRIANGLE_STRIP;
import static com.jogamp.opengl.GL2.GL_UNSIGNED_SHORT;
import static com.jogamp.opengl.GL2.GL_VERTEX_ARRAY;

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
public class AwtDisplay extends PortableDisplay implements GLEventListener {
	public Frame frame;
	public GLCanvas canvas;
	private GL2 GLES20;
	private TextRenderer renderer;
	private V2 size = new V2();
	private DisplayEventListener eventListener;

	private void checkGLError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Util.log(op + ": glError " + error);
		}
	}

	public AwtDisplay(DisplayEventListener eventListener) {
		this.eventListener = eventListener;
		renderer = new TextRenderer(new Font("Deja Vu Sans", Font.PLAIN, 12), true, true);
		setupGlCanvas();
		setupFrame();
	}

	private void setupGlCanvas() {
		GLCapabilities capabilities = new GLCapabilities(GLProfile.getDefault());
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
		try {
			Image image = ImageIO.read(ResourceResolverRegistry.getInstance().getRawResource("icon.png"));
			frame.setIconImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setVisible(true);
	}

	@Override
	public V2 getTextBounds(final String text) {
		Rectangle2D bounds = renderer.getBounds(text);
		return new V2((float) bounds.getWidth(), (float) bounds.getHeight());
	}

	@Override
	public void drawText(final Layer layer, String text, boolean centered, final V2 location, final RGB color, float alpha) {
		V2 loc;
		if (layer != null) {
			loc = layer.convertToPhysical(location);
		} else {
			loc = location.clone();
		}
		if (centered) {
			final V2 textBounds = getTextBounds(text);
			loc.x -= textBounds.x / 2;
			loc.y -= textBounds.y / 2;
		}

		renderer.beginRendering((int) size.x, (int) size.y);
		renderer.setColor(color.R, color.G, color.B, alpha);
		renderer.draw(text, (int) loc.x, (int) loc.y);
		renderer.endRendering();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
	}

	/*
	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// Not implemented by JOGL.
	}
	*/

	@Override
	public void init(GLAutoDrawable drawable) {
		// We have a fresh GL context, retrieve reference.
		GLES20 = (GL2) canvas.getGL();

		GLES20.glEnable(GL_BLEND);
		GLES20.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		eventListener.onNewScreenContext();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		throw new RuntimeException("added for jogl2. unneeded(?).");
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
						int height) {
		size = new V2(width, height);

		GLES20.glViewport(0, 0, (int) size.x, (int) size.y);

		initializeMatrices(width, height);

		GLES20.glMatrixMode(GL_PROJECTION);
		GLES20.glLoadMatrixf(getProjectionMatrix(), 0);
		GLES20.glMatrixMode(GL_MODELVIEW);
		GLES20.glLoadMatrixf(getModelViewMatrix(), 0);

		eventListener.onReshapeScreen();
	}

	@Override
	public void prepareScreen() {
		// Paint background, clearing previous drawings.
		GLES20.glColor3d(0.0, 0.0, 0.0);
		GLES20.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void prepareTransformationForLayer(Layer layer) {
		super.prepareTransformationForLayer(layer);
		GLES20.glLoadMatrixf(getModelViewMatrix(), 0);
	}

	@Override
	public void resetTransformation() {
		super.resetTransformation();
		GLES20.glLoadMatrixf(getModelViewMatrix(), 0);
	}

	@Override
	public void drawVertexArray(final VertexArray array) {
		assert array.coordBuffer != null;
		GLES20.glEnableClientState(GL_VERTEX_ARRAY);
		array.coordBuffer.position(0);
		GLES20.glVertexPointer(2, GL_FLOAT, 0, array.coordBuffer);
		if (array.hasColour) {
			assert array.colourBuffer != null;
			GLES20.glEnableClientState(GL_COLOR_ARRAY);
			array.colourBuffer.position(0);
			GLES20.glColorPointer(4, GL_FLOAT, 0, array.colourBuffer);
		}
		if (array.hasTexture) {
			assert array.textureBuffer != null;
			assert array.texture != null;
			array.textureBuffer.position(0);
			GLES20.glEnable(GL_TEXTURE_2D);
			GLES20.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			GLES20.glBindTexture(GL_TEXTURE_2D, array.texture.textureId);
			GLES20.glTexCoordPointer(2, GL_FLOAT, 0, array.textureBuffer);
		}
		if (array.hasShader) {
			assert array.shader != null;
			int program = array.shader.getProgram();

			HashMap<String, Shader.Variable> variables = array.shader.getUniforms();
			for (Shader.Variable variable : variables.values()) {
				if (variable.value == null) {
					continue;
				}
				if (variable.value instanceof Integer) {
					GLES20.glProgramUniform1i(program, variable.uniformLocation, (Integer) variable.value);
				}
				if (variable.value instanceof Boolean) {
					GLES20.glProgramUniform1i(program, variable.uniformLocation, (Boolean) variable.value ? 1 : 0);
				}
				if (variable.value instanceof Float) {
					GLES20.glProgramUniform1f(program, variable.uniformLocation, (Float) variable.value);
				}
				if (variable.value instanceof V2) {
					V2 v = (V2) variable.value;
					GLES20.glProgramUniform2f(program, variable.uniformLocation, v.x, v.y);
				}
			}

			GLES20.glUseProgram(program);

			int mvpMatrixLoc = GLES20.glGetUniformLocation(program, "u_mvpMatrix");
			GLES20.glUniformMatrix4fv(mvpMatrixLoc, 1, false, getMvpMatrix(), 0);

			int posAttrib = GLES20.glGetAttribLocation(program, "a_position");
			GLES20.glEnableVertexAttribArray(posAttrib);
			GLES20.glVertexAttribPointer(posAttrib, 2, GL_FLOAT, false, 0, array.coordBuffer);

			int colAttrib = GLES20.glGetAttribLocation(program, "a_color");
			GLES20.glEnableVertexAttribArray(colAttrib);
			GLES20.glVertexAttribPointer(colAttrib, 4, GL_FLOAT, false, 0, array.colourBuffer);
		}

		if (array.mode == VertexArray.Mode.TRIANGLE_FAN) {
			GLES20.glDrawArrays(GL_TRIANGLE_FAN, 0, array.numCoords);
		} else if (array.mode == VertexArray.Mode.TRIANGLE_STRIP) {
			GLES20.glDrawArrays(GL_TRIANGLE_STRIP, 0, array.numCoords);
		} else if (array.mode == VertexArray.Mode.TRIANGLES) {
			assert array.indexBuffer != null;
			array.indexBuffer.position(0);
			GLES20.glDrawElements(GL_TRIANGLES, array.numIndexes, GL_UNSIGNED_SHORT, array.indexBuffer);
		} else if (array.mode == VertexArray.Mode.LINE_STRIP) {
			GLES20.glDrawArrays(GL_LINE_STRIP, 0, array.numCoords);
		}

		if (array.hasShader) {
			int program = array.shader.getProgram();
			int posAttrib = GLES20.glGetAttribLocation(program, "a_position");
			GLES20.glDisableVertexAttribArray(posAttrib);
			int colAttrib = GLES20.glGetAttribLocation(program, "a_color");
			GLES20.glDisableVertexAttribArray(colAttrib);
			GLES20.glUseProgram(0);
		}
		if (array.hasTexture) {
			GLES20.glBindTexture(GL_TEXTURE_2D, 0);
			GLES20.glDisableClientState(GL_TEXTURE_COORD_ARRAY);
			GLES20.glDisable(GL_TEXTURE_2D);
		}
		if (array.hasColour) {
			GLES20.glDisableClientState(GL_COLOR_ARRAY);
		}
		GLES20.glDisableClientState(GL_VERTEX_ARRAY);
	}

	@Override
	public Texture allocateTexture() {
		Texture texture = new AwtTexture(GLES20);

		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		texture.textureId = textures[0];

		GLES20.glBindTexture(GL_TEXTURE_2D, texture.textureId);

		GLES20.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		GLES20.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		GLES20.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		GLES20.glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);

		checkGLError("after drawVertexArray");
		return texture;
	}

	@Override
	public Shader allocateShader(String name) {
		return new AwtShader(GLES20, name);
	}

	@Override
	public V2 getSize() {
		return size;
	}

	@Override
	public int userSelectsAString(String title, String message, String[] strings) {
		return MainLoop.userSelectsAString(title, message, strings);
	}
}
