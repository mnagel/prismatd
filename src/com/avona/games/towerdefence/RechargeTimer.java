package com.avona.games.towerdefence;

public class RechargeTimer extends TimedCode {
	private TimedCodeManager timedCodeManager;
	public float time;
	public boolean ready = true;

	public RechargeTimer(TimedCodeManager timedCodeManager, float time) {
		this.timedCodeManager = timedCodeManager;
		this.time = time;
	}

	public RechargeTimer(RechargeTimer t) {
		this.timedCodeManager = t.timedCodeManager;
		this.time = t.time;
	}

	public RechargeTimer copy() {
		return new RechargeTimer(this);
	}

	public void rearm() {
		ready = false;
		timedCodeManager.addCode(time, this);
	}

	@Override
	public void execute() {
		ready = true;
	}
}
