package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.mission.data.*;
import com.avona.games.towerdefence.score.Highscores;

import java.io.File;
import java.util.Locale;

@SuppressWarnings("unchecked")
public final class MissionList {
	public static final Class<Mission>[] availableMissions = new Class[]{
			_010_Hello_World.class,
			_020_About_Colors.class,
			_025_Tower_Levels.class,
			_030_Mixing_Colors.class,
			_031_More_Colors.class,
			_040_About_Money.class,
			_050_SpecialTower_PaintRed.class,
			_100_Mission.class,
			_911_Red_Mission.class,
			_912_Green_Mission.class,
			_913_Blue_Mission.class,
			_900_Benchmark.class,
			_901_Mini_Mission.class,
			_901_Mini_Mission.class,
	};

	public static String[] getAvailableMissionNames() {
		Class<Mission>[] missions = availableMissions;
		String[] missionNames = new String[missions.length];
		for (int i = 0; i < missions.length; i++) {
			String score = new Highscores(new File(Highscores.BACKING_FILE_NAME)).getHighscore(missions[i].getSimpleName()).getLevelListString();
			if (score.length() > 0) {
				score = " --- " + score;
			}
			missionNames[i] = String.format(
					Locale.US,
					"%d: %s %s",
					i + 1,
					missions[i].getAnnotation(MissionName.class).value(),
					score
			);
		}
		return missionNames;
	}
}
