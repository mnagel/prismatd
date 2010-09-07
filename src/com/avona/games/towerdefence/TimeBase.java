package com.avona.games.towerdefence;

public final class TimeBase {
	/* Use nanosecond precision. */
	public static final double BASE = Math.pow(10, -9);
	
	public static long getTime() {
		return System.nanoTime();
	}
	
	public static double fractionOfSecond(long time) {
		return time * BASE;
	}
	
	private static long initialTime;
	
	public static void init() {
		initialTime = getTime();
	}
}
