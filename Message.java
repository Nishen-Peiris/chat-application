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
public class Message implements Serializable {
    private final String header;
    private final byte[] payload;
    
    public Message(String header) {
        this.header = header;
        this.payload = null;
    }
    
    public Message(String header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public String getHeader() {
        return header;
    }
    
    public byte[] getPayload() {
        return payload;
    }
    
    public static byte[] serialize(Message msg) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(msg);
            }
            return baos.toByteArray();
        } catch(Exception ex) {
            System.out.println("Can't serialize the message: Message.serialize()");
            return null;
        }
    }
    
    public static Message deserialize(byte[] buffer) {
        try {
            ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
            ObjectInputStream oos = new ObjectInputStream(baos);
            return (Message)oos.readObject();
        } catch(IOException | ClassNotFoundException ex) {
            System.out.println("can't deserialize the message: Message.deserialize()");
        }
        return null;
    }
    
    /*
     * unit test for message serialization and deserialization
     */
    public static void main(String[] args) {
        String header = "call";
        byte[] payload = "Hello...".getBytes();
        Message msg = new Message(header, payload);
        System.out.println("message instance created.");
        
        byte[] arr = Message.serialize(msg);
        System.out.println("message serialized.");
        
        Message msgR = Message.deserialize(arr);
        System.out.println("message deserialized.");
        
        System.out.println("message header: " + msgR.getHeader());
        System.out.println("message payload: " + new String(msgR.getPayload()));
        
    }
}