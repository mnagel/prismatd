package com.avona.games.towerdefence.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Point2d;
import javax.vecmath.Point2f;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

import com.avona.games.towerdefence.Enemy;
import com.avona.games.towerdefence.Game;
import com.avona.games.towerdefence.Particle;
import com.avona.games.towerdefence.TimeTrack;
import com.avona.games.towerdefence.Tower;

/**
 * The GraphicsEngine object currently incorporates all drawing operations. It
 * will iterate over all in-game objects and call (possibly overloaded) class
 * methods to perform the GL calls. It will not touch any in-game state, though.
 */
public class GraphicsEngine implements Renderer {
	protected Game game;
	public GL10 gl;
	public GLU glu;
	public Point2d size;
	public TimeTrack graphicsTime = new TimeTrack();

	final public int defaultHeight = 800;
	final public int defaultWidth = 800;

	public GraphicsEngine(Game game) {
		this.game = game;
	}

	public void render(GL10 gl, double gameDelta, double graphicsDelta) {
		this.gl = gl;
		graphicsTime.updateTick(graphicsDelta);

		// Paint background, clearing previous drawings.
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		
		for (Enemy e : game.enemies) {
			renderEnemy(e);
		}
		for (Tower t : game.towers) {
			renderTower(t);
		}
		for (Particle p : game.particles) {
			renderParticle(p);
		}

		/*renderer.beginRendering(800, 600);
		// optionally set the color
		renderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
		renderer.draw("Text to draw", 100 + (int) (100 * Math
				.sin(graphicsTime.clock)), 100 + (int) (100 * Math
				.cos(graphicsTime.clock)));
		// ... more draw commands, color changes, etc.
		renderer.endRendering();*/

		renderMouse();
	}

    private FloatBuffer enemyBuffer;
    
	private void initEnemyBuffer() {
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(8 * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		enemyBuffer = byteBuf.asFloatBuffer();
	}
	
	public void renderEnemy(final Enemy e) {
		if (e.isDead())
			return;

		final double width = 0.04;
		final Point2d location = e.location;

		final float[] vertices = {
				(float) (location.x - width / 2), (float) (location.y - width / 2),
				(float) (location.x + width / 2), (float) (location.y - width / 2),
				(float) (location.x + width / 2), (float) (location.y + width / 2),
				(float) (location.x - width / 2), (float) (location.y + width / 2)
		};
		enemyBuffer.position(0);
		enemyBuffer.put(vertices);
		enemyBuffer.position(0);
		
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, enemyBuffer);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(0.0f, 0.0f, 1.0f, 0.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void renderTower(final Tower t) {
		/*final double width = 0.03;
		final Point2d location = t.location;

		if (t.showRange) {
			gl.glColor3d(1.0, 1.0, 1.0);
			drawCircle(t.location.x, t.location.y, t.range);
		}

		gl.glBegin(GL.GL_QUADS);
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glVertex2d(location.x - width / 2, location.y - width / 2);
		gl.glColor3d(0.6, 0.0, 0.0);
		gl.glVertex2d(location.x + width / 2, location.y - width / 2);
		gl.glColor3d(0.9, 0.0, 0.0);
		gl.glVertex2d(location.x + width / 2, location.y + width / 2);
		gl.glColor3d(0.8, 0.0, 0.0);
		gl.glVertex2d(location.x - width / 2, location.y + width / 2);
		gl.glEnd();*/
		final double width = 0.04;
		final Point2d location = t.location;

		final float[] vertices = {
				(float) (location.x - width / 2), (float) (location.y - width / 2),
				(float) (location.x + width / 2), (float) (location.y - width / 2),
				(float) (location.x + width / 2), (float) (location.y + width / 2),
				(float) (location.x - width / 2), (float) (location.y + width / 2)
		};
		enemyBuffer.position(0);
		enemyBuffer.put(vertices);
		enemyBuffer.position(0);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void renderParticle(final Particle p) {
		/*if (p.isDead())
			return;

		final double width = 0.02;
		final Point2d location = p.location;

		gl.glBegin(GL.GL_QUADS);
		gl.glColor3d(1.0, 0.8, 0.2);
		gl.glVertex2d(location.x - width / 2, location.y - width / 2);
		gl.glColor3d(0.6, 0.9, 0.2);
		gl.glVertex2d(location.x + width / 2, location.y - width / 2);
		gl.glColor3d(0.9, 0.6, 0.2);
		gl.glVertex2d(location.x + width / 2, location.y + width / 2);
		gl.glColor3d(0.8, 1.0, 0.2);
		gl.glVertex2d(location.x - width / 2, location.y + width / 2);
		gl.glEnd();*/
		
		final double width = 0.04;
		final Point2d location = p.location;

		final float[] vertices = {
				(float) (location.x - width / 2), (float) (location.y - width / 2),
				(float) (location.x + width / 2), (float) (location.y - width / 2),
				(float) (location.x + width / 2), (float) (location.y + width / 2),
				(float) (location.x - width / 2), (float) (location.y + width / 2)
		};	
		enemyBuffer.position(0);
		enemyBuffer.put(vertices);
		enemyBuffer.position(0);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void drawCircle(final double x, final double y, final double radius) {
		drawCircle(x, y, radius, 100, GL10.GL_LINE_LOOP);
	}

	public void drawFilledCircle(final double x, final double y,
			final double radius) {
		//drawCircle(x, y, radius, 100, GL10.GL_POLYGON);
	}

	public void drawCircle(final double x, final double y, final double radius,
			final int segments, final int mode) {
/*		final double angleStep = 2 * Math.PI / segments;
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glLineWidth(1.0f);

		gl.glBegin(mode);
		for (int i = 0; i < segments; ++i) {
			final double angle = i * angleStep;
			gl.glVertex2d(x + (Math.cos(angle) * radius), y
					+ (Math.sin(angle) * radius));
		}
		gl.glEnd();
		gl.glPopMatrix();*/
	}

	public void renderMouse() {
		final Point2d p = game.mouse.location;
		final float col = (float) (0.4 + 0.6 * Math.abs(Math
				.sin(2 * graphicsTime.clock)));
		gl.glColor4f(col, col, col, 0.0f);
		drawFilledCircle(p.x, p.y, game.mouse.radius);
	}

	public void init(GL10 gl) {
		// We have a fresh GL context, retrieve reference.
		this.gl = gl;

		// ... and initialise.
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluOrtho2D(gl, -1.0f, 1.0f, -1.0f, 1.0f); // drawing square
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		//renderer = new TextRenderer(new Font("Deja Vu Sans", Font.PLAIN, 36),
		//		true, true);
		
        initEnemyBuffer();
	}

	//TextRenderer renderer;

	public void reshape(GL10 gl, int x, int y, int width,
			int height) {
		// The canvas has been updated.
		gl.glViewport(0, 0, width, height);
		size = new Point2d(width, height);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		reshape(gl, 0, 0, width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		init(gl);
	}
}
