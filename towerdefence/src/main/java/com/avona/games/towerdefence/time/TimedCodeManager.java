package com.avona.games.towerdefence.time;

import java.io.Serializable;
import java.util.Comparator;
import java.util.PriorityQueue;

public class TimedCodeManager implements Serializable {
	private PriorityQueue<TimedCode> timedCodes = new PriorityQueue<>(100, new Comparator<TimedCode>() {
		@Override
		public int compare(TimedCode t1, TimedCode t2) {
			return Double.compare(t1.startTime, t2.startTime);
		}
	});

	private double clock = 0.0;

	public void update(final float dt) {
		clock += dt;

		TimedCode head = timedCodes.peek();
		while (head != null && head.startTime < clock) {
			TimedCode dueCode = timedCodes.poll();
			dueCode.run();
			head = timedCodes.peek();
		}
	}

	public void addCode(TimedCode newCode) {
		newCode.activate(clock);
		timedCodes.add(newCode);
	}

	public void clear() {
		timedCodes.clear();
	}
}
