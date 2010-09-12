package com.avona.games.towerdefence;

public final class Util {
	public static void dumpPoint(String description, V2 p) {
		log(String.format("%s: (%f, %f)", description, p.x, p.y));
	}

	public static void log(String s) {
		System.out.println(s);
	}
}
