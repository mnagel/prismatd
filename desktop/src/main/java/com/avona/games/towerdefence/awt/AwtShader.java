package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.gfx.ShaderSource;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.avona.games.towerdefence.util.FeatureFlags;
import com.jogamp.opengl.GL2;

import java.io.InputStream;
import java.util.Locale;

public class AwtShader extends Shader {
	private final GL2 GLES20;
	private int vertexShader = -1;
	private int fragmentShader = -1;
	private ShaderSource vertexSource;
	private ShaderSource fragmentSource;

	public AwtShader(final GL2 GLES20, String name) {
		super(name);

		this.GLES20 = GLES20;
	}

	private int compileShader(GL2 gl, String shaderString, int shaderType) {
		int program = gl.glCreateShader(shaderType);

		String[] lines = new String[]{shaderString};
		int[] lengths = new int[]{lines[0].length()};
		gl.glShaderSource(program, lines.length, lines, lengths, 0);
		gl.glCompileShader(program);

		int[] compiled = new int[1];
		gl.glGetShaderiv(program, GL2.GL_COMPILE_STATUS, compiled, 0);

		if (compiled[0] == 0) {
			int[] logLength = new int[1];
			gl.glGetShaderiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(program, logLength[0], logLength, 0, log, 0);

			throw new RuntimeException(String.format(Locale.US, "Error compiling shader %s in format 0:$LINE(...) %s, %s, %d", getName(), new String(log, 0, logLength[0]), shaderString, shaderType));
		}

		return program;
	}

	private int compileVertexShader(GL2 gl, ShaderSource vertexShaderSource) {
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

		if (gl.isGL3core()) {
			vertexShaderString = "#version 130\n" + vertexShaderString;
		}

		return compileShader(gl, vertexShaderString, GL2.GL_VERTEX_SHADER);
	}

	private int compileFragmentShader(GL2 gl, ShaderSource fragmentShaderSource) {
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

		if (gl.isGL3core()) {
			fragmentShaderString = "#version 130\n" + fragmentShaderString;
		}

		return compileShader(gl, fragmentShaderString, GL2.GL_FRAGMENT_SHADER);
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
		final String baseDir = "shaders/";
		final InputStream is = ResourceResolverRegistry.getInstance().getRawResource(baseDir + filename);

		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return new ShaderSource(s.hasNext() ? s.next() : "");
	}

	@Override
	public void loadShaderProgramFromSource(ShaderSource vertexShaderSource, ShaderSource fragmentShaderSource) {
		unloadShaderProgram();

		program = GLES20.glCreateProgram();

		if (vertexShaderSource != null) {
			vertexShader = compileVertexShader(GLES20, vertexShaderSource);
			GLES20.glAttachShader(program, vertexShader);
		}

		if (fragmentShaderSource != null) {
			fragmentShader = compileFragmentShader(GLES20, fragmentShaderSource);
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
		GLES20.glGetProgramiv(program, GL2.GL_LINK_STATUS, linked, 0);

		if (linked[0] == 0) {
			int[] logLength = new int[1];
			GLES20.glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
			GLES20.glGetShaderiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			GLES20.glGetProgramInfoLog(program, logLength[0], logLength, 0, log, 0);
			throw new RuntimeException("Error compiling shader: " + new String(log, 0, logLength[0]));
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
		AwtDisplay.checkGLError_static("shader bind", GLES20);
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