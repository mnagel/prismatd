package com.avona.games.towerdefence.gfx;

import java.util.HashMap;

public abstract class Shader {
	private static final String EXT_VERTEX_SHADER = ".vert";
	private static final String EXT_FRAGMENT_SHADER = ".frag";
	protected int program = -1;
	private String name;
	private HashMap<String, Variable> uniforms = new HashMap<>();

	public Shader(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	abstract public ShaderSource getShaderSource(String filename);

	abstract public void loadShaderProgramFromSource(ShaderSource vertexShaderSource, ShaderSource fragmentShaderSource);

	public void loadShaderProgramFromFile(String filename) {
		ShaderSource vertexShaderSource = getShaderSource(filename + EXT_VERTEX_SHADER);
		ShaderSource fragmentShaderSource = getShaderSource(filename + EXT_FRAGMENT_SHADER);
		loadShaderProgramFromSource(vertexShaderSource, fragmentShaderSource);
	}

	abstract public void unloadShaderProgram();

	public int getProgram() {
		if (program < 0) {
			throw new RuntimeException("Shader program not initialized");
		}

		return program;
	}

	public abstract ShaderSource getVertexSource();

	public abstract ShaderSource getFragmentSource();

	protected abstract int getUniformLocation(String name);

	public void setUniform(String name, Object value) {
		if (!uniforms.containsKey(name)) {
			uniforms.put(name, new Variable(name, getUniformLocation(name)));
		}

		uniforms.get(name).value = value;
	}

	public HashMap<String, Variable> getUniforms() {
		return uniforms;
	}

	public class Variable {
		public String name;
		public int uniformLocation;
		public Object value;

		public Variable(String name, int uniformLocation) {
			this.name = name;
			this.uniformLocation = uniformLocation;
		}
	}
}
