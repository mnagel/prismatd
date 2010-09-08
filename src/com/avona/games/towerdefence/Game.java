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
import java.util.Iterator;
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
	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();
	protected World world;
	public Mouse mouse;
	
	protected GraphicsEngine ge;

	protected long lastTimestamp;

	public Game() {
		world = new World();
		enemies.add(new Enemy(world));
		towers.add(new Tower(new Point2d(0.5, 0.5)));
		towers.add(new Tower(new Point2d(0.3, 0.5)));
		
		mouse = new Mouse();
		ge = new GraphicsEngine(this);
	}

	public void display(GLAutoDrawable glDrawable) {
		final long newTime = TimeBase.getTime();
		final long delta = newTime - lastTimestamp;
		lastTimestamp = newTime;
		final double dt = TimeBase.fractionOfSecond(delta);

		// Update the world.
		updateWorld(dt);

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

		ge.render(glDrawable);
	}

	protected void updateWorld(final double dt) {
		/**
		 * Step all objects first.  This will cause them to move.
		 */
		for(Enemy e : enemies) {
			e.step(dt);
		}
		for(Tower t : towers) {
			t.step(dt);
		}
		for(Particle p : particles) {
			p.step(dt);
		}

		/**
		 * Look for each tower, if it's ready to shoot and if an enemy
		 * ready to shoot.  If so, create a new particle.  The tower is
		 * free to create any particle in its shootTowards() method.
		 */
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

		/**
		 * Check for any particle collisions and handle damage.
		 */
		Iterator<Particle> piter = particles.iterator();
		while(piter.hasNext()) {
			final Particle p = piter.next();
			Iterator<Enemy> eiter = enemies.iterator();
			while(eiter.hasNext()) {
				final Enemy e = eiter.next();
				if(p.inRange(e)) {
					p.attack(e);
					if(e.isDead()) {
						eiter.remove();
					}
				}
			}
			if(p.isDead()) {
				piter.remove();
			}
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
		if (e.getButton() == MouseEvent.BUTTON1) {
			double x = e.getX() / 800.0f - 1.0f;
			double y = -(e.getY() / 600.0f) +1.0f;
			Util.log("spawning at " + x + "  " + y);
			towers.add(new Tower(new Point2d(x, y)));	
		}
		else {
			enemies.add(new Enemy(world));
		}	
	}


	public void mouseMoved(MouseEvent e) {
		Util.log("Mouse moved");
		if (mouse == null) return; // fixme, nicht immer testen
		Util.log("Mouse moved" + mouse.toString());
		
		double x = e.getX() / 800.0f - 1.0f;
		double y = -(e.getY() / 600.0f) +1.0f;
		
		mouse.location.x = x;
		mouse.location.y =y;
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
