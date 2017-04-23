package com.avona.games.towerdefence.android;

import android.opengl.GLES20;

import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;

import java.io.InputStream;

public class AndroidShader extends Shader {
	private String name;
	private int vertexShader = -1;
	private int fragmentShader = -1;

	public AndroidShader(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	private static int compileShader(String shaderString, int shaderType) {
		int program = GLES20.glCreateShader(shaderType);

		GLES20.glShaderSource(program, shaderString);
		GLES20.glCompileShader(program);

		int[] compiled = new int[1];
		GLES20.glGetShaderiv(program, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			GLES20.glDeleteProgram(program);
			throw new RuntimeException("Error compiling shader: " + GLES20.glGetShaderInfoLog(program));
		}

		return program;
	}

	private static int compileVertexShader(String vertexShaderString) {
		return compileShader(vertexShaderString, GLES20.GL_VERTEX_SHADER);
	}

	private static int compileFragmentShader(String fragmentShaderString) {
		return compileShader(fragmentShaderString, GLES20.GL_FRAGMENT_SHADER);
	}

	private String getShaderAsString(String shaderFilename) {
		final String baseDir = "";
		final InputStream is = ResourceResolverRegistry.getInstance().getRawResource(baseDir + shaderFilename);

		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	@Override
	public void loadShaderProgram(String vertexShaderFilename, String fragmentShaderFilename) {
		unloadShaderProgram();

		program = GLES20.glCreateProgram();

		if (vertexShaderFilename != null) {
			vertexShader = compileVertexShader(getShaderAsString(vertexShaderFilename));
			GLES20.glAttachShader(program, vertexShader);
		}

		if (fragmentShaderFilename != null) {
			fragmentShader = compileFragmentShader(getShaderAsString(fragmentShaderFilename));
			GLES20.glAttachShader(program, fragmentShader);
		}

		GLES20.glBindAttribLocation(program, 0, "a_position");
		GLES20.glBindAttribLocation(program, 1, "a_color");

		GLES20.glLinkProgram(program);

		int[] linked = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
		if (linked[0] == 0) {
			GLES20.glDeleteProgram(program);
			throw new RuntimeException("Error linking shader: " + GLES20.glGetProgramInfoLog(program));
		}
	}

	@Override
	public void unloadShaderProgram() {
		if (vertexShader >= 0) {
			GLES20.glDetachShader(program, vertexShader);
			GLES20.glDeleteShader(vertexShader);
			vertexShader = -1;
		}

		if (fragmentShader >= 0) {
			GLES20.glDetachShader(program, fragmentShader);
			GLES20.glDeleteShader(fragmentShader);
			fragmentShader = -1;
		}

		if (program >= 0) {
			GLES20.glDeleteProgram(program);
			program = -1;
		}
	}

	@Override
	protected int getUniformLocation(String name) {
		return GLES20.glGetUniformLocation(program, name);
	}
}