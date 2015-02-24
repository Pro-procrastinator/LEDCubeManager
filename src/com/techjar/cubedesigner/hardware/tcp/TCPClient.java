
package com.techjar.cubedesigner.hardware.tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import lombok.SneakyThrows;

/**
 *
 * @author Techjar
 */
public class TCPClient {
    private Thread timeoutThread;
    private Thread sendThread;
    @Getter private Socket socket;
    @Getter private InputStream inputStream;
    @Getter private OutputStream outputStream;
    private Queue<Packet> sendQueue = new ConcurrentLinkedQueue<>();

    public TCPClient(Socket socket, int index) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        timeoutThread = new Thread("Client Timeout Thread #" + index) {
            @Override
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(TCPClient.this.socket.getInputStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    TCPClient.this.socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        timeoutThread.setDaemon(true);
        timeoutThread.start();
        sendThread = new Thread("Client Send Thread #" + index) {
            @Override
            @SneakyThrows(InterruptedException.class)
            public void run() {
                Packet packet;
                DataOutputStream out = new DataOutputStream(TCPClient.this.outputStream);
                while (!TCPClient.this.socket.isClosed()) {
                    while ((packet = sendQueue.poll()) != null) {
                        try {
                            Packet.writePacket(out, packet);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            try {
                                close();
                            } catch (IOException ex2) {
                                ex2.printStackTrace();
                            }
                            return;
                        }
                    }
                    Thread.sleep(1);
                }
            }
        };
        sendThread.start();
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public int getPort() {
        return socket.getPort();
    }

    public synchronized void close() throws IOException {
        socket.close();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void queuePacket(Packet packet) {
        sendQueue.add(packet);
    }
}