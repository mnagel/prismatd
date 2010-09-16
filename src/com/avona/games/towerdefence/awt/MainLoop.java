package com.avona.games.towerdefence.awt;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.InputActor;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.TimeTrack;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

public class MainLoop extends PortableMainLoop implements GLEventListener {
	final private int EXPECTED_FPS = 60;

	public InputMangler input;
	private Animator animator;

	@Override
	public void exit() {
		animator.stop();
		System.exit(0);
	}

	public MainLoop() {
		super();

		//gameTime = new TimeTrack();
		//graphicsTime = new TimeTrack();

		//game = new Game();

		GraphicsEngine graphicsEngine = new GraphicsEngine(game, mouse,
				layerHerder);
		ge = graphicsEngine;
		graphicsEngine.canvas.addGLEventListener(this);
		inputActor = new InputActor(this, game, mouse, layerHerder);
		input = new InputMangler(graphicsEngine, this, inputActor);

		animator = new FPSAnimator(graphicsEngine.canvas, EXPECTED_FPS);
		animator.setRunAsFastAsPossible(false);
		animator.start();
	}

	public static void main(String[] args) {
		new MainLoop();
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		performLoop();
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
		// Unused.
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// Unused.
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4) {
		// Unused.
	}
}
