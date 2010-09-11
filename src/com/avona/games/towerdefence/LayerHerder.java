package com.avona.games.towerdefence;

import java.util.ArrayList;
import java.util.List;

public class LayerHerder {
	public static Layer DEFAULT_LAYER = new Layer();

	public List<Layer> layers = new ArrayList<Layer>();

	/**
	 * Determines which layer the specified screen point falls into.
	 *  
	 * @param point Screen position.
	 * @return Returns the matching layer or DEFAULT_LAYER if none was found.
	 */
	public Layer findLayerWithinPoint(V2 point) {
		for (Layer layer : layers) {
			if (layer.offset.x <= point.x
					&& layer.offset.x + layer.region.x >= point.x
					&& layer.offset.y <= point.y
					&& layer.offset.y + layer.region.y >= point.y) {
				return layer;
			}
		}
		return DEFAULT_LAYER;
	}
}
