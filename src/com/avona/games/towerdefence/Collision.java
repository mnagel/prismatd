package com.avona.games.towerdefence;

public class Collision {
	public static boolean circleCollidesWithCircle(final V2 location,
			float radius, final V2 otherLocation, final double otherRadius) {
		final double squaredDist = location.dist_sq(otherLocation);
		final double totalRadius = radius + otherRadius;
		final double squaredTotalRadius = totalRadius * totalRadius;
		return (squaredDist < squaredTotalRadius);
	}
}
