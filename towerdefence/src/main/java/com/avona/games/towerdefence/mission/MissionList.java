package com.avona.games.towerdefence.mission;

import com.avona.games.towerdefence.mission.data.*;

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
		String[] res = new String[missions.length];
		for (int i = 0; i < missions.length; i++) {
			res[i] = Integer.toString(i + 1) + ": " + missions[i].getAnnotation(MissionName.class).value();
		}
		return res;
	}
}
