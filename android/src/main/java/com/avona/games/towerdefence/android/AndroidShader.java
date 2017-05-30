package com.avona.games.towerdefence.android;

import android.opengl.GLES20;
import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.ShaderSource;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.avona.games.towerdefence.util.FeatureFlags;

import java.io.InputStream;

public class AndroidShader extends Shader {
	private int vertexShader = -1;
	private int fragmentShader = -1;
	private ShaderSource vertexSource;
	private ShaderSource fragmentSource;

	public AndroidShader(String name) {
		super(name);
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

	private int compileVertexShader(ShaderSource vertexShaderSource) {
		vertexSource = vertexShaderSource;

		// Compatibility
		String vertexShaderString =
				"#if __VERSION__ >= 130\n" +
						"  #define attribute in\n" +
						"  #define varying out\n" +
						"#endif\n" +
						"#ifdef GL_ES \n" +
						"  precision mediump float; \n" +
						"  precision mediump int; \n" +
						"#else\n" +
						"  #define mediump\n" +
						"  #define highp\n" +
						"  #define lowp\n" +
						"#endif \n" +
						vertexSource.toString();

		return compileShader(vertexShaderString, GLES20.GL_VERTEX_SHADER);
	}

	private int compileFragmentShader(ShaderSource fragmentShaderSource) {
		fragmentSource = fragmentShaderSource;

		// Compatibility
		String fragmentShaderString =
				"#if __VERSION__ >= 130\n" +
						"  #define varying in\n" +
						"  out vec4 mgl_FragColor;\n" +
						"  #define texture2D texture\n" +
						"  #define gl_FragColor mgl_FragColor\n" +
						"#endif\n" +
						"#ifdef GL_ES \n" +
						"  precision mediump float; \n" +
						"  precision mediump int; \n" +
						"#else\n" +
						"  #define mediump\n" +
						"  #define highp\n" +
						"  #define lowp\n" +
						"#endif \n" +
						fragmentSource.toString();

		return compileShader(fragmentShaderString, GLES20.GL_FRAGMENT_SHADER);
	}

	@Override
	public ShaderSource getVertexSource() {
		return vertexSource;
	}

	@Override
	public ShaderSource getFragmentSource() {
		return fragmentSource;
	}

	@Override
	public ShaderSource getShaderSource(String filename) {
		final String baseDir = "";
		final InputStream is = ResourceResolverRegistry.getInstance().getRawResource(baseDir + filename);

		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return new ShaderSource(s.hasNext() ? s.next() : "");
	}

	@Override
	public void loadShaderProgramFromSource(ShaderSource vertexShaderSource, ShaderSource fragmentShaderSource) {
		unloadShaderProgram();

		program = GLES20.glCreateProgram();

		if (vertexShaderSource != null) {
			vertexShader = compileVertexShader(vertexShaderSource);
			GLES20.glAttachShader(program, vertexShader);
		}

		if (fragmentShaderSource != null) {
			fragmentShader = compileFragmentShader(fragmentShaderSource);
			GLES20.glAttachShader(program, fragmentShader);
		}

		GLES20.glBindAttribLocation(program, 0, "a_position");
		GLES20.glBindAttribLocation(program, 1, "a_color");
		GLES20.glBindAttribLocation(program, 2, "a_texCoordinate");

		if (name.equals("text")) {
			GLES20.glBindAttribLocation(program, 3, "a_mvpMatrixIndex");
		}

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
	public int getUniformLocation(String name) {
		int res = GLES20.glGetUniformLocation(program, name);
		AndroidDisplay.checkGLError_static("shader bind");
		if (res == -1 && FeatureFlags.TRACE_ON_GL_ERROR) {
			if (FeatureFlags.CRASH_ON_GL_ERROR) {
				throw new RuntimeException("shader bind");
			}
		}
		return res;
	}

	@Override
	public int getAttribLocation(String name) {
		return GLES20.glGetAttribLocation(program, name);
	}
}