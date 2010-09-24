package com.avona.games.towerdefence.inputActors;

import java.util.HashMap;

import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.LayerHerder;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.V2;

public class LayeredInputActor implements InputActor {
	private PortableMainLoop ml;
	private Mouse mouse;
	private LayerHerder layerHerder;
	public HashMap<Layer, InputActor> inputLayerMap = new HashMap<Layer, InputActor>();

	public LayeredInputActor(PortableMainLoop mainLoop, Mouse mouse,
			LayerHerder layerHerder) {
		this.ml = mainLoop;
		this.mouse = mouse;
		this.layerHerder = layerHerder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#pressedEscapeKey()
	 */
	public void pressedEscapeKey() {
		ml.exit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#onPause()
	 */
	public void onPause() {
		ml.pauseGame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#onResume()
	 */
	public void onResume() {
		ml.unpauseGame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#pressedSpaceKey()
	 */
	public void pressedSpaceKey() {
		ml.toggleGamePause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#pressedMouseBtn1At(com.avona.
	 * games.towerdefence.V2)
	 */
	public void pressedMouseBtn1At(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.pressedMouseBtn1At(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#pressedMouseBtn2At(com.avona.
	 * games.towerdefence.V2)
	 */
	public void pressedMouseBtn2At(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.pressedMouseBtn2At(location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#mouseEntered()
	 */
	public void mouseEntered() {
		mouse.onScreen = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.avona.games.towerdefence.InputActor#mouseExited()
	 */
	public void mouseExited() {
		mouse.onScreen = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#mouseMovedTo(com.avona.games.
	 * towerdefence.V2)
	 */
	public void mouseMovedTo(V2 location) {
		mouse.location = location;

		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseMovedTo(location);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.avona.games.towerdefence.InputActor#mouseDraggedTo(com.avona.games
	 * .towerdefence.V2)
	 */
	public void mouseDraggedTo(V2 location) {
		mouse.location = location;

		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseDraggedTo(location);
	}
}
