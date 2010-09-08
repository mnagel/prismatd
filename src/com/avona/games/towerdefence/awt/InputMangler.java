package com.avona.games.towerdefence.awt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Util;

public class InputMangler implements KeyListener, MouseListener,
		MouseMotionListener {
	private GraphicsEngine ge;
	private MainLoop ml;
	private Game game;

	public InputMangler(MainLoop ml, GraphicsEngine ge, Game game) {
		this.ml = ml;
		this.ge = ge;
		this.game = game;

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
	}

	public void mousePressed(MouseEvent e) {
		Util.log("Mouse pressed (# of clicks: " + e.getClickCount() + ")");
		Point2d location = new Point2d();
		eventLocation(e, location);
		if (e.getButton() == MouseEvent.BUTTON1) {
			game.addTowerAt(location);
		} else {
			game.addEnemyAt(location);
		}
	}

	public void mouseReleased(MouseEvent e) {
		Util.log("Mouse released (# of clicks: " + e.getClickCount() + ")");
	}

	public void mouseEntered(MouseEvent e) {
		Util.log("Mouse entered");
	}

	public void mouseExited(MouseEvent e) {
		Util.log("Mouse exited");
	}

	public void eventLocation(MouseEvent e, Point2d location) {
		final double xf = e.getX();
		final double yf = -e.getY();
		final Point2d canvasSize = ge.size;

		final double x = (xf / canvasSize.x) * 2 - 1.0f;
		final double y = (yf / canvasSize.y) * 2 + 1.0f;

		location.x = x;
		location.y = y;
	}

	public void mouseClicked(MouseEvent e) {
		Util.log("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
	}

	public void mouseMoved(MouseEvent e) {
		Util.log("Mouse moved" + game.mouse.toString());
		eventLocation(e, game.mouse.location);
	}

	public void mouseDragged(MouseEvent e) {
		Util.log("Mouse dragged");
		eventLocation(e, game.mouse.location);
	}
}
