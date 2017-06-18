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

	public void add(RGB other) {
		R += other.R;
		G += other.G;
		B += other.B;
	}

	public RGB added(RGB other) {
		return new RGB(
				R + other.R,
				G + other.G,
				B + other.B
		);
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

	public RGB compDived(RGB other) {
		final float e = 1e-5f;

		float r = R;
		float g = G;
		float b = B;

		// can devide : do it
		if (other.R > e) {
			r /= other.R;
		} else {
			// cannot divide

			// cannot reach
			if (R > 0) {
				r = 1/e;
			} else {
				// is already reached
				r = 0;
			}
		}

		// can devide : do it
		if (other.G > e) {
			g /= other.G;
		} else {
			// cannot divide

			// cannot reach
			if (G > 0) {
				g = 1/e;
			} else {
				// is already reached
				g = 0;
			}
		}

		// can devide : do it
		if (other.B > e) {
			b /= other.B;
		} else {
			// cannot divide

			// cannot reach
			if (B > 0) {
				b = 1/e;
			} else {
				// is already reached
				b = 0;
			}
		}

		return new RGB(r, g, b);
	}

	public float maxComp() {
		return Math.max(R, (Math.max(G, B)));
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
