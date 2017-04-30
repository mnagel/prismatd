package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;

import java.util.Locale;

public class Layer {
	public static int MIN_DEPTH = 0;
	public static int MAX_DEPTH = 2;
	/**
	 * Name of the layer. Must be unique among all layers.
	 */
	public String name;
	/**
	 * Size used by screen, i.e. pixel size.
	 */
	public V2 region = new V2();
	/**
	 * Size used by internal logic. Typically constant for a certain layer type.
	 */
	public V2 virtualRegion = new V2();
	/**
	 * Absolute offset from the screens lower left corner.
	 */
	public V2 offset = new V2();
	/**
	 * Defines the z order of the layer.
	 */
	int depth = 1;
	private Layer parent = null;

	public Layer(String name, Layer parent, V2 offset, V2 region, V2 virtualRegion) {
		this.name = name;
		this.parent = parent;
		this.offset = offset;
		this.region = region;
		this.virtualRegion = virtualRegion;
	}

	public String toString() {
		return String.format(Locale.US, "L:%s v%s r%s at %s", name, virtualRegion.toString(0), region.toString(0), offset.toString(0));
	}

	/**
	 * Convert screen coordinate to a coordinate that is relative to and scaled
	 * for the layer.
	 *
	 * @param realPosition Screen coordinate to be converted.
	 * @return Layer relative coordinate.
	 */
	public V2 convertToVirtual(final V2 realPosition) {
		return new V2((realPosition.x - offset.x)
				* (virtualRegion.x / region.x), (realPosition.y - offset.y)
				* (virtualRegion.y / region.y));
	}

	public V2 convertToPhysical(final V2 virtualPosition) {
		return new V2(
				virtualPosition.x / virtualRegion.x * region.x + offset.x,
				virtualPosition.y / virtualRegion.y * region.y + offset.y);
	}

	public float scaleToPhysical(float value) {
		// Assumption: x scale = y scale
		return value * region.x / virtualRegion.x;
	}
}
