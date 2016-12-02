package com.avona.games.towerdefence.res;

public class ResourceResolverRegistry {
	private static ResourceResolver instance;

	public static ResourceResolver getInstance() {
		assert (instance != null);
		return instance;
	}

	public static void setInstance(ResourceResolver inst) {
		instance = inst;
	}
}
