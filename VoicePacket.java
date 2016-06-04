/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author nishen
 */
public class VoicePacket implements Serializable {
    private String header;
    private int sequenceNo;
    private byte[] payload;

    public VoicePacket(String header, int sequenceNo, byte[] payload) {
        this.header = header;
        this.sequenceNo = sequenceNo;
        this.payload = payload;
    }
    
    public byte[] getPayload() {
        return payload;
    }

    public String getHeader() {
        return header;
    }

    public int getSequenceNo() {
        return sequenceNo;
    }
    
    public static byte[] serialize(VoicePacket voicePacket) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(voicePacket);
            }
            return baos.toByteArray();
        } catch(Exception ex) {
            System.out.println("Can't serialize the voice packet: VoicePacket.serialize()");
            return null;
        }
    }
    
    public static VoicePacket deserialize(byte[] payload) {
        try {
            ByteArrayInputStream baos = new ByteArrayInputStream(payload);
            ObjectInputStream oos = new ObjectInputStream(baos);
            return (VoicePacket)oos.readObject();
        } catch(IOException | ClassNotFoundException ex) {
            System.out.println("can't deserialize the voice packet: VoicePacket.deserialize()");
        }
        return null;
    }
    
    public static void main(String[] args) {
        Recorder recorder = new Recorder();
        Player player = new Player();
        VoicePacket voicePacketS;
        VoicePacket voicePacketR;
        byte[] payload = new byte[200];
        String header = "voice";
        int sequenceNo = 0;
        
        while(true) {
            recorder.record(payload);
            voicePacketS = new VoicePacket(header, sequenceNo, payload);
            byte[] arr = VoicePacket.serialize(voicePacketS);
            
            voicePacketR = VoicePacket.deserialize(arr);
            System.out.println(voicePacketR.getHeader() + " - " + voicePacketR.getSequenceNo() + " - playing...");
            player.play(voicePacketR.getPayload());
            
            sequenceNo++;
        }
    }
}
