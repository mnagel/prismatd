package com.avona.games.towerdefence.awt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Tower;
import com.avona.games.towerdefence.Util;

public class InputMangler implements KeyListener, MouseListener,
		MouseMotionListener {
	private GraphicsEngine ge;
	private MainLoop ml;
	private Game game;

	public InputMangler(MainLoop mainLoop, GraphicsEngine ge, Game game) {
		this.ml = mainLoop;
		this.ge = ge;
		this.game = game;

		ge.frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				ml.exit();
			}
		});

		ge.canvas.addKeyListener(this);
		ge.canvas.addMouseListener(this);
		ge.canvas.addMouseMotionListener(this);
	}

	public void keyPressed(KeyEvent e) {
		Util.log(e.paramString());
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			ml.exit();
		}
	}

	public void keyReleased(KeyEvent e) {
		Util.log(e.paramString());
	}

	public void keyTyped(KeyEvent e) {
		Util.log(e.paramString());
		char key = e.getKeyChar();
		Util.log("Key character: \"" + key + "\"");
		if (key == ' ') {
			ml.toggleGamePause();
		}
	}

	public void mousePressed(MouseEvent e) {
		Util.log("Mouse pressed (# of clicks: " + e.getClickCount() + ")");
		Point2d location = new Point2d();
		eventLocation(e, location);
		if (e.getButton() == MouseEvent.BUTTON1) {
			game.addTowerAt(location);
			checkMouseOverTower(game.mouse.location);
		} else {
			game.addEnemyAt(location);
		}
	}

	public void mouseReleased(MouseEvent e) {
		Util.log("Mouse released (# of clicks: " + e.getClickCount() + ")");
	}

	public void mouseEntered(MouseEvent e) {
		Util.log("Mouse entered");
		game.mouse.onScreen = true;
	}

	public void mouseExited(MouseEvent e) {
		Util.log("Mouse exited");
		game.mouse.onScreen = false;
	}

	public void eventLocation(MouseEvent e, Point2d location) {
		final double xf = e.getX();
		final double yf = e.getY();
		final Point2d canvasSize = ge.size;

		location.x = xf;
		location.y = canvasSize.y - yf;
	}

	public void mouseClicked(MouseEvent e) {
		Util.log("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
	}

	public void checkMouseOverTower(Point2d mouseLocation) {
		Tower t = game.closestTowerWithinRadius(mouseLocation,
				GraphicsEngine.TOWER_WIDTH);
		game.showTowersRange(t);
	}

	public void mouseMoved(MouseEvent e) {
		eventLocation(e, game.mouse.location);
		checkMouseOverTower(game.mouse.location);
	}

	public void mouseDragged(MouseEvent e) {
		eventLocation(e, game.mouse.location);
	}
}
