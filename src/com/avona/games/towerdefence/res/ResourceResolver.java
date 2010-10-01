package com.avona.games.towerdefence.res;

import java.io.InputStream;

public interface ResourceResolver {
	public InputStream getRawResource(final String name);
}
