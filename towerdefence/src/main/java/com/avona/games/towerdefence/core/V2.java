package com.avona.games.towerdefence.core;

import java.io.Serializable;
import java.util.Locale;

public final class V2 implements Serializable {
	private static final long serialVersionUID = 1L;
	public float x = 0.0f;
	public float y = 0.0f;

	public V2() {
	}

	public V2(final float x, final float y) {
		this.x = x;
		this.y = y;
	}

	public V2(final V2 orig) {
		x = orig.x;
		y = orig.y;
	}

	/**
	 * Construct a non-empty V2 vector.
	 *
	 * @param direction Direction of the vector.
	 * @param length    Length of the vector.
	 */
	public V2(final V2 direction, final float length) {
		this(direction);
		this.setLength(length);
	}

	public static float dist(final V2 from, final V2 to) {
		return (float) Math.sqrt((to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y));
	}

	public static float squaredDist(final V2 from, final V2 to) {
		return (to.x - from.x) * (to.x - from.x) + (to.y - from.y) * (to.y - from.y);
	}

	public static float dot(final V2 vec0, final V2 vec1) {
		return vec0.x * vec1.x + vec0.y * vec1.y;
	}

	/**
	 * Project one vector onto the other vector.
	 *
	 * @param toBeProjectedVec Vector that will be projected onto baseVec.
	 * @param baseVec          Vector upon the projection will take place.
	 * @return Returns the projected vector as a new vector instance.
	 */
	public static V2 project(final V2 toBeProjectedVec, final V2 baseVec) {
		V2 projectedVec = new V2(baseVec);
		projectedVec.mult(dot(toBeProjectedVec, baseVec) / dot(baseVec, baseVec));
		return projectedVec;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		V2 v2 = (V2) o;

		return Float.compare(v2.x, x) == 0 && Float.compare(v2.y, y) == 0;

	}

	@Override
	public int hashCode() {
		int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
		result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
		return result;
	}

	public V2 clone2() {
		return new V2(this);
	}

	public String toString() {
		return String.format(Locale.US, "V2<%f|%f>", x, y);
	}

	public String toString(int prec) {
		String f = "V2<%." + prec + "f|%." + prec + "f>";
		return String.format(f, x, y);
	}

	public V2 add(final V2 v) {
		x += v.x;
		y += v.y;
		return this;
	}

	public V2 add(final float add_x, final float add_y) {
		x += add_x;
		y += add_y;
		return this;
	}

	/**
	 * Add other V2 with weight factor to this instance.
	 *
	 * @param other  V2 to accumulate onto this instance.
	 * @param weight Weight factor. 1.0f to get plain addition.
	 */
	public V2 addWeighted(final V2 other, final float weight) {
		this.add(other.x * weight, other.y * weight);
		return this;
	}

	public V2 sub(final V2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}

	public V2 sub(final float sub_x, final float sub_y) {
		x -= sub_x;
		y -= sub_y;
		return this;
	}

	public V2 mult(final float f) {
		x *= f;
		y *= f;
		return this;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public V2 setLength(float targetLength) {
		if (Math.abs(targetLength) < 1e-5) {
			x = y = 0;
			return this;
		}

		float f = targetLength / length();
		return this.mult(f);
	}

	public float squaredLength() {
		return (x * x + y * y);
	}

	public V2 normalize() {
		this.setLength(1.0f);
		return this;
	}

	public float dist(final V2 dest) {
		return dist(this, dest);
	}

	public float squaredDist(final V2 dest) {
		return squaredDist(this, dest);
	}

	/**
	 * Set the direction based on the two points from and to. The resulting
	 * direction will point from "from" to "to". Length is unchanged.
	 *
	 * @param from Starting point. Will not be modified.
	 * @param to   Target point. Will not be modified.
	 */
	public V2 setDirection(final V2 from, final V2 to) {
		final float l = this.length();
		this.x = to.x - from.x;
		this.y = to.y - from.y;
		return this.setLength(l);
	}

	public V2 rotate(final V2 origin, final float degrees) {
		float radians = degrees * (float) Math.PI / 180;
		float xx = (float) (Math.cos(radians) * (this.x - origin.x) - Math.sin(radians) * (this.y - origin.y) + origin.x);
		float yy = (float) (Math.sin(radians) * (this.x - origin.x) + Math.cos(radians) * (this.y - origin.y) + origin.y);
		this.x = xx;
		this.y = yy;
		return this;
	}
}
