package com.avona.games.towerdefence.core;

import java.io.Serializable;
import java.util.Locale;

public final class RGB implements Serializable {
	public static final RGB WHITE = new RGB(1.0f, 1.0f, 1.0f);
	private static final long serialVersionUID = 1L;
	public float R;
	public float G;
	public float B;

	public RGB(float r, float g, float b) {
		R = r;
		G = g;
		B = b;
	}

	public RGB(RGB orig) {
		R = orig.R;
		G = orig.G;
		B = orig.B;
	}

	public RGB clone2() {
		return new RGB(this);
	}

	public RGB subUpto(RGB other, float cutoff) {
		R -= other.R;
		G -= other.G;
		B -= other.B;

		R = Math.max(R, cutoff);
		G = Math.max(G, cutoff);
		B = Math.max(B, cutoff);
		return this;
	}

	public void rotate() {
		float Rold = R;
		R = G;
		G = B;
		B = Rold;
	}

	public RGB scaled(float factor) {
		return new RGB(R * factor, G * factor, B * factor);
	}

	public RGB normalized() {
		return scaled(1.0f / length());
	}

	public float length() {
		return R + G + B;
	}

	public String toString() {
		return String.format(Locale.US, "(%.0f,%.0f,%.0f)", R, G, B);
	}
}
