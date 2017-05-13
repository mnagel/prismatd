package com.avona.games.towerdefence.awt;

import com.avona.games.towerdefence.gfx.ShaderSource;
import com.avona.games.towerdefence.util.Util;
import com.jogamp.opengl.GL2;

public class AwtReplShader extends AwtShader {
	private static AwtReplShader instance;
	private ShaderSource vertexShaderSource;
	private ShaderSource fragmentShaderSource;

	private boolean needsRecompile = false;

	AwtReplShader(GL2 gl, String name) {
		super(gl, name);
	}

	public static AwtReplShader getInstance() {
		return instance;
	}

	static void setInstance(AwtReplShader theInstance) {
		instance = theInstance;
	}

	// TODO "mostly threadsafe..."
	void setPrograms(ShaderSource vertexShaderSource, ShaderSource fragmentShaderSource) {
		this.vertexShaderSource = vertexShaderSource;
		this.fragmentShaderSource = fragmentShaderSource;
		this.needsRecompile = true;
	}

	public void recompileIfNeeded() {
		if (needsRecompile) {
			Util.log("compiling shader vertexShaderSource: " + vertexShaderSource);
			Util.log("compiling shader fragmentShaderSource: " + fragmentShaderSource);
			this.loadShaderProgramFromSource(vertexShaderSource, fragmentShaderSource);
		}
		needsRecompile = false;
	}

	@Override
	public void setUniform(String name, Object value) {
		recompileIfNeeded(); // TODO hijacking this call is a dirty hack...
		// ...but it is somewhat guaranteed to happen between two frames in the correct thread
		super.setUniform(name, value);
	}
}