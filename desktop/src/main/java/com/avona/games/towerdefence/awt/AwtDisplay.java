package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.gfx.*;
import com.avona.games.towerdefence.input.Layer;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.avona.games.towerdefence.util.Util;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.awt.TextRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
@SuppressWarnings("AccessStaticViaInstance")
public class AwtDisplay extends PortableDisplay implements GLEventListener {
	public static final int DEFAULT_HEIGHT = 480;
	public static final int DEFAULT_WIDTH = 675;

	// CTRL-F courtesy: FONTSIZE FONT_SIZE FONT SIZE
	public Frame frame;
	public GLCanvas canvas;
	private Shader defaultShader;
	private GL2 GLES20;
	private TextRenderer renderer;
	private V2 size = new V2();
	private DisplayEventListener eventListener;

	public AwtDisplay(DisplayEventListener eventListener) {
		this.eventListener = eventListener;
		// CTRL-F courtesy: FONTSIZE FONT_SIZE FONT SIZE
		setupGlCanvas();
		setupFrame();
	}

	public static void checkGLError_static(String trace, GL2 GLES20) {
		int error;
		boolean hadError = false;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Util.log(trace + ": glError " + error);
			hadError = true;
		}

		if (hadError && FeatureFlags.TRACE_ON_GL_ERROR) {
			if (FeatureFlags.CRASH_ON_GL_ERROR) {
				throw new RuntimeException("crash on glError");
			}
		}
	}

	@Override
	public void checkGLError(String format, Object... args) {
		if (FeatureFlags.CHECK_FOR_GL_ERROR) {
			checkGLError_static(String.format(format, args), GLES20);
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
		frame = new Frame("PrismaTD");
		frame.add(canvas);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
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

		float ratio = 800.0f / 480.0f;
		int ratioHeight = (int) ((float) width / ratio);
		ratioHeight = Math.min(height, ratioHeight);
		int fontSize = (int) ((float) ratioHeight * FONT_SIZE_RATIO_HEIGHT_FACTOR + 0.5f);
		renderer = new TextRenderer(new Font("Deja Vu Sans", Font.PLAIN, fontSize), true, true);

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
		defaultShader.loadShaderProgramFromFile("default");

		AwtReplShader awtReplShader = new AwtReplShader(GLES20, "repl shader hack");
		awtReplShader.loadShaderProgramFromFile("default");
		AwtReplShader.setInstance(awtReplShader);

		eventListener.onNewScreenContext();
		checkGLError("after onSurfaceCreated");
	}

	@Override
	public void prepareScreen() {
		// Paint background, clearing previous drawings.
		//GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	}

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
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, array.texture.textureId);
			int textureLoc = GLES20.glGetUniformLocation(program, "u_texture");
			GLES20.glUniform1i(textureLoc, 0);

			array.textureBuffer.position(0);
			int texCoordinateAttrib = GLES20.glGetAttribLocation(program, "a_texCoordinate");
			GLES20.glVertexAttribPointer(texCoordinateAttrib, 2, GLES20.GL_FLOAT, false, 0, array.textureBuffer);
			GLES20.glEnableVertexAttribArray(texCoordinateAttrib);
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
	public void drawText(final Layer layer, String text, boolean centeredHorizontal, boolean centeredVertical, final V2 location, final RGB color, float alpha) {
		V2 loc;
		if (layer != null) {
			loc = layer.convertToPhysical(location);
		} else {
			loc = location.clone2();
		}
		V2 textBounds = null;
		if (centeredHorizontal || centeredVertical) {
			textBounds = getTextBounds(text);
		}
		if (centeredHorizontal) {
			loc.x -= textBounds.x / 2;
		}
		if (centeredVertical) {
			loc.y -= textBounds.y / 2;
		}

		renderer.beginRendering((int) size.x, (int) size.y);
		renderer.setColor(color.R, color.G, color.B, alpha);
		renderer.draw(text, (int) loc.x, (int) loc.y);
		renderer.endRendering();
	}

	public V2 getTextBounds(final String text) {
		Rectangle2D bounds = renderer.getBounds(text);
		return new V2((float) bounds.getWidth(), (float) bounds.getHeight());
	}
}
