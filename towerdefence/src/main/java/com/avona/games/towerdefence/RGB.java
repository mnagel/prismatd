package com.avona.games.towerdefence;

import java.io.Serializable;

public final class RGB implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final RGB WHITE = new RGB(1.0f, 1.0f, 1.0f);

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

	@Override
	public RGB clone() {
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

	public RGB scaled(float factor) {
		return new RGB(R * factor, G * factor, B * factor);
	}

	public RGB normalized() {
		float f = 1.0f / length();
		return new RGB(R * f, G * f, B * f);
	}

	public float length() {
		return R + G + B;
	}

	public String toString() {
		return String.format("(%.0f,%.0f,%.0f)", R, G, B);
	}
}
