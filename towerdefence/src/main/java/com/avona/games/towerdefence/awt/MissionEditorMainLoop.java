package com.avona.games.towerdefence.awt;

import java.util.Arrays;

public class MissionEditorMainLoop {

	public static void main(String[] args) {
		String[] args2 = Arrays.copyOf(args, args.length + 1);
		args2[args.length] = "--missioneditor";
		MainLoop.main(args2);
	}
}
