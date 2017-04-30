package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.util.Util;
import com.jogamp.opengl.GL2;

public class AwtReplShader extends AwtShader {
	private static AwtReplShader instance;
	private String vertexShaderProgram;
	private String fragmentShaderProgram;

	private boolean needsRecompile = false;

	AwtReplShader(GL2 gl, String name) {
		super(gl, name);
	}

	// TODO "mostly threadsafe..."
	void setPrograms(String vertexShaderProgram, String fragmentShaderProgram) {
		this.vertexShaderProgram = vertexShaderProgram;
		this.fragmentShaderProgram = fragmentShaderProgram;
		this.needsRecompile = true;
	}

	public void recompileIfNeeded() {
		if (needsRecompile) {
			Util.log("compiling shaders vertexShaderProgram: " + vertexShaderProgram);
			Util.log("compiling shaders fragmentShaderProgram: " + fragmentShaderProgram);
			this.loadShaderPrograms(vertexShaderProgram, fragmentShaderProgram);
		}
		needsRecompile = false;
	}

	@Override
	public void setUniform(String name, Object value) {
		recompileIfNeeded(); // TODO hijacking this call is a dirty hack...
		// ...but it is somewhat guaranteed to happen between two frames in the correct thread
		super.setUniform(name, value);
	}

	public static AwtReplShader getInstance() {
		return instance;
	}

	static void setInstance(AwtReplShader theInstance) {
		instance = theInstance;
	}
}