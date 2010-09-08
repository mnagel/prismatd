package com.avona.games.towerdefence.awt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.GraphicsEngine;
import com.avona.games.towerdefence.Tower;
import com.avona.games.towerdefence.Util;

public class InputMangler implements KeyListener, MouseListener, MouseMotionListener {
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

			double x = (xf / ge.size.x) * 2 - 1.0f;
			double y = (yf / ge.size.y) * 2 + 1.0f;

			game.towers.add(new Tower(new Point2d(x, y)));	
		} else {
			game.enemies.add(new Enemy(game.world));
		}	
	}

	public void mouseMoved(MouseEvent e) {
		Util.log("Mouse moved" + game.mouse.toString());

		double xf = e.getX();
		double yf = -e.getY();

		double x = (xf/ ge.size.x) * 2 - 1.0f;
		double y = (yf / ge.size.y) * 2 + 1.0f;

		game.mouse.location.x = x;
		game.mouse.location.y =y;
	}

	public void mouseDragged(MouseEvent e) {
		Util.log("Mouse dragged");
	}
}
