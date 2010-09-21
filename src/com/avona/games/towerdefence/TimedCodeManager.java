package com.avona.games.towerdefence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TimedCodeManager {

	private List<TimedCode> timedCode = new LinkedList<TimedCode>();
	private double clock = 0.0;

	public void update(final float dt) {
		clock += dt;

		// find code that timed out and execute it
		Iterator<TimedCode> titer = timedCode.iterator();
		while (titer.hasNext()) {
			final TimedCode tc = titer.next();
			if (tc.startTime < clock) {
				tc.execute();
				titer.remove();
			} else {
				break; // list must be sorted
			}
		}
	}

	/**
	 * startTime should not be set beforehand. when calling multiple times, use
	 * in chronological order.
	 * 
	 * @param delay
	 *            delay in seconds from now
	 * @param newCode
	 *            code to execute then.
	 */
	public void addCode(final float delay, final TimedCode newCode) {
		newCode.startTime = clock + delay;

		ListIterator<TimedCode> titer = timedCode
				.listIterator(timedCode.size()); // traverse backwards
		while (titer.hasPrevious()) {
			final TimedCode reference = titer.previous();
			if (newCode.startTime > reference.startTime) {
				titer.next(); // forward again, went too far
				titer.add(newCode);
				return;
			}
		}
		titer.add(newCode); // add as first element
	}

	public String toString() {
		String s = "" + clock + ": ";
		ListIterator<TimedCode> titer = timedCode.listIterator(0);
		while (titer.hasNext()) {
			s += " " + String.format("%.1f", titer.next().startTime);
		}
		return s;
	}
}
