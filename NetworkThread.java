/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author nishen
 */
class NetworkThread extends Thread {
    private final ChatApplication chatApplication;
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private final int packetSize = 500;
    
    NetworkThread(ChatApplication chatApplication) {
        this.chatApplication = chatApplication;
    }

    @Override
    public void run() {
        try {
            datagramSocket = new DatagramSocket(ChatApplication.getPORT());
        } catch (SocketException ex) {
            System.out.println(ex + ": NetworkThread.run()");
            System.out.println("exiting...");
            //socket can't be created. no point of continuing further
            System.exit(0);
        }
        String header;
        Message msg;
        byte[] inBuf;
        byte[] data;
        while(true) {
            try {
                inBuf = new byte[this.packetSize];
                datagramPacket = new DatagramPacket(inBuf, inBuf.length);
                datagramSocket.receive(datagramPacket);
                data = datagramPacket.getData();
                msg = Message.deserialize(data);
                header = msg.getHeader();
                
                switch(header) {
                    //someone is calling
                    case "call":                
                        chatApplication.calling(new String(msg.getPayload()));
                        break;
                    //peer has accepted the call
                    case "accept":
                        chatApplication.accepted(new String(msg.getPayload()));
                        break;
                    //peer rejected the call
                    case "reject":
                        chatApplication.reject(new String(msg.getPayload()));
                        break;
                    //peer ended the call
                    case "end":
                        chatApplication.end(new String(msg.getPayload()));
                        break;
                    //someone is making a conference call
                    case "callConference":
                        String[] str = (new String(msg.getPayload())).split(" ");                        
                        chatApplication.callConferenceReceiving(str[0], str[1]);
                        break;
                    case "busy":
                        break;
                }
            } catch (SocketException | UnknownHostException ex) {
                System.out.println(ex + ": NetworkThread.run()");
            } catch (IOException ex) {
                System.out.println(ex + ": NetworkThread.run()");
            }
        }
    }
}
