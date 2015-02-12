
package com.techjar.cubedesigner.hardware;

import com.techjar.cubedesigner.CubeDesigner;
import com.techjar.cubedesigner.hardware.animation.Animation;
import com.techjar.cubedesigner.hardware.animation.AnimationSequence;
import com.techjar.cubedesigner.hardware.tcp.TCPServer;
import com.techjar.cubedesigner.util.Constants;
import com.techjar.cubedesigner.util.MathHelper;
import com.techjar.cubedesigner.util.Timer;
import java.io.IOException;
import jssc.SerialPort;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 *
 * @author Techjar
 */
public class CommThread extends Thread {
    private final Object lock = new Object();
    private final LEDManager ledManager;
    private long updateTime;
    private long ticks;
    @Getter @Setter private int refreshRate = 60;
    @Getter private Animation currentAnimation;
    @Getter private AnimationSequence currentSequence;
    private SerialPort port;
    @Getter private TCPServer tcpServer;
    /*int numRecv;
    Timer timer = new Timer();
    int lastRecv = -1;
    int numErr;*/

    public CommThread() throws IOException {
        this.setName("Animation / Communication");
        ledManager = CubeDesigner.getLEDManager();
        updateTime = System.nanoTime();
        port = new SerialPort(CubeDesigner.getSerialPortName());
        tcpServer = new TCPServer(CubeDesigner.getServerPort());
        Runtime.getRuntime().addShutdownHook(new ShutdownThread());
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    public void run() {
        while (true) {
            /*if (timer.getSeconds() >= 1) {
                timer.restart();
                System.out.println("Recv rate: " + numRecv);
                System.out.println("Error count: " + numErr);
                numRecv = 0;
            }*/
            long interval = 1000000000 / refreshRate;
            long diff = System.nanoTime() - updateTime;
            if (diff >= interval) {
                updateTime = System.nanoTime();
                ticks++;
                synchronized (lock) {
                    if (currentSequence != null) currentSequence.update();
                    if (currentAnimation != null) currentAnimation.refresh();
                    byte[] data = ledManager.getCommData();
                    tcpServer.sendData(data);
                    try {
                        if (port.isOpened()) {
                            port.writeBytes(data);
                            //port.writeByte((byte)1);
                            /*byte[] bytes = port.readBytes(1152, 1000);
                            if (bytes != null) {
                                //System.out.println(new String(bytes, "UTF-8"));
                                for (int i = 0; i < 1152; i++) {
                                    if (bytes[i] != data[i]) System.out.println("ERROR @ " + i);
                                }
                                for (byte b : bytes) {
                                    int bb = b & 0xFF;
                                    if (lastRecv != -1 && (lastRecv < 255 && bb != lastRecv + 1) || (lastRecv == 255 && bb != 0)) {
                                        //System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!");
                                        numErr++;
                                    }
                                    lastRecv = bb;
                                    //System.out.println("0x" + Integer.toHexString(b & 0xFF).toUpperCase());
                                    numRecv++;
                                }
                            }*/
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        closePort();
                    }
                }
            }
            else if (interval - diff > 1000000) {
                Thread.sleep(1);
            }
        }
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        synchronized (lock) {
            this.currentAnimation = currentAnimation;
            if (currentAnimation != null) currentAnimation.reset();
        }
    }

    public void setCurrentSequence(AnimationSequence currentSequence) {
        synchronized (lock) {
            this.currentSequence = currentSequence;
            CubeDesigner.getInstance().getScreenMainControl().animComboBox.setEnabled(currentSequence == null);
            if (currentSequence != null) currentSequence.start();
        }
    }

    public void openPort() {
        synchronized (lock) {
            if (!port.isOpened()) {
                try {
                    port.openPort();
                    port.setParams(2000000, 8, 1, 0);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void closePort() {
        synchronized (lock) {
            if (port.isOpened()) {
                try {
                    port.closePort();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean isPortOpen() {
        return port.isOpened();
    }

    public int getNumTCPClients() {
        return tcpServer.getNumClients();
    }

    private class ShutdownThread extends Thread {
        @Override
        public void run() {
            closePort();
        }
    }
}
