package com.avona.games.towerdefence.inputActors;

import java.util.HashMap;

import com.avona.games.towerdefence.Layer;
import com.avona.games.towerdefence.LayerHerder;
import com.avona.games.towerdefence.Mouse;
import com.avona.games.towerdefence.PortableMainLoop;
import com.avona.games.towerdefence.Util;
import com.avona.games.towerdefence.V2;

public class LayeredInputActor implements InputActor {
	private PortableMainLoop ml;
	private Mouse mouse;
	private LayerHerder layerHerder;
	private Layer lastLayer = null;
	private boolean currentLayerSawMouseBtn1Down = false;
	private boolean currentLayerSawMouseBtn2Down = false;
	public HashMap<Layer, InputActor> inputLayerMap = new HashMap<Layer, InputActor>();

	public LayeredInputActor(PortableMainLoop mainLoop, Mouse mouse,
			LayerHerder layerHerder) {
		this.ml = mainLoop;
		this.mouse = mouse;
		this.layerHerder = layerHerder;
	}

	@Override
	public void pressedMenuKey() {
		ml.exit();
	}

	@Override
	public void pause() {
		ml.pauseGame();
	}

	@Override
	public void resume() {
		ml.unpauseGame();
	}

	@Override
	public void mouseBtn1DownAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		currentLayerSawMouseBtn1Down = true;
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseBtn1DownAt(location);
	}

	@Override
	public void mouseBtn1UpAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null)
			return;
		if (!currentLayerSawMouseBtn1Down)
			return;
		currentLayerSawMouseBtn1Down = false;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseBtn1UpAt(location);
	}

	@Override
	public void mouseBtn2DownAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		currentLayerSawMouseBtn2Down = true;
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseBtn2DownAt(location);
	}

	@Override
	public void mouseBtn2UpAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null)
			return;
		if (!currentLayerSawMouseBtn2Down)
			return;
		currentLayerSawMouseBtn2Down = false;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseBtn2UpAt(location);
	}

	private void mouseEnteredLayer(final Layer layer) {
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseEntered();
	}

	@Override
	public void mouseEntered() {
		mouse.onScreen = true;
		currentLayerSawMouseBtn1Down = false;
		currentLayerSawMouseBtn2Down = false;

		final Layer layer = layerHerder.findLayerWithinPoint(mouse.location);
		mouseEnteredLayer(layer);
		lastLayer = layer;
	}

	private void mouseExitedLayer(final Layer layer) {
		currentLayerSawMouseBtn1Down = false;
		currentLayerSawMouseBtn2Down = false;

		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseExited();
	}

	@Override
	public void mouseExited() {
		mouse.onScreen = false;

		mouseExitedLayer(lastLayer);
		lastLayer = null;
	}

	@Override
	public void mouseMovedTo(V2 location) {
		mouse.location = location;

		final Layer layer = layerHerder.findLayerWithinPoint(location);
		detectLayerSwitch(layer);
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseMovedTo(location);
	}

	@Override
	public void mouseDraggedTo(V2 location) {
		mouse.location = location;

		final Layer layer = layerHerder.findLayerWithinPoint(location);
		detectLayerSwitch(layer);
		if (layer == null)
			return;
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null)
			return;
		inputActor.mouseDraggedTo(location);
	}

	private void detectLayerSwitch(final Layer layer) {
		if (layer == lastLayer)
			return;

		if (lastLayer != null)
			mouseExitedLayer(lastLayer);
		if (layer != null)
			mouseEnteredLayer(layer);
		lastLayer = layer;
	}

	@Override
	public void togglePause() {
		ml.toggleGamePause();
	}

	@Override
	public void pressedOtherKey(char keyCode) {
		if (keyCode == ' ') {
			ml.game.pressForwardButton();
		}
		if (keyCode == 'k') {
			ml.game.killAllEnemies();
		}
		if (keyCode == 'd') {
			ml.game.logDebugInfo();
		}
		if (keyCode == 'l') {
			String[] levels = Util.mapLevelNames(ml.game.levels);
			int what = ml.display.userSelectsAString("Load Level", "Please select a Level to load:", levels);
			if (what >= 0) {
				ml.game.loadLevel(what);
			}
		}
	}
}
