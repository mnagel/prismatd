package com.avona.games.towerdefence;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TimedCodeManager implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<TimedCode> timedCode = new LinkedList<TimedCode>();
	private double clock = 0.0;

	/**
	 * Flag to detect list changes while processing the list.
	 */
	private boolean modified;

	public void update(final float dt) {
		clock += dt;

		// Find code that timed out and execute it.
		parse_again: do {
			modified = false;
			Iterator<TimedCode> titer = timedCode.iterator();
			while (titer.hasNext()) {
				final TimedCode tc = titer.next();
				if (tc.startTime < clock) {
					titer.remove();
					tc.execute();
					if (modified) {
						// The list was modified, so we need new iterators.
						continue parse_again;
					}
				} else {
					// The list is sorted, so we can exit as soon as we hit a
					// future timeout.
					break;
				}
			}
		} while (false);
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
		modified = true;
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
