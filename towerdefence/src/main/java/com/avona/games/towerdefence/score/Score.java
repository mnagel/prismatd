package com.avona.games.towerdefence.score;

import java.util.Locale;

public class Score implements Comparable<Score> {
	private boolean missionCompleted;
	private int maxWave;
	private int lives;
	private int money;

	@SuppressWarnings({"FieldCanBeLocal", "unused"}) // serialization and stuff
	private String timestamp;
	@SuppressWarnings({"FieldCanBeLocal", "unused"}) // serialization and stuff
	private String playerName;

	public Score(boolean missionCompleted, int maxWave, int lives, int money, String timestamp, String playerName) {
		this.missionCompleted = missionCompleted;
		this.maxWave = maxWave;
		this.lives = lives;
		this.money = money;
		this.timestamp = timestamp;
		this.playerName = playerName;
	}

	static Score getEmpty() {
		return new Score(false, 0, 0, 0, "", "");
	}

	@Override
	public int compareTo(Score old) {
		int result = Boolean.valueOf(this.missionCompleted).compareTo(old.missionCompleted);
		if (result == 0) {
			result = Integer.valueOf(this.maxWave).compareTo(old.maxWave);
		}
		if (result == 0) {
			result = Integer.valueOf(this.lives).compareTo(old.lives);
		}
		if (result == 0) {
			result = Integer.valueOf(this.money).compareTo(old.money);
		}
		return result;
	}

	public String getLevelListString() {
		if (missionCompleted) {
			return String.format(Locale.US, "DONE! %d lives, $%d", lives, money);
		} else {
			if (maxWave == 0) {
				return "";
			}
			return String.format(Locale.US, "FAILED at wave %d", maxWave);
		}
	}
}
