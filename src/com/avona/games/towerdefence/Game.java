package com.avona.games.towerdefence;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.awt.MainLoop;

public class Game implements KeyListener, MouseListener, MouseMotionListener {
	public List<Enemy> enemies = new LinkedList<Enemy>();
	public List<Tower> towers = new LinkedList<Tower>();
	public List<Particle> particles = new LinkedList<Particle>();
	protected World world;
	public Mouse mouse;
	public MainLoop main;

	protected GraphicsEngine ge;

	public Game(MainLoop main) {
		this.main = main;
		world = new World();
		enemies.add(new Enemy(world));
		towers.add(new Tower(new Point2d(0.5, 0.5)));
		towers.add(new Tower(new Point2d(0.3, 0.5)));

		mouse = new Mouse();
	}
	
	public void init(GraphicsEngine ge) {
		this.ge = ge;
		ge.canvas.addKeyListener(this);
		ge.canvas.addMouseListener(this);
		ge.canvas.addMouseMotionListener(this);
	}

	public void updateWorld(final double dt) {
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

	public void keyPressed(KeyEvent e) {
		Util.log(e.paramString());
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			main.exit();
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
			double xf = e.getX();
			double yf = -e.getY();

			double x = (xf/ ge.size.x) * 2 - 1.0f;
			double y = (yf / ge.size.y) * 2 + 1.0f;
			
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

		double xf = e.getX();
		double yf = -e.getY();

		double x = (xf/ ge.size.x) * 2 - 1.0f;
		double y = (yf / ge.size.y) * 2 + 1.0f;
		mouse.location.x = x;
		mouse.location.y =y;
	}

	public void mouseDragged(MouseEvent e) {
		Util.log("Mouse dragged");
	}
}
