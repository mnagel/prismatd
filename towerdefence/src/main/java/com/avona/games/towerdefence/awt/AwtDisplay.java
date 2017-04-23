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

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class AwtDisplay extends PortableDisplay implements GLEventListener {
	public Frame frame;
	public GLCanvas canvas;
	private Shader defaultShader;
	private GL2 GLES20;
	private TextRenderer renderer;
	private V2 size = new V2();
	private DisplayEventListener eventListener;

	public AwtDisplay(DisplayEventListener eventListener) {
		this.eventListener = eventListener;
		renderer = new TextRenderer(new Font("Deja Vu Sans", Font.PLAIN, 12), true, true);
		setupGlCanvas();
		setupFrame();
	}

	private void checkGLError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Util.log(op + ": glError " + error);
		}
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
		frame.setSize(PortableGraphicsEngine.DEFAULT_WIDTH, PortableGraphicsEngine.DEFAULT_HEIGHT);
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
	public void dispose(GLAutoDrawable drawable) {
		throw new RuntimeException("added for jogl2. unneeded(?).");
	}

	@Override
	public void display(GLAutoDrawable drawable) {

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		size = new V2(width, height);

		GLES20.glViewport(0, 0, (int) size.x, (int) size.y);

		initializeMatrices(width, height);

		eventListener.onReshapeScreen();
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// We have a fresh GL context, retrieve reference.
		GLES20 = (GL2) canvas.getGL();
		myinit();
	}

	public void myinit() {
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		defaultShader = allocateShader("default");
		defaultShader.loadShaderProgram("default.vert", "default.frag");

		eventListener.onNewScreenContext();
		checkGLError("after onSurfaceCreated");
	}

	public void prepareScreen() {
		// Paint background, clearing previous drawings.
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

	@Override
	public void drawVertexArray(final VertexArray array) {
		int program;
		if (array.hasShader) {
			program = array.shader.getProgram();
			checkGLError("before glUseProgram");
			GLES20.glUseProgram(program);

			HashMap<String, Shader.Variable> variables = array.shader.getUniforms();
			for (Shader.Variable variable : variables.values()) {
				if (variable.value == null) {
					continue;
				}
				if (variable.value instanceof Integer) {
					GLES20.glUniform1i(variable.uniformLocation, (Integer) variable.value);
				}
				if (variable.value instanceof Boolean) {
					GLES20.glUniform1i(variable.uniformLocation, (Boolean) variable.value ? 1 : 0);
				}
				if (variable.value instanceof Float) {
					GLES20.glUniform1f(variable.uniformLocation, (Float) variable.value);
				}
				if (variable.value instanceof V2) {
					V2 v = (V2) variable.value;
					GLES20.glUniform2f(variable.uniformLocation, v.x, v.y);
				}
				checkGLError("after variable " + array.shader.getName() + ":" + variable.name);
			}
		} else {
			program = defaultShader.getProgram();
			checkGLError("before glUseProgram");
			GLES20.glUseProgram(program);
		}

		int mvpMatrixLoc = GLES20.glGetUniformLocation(program, "u_mvpMatrix");
		GLES20.glUniformMatrix4fv(mvpMatrixLoc, 1, false, getMvpMatrix(), 0);
		checkGLError("after u_mvpMatrix");

		array.coordBuffer.position(0);
		int posAttrib = GLES20.glGetAttribLocation(program, "a_position");
		GLES20.glVertexAttribPointer(posAttrib, 2, GLES20.GL_FLOAT, false, 0, array.coordBuffer);
		GLES20.glEnableVertexAttribArray(posAttrib);
		checkGLError("after a_position");

		array.colourBuffer.position(0);
		int colAttrib = GLES20.glGetAttribLocation(program, "a_color");
		GLES20.glVertexAttribPointer(colAttrib, 4, GLES20.GL_FLOAT, false, 0, array.colourBuffer);
		GLES20.glEnableVertexAttribArray(colAttrib);
		checkGLError("after a_color");

		if (array.hasTexture) {
			int textureLoc = GLES20.glGetUniformLocation(program, "u_texture");
			int texCoordinateLoc = GLES20.glGetAttribLocation(program, "a_texCoordinate");

			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, array.texture.textureId);
			GLES20.glUniform1i(textureLoc, 0);

			array.textureBuffer.position(0);
			GLES20.glVertexAttribPointer(texCoordinateLoc, 2, GLES20.GL_FLOAT, false, 0, array.textureBuffer);
			GLES20.glEnableVertexAttribArray(texCoordinateLoc);
			checkGLError("after u_texture");
		}

		if (array.mode == VertexArray.Mode.TRIANGLE_FAN) {
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, array.numCoords);
		} else if (array.mode == VertexArray.Mode.TRIANGLE_STRIP) {
			GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, array.numCoords);
		} else if (array.mode == VertexArray.Mode.TRIANGLES) {
			assert array.indexBuffer != null;
			array.indexBuffer.position(0);
			GLES20.glDrawElements(GLES20.GL_TRIANGLES, array.numIndexes, GLES20.GL_UNSIGNED_SHORT, array.indexBuffer);
		} else if (array.mode == VertexArray.Mode.LINE_STRIP) {
			GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, array.numCoords);
		}
		checkGLError("after glDrawArrays");

		if (array.hasTexture) {
			int texCoordinateLoc = GLES20.glGetAttribLocation(program, "a_texCoordinate");
			GLES20.glDisableVertexAttribArray(texCoordinateLoc);
		}

		GLES20.glDisableVertexAttribArray(posAttrib);
		GLES20.glDisableVertexAttribArray(colAttrib);
		GLES20.glUseProgram(0);
		checkGLError("after drawVertexArray");
	}

	@Override
	public Texture allocateTexture() {
		Texture texture = new AwtTexture(GLES20);

		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		texture.textureId = textures[0];

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture.textureId);

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

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
	public V2 getTextBounds(final String text) {
		Rectangle2D bounds = renderer.getBounds(text);
		return new V2((float) bounds.getWidth(), (float) bounds.getHeight());
	}
}
