/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nishen
 */
public class MulticastReceiveAndPlay extends Thread {
    private final int packetSize = 500;
    private final int port;
    private final InetAddress multicastIP;
    private final Player player;
    
    public MulticastReceiveAndPlay(InetAddress multicastIP, int port) {
        this.port = port;
        this.multicastIP = multicastIP;
        this.player = new Player();
    }
    
    @Override
    public void run() { 
        MulticastSocket multicastSocket = null;
        DatagramPacket datagramPacket;
        VoicePacket voicePacket;
        try {
            // Receive the record
            multicastSocket = new MulticastSocket(this.port);
        } catch (SocketException ex) {
            System.out.println(ex + ": ReceiveAndPlay thread.run()");
            System.exit(0);
        } catch (IOException ex) {
            System.out.println(ex + ": ReceiveAndPlay thread.run()");
            System.exit(0);
        }
        try {
            multicastSocket.joinGroup(multicastIP);
        } catch (IOException ex) {
            Logger.getLogger(MulticastReceiveAndPlay.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        datagramPacket = new DatagramPacket(new byte[this.packetSize], this.packetSize);
        
        while(true) {
            try {
                multicastSocket.receive(datagramPacket);
                voicePacket = VoicePacket.deserialize(datagramPacket.getData());
                this.player.play(voicePacket.getPayload());
            } catch (IOException ex) {
                System.out.println(ex + ": ReceiveAndPlay thread.run()");
            }           
        }
    }   
}
