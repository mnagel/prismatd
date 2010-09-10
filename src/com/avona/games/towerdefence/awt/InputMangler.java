package com.avona.games.towerdefence.awt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.vecmath.Point2d;

import com.avona.games.towerdefence.InputActor;
import com.avona.games.towerdefence.PortableGraphicsEngine;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.Util;

public class InputMangler implements KeyListener, MouseListener,
		MouseMotionListener {
	private PortableGraphicsEngine ge;
	private PortableMainLoop ml;
	private InputActor actor;

	public InputMangler(GraphicsEngine ge, PortableMainLoop mainLoop,
			InputActor actor) {
		this.ge = ge;
		this.ml = mainLoop;
		this.actor = actor;

		ge.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ml.exit();
			}
		});

		ge.canvas.addKeyListener(this);
		ge.canvas.addMouseListener(this);
		ge.canvas.addMouseMotionListener(this);
	}

	protected Point2d eventLocation(MouseEvent e) {
		Point2d location = new Point2d();
		final double xf = e.getX();
		final double yf = e.getY();
		final Point2d canvasSize = ge.size;

		location.x = xf;
		location.y = canvasSize.y - yf;
		return location;
	}

	public void keyPressed(KeyEvent e) {
		Util.log(e.paramString());
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			actor.pressedEscapeKey();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			actor.pressedSpaceKey();
		}
	}

	public void keyReleased(KeyEvent e) {
		Util.log(e.paramString());
	}

	public void mousePressed(MouseEvent e) {
		Util.log("Mouse pressed (# of clicks: " + e.getClickCount() + ")");
		final Point2d location = eventLocation(e);
		if (e.getButton() == MouseEvent.BUTTON1) {
			actor.pressedMouseBtn1At(location);
		} else {
			actor.pressedMouseBtn2At(location);
		}
	}

	public void mouseReleased(MouseEvent e) {
		Util.log("Mouse released (# of clicks: " + e.getClickCount() + ")");
	}

	public void mouseEntered(MouseEvent e) {
		Util.log("Mouse entered");
		actor.mouseEntered();
	}

	public void mouseExited(MouseEvent e) {
		Util.log("Mouse exited");
		actor.mouseExited();
	}

	public void mouseClicked(MouseEvent e) {
		Util.log("Mouse clicked (# of clicks: " + e.getClickCount() + ")");
	}

	public void mouseMoved(MouseEvent e) {
		actor.mouseMovedTo(eventLocation(e));
	}

	public void mouseDragged(MouseEvent e) {
		actor.mouseDraggedTo(eventLocation(e));
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
