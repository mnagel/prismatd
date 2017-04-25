package com.avona.games.towerdefence;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioEngine extends Thread {

	private final int EXTERNAL_BUFFER_SIZE = 128 * 1024;
	private String filename;

	public AudioEngine(String wavfile) {
		filename = wavfile;
	}

	public static void main(String[] args) {
		new AudioEngine("/tmp/bla.wav").start();
		new AudioEngine("/tmp/bla2.wav").start();
	}

	public void run() {

		File soundFile = new File(filename);
		if (!soundFile.exists()) {
			System.err.println("Wave file not found: " + filename);
			return;
		}

		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
			return;
		}

		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}

	}
}