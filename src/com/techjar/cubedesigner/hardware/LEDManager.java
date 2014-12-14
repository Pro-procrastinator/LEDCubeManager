
package com.techjar.cubedesigner.hardware;

import com.techjar.cubedesigner.util.Dimension3D;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

/**
 *
 * @author Techjar
 */
public interface LEDManager {
    /**
     * Returns the brightness resolution as the maximum value that can be used.
     * For example, 4-bit brightness is 0-15, so the return value would be 15.
     */
    public int getResolution();

    /**
     * Returns the dimensions of the LED display.
     * Currently, changing this from 8x8x8 is not supported.
     */
    public Dimension3D getDimensions();

    /**
     * Returns byte array to be sent across the serial connection.
     */
    public byte[] getSerialData();

    /**
     * Gets the color of an LED as the raw value, not normalized into the 24-bit RGB color space.
     */
    public Color getLEDColorRaw(int x, int y, int z);

    /**
     * Gets the color of an LED normalized into the 24-bit RGB color space.
     */
    public Color getLEDColor(int x, int y, int z);

    /**
     * Sets the color of an LED as the raw value, not normalized into the 24-bit RGB color space.
     */
    public void setLEDColorRaw(int x, int y, int z, ReadableColor color);

    /**
     * Sets the color of an LED normalized into the 24-bit RGB color space.
     */
    public void setLEDColor(int x, int y, int z, ReadableColor color);
}
