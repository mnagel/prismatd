package com.avona.games.towerdefence;

public class Collision {
	public static boolean circleCollidesWithCircle(final V2 location0,
			final float radius0, final V2 location1, final float radius1) {
		final float squaredDist = location0.squaredDist(location1);
		final float totalRadius = radius0 + radius1;
		final float squaredTotalRadius = totalRadius * totalRadius;
		return (squaredDist < squaredTotalRadius);
	}

	public static boolean movingCircleCollidesWithCircle(final V2 location0,
			final V2 velocity0, final float radius0, final V2 location1,
			final V2 velocity1, final float radius1, final float dt) {
		final float totalRadius = radius0 + radius1;
		final float squaredTotalRadius = totalRadius * totalRadius;
		Util.log("squaredTotalRadius: " + squaredTotalRadius);

		// Combine and scale the velocities, so that we only have to look at a
		// single velocity.
		V2 resultingVelocity = new V2(velocity0);
		resultingVelocity.sub(velocity1);
		resultingVelocity.mult(dt);
		Util.log("resultingVelocity: " + resultingVelocity);

		V2 fromTo = new V2(location1);
		fromTo.sub(location0);
		Util.log("fromTo: " + fromTo);

		if (resultingVelocity.squaredLength() > 0.0f) {
			// Calculate shortest distance vector of the two circles.
			V2 nearestVector = V2.project(fromTo, resultingVelocity);

			V2 distFlyBy = new V2(fromTo);
			if (nearestVector.squaredLength() > resultingVelocity
					.squaredLength()) {
				distFlyBy.sub(resultingVelocity);
			} else {
				distFlyBy.sub(nearestVector);
			}
			Util.log("distFlyBy: " + distFlyBy);

			final float squaredDist = distFlyBy.squaredLength();
			Util.log("squaredDist: " + squaredDist);
			return (squaredDist < squaredTotalRadius);
		} else {
			// The circles are not moving, check whether intersected from the
			// beginning.
			final float squaredDist = fromTo.squaredLength();
			Util.log("unmoving squaredDist: " + squaredDist);
			return (squaredDist < squaredTotalRadius);
		}
	}
}
