package com.avona.games.towerdefence;

import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.Collision;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollisionTest {

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
				.movingCircleCollidesWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));

		radius0 = 5.0f;
		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidesWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));
	}

	@Test
	public void testMovingMovableCircleCollidesWithCircle() {
		V2 pos0 = new V2(0.0f, 0.0f);
		V2 velocity0 = new V2(0, 1.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;

		assertFalse("non-intersecting circles intersect", Collision
				.movingCircleCollidesWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));

		radius0 = 4.0f;
		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidesWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));
	}

	@Test
	public void testMovingCircleMovesFullyOverOtherCircle() {
		V2 pos0 = new V2(0.0f, 0.0f);
		V2 velocity0 = new V2(0, 10.0f);
		float radius0 = 1.0f;
		V2 pos1 = new V2(0.0f, 5.0f);
		V2 velocity1 = new V2();
		float radius1 = 1.0f;

		assertTrue("intersecting circles do not intersect", Collision
				.movingCircleCollidesWithCircle(pos0, velocity0, radius0, pos1,
						velocity1, radius1, 1.0f));
	}
}
