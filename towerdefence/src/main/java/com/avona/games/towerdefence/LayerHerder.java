package com.avona.games.towerdefence;

import java.util.LinkedList;
import java.util.List;

public class LayerHerder {
	private static Layer DEFAULT_LAYER = new Layer();

	@SuppressWarnings("unchecked")
	private List<Layer>[] layers = new List[Layer.MAX_DEPTH + 1];

	LayerHerder() {
		for (int depth = 0; depth <= Layer.MAX_DEPTH; ++depth) {
			layers[depth] = new LinkedList<>();
		}
	}

	void addLayer(final Layer layer) {
		layers[layer.depth].add(layer);
	}

	public void removeLayer(final Layer layer) {
		layers[layer.depth].remove(layer);
	}

	/**
	 * Determines which layer the specified screen point falls into.
	 *
	 * @param point Screen position.
	 * @return Returns the matching layer or DEFAULT_LAYER if none was found.
	 */
	public Layer findLayerWithinPoint(V2 point) {
		for (int depth = 0; depth <= Layer.MAX_DEPTH; ++depth) {
			for (Layer layer : layers[depth]) {
				if (layer.offset.x <= point.x
						&& layer.offset.x + layer.region.x >= point.x
						&& layer.offset.y <= point.y
						&& layer.offset.y + layer.region.y >= point.y) {
					return layer;
				}
			}
		}
		return DEFAULT_LAYER;
	}

	public Layer findLayerByName(String name) {
		for (int depth = 0; depth <= Layer.MAX_DEPTH; ++depth) {
			for (Layer layer : layers[depth]) {
				if (layer.name.equals(name)) {
					return layer;
				}
			}
		}
		return null;
	}
}
