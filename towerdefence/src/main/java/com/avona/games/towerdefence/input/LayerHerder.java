package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.core.V2;

import java.util.LinkedList;
import java.util.List;

public class LayerHerder {
	private static Layer DEFAULT_LAYER = new Layer(
			"DEFAULTLAYER",
			2,
			null,
			new V2(),
			new V2(),
			new V2()
	);
	public Layer gameLayer;
	public MenuLayer menuLayer;
	@SuppressWarnings("unchecked")
	private List<Layer>[] layers = new List[Layer.MAX_DEPTH + 1];
	public LayerHerder() {
		for (int depth = 0; depth <= Layer.MAX_DEPTH; ++depth) {
			layers[depth] = new LinkedList<>();
		}
	}

	public void onReshapeScreen(V2 newSize) {
		final float gameFieldPercentage = gameLayer.virtualRegion.x
				/ (gameLayer.virtualRegion.x + menuLayer.virtualRegion.x);

		final float gameRatio = gameLayer.virtualRegion.x
				/ gameLayer.virtualRegion.y;

		gameLayer.region.y = newSize.y;
		gameLayer.region.x = gameRatio * gameLayer.region.y;
		if (gameLayer.region.x / newSize.x > gameFieldPercentage) {
			// Too wide, screen width is the limit.
			gameLayer.region.x = newSize.x * gameFieldPercentage;
			gameLayer.region.y = gameLayer.region.x / gameRatio;
		}
		gameLayer.offset.x = 0;
		gameLayer.offset.y = (newSize.y - gameLayer.region.y) * 0.5f;

		final float menuRatio = menuLayer.virtualRegion.x
				/ menuLayer.virtualRegion.y;

		final V2 remainingSize = new V2(newSize.x - gameLayer.offset.x
				- gameLayer.region.x, newSize.y);

		menuLayer.region.y = remainingSize.y;
		menuLayer.region.x = menuRatio * menuLayer.region.y;
		if (menuLayer.region.x > remainingSize.x) {
			// Too wide, screen width is the limit.
			menuLayer.region.x = remainingSize.x;
			menuLayer.region.y = menuLayer.region.x / menuRatio;
		}

		gameLayer.offset.x += (remainingSize.x - menuLayer.region.x) * .5f;

		menuLayer.offset.y = gameLayer.offset.y;
		menuLayer.offset.x = gameLayer.offset.x + gameLayer.region.x;

		menuLayer.resizeChildren();
	}

	public void addLayer(final Layer layer) {
		layers[layer.depth].add(layer);
	}

	public void addGameLayer(Layer gameLayer) {
		this.gameLayer = gameLayer;
		addLayer(gameLayer);
	}

	public void addMenuLayer(MenuLayer menuLayer) {
		this.menuLayer = menuLayer;
		addLayer(menuLayer);
	}

	public void removeLayer(final Layer layer) {
		layers[layer.depth].remove(layer);
	}

	/**
	 * Determines which layer the specified screen point falls into.
	 * Lower depth gets served/returned first.
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
