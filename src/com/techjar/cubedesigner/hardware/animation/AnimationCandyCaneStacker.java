
package com.techjar.cubedesigner.hardware.animation;

import com.techjar.cubedesigner.CubeDesigner;
import com.techjar.cubedesigner.util.MathHelper;
import com.techjar.cubedesigner.util.Timer;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 *
 * @author Techjar
 */
public class AnimationCandyCaneStacker extends Animation {
    private Timer timer = new Timer();
    private int layers;
    private int curLayer;
    private int curLayerNum;
    private int topLayer;
    private int allLayers;

    public AnimationCandyCaneStacker() {
        super();
        topLayer = (int)Math.pow(2, dimension.y - 1);
        allLayers = (int)Math.pow(2, dimension.y) - 1;
    }

    @Override
    public String getName() {
        return "Candy Cane Stacker";
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
                    curLayerNum++;
                } else {
                    curLayer >>= 1;
                }
            }
            for (int y = 0; y < dimension.y; y++) {
                Color color = checkBit(layers, y) ? getColorAtLayer(y) : checkBit(curLayer, y) ? getColorAtLayer(curLayerNum) : new Color();
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
        curLayerNum = 0;
    }

    private boolean checkBit(int number, int bit) {
        return (number & (1 << bit)) != 0;
    }

    private Color getColorAtLayer(int layer) {
        if (layer % 2 == 0) return new Color(255, 0, 0);
        return new Color(255, 255, 255);
    }
}
