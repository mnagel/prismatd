package com.avona.games.towerdefence.awt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.res.ResourceResolverRegistry;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;

public class MainLoop extends PortableMainLoop implements GLEventListener {
	private static final long serialVersionUID = 1L;

	final private int EXPECTED_FPS = 30;

	public InputMangler input;
	private Animator animator;

	@Override
	public void exit() {
		animator.stop();
		System.exit(0);
	}

	public MainLoop() {
		super();

		initNewGame();
	}

	public MainLoop(String[] args) {
		super();

		if (args.length == 0) {
			initNewGame();
		} else {
			try {
				loadGame(args[0]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void initNewGame() {
		ResourceResolverRegistry.setInstance(new FileResourceResolver("gfx"));

		GraphicsEngine graphicsEngine = new GraphicsEngine(game, mouse,
				layerHerder, graphicsTime);
		ge = graphicsEngine;
		graphicsEngine.canvas.addGLEventListener(this);

		setupInputActors();
		input = new InputMangler(graphicsEngine, this, inputActor);

		animator = new FPSAnimator(graphicsEngine.canvas, EXPECTED_FPS);
		animator.setRunAsFastAsPossible(false);
		animator.start();

		try {
			final FileOutputStream fos = new FileOutputStream("savegame");
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadGame(final String filename) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		final InputStream is = new FileInputStream(filename);
		final ObjectInputStream ois = new ObjectInputStream(is);

		// Otherwise the parent class would've set up game...
		game = (Game) ois.readObject();
		// ...thus we can call initNewGame now.
		initNewGame();
	}

	public static void main(String[] args) {
		new MainLoop(args);
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

	@Override
	public void serialize() {
		try {
			final FileOutputStream fos = new FileOutputStream("savegame");
			final ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game);
			System.out.println("game written");
		} catch (IOException exc) {
			// TODO Auto-generated catch block
			exc.printStackTrace();
		}
	}
}
