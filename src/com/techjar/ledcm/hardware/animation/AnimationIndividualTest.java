
package com.techjar.ledcm.hardware.animation;

import com.techjar.ledcm.hardware.LEDUtil;
import com.techjar.ledcm.util.Util;
import com.techjar.ledcm.util.Vector3;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 *
 * @author Techjar
 */
public class AnimationIndividualTest extends Animation {
    private final int count;
    private int index;
    private int subIndex;

    public AnimationIndividualTest() {
        super();
        count = dimension.x * dimension.y * dimension.z * 3;
    }

    @Override
    public String getName() {
        return "Individual Test";
    }

    @Override
    public void refresh() {
        if (ticks % 12 == 0) {
            LEDUtil.clear(ledManager);
            Vector3 pos = Util.decodeCubeVector(index / 3);
            ledManager.setLEDColor((int)pos.getX(), (int)pos.getY(), (int)pos.getZ(), new Color(index % 3 == 0 ? 255 : 0, index % 3 == 1 ? 255 : 0, index % 3 == 2 ? 255 : 0));
            if (++index >= count) {
                index = 0;
            }
        }
    }

    @Override
    public void reset() {
        index = 0;
        subIndex = 0;
    }

}
