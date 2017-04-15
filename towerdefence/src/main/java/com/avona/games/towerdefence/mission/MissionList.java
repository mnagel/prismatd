package com.avona.games.towerdefence.mission;

@SuppressWarnings("unchecked")
public final class MissionList {
	public static final Class<Mission>[] availableMissions = new Class[]{
			_010_Hello_World.class,
			_020_About_Colors.class,
			_030_Mixing_Colors.class,
			_100_Grass.class,
			_101_Space.class
	};

	public static String[] getAvailableMissionNames() {
		Class<Mission>[] missions = availableMissions;
		String[] res = new String[missions.length];
		for (int i = 0; i < missions.length; i++) {
			res[i] = missions[i].getAnnotation(MissionName.class).value();
		}
		return res;
	}
}
