package com.avona.games.towerdefence.inputActors;

import com.avona.games.towerdefence.*;
import com.avona.games.towerdefence.awt.AwtReplShader;
import com.avona.games.towerdefence.awt.ReplShaderGui;
import com.avona.games.towerdefence.tower.Tower;

import java.util.HashMap;

public class LayeredInputActor implements InputActor {
	public HashMap<Layer, InputActor> inputLayerMap = new HashMap<>();
	private PortableMainLoop ml;
	private Mouse mouse;
	private LayerHerder layerHerder;
	private Layer lastLayer = null;
	private boolean currentLayerSawMouseBtn1Down = false;
	private boolean currentLayerSawMouseBtn2Down = false;

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
		// SPACE <<< yeah, search function works now
		if (keyCode == ' ') {
			ml.game.pressForwardButton();
		}
		if (keyCode == '+') {
			if (ml.game.selectedObject instanceof Tower) {
				final Tower t = (Tower) ml.game.selectedObject;
				ml.game.levelUpTower(t);
			}
		}
		if (keyCode == 'd') {
			ml.game.logDebugInfo();
		}
		if (keyCode == 'k') {
			ml.game.killAllEnemies();
		}
		if (keyCode == 'l') {
			ml.game.loadMissionInteractive();
		}
		if (keyCode == 'x') { // not pretty, but works
			ReplShaderGui.main2();
			ml.ge.setTowerShader(AwtReplShader.getInstance());
		}
	}
}
