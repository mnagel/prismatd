package com.avona.games.towerdefence.android;

import android.content.res.Resources;
import com.avona.games.towerdefence.util.Util;
import com.avona.games.towerdefence.res.ResourceResolver;

import java.io.InputStream;

public class AndroidResourceResolver implements ResourceResolver {
	public static String PACKAGE_NAME = "com.avona.games.towerdefence.android";
	private Resources res;

	public AndroidResourceResolver(Resources res) {
		this.res = res;
	}

	@Override
	public InputStream getRawResource(String name) {
		try {
			if (name.endsWith(".vert") || name.endsWith(".frag")) {
				name = "raw/" + name;
				name = name.replace(".", "_");
			}
			final int id = res.getIdentifier(PACKAGE_NAME + ":" + name, null, null);
			return res.openRawResource(id);
		} catch (Exception e) {
			// FIXME should display error here
			// but cannot because not in a GUI thread...

			String msg = "truckload of badness when loading ressource: " + name; // + "\n" + Util.exception2String(e);

			Util.log(msg);
			android.util.Log.e("prismatd", msg, e);

			// System.exit(1);
			return null;
		}
	}
}
