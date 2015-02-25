
package com.techjar.ledcm.hardware.animation;

import com.techjar.ledcm.LEDCubeManager;
import com.techjar.ledcm.util.MathHelper;
import com.techjar.ledcm.util.Timer;
import com.techjar.ledcm.util.Vector2;
import java.util.Random;
import org.lwjgl.util.Color;

/**
 *
 * @author Techjar
 */
public class AnimationFaucet extends Animation {
    private Random random = new Random();
    private Vector2 pos = new Vector2();

    public AnimationFaucet() {
        super();
    }

    @Override
    public String getName() {
        return "Faucet";
    }

    @Override
    public void refresh() {
        if (ticks % 3 == 0) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    for (int y = 1; y < 8; y++) {
                        ledManager.setLEDColor(x, y - 1, z, ledManager.getLEDColor(x, y, z));
                        ledManager.setLEDColor(x, y, z, new Color());
                    }
                }
            }
            if (random.nextInt(2) == 0) {
                pos.setX(MathHelper.clamp(pos.getX() + (random.nextInt(3) - 1), 0, 7));
                pos.setY(MathHelper.clamp(pos.getY() + (random.nextInt(3) - 1), 0, 7));
            }
            ledManager.setLEDColor((int)pos.getX(), 7, (int)pos.getY(), LEDCubeManager.getPaintColor());
        }
    }

    @Override
    public void reset() {
        pos = new Vector2(random.nextInt(8), random.nextInt(8));
    }
}