package com.avona.games.towerdefence.awt;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.avona.games.towerdefence.PortableGraphicsEngine;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.V2;
import com.avona.games.towerdefence.inputActors.InputActor;

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

	protected V2 eventLocation(MouseEvent e) {
		V2 location = new V2();
		final float xf = e.getX();
		final float yf = e.getY();
		final V2 canvasSize = ge.size;

		location.x = xf;
		location.y = canvasSize.y - yf;
		return location;
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			actor.pressedEscapeKey();
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			actor.pressedSpaceKey();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		final V2 location = eventLocation(e);
		if (e.getButton() == MouseEvent.BUTTON1) {
			actor.mouseBtn1DownAt(location);
		} else {
			actor.mouseBtn2DownAt(location);
		}
	}

	public void mouseReleased(MouseEvent e) {
		final V2 location = eventLocation(e);
		if (e.getButton() == MouseEvent.BUTTON1) {
			actor.mouseBtn1UpAt(location);
		} else {
			actor.mouseBtn2UpAt(location);
		}
	}

	public void mouseEntered(MouseEvent e) {
		actor.mouseEntered();
	}

	public void mouseExited(MouseEvent e) {
		actor.mouseExited();
	}

	public void mouseClicked(MouseEvent e) {
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
