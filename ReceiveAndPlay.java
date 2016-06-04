package chatapplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author nishen
 */
public class ReceiveAndPlay extends Thread {  
    private final int packetSize = 500;
    private final int port;
    private final Player player;
    
    public ReceiveAndPlay(int port) {
        this.port = port;
        this.player = new Player();
    }
    
    @Override
    public void run() { 
        DatagramSocket datagramSocket = null;
        DatagramPacket datagramPacket;
        VoicePacket voicePacket;
        try {
            // Receive the record
            datagramSocket = new DatagramSocket(this.port);
        } catch (SocketException ex) {
            System.out.println(ex + ": ReceiveAndPlay thread.run()");
            System.exit(0);
        }
        datagramPacket = new DatagramPacket(new byte[this.packetSize], this.packetSize);
        
        while(true) {
            try {
                datagramSocket.receive(datagramPacket);
                voicePacket = VoicePacket.deserialize(datagramPacket.getData());
                this.player.play(voicePacket.getPayload());
            } catch (IOException ex) {
                System.out.println(ex + ": ReceiveAndPlay thread.run()");
            }           
        }
    }    
}
