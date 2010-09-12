package com.avona.games.towerdefence;

public class Collision {
	public static boolean circleCollidesWithCircle(final V2 location0,
			final float radius0, final V2 location1, final float radius1) {
		final float squaredDist = location0.squaredDist(location1);
		final float totalRadius = radius0 + radius1;
		final float squaredTotalRadius = totalRadius * totalRadius;
		return (squaredDist < squaredTotalRadius);
	}
}
