package com.avona.games.towerdefence;

public final class Util {
	public static void dumpPoint(String description, V2 p) {
		System.out
				.println(String.format("%s: (%f, %f)", description, p.x, p.y));
	}

	public static void log(String s) {
		System.out.println(s);
	}

	/**
	 * Calculate if two points are in range of each other, using the Euclidean
	 * distance.
	 * 
	 * @param p1
	 *            First point
	 * @param p2
	 *            Second point
	 * @param range
	 *            Maximum distance between the two to consider them in range
	 * @return true if the two points are in the given range or not, false
	 *         otherwise
	 */
	// public static boolean pointsInRange(Point p1, Point p2, double range) {
	// return Math.sqrt(Math.pow(p1.x - p1.y, 2) + Math.pow(p2.x - p2.y, 2)) <
	// range;
	// }
}
