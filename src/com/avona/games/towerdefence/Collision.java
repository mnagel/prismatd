package com.avona.games.towerdefence;

public class Collision {
	public static boolean circleCollidesWithCircle(final V2 location0,
			final float radius0, final V2 location1, final float radius1) {
		final float squaredDist = location0.squaredDist(location1);
		final float totalRadius = radius0 + radius1;
		final float squaredTotalRadius = totalRadius * totalRadius;
		return (squaredDist < squaredTotalRadius);
	}

	public static boolean movingCircleCollidedWithCircle(V2 location0,
			V2 velocity0, final float radius0, V2 location1, V2 velocity1,
			final float radius1, final float dt) {
		final float totalRadius = radius0 + radius1;
		final float squaredTotalRadius = totalRadius * totalRadius;

		location0 = new V2(location0);
		velocity0 = new V2(velocity0);
		velocity0.mult(dt);
		location0.sub(velocity0);

		location1 = new V2(location1);
		velocity1 = new V2(velocity1);
		velocity1.mult(dt);
		location1.sub(velocity1);

		// Combine and scale the velocities, so that we only have to look at a
		// single velocity.
		V2 resultingVelocity = new V2(velocity0);
		resultingVelocity.sub(velocity1);
		resultingVelocity.mult(dt);

		// Calculate vector from first circle centre to second circle centre.
		V2 fromTo = new V2(location1);
		fromTo.sub(location0);
		final float fromToSquaredLength = fromTo.squaredLength();

		final float resultingVelocitySquaredLength = resultingVelocity
				.squaredLength();

		// Will hold the distance of the two circles while they were closest.
		float squaredDist;

		// Were any of the circles moving?
		if (resultingVelocitySquaredLength > 0.0f) {
			// Calculate shortest distance vector of the two circles.
			final float dotFromToVelocity = V2.dot(fromTo, resultingVelocity);
			final float angleFromToVelocity = dotFromToVelocity
					/ Math.abs(fromToSquaredLength
							* resultingVelocitySquaredLength);

			if (dotFromToVelocity > 0.0f) {
				V2 nearestVector = new V2(resultingVelocity);
				nearestVector.normalize();
				nearestVector.mult((float) (dotFromToVelocity / Math
						.sqrt(resultingVelocitySquaredLength)));

				V2 distFlyBy = new V2(fromTo);
				if (nearestVector.squaredLength() <= resultingVelocitySquaredLength) {
					// The nearest point was reached at nearestVector. Calculate
					// the distance of the circles at that point.
					distFlyBy.sub(nearestVector);
				} else {
					// The nearest point is beyond the point the circle reached
					// during its flight. Use the end point of its flight as the
					// nearest point.
					distFlyBy.sub(resultingVelocity);
				}

				squaredDist = distFlyBy.squaredLength();
			} else {
				// The circles were moving away from one-another, but maybe they
				// were intersecting at the beginning.
				squaredDist = fromToSquaredLength;
			}
		} else {
			// The circles weren't moving, so the closest they ever got is
			// their current distance.
			squaredDist = fromToSquaredLength;
		}
		return (squaredDist <= squaredTotalRadius);
	}
}
