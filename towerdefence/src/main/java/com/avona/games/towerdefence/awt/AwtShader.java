package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.gfx.Shader;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.jogamp.opengl.GL2;

import java.io.InputStream;

public class AwtShader extends Shader {
	private final GL2 gl;
	private String name;
	private int vertexShader = -1;
	private int fragmentShader = -1;
	private String vertexSource;
	private String fragmentSource;

	public AwtShader(final GL2 gl, String name) {
		this.gl = gl;
		this.name = name;
	}

	private static int compileShader(GL2 gl, String shaderString, int shaderType) {
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

			throw new RuntimeException("Error compiling shader: " + new String(log, 0, logLength[0]));
		}

		return program;
	}

	private int compileVertexShader(GL2 gl, String vertexShaderString) {
		// Compatibility
		vertexShaderString =
				"#if __VERSION__ >= 130\n" +
						"  #define attribute in\n" +
						"  #define varying out\n" +
						"#endif\n" +
						"#ifdef GL_ES \n" +
						"  precision mediump float; \n" +
						"  precision mediump int; \n" +
						"#endif \n" +
						vertexShaderString;
		vertexSource = vertexShaderString;

		if (gl.isGL3core()) {
			vertexShaderString = "#version 130\n" + vertexShaderString;
		}

		return compileShader(gl, vertexShaderString, GL2.GL_VERTEX_SHADER);
	}

	private int compileFragmentShader(GL2 gl, String fragmentShaderString) {
		// Compatibility
		fragmentShaderString =
				"#if __VERSION__ >= 130\n" +
						"  #define varying in\n" +
						"  out vec4 mgl_FragColor;\n" +
						"  #define texture2D texture\n" +
						"  #define gl_FragColor mgl_FragColor\n" +
						"#endif\n" +
						"#ifdef GL_ES \n" +
						"  precision mediump float; \n" +
						"  precision mediump int; \n" +
						"#endif \n" +
						fragmentShaderString;

		fragmentSource = fragmentShaderString;

		if (gl.isGL3core()) {
			fragmentShaderString = "#version 130\n" + fragmentShaderString;
		}

		return compileShader(gl, fragmentShaderString, GL2.GL_FRAGMENT_SHADER);
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

	@Override
	public String getShaderProgram(String shaderProgramName) {
		final String baseDir = "shaders/";
		final InputStream is = ResourceResolverRegistry.getInstance().getRawResource(baseDir + shaderProgramName);

		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public void loadShaderPrograms(String vertexShaderProgram, String fragmentShaderProgram) {
		unloadShaderProgram();

		program = gl.glCreateProgram();

		if (vertexShaderProgram != null) {
			vertexShader = compileVertexShader(gl, vertexShaderProgram);
			gl.glAttachShader(program, vertexShader);
		}

		if (fragmentShaderProgram != null) {
			fragmentShader = compileFragmentShader(gl, fragmentShaderProgram);
			gl.glAttachShader(program, fragmentShader);
		}

		gl.glLinkProgram(program);

		int[] linked = new int[1];
		gl.glGetProgramiv(program, GL2.GL_LINK_STATUS, linked, 0);

		if (linked[0] == 0) {
			int[] logLength = new int[1];
			gl.glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);
			gl.glGetShaderiv(program, GL2.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetProgramInfoLog(program, logLength[0], logLength, 0, log, 0);
			throw new RuntimeException("Error compiling shader: " + new String(log, 0, logLength[0]));
		}
	}

	@Override
	public void unloadShaderProgram() {
		if (vertexShader >= 0) {
			gl.glDetachShader(program, vertexShader);
			gl.glDeleteShader(vertexShader);
			vertexShader = -1;
		}

		if (fragmentShader >= 0) {
			gl.glDetachShader(program, fragmentShader);
			gl.glDeleteShader(fragmentShader);
			fragmentShader = -1;
		}

		if (program >= 0) {
			gl.glDeleteProgram(program);
			program = -1;
		}
	}

	@Override
	protected int getUniformLocation(String name) {
		return gl.glGetUniformLocation(program, name);
	}
}