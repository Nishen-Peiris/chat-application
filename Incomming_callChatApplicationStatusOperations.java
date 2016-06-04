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
class Incomming_callChatApplicationStatusOperations implements ChatApplicationStatusOperations {
    @Override
    public ChatApplicationStatus dial(ChatApplication chatApplication, String ip) {
        System.out.println("operation not supported.");
        return ChatApplicationStatus.INCOMMING_CALL;
    }

    @Override
    public ChatApplicationStatus calling(ChatApplication chatApplication, String ip) {
        // say the caller that the callee is busy
        DatagramSocket datagramSocket;
        InetAddress ipLocalHost;
        InetAddress ipAddress;
        String header = "busy";
        try {
            ipLocalHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            // Logger.getLogger(Incomming_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no internet connectivity: Incomming_callChatApplicationStatusOperations.calling()");
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        byte[] payload = ipLocalHost.toString().getBytes();
        Message msg = new Message(header, payload);
        byte[] arr = Message.serialize(msg);
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        DatagramPacket datagramPacket = new DatagramPacket(arr, arr.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't create datagram socket: Incomming_callChatApplicationStatus.calling()");
            return ChatApplicationStatus.DIALLING;
        }
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        
        return ChatApplicationStatus.INCOMMING_CALL;
    }

    @Override
    public ChatApplicationStatus cancel(ChatApplication chatApplication) {
        chatApplication.end(ChatApplication.getPeer().toString());
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus accepted(ChatApplication chatApplication, String ip) {
        // say the caller that the callee is busy
        DatagramSocket datagramSocket;
        InetAddress ipAddress;
        InetAddress ipLocalHost;
        try {
            // say caller that the callee is busy
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't create datagram socket: Incomming_callChatApplicationStatus.accepted()");
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        String header = "busy";     
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        try {
            ipLocalHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            // Logger.getLogger(Incomming_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no internet connectivity.");
            return ChatApplicationStatus.IDLE;
        }
        byte[] payload = ipLocalHost.toString().getBytes();
        Message msg = new Message(header, payload);
        byte[] outBuf = Message.serialize(msg);
        DatagramPacket datagramPacket = new DatagramPacket(outBuf, outBuf.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't send the packet: Incomming_callChatApplicationStatus.accepted");
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        
        return ChatApplicationStatus.INCOMMING_CALL;
    }

    @Override
    public ChatApplicationStatus notAccepted(ChatApplication chatApplication) {
        return ChatApplicationStatus.INCOMMING_CALL;
    }
    
    @Override
    public ChatApplicationStatus busy(ChatApplication chatApplication) {
        return ChatApplicationStatus.INCOMMING_CALL;
    }

    @Override
    public ChatApplicationStatus accept(ChatApplication chatApplication) {
        // notify the peer of answering
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            String header = "accept";
            byte[] payload = ChatApplication.findLocalHostIP().getBytes();
            Message msg = new Message(header, payload);
            byte[] data = Message.serialize(msg);
            InetAddress ipAddress = ChatApplication.getPeer();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, ipAddress, ChatApplication.getPORT());
            datagramSocket.send(datagramPacket);
        } catch (SocketException | UnknownHostException ex) {
            //Logger.getLogger(IdleChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex + ": Incomming_callChatApplicationOperations.accept()");
        } catch (IOException ex) {
            //Logger.getLogger(IdleChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex + ": Incomming_callChatApplicationOperations.accept()");
        }
        
        // start the conversation
        InetAddress ipAddress;
        // start the conversation
        chatApplication.setReceiveAndPlay(new ReceiveAndPlay(ChatApplication.getCALLPORT()));
        chatApplication.getReceiveAndPlay().start();

        ipAddress = ChatApplication.getPeer();
        chatApplication.setRecordAndSend(new RecordAndSend(ipAddress, ChatApplication.getCALLPORT(), ChatApplication.getVOICEPACKETSIZE()));
        chatApplication.getRecordAndSend().start();
        System.out.println("in call...");
        
        return ChatApplicationStatus.IN_CALL;
    }

    @Override
    public ChatApplicationStatus reject(ChatApplication chatApplication, String ip) {
        System.out.println("call rejected.");
        
        // say the caller that the callee is busy
        DatagramSocket datagramSocket;
        try {
            // say caller that the callee is busy
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't create datagram socket: Incomming_callChatApplicationStatus.reject()");
            return ChatApplicationStatus.IDLE;
        }
        String header = "busy";
        byte[] payload;
        try {
            payload = InetAddress.getLocalHost().toString().getBytes();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(Incomming_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no internet connectivity.");
            return ChatApplicationStatus.IDLE;
        }
        Message msg = new Message(header, payload);
        byte[] arr = Message.serialize(msg);
        InetAddress ipAddress;
        ipAddress = ChatApplication.getPeer();
        DatagramPacket datagramPacket = new DatagramPacket(arr, arr.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            // Logger.getLogger(DiallingChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("can't send the packet: Incomming_callChatApplicationStatus.accepted");
            return ChatApplicationStatus.IDLE;
        }
        
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus end(ChatApplication chatApplication, String ip) {
        chatApplication.reject(ip);
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
