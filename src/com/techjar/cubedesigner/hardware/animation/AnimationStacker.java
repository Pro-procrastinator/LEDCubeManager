
package com.techjar.cubedesigner.hardware.animation;

import com.techjar.cubedesigner.CubeDesigner;
import com.techjar.cubedesigner.util.Timer;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 *
 * @author Techjar
 */
public class AnimationStacker extends Animation {
    private Timer timer = new Timer();
    private int layers;
    private int curLayer;
    private int topLayer;
    private int allLayers;

    public AnimationStacker() {
        super();
        topLayer = (int)Math.pow(2, dimension.y - 1);
        allLayers = (int)Math.pow(2, dimension.y) - 1;
    }

    @Override
    public String getName() {
        return "Stacker";
    }

    @Override
    public void refresh() {
        if (timer.getMilliseconds() >= 50) {
            timer.restart();
            if (layers != allLayers) {
                if (curLayer == 0) {
                    curLayer |= topLayer;
                } else if ((layers | (curLayer >> 1)) == layers) {
                    layers |= curLayer;
                    curLayer = 0;
                } else {
                    curLayer >>= 1;
                }
            }
            for (int y = 0; y < dimension.y; y++) {
                Color color = checkBit(layers | curLayer, y) ? CubeDesigner.getPaintColor() : new Color();
                for (int x = 0; x < dimension.x; x++) {
                    for (int z = 0; z < dimension.z; z++) {
                        ledManager.setLEDColor(x, y, z, color);
                    }
                }
            }
        }
    }

    @Override
    public void reset() {
        layers = 0;
    }

    private boolean checkBit(int number, int bit) {
        return (number & (1 << bit)) != 0;
    }
}
