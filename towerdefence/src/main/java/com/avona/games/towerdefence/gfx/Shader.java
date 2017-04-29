package com.avona.games.towerdefence.gfx;

import java.util.HashMap;

public abstract class Shader {
	protected int program = -1;
	private HashMap<String, Variable> uniforms = new HashMap<>();

	abstract public String getShaderProgram(String shaderProgramName);

	abstract public void loadShaderPrograms(String vertexShaderProgram, String fragmentShaderProgram);

	public void loadShaderProgramsByName(String vertexShaderProgramName, String fragmentShaderProgramName) {
		String vertexShaderProgram = getShaderProgram(vertexShaderProgramName);
		String fragmentShaderProgram = getShaderProgram(fragmentShaderProgramName);
		loadShaderPrograms(vertexShaderProgram, fragmentShaderProgram);
	}

	abstract public void unloadShaderProgram();

	public int getProgram() {
		if (program < 0) {
			throw new RuntimeException("Shader program not initialized");
		}

		return program;
	}

	public abstract String getName();

	public abstract String getVertexSource();

	public abstract String getFragmentSource();

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
