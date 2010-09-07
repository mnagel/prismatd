package com.avona.games.towerdefence;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point2d;

import com.sun.opengl.util.Animator;

public class Game implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
	protected List<Enemy> enemies = new LinkedList<Enemy>();
	protected List<Tower> towers = new LinkedList<Tower>();
	protected List<Particle> particles = new LinkedList<Particle>();
	protected World world;

	protected long lastTimestamp;

	public Game() {
		world = new World();
		enemies.add(new Enemy(world));
		towers.add(new Tower(new Point2d(0.5, 0.5)));
	}

	public void display(GLAutoDrawable glDrawable) {
		final long newTime = TimeBase.getTime();
		final long dt = newTime - lastTimestamp;
		lastTimestamp = newTime;

		// Update the world.
		for(Enemy e : enemies) {
			e.step(dt);
		}

		for(Tower t : towers) {
			for(Enemy e : enemies) {
				if(t.inRange(e)) {
					Particle p = t.shootTowards(e, dt);
					if(p != null) {
						particles.add(p);
					}
				}
			}
		}

		for(Particle p : particles) {
			for(Enemy e : enemies) {
				if(p.inRange(e)) {
					p.attack(e);
					if(e.isDead()) {
						//						enemies.remove(e);
					}
				}
			}
			if(p.isDead()) {
				//				particles.remove(p);
			}
			else {
				p.step(dt);
			}
		}

		// Draw it.
		final GL gl = glDrawable.getGL();
		gl.glColor3d(0.0, 0.0, 0.0);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		//gl.glBegin(GL.GL_QUADS);
		//gl.glColor3d(1.0, 1.0, 1.0);
		//gl.glVertex2d(0.9, 0.9);
		//gl.glVertex2d(-0.9, 0.9);
		//gl.glVertex2d(-0.9, -0.9);
		//gl.glVertex2d(0.9, -0.9);
		//gl.glEnd();

		for(Enemy e : enemies) {
			//System.out.println(e);
			e.display(glDrawable);
		}
		for(Tower t : towers) {
			t.display(glDrawable);
		}
		for(Particle p : particles) {
			p.display(glDrawable);
		}
	}

	@Override
	public void displayChanged(GLAutoDrawable glDrawable, boolean arg1, boolean arg2) {
		// NOT IMPLEMENTED BY JOGL.
	}

	@Override
	public void init(GLAutoDrawable glDrawable) {
		final GL gl = glDrawable.getGL();
		glDrawable.addKeyListener(this);
		glDrawable.addMouseListener(this);
		glDrawable.addMouseMotionListener(this);
		final GLU glu = new GLU();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(-1.0f, 1.0f, -1.0f, 1.0f); // drawing square
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();

		TimeBase.init();
		lastTimestamp = TimeBase.getTime();
	}

	public static void exit(){
		animator.stop();
		// frame.dispose();
		System.exit(0);
	}


	public void keyPressed(KeyEvent e) {
		Util.log(e.paramString());
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			exit();
		}
	}

	public void keyReleased(KeyEvent e) {
		Util.log(e.paramString());
	}

	public void keyTyped(KeyEvent e) {
		Util.log(e.paramString());
	}

	public void mousePressed(MouseEvent e) {
		Util.log("Mouse pressed (# of clicks: "
				+ e.getClickCount() + ")");
	}

	public void mouseReleased(MouseEvent e) {
		Util.log("Mouse released (# of clicks: "
				+ e.getClickCount() + ")");
	}

	public void mouseEntered(MouseEvent e) {
		Util.log("Mouse entered");
	}

	public void mouseExited(MouseEvent e) {
		Util.log("Mouse exited");
	}

	public void mouseClicked(MouseEvent e) {
		Util.log("Mouse clicked (# of clicks: "
				+ e.getClickCount() + ")");
	}


	public void mouseMoved(MouseEvent e) {
		Util.log("Mouse moved");
	}

	public void mouseDragged(MouseEvent e) {
		Util.log("Mouse dragged");
	}


	@Override
	public void reshape(GLAutoDrawable glDrawable, int x, int y, int width,
			int height) {
		final GL gl = glDrawable.getGL();
		gl.glViewport(0, 0, width, height);
	}

	static Animator animator = new Animator();

	public static void main(String[] args) {
		final Frame frame = new Frame("Game");

		GLCapabilities capabilities = new GLCapabilities();
		capabilities.setDoubleBuffered(true);

		final GLCanvas canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(new Game());
		canvas.setAutoSwapBufferMode(true);
		animator.add(canvas);

		frame.add(canvas);		

		frame.setSize(800, 600);
		frame.setBackground(Color.WHITE);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		frame.show();

		animator.start();
	}
}
