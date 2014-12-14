
package com.techjar.cubedesigner.hardware.animation;

import com.techjar.cubedesigner.CubeDesigner;
import com.techjar.cubedesigner.util.MathHelper;
import com.techjar.cubedesigner.util.Timer;
import com.techjar.cubedesigner.util.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.lwjgl.util.Color;

/**
 *
 * @author Techjar
 */
public class AnimationMultiFaucet extends Animation {
    private Timer timer = new Timer();
    private Random random = new Random();
    private List<Vector2> faucets = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();

    public AnimationMultiFaucet() {
        super();
    }

    @Override
    public String getName() {
        return "Multi Faucet";
    }

    @Override
    public void refresh() {
        if (timer.getMilliseconds() >= 50) {
            timer.restart();
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    for (int y = 1; y < 8; y++) {
                        ledManager.setLEDColor(x, y - 1, z, ledManager.getLEDColor(x, y, z));
                        ledManager.setLEDColor(x, y, z, new Color());
                    }
                }
            }
            for (int i = 0; i < faucets.size(); i++) {
                Vector2 pos = faucets.get(i);
                if (random.nextInt(2) == 0) {
                    do {
                        pos.setX(MathHelper.clamp(pos.getX() + (random.nextInt(3) - 1), 0, 7));
                        pos.setY(MathHelper.clamp(pos.getY() + (random.nextInt(3) - 1), 0, 7));
                    } while (isOtherFaucetAt(pos));
                }
                ledManager.setLEDColor((int)pos.getX(), 7, (int)pos.getY(), colors.get(i));
            }
        }
    }

    @Override
    public void reset() {
        faucets.clear();
        colors.clear();
        for (int i = 0; i < 5; i++) {
            faucets.add(new Vector2(random.nextInt(8), random.nextInt(8)));
            colors.add(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }
    }

    private boolean isFaucetAt(Vector2 pos) {
        for (Vector2 faucet : faucets) {
            if ((int)faucet.getX() == (int)pos.getX() && (int)faucet.getY() == (int)pos.getY()) return true;
        }
        return false;
    }

    private boolean isOtherFaucetAt(Vector2 pos) {
        for (Vector2 faucet : faucets) {
            if (faucet != pos && (int)faucet.getX() == (int)pos.getX() && (int)faucet.getY() == (int)pos.getY()) return true;
        }
        return false;
    }
}
