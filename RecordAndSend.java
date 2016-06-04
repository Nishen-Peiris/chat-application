/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author nishen
 */
public class RecordAndSend extends Thread {  
    private final InetAddress ip;
    private final int port;
    public final int packetSize;
    private final Recorder recorder;

    RecordAndSend(InetAddress ip, int port, int packetSize) {
        this.ip = ip;
        this.port = port;
        this.packetSize = packetSize;
        this.recorder = new Recorder();
    }
    
    @Override
    public void run() {
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            System.out.println(ex + ": RecordAndSend.run()");
            System.exit(0);
        }
        String header = "voice";
        int sequenceNo = 0;
        DatagramPacket datagramPacket;
        VoicePacket voicePacket;
        byte[] paylaod;
        byte[] outBuf;
        while(true) {
            try {
                paylaod = new byte[this.packetSize];
                this.recorder.record(paylaod);
                voicePacket = new VoicePacket(header, sequenceNo, paylaod);
                outBuf = VoicePacket.serialize(voicePacket);
                datagramPacket = new DatagramPacket(outBuf, outBuf.length, this.ip, this.port);
                datagramSocket.send(datagramPacket);
            } catch (SocketException ex) {
                System.out.println(ex + ": RecordAndSend thread.run()");
            } catch (IOException ex) {
                System.out.println(ex + ": RecordAndSend thread.run()");
            }
            sequenceNo++;
        }
        
//        String header = "voice";
//        int sequenceNo = 0;
//        byte[] payload = new byte[ChatApplication.getVOICEPACKETSIZE()-header.length()-Integer.SIZE-((ChatApplication.getVOICEPACKETSIZE()-header.length()-Integer.SIZE)%4)];
//        System.out.println("voice size: " + payload.length);
//        while(true) {
//            int count = recorder.record(payload);
//            
//            // Create voice packet, serialize it
//            voicePacket = new VoicePacket(header, sequenceNo, payload);
//            byte[] voice = VoicePacket.serialize(voicePacket);
//            
//            // Send the recording to the peer.
//                DatagramPacket packet = new DatagramPacket(voice, voice.length, ip, port);
//            try {
//                datagramSocket.send(packet);
//                sequenceNo++;
//            } catch (IOException ex) {
//                //Logger.getLogger(RecordAndSend.class.getName()).log(Level.SEVERE, null, ex);
//                System.out.println("Can't send packet: RecordAndSend thread.run()");
//            }
//            sequenceNo++;
//        }
    }  
}
