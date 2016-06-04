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
class In_callChatApplicationStatusOperations implements ChatApplicationStatusOperations {
    @Override
    public ChatApplicationStatus dial(ChatApplication chatApplication, String ip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChatApplicationStatus calling(ChatApplication chatApplication, String ip) {
        // say the caller that the callee is busy
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket;
        InetAddress ipAddress;
        InetAddress ipLocalHost;
        String header = "busy";
        try {
            ipLocalHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no internet connectivity: In_callChatApplicationStatusOperations.accepted()");
            return ChatApplicationStatus.IDLE;
        }
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.IN_CALL;
        }
        byte[] payload = ipLocalHost.toString().getBytes();
        Message msg = new Message(header, payload);
        byte[] outBuf = Message.serialize(msg);
        datagramPacket = new DatagramPacket(outBuf, outBuf.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.IN_CALL;
        }
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ChatApplicationStatus.IN_CALL;
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
        DatagramPacket datagramPacket;
        InetAddress ipAddress;
        InetAddress ipLocalHost;
        String header = "busy";
        try {
            ipLocalHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no internet connectivity: In_callChatApplicationStatusOperations.accepted()");
            return ChatApplicationStatus.IDLE;
        }
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        byte[] payload = ipLocalHost.toString().getBytes();
        Message msg = new Message(header, payload);
        byte[] outBuf = Message.serialize(msg);
        datagramPacket = new DatagramPacket(outBuf, outBuf.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.INCOMMING_CALL;
        }
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            datagramSocket.close();
        }
        datagramSocket.close();
        return ChatApplicationStatus.IN_CALL;
    }

    @Override
    public ChatApplicationStatus notAccepted(ChatApplication chatApplication) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ChatApplicationStatus busy(ChatApplication chatApplication) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus accept(ChatApplication chatApplication) {
        return ChatApplicationStatus.IN_CALL;
    }

    @Override
    public ChatApplicationStatus reject(ChatApplication chatApplication, String ip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChatApplicationStatus end(ChatApplication chatApplication, String ip) {
        DatagramSocket datagramSocket;
        DatagramPacket datagramPacket;
        InetAddress ipAddress;
        InetAddress ipLocalHost;
        String header = "end";
        try {
            ipLocalHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("no internet connectivity: In_callChatApplicationStatusOperations.accepted()");
            return ChatApplicationStatus.IDLE;
        }
        ipAddress = ChatApplication.getPeer();
        byte[] payload = ipLocalHost.toString().getBytes();
        Message msg = new Message(header, payload);
        byte[] outBuf = Message.serialize(msg);
        datagramPacket = new DatagramPacket(outBuf, outBuf.length, ipAddress, ChatApplication.getPORT());
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ChatApplicationStatus.IDLE;
        }
        try {
            datagramSocket.send(datagramPacket);
        } catch (IOException ex) {
            //Logger.getLogger(In_callChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            datagramSocket.close();
        }
        datagramSocket.close();
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
