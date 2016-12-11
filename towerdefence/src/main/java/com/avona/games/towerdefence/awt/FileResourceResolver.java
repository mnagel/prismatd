package com.avona.games.towerdefence.awt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.avona.games.towerdefence.res.ResourceResolver;

public class FileResourceResolver implements ResourceResolver {
	private String baseDir;

	public FileResourceResolver(final String baseDir) {
		this.baseDir = baseDir;
	}

	@Override
	public InputStream getRawResource(String name) {
		File f = new File(baseDir + "/" + name);
		if (!f.canRead()) {
			throw new RuntimeException("Could not access resource \"" + name
					+ "\" as \"" + f + "\"");
		}

		try {
			return new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Could not access resource \"" + name
					+ "\" as \"" + f + "\", exception: " + e);
		}
	}

}
