package com.avona.games.towerdefence.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import com.android.texample2.GLText;
import com.avona.games.towerdefence.core.RGB;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.gfx.*;
import com.avona.games.towerdefence.input.Layer;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.avona.games.towerdefence.util.Util;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;

/**
 * This class provides all basic drawing primitives for the Android platform.
 */
public class AndroidDisplay extends PortableDisplay implements Renderer {
	private Shader defaultShader;
	private GLText glText;
	private V2 size;
	private DisplayEventListener eventListener;
	private AssetManager assetManager;

	public AndroidDisplay(Context context, DisplayEventListener eventListener) {
		this.eventListener = eventListener;
		this.assetManager = context.getAssets();
	}

	public static void checkGLError_static(String trace) {
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
	public void checkGLError(String trace) {
		checkGLError_static(trace);
	}

	@Override
	public void onSurfaceChanged(GL10 glUnused, int width, int height) {
		size = new V2(width, height);

		GLES20.glViewport(0, 0, (int) size.x, (int) size.y);

		initializeMatrices(width, height);

		float ratio = 800.0f / 480.0f;
		int ratioHeight = (int) ((float) width / ratio);
		ratioHeight = Math.min(height, ratioHeight);
		int fontSize = (int) ((float) ratioHeight * FONT_SIZE_RATIO_HEIGHT_FACTOR + 0.5f);
		glText.load("Roboto-Regular.ttf", fontSize, 2, 2);

		eventListener.onReshapeScreen();
	}

	@Override
	public void onDrawFrame(GL10 gl) {

	}

	@Override
	public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		defaultShader = allocateShader("default");
		defaultShader.loadShaderProgramFromFile("default");

		Shader textShader = allocateShader("text");
		textShader.loadShaderProgramFromFile("text");

		glText = new GLText(textShader.getProgram(), assetManager);

		eventListener.onNewScreenContext();
		checkGLError("after onSurfaceCreated");
	}

	public void prepareScreen() {
		// Paint background, clearing previous drawings.
		//GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
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
				checkGLError(String.format("shader: %s, variable: %s, value: %s", array.shader.getName(), variable.name, variable.value));
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
		Texture texture = new AndroidTexture();

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
		return new AndroidShader(name);
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

		glText.begin(color.R, color.G, color.B, alpha, getViewProjectionMatrix());
		glText.draw(text, loc.x, loc.y);
		glText.end();
		checkGLError("after drawText");
	}

	@Override
	public V2 getTextBounds(final String text) {
		float height = glText.getHeight();
		float width = glText.getLength(text);
		return new V2(width, height);
	}
}
