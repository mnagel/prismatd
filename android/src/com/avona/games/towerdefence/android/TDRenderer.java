package com.avona.games.towerdefence.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Point2d;

import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.TimeTrack;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class TDRenderer implements Renderer {
	private Point2d size;
	
	private TimeTrack gameTime = new TimeTrack();
	private TimeTrack graphicsTime = new TimeTrack();
	private GraphicsEngine ge;
	
	private static final double FIXED_TICK = 0.04;
	private double gameTicks = 0;

	public Game game;
	
	public TDRenderer() {
		game = new Game();
		ge = new GraphicsEngine(game);
	}
	
	public static double getWallClock() {
		return System.nanoTime() * Math.pow(10, -9);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
        //gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        //gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
 
		final float[] vertices = {
				-0.7f, -0.7f, 0.0f,
				-0.7f, 0.7f, 0.0f,
				0.7f, -0.7f, 0.0f,
				0.7f, 0.7f, 0.0f
		};
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		FloatBuffer floatBuf = byteBuf.asFloatBuffer();
		floatBuf.put(vertices);
		floatBuf.position(0);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, floatBuf);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		
		final double wallClock = getWallClock();
		graphicsTime.update(wallClock);
		gameTime.update(wallClock);

		// Updating of inputs is done asynchronously.

		// Update the world with a fixed rate.
		gameTicks += gameTime.tick;
		while (gameTicks >= FIXED_TICK) {
			game.updateWorld(FIXED_TICK);
			gameTicks -= FIXED_TICK;
		}

		// Show the world.
		ge.render(gl, gameTime.tick, graphicsTime.tick);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		
		size = new Point2d(width, height);
		
		// ... and initialise.
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		//gl.glOrtho(0, width, 0, height, -1, 1); // drawing square
		GLU.gluOrtho2D(gl, 0, width, 0, height); // drawing square
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		ge.onSurfaceChanged(gl, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		System.out.println("surfaceCreated");

		ge.onSurfaceCreated(gl, config);
	}
}
