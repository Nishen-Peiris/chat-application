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
import java.net.UnknownHostException;

/**
 *
 * @author nishen
 */
class DiallingChatApplicationStatusOperations implements ChatApplicationStatusOperations {
    @Override
    public ChatApplicationStatus dial(ChatApplication chatApplication, String ip) {
        System.out.println("operation not supported.");
        return ChatApplicationStatus.DIALLING;
    }

    @Override
    public ChatApplicationStatus calling(ChatApplication chatApplication, String ip) {
        DatagramSocket datagramSocket;
        try {
            // say caller that the callee is busy
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't create datagram socket: DiallingChatApplicationStatus.calling()");
            return ChatApplicationStatus.DIALLING;
        }
        String header = "busy";
        Message msg = new Message(header);
        byte[] arr = Message.serialize(msg);
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            datagramSocket.close();
            return ChatApplicationStatus.DIALLING;
        }
        DatagramPacket datagramPacket = new DatagramPacket(arr, arr.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            datagramSocket.close();
            return ChatApplicationStatus.DIALLING;
        }
        datagramSocket.close();
        return ChatApplicationStatus.DIALLING;
    }

    @Override
    public ChatApplicationStatus cancel(ChatApplication chatApplication) {
        System.out.println("call ended.");
        return ChatApplicationStatus.IDLE;
    }
    
        
    @Override
    public ChatApplicationStatus busy(ChatApplication chatApplication) {
        System.out.println("user busy.");
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus accepted(ChatApplication chatApplication, String ip) {
        System.out.println("in call...");
        InetAddress ipAddress;
        // start the conversation
        chatApplication.setReceiveAndPlay(new ReceiveAndPlay(ChatApplication.getCALLPORT()));
        chatApplication.getReceiveAndPlay().start();

        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            return ChatApplicationStatus.IN_CALL;
        }
        chatApplication.setRecordAndSend(new RecordAndSend(ipAddress, ChatApplication.getCALLPORT(), ChatApplication.getVOICEPACKETSIZE()));
        chatApplication.getRecordAndSend().start();
        return ChatApplicationStatus.IN_CALL;
    }

    @Override
    public ChatApplicationStatus notAccepted(ChatApplication chatApplication) {
        System.out.println("not answered.");
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus accept(ChatApplication chatApplication) {
        return ChatApplicationStatus.DIALLING;
    }

    @Override
    public ChatApplicationStatus reject(ChatApplication chatApplication, String ip) {
        return ChatApplicationStatus.DIALLING;
    }

    @Override
    public ChatApplicationStatus end(ChatApplication chatApplication, String ip) {
        chatApplication.cancel();
        return ChatApplicationStatus.IDLE;
    }    

    @Override
    public ChatApplicationStatus callConference(ChatApplication chatApplication, String multicastIP, String[] peerArray) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChatApplicationStatus callConferenceReceiving(ChatApplication chatApplication, String ip, String multicastIP) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
