package com.avona.games.towerdefence.input;

import com.avona.games.towerdefence.awt.AwtReplShader;
import com.avona.games.towerdefence.awt.ReplShaderGui;
import com.avona.games.towerdefence.core.V2;
import com.avona.games.towerdefence.engine.PortableMainLoop;
import com.avona.games.towerdefence.util.Util;

import java.util.HashMap;

public class LayeredInputActor implements InputActor {
	public HashMap<Layer, InputActor> inputLayerMap = new HashMap<>();
	private PortableMainLoop ml;
	private Mouse mouse;
	private LayerHerder layerHerder;

	public LayeredInputActor(PortableMainLoop mainLoop, Mouse mouse, LayerHerder layerHerder) {
		this.ml = mainLoop;
		this.mouse = mouse;
		this.layerHerder = layerHerder;
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
		Util.log("got mouse 1 down... looking for layer");
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null) {
			return;
		}
		Util.log("proceeding with layer " + layer);
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null) {
			return;
		}
		Util.log("proceeding with inputactor " + inputActor);
		inputActor.mouseBtn1DownAt(location);
	}

	@Override
	public void mouseBtn1UpAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null) {
			return;
		}
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null) {
			return;
		}
		inputActor.mouseBtn1UpAt(location);
	}

	@Override
	public void mouseBtn2DownAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null) {
			return;
		}
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null) {
			return;
		}
		inputActor.mouseBtn2DownAt(location);
	}

	@Override
	public void mouseBtn2UpAt(V2 location) {
		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null) {
			return;
		}
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null) {
			return;
		}
		inputActor.mouseBtn2UpAt(location);
	}

	@Override
	public void mouseMovedTo(V2 location) {
		mouse.physicalLocation = location;

		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null) {
			return;
		}
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null) {
			return;
		}
		inputActor.mouseMovedTo(location);
	}

	@Override
	public void mouseDraggedTo(V2 location) {
		mouse.physicalLocation = location;

		final Layer layer = layerHerder.findLayerWithinPoint(location);
		if (layer == null) {
			return;
		}
		location = layer.convertToVirtual(location);
		final InputActor inputActor = inputLayerMap.get(layer);
		if (inputActor == null) {
			return;
		}
		inputActor.mouseDraggedTo(location);
	}

	@Override
	public void togglePause() {
		ml.toggleGamePause();
	}

	@Override
	public void pressedOtherKey(char keyCode) {
		if (keyCode == 'x') { // not pretty, but works
			ReplShaderGui.main2();
			ml.ge.setTowerShader(AwtReplShader.getInstance());
		}

		// forward to game - in the hope that layer can handle it
		final Layer layer = layerHerder.findLayerByName(PortableMainLoop.GAME_LAYER_NAME);
		final InputActor inputActor = inputLayerMap.get(layer);
		inputActor.pressedOtherKey(keyCode);
	}
}
