package com.avona.games.towerdefence.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.avona.games.towerdefence.Collision;
import com.avona.games.towerdefence.V2;

public class CollisionUnitTest {

	@Test
	public void testCircleCollidesWithCircle() {
		V2 pos0 = new V2(0.0f, 0.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		float radius1 = 1.0f;

		assertFalse("non-intersecting circles intersect", Collision
				.circleCollidesWithCircle(pos0, radius0, pos1, radius1));

		radius0 = 5.0f;
		assertTrue("intersecting circles do not intersect", Collision
				.circleCollidesWithCircle(pos0, radius0, pos1, radius1));
	}

	@Test
	public void testNonMovingMovableCircleCollidesWithCircle() {
		V2 pos0 = new V2(0.0f, 0.0f);
		V2 velocity0 = new V2();
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;

		assertFalse("non-intersecting circles intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));

		radius0 = 5.0f;
		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));
	}

	@Test
	public void testMovingMovableCircleCollidedWithCircle() {
		V2 pos0 = new V2(0.0f, 10.0f);
		V2 velocity0 = new V2(0, 1.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;
		final float dt = 1.0f;

		assertFalse("non-intersecting circles intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, dt));

		radius0 = 3.0f;
		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, dt));
	}

	@Test
	public void testMovingCircleMovedFullyOverOtherCircle() {
		V2 pos0 = new V2(0.0f, 10.0f);
		V2 velocity0 = new V2(0, 10.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;
		final float dt = 1.0f;

		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, dt));
	}

	@Test
	public void testMovingCircleHasntReachedOtherCircle() {
		V2 pos0 = new V2(0.0f, 0.0f);
		V2 velocity0 = new V2(0, 10.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;
		final float dt = 1.0f;

		assertFalse(Collision.movingCircleCollidedWithCircle(pos0, velocity0,
				radius0, pos1, velocity1, radius1, dt));
	}

	@Test
	public void testMovingCircleMovingAwayFromOtherCircle() {
		V2 pos0 = new V2(0.0f, 20.0f);
		V2 velocity0 = new V2(0, 10.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;
		final float dt = 1.0f;
		
		assertFalse(Collision.movingCircleCollidedWithCircle(pos0, velocity0,
				radius0, pos1, velocity1, radius1, dt));
	}

	@Test
	public void testCircleTouchedPassingCircle() {
		V2 pos0 = new V2(1.0f, 5.0f);
		V2 velocity0 = new V2(0, 5.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 2.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;
		final float dt = 1.0f;

		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, dt));
	}

	@Test
	public void testCircleMissedPassingCircle() {
		V2 pos0 = new V2(1.0f, 5.0f);
		V2 velocity0 = new V2(-2.0f, 5.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 2.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;
		final float dt = 1.0f;

		assertFalse("non-intersecting circles intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, dt));

		velocity0 = new V2(-1.0f, 5.0f);
		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidedWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, dt));
	}
}
