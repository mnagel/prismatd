package com.avona.games.towerdefence.score;

import com.avona.games.towerdefence.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class Highscores {
	public static final String BACKING_FILE_NAME = "prismatd.scores.txt";
	// TODO make singleton
	private static HashMap<String, Score> missionClassName2Highscore = new HashMap<>();
	private final File backingFile;

	public Highscores(File backingFile) {
		this.backingFile = backingFile;
		loadFile();
	}

	private void loadFile() {
		Gson gson = new Gson();
		try (Reader reader = new InputStreamReader(new FileInputStream(backingFile), "UTF-8")) {
			Type myType = new TypeToken<HashMap<String, Score>>(){}.getType();
			missionClassName2Highscore = gson.fromJson(reader, myType);
			Util.log("Loaded Highscores.");
		} catch (IOException | RuntimeException e) {
			Util.log("Highscore Load failed: " + e);
		}
	}

	// TODO if load failed for some reason this destroys all data...
	private void saveFile() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer writer = new OutputStreamWriter(new FileOutputStream(backingFile), "UTF-8")) {
			gson.toJson(missionClassName2Highscore, writer);
			Util.log("Saved Highscores.");
		} catch (IOException | RuntimeException e) {
			Util.log("Highscore Save failed: " + e);
		}
	}

	public Score getHighscore(String missionClassName) {
		Score result = missionClassName2Highscore.get(missionClassName);
		if (result == null) {
			result = Score.getEmpty();
		}
		return result;
	}

	private boolean isHighscore(String missionClassName, Score score) {
		return score.compareTo(getHighscore(missionClassName)) > 0;
	}

	public void submit(String missionClassName, Score score) {
		if (isHighscore(missionClassName, score)) {
			missionClassName2Highscore.put(missionClassName, score);
			saveFile();
		}
	}
}
