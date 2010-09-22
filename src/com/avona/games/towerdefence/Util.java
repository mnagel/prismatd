package com.avona.games.towerdefence;

public final class Util {
	public static void dumpPoint(String description, V2 p) {
		log(String.format("%s: (%f, %f)", description, p.x, p.y));
	}

	public static void log(String s) {
		System.out.println(s);
	}

	/**
	 * Find the smallest power of two >= the input value. (Doesn't work for
	 * negative numbers.)
	 */
	public static int roundUpPower2(int x) {
		x = x - 1;
		x = x | (x >> 1);
		x = x | (x >> 2);
		x = x | (x >> 4);
		x = x | (x >> 8);
		x = x | (x >> 16);
		return x + 1;
	}
}
