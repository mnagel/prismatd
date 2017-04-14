package com.avona.games.towerdefence;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.avona.games.towerdefence.mission.Mission;

public final class Util {
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

	public static String exception2String(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	// FIXME get some support for functional mapping from somewhere
	public static String[] mapMissionNames(Class<Mission>[] missions) {
		String[] res = new String[missions.length];
		for (int i = 0; i < missions.length; i++) {
			res[i] = missions[i].getSimpleName();
		}
		return res;
	}
}
