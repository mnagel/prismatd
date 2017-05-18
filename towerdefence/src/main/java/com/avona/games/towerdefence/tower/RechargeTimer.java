package com.avona.games.towerdefence.tower;

import com.avona.games.towerdefence.time.TimedCode;
import com.avona.games.towerdefence.time.TimedCodeManager;

public class RechargeTimer extends TimedCode {
	private static final long serialVersionUID = 1L;
	public float time;
	boolean ready = true;
	private TimedCodeManager timedCodeManager;

	RechargeTimer(TimedCodeManager timedCodeManager, float time) {
		this.timedCodeManager = timedCodeManager;
		this.time = time;
	}

	private RechargeTimer(RechargeTimer t) {
		this.timedCodeManager = t.timedCodeManager;
		this.time = t.time;
	}

	public RechargeTimer clone2() {
		return new RechargeTimer(this);
	}

	void rearm() {
		ready = false;
		timedCodeManager.addCode(time, this);
	}

	@Override
	public void execute() {
		ready = true;
	}
}
