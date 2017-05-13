package com.avona.games.towerdefence.android;

import android.opengl.GLES20;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.avona.games.towerdefence.util.FeatureFlags;

import java.io.InputStream;

public class AndroidShader extends Shader {
	private String name;
	private int vertexShader = -1;
	private String vertexSource;
	private int fragmentShader = -1;
	private String fragmentSource;

	public AndroidShader(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getVertexSource() {
		return vertexSource;
	}

	@Override
	public String getFragmentSource() {
		return fragmentSource;
	}

	private int compileShader(String shaderString, int shaderType) {
		int program = GLES20.glCreateShader(shaderType);

		GLES20.glShaderSource(program, shaderString);
		GLES20.glCompileShader(program);

		int[] compiled = new int[1];
		GLES20.glGetShaderiv(program, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] == 0) {
			String shaderInfoLog = GLES20.glGetShaderInfoLog(program);
			GLES20.glDeleteProgram(program);
			throw new RuntimeException("Error compiling shader: " + this.getName() + ": " + shaderInfoLog);
		}

		return program;
	}

	private int compileVertexShader(String vertexShaderString) {
		vertexSource = vertexShaderString;
		return compileShader(vertexShaderString, GLES20.GL_VERTEX_SHADER);
	}

	private int compileFragmentShader(String fragmentShaderString) {
		fragmentSource = fragmentShaderString;
		return compileShader(fragmentShaderString, GLES20.GL_FRAGMENT_SHADER);
	}

	@Override
	public String getShaderProgram(String shaderProgramName) {
		final String baseDir = "";
		final InputStream is = ResourceResolverRegistry.getInstance().getRawResource(baseDir + shaderProgramName);

		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	@Override
	public void loadShaderPrograms(String vertexShaderProgram, String fragmentShaderProgram) {
		unloadShaderProgram();

		program = GLES20.glCreateProgram();

		if (vertexShaderProgram != null) {
			vertexShader = compileVertexShader(vertexShaderProgram);
			GLES20.glAttachShader(program, vertexShader);
		}

		if (fragmentShaderProgram != null) {
			fragmentShader = compileFragmentShader(fragmentShaderProgram);
			GLES20.glAttachShader(program, fragmentShader);
		}

		GLES20.glBindAttribLocation(program, 0, "a_position");
		GLES20.glBindAttribLocation(program, 1, "a_color");

		GLES20.glLinkProgram(program);

		int[] linked = new int[1];
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);
		if (linked[0] == 0) {
			String programInfoLog = GLES20.glGetProgramInfoLog(program);
			GLES20.glDeleteProgram(program);
			throw new RuntimeException("Error linking shader: " + programInfoLog);
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
		int res = GLES20.glGetUniformLocation(program, name);
		AndroidDisplay.checkGLError_static("shader bind");
		if (res == -1 && FeatureFlags.TRACE_ON_GL_ERROR) {
			if (FeatureFlags.CRASH_ON_GL_ERROR) {
				throw new RuntimeException("shader bind");
			}
		}
		return res;
	}
}