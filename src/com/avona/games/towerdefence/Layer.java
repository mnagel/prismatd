package com.avona.games.towerdefence;

public class Layer {
	/**
	 * Name of the layer. Must be unique among all layers.
	 */
	public String name = new String();

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

	public String toString() {
		return "Layer<offset " + offset + ", region " + region + ">";
	}

	/**
	 * Convert screen coordinate to a coordinate that is relative to and scaled
	 * for the layer.
	 * 
	 * @param realPosition Screen coordinate to be converted.
	 * @return Layer relative coordinate.
	 */
	public V2 convertToVirtual(V2 realPosition) {
		return new V2((realPosition.x - offset.x)
				* (virtualRegion.x / region.x), (realPosition.y - offset.y)
				* (virtualRegion.y / region.y));
	}
}
