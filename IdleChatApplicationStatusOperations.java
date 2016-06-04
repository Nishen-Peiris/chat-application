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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nishen
 */
class IdleChatApplicationStatusOperations implements ChatApplicationStatusOperations {
    @Override
    public ChatApplicationStatus dial(ChatApplication chatApplication, String ip) {       
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            String header = "call";
            byte[] payload = ChatApplication.findLocalHostIP().getBytes();
            Message msg = new Message(header, payload);
            byte[] data = Message.serialize(msg);
            InetAddress ipAddress = InetAddress.getByName(ip);
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, ipAddress, ChatApplication.getPORT());
            datagramSocket.send(datagramPacket);
            System.out.println("calling " + ip);
            final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                // call timeout method
                chatApplication.notAccepted();
                }
            }, 20, 1, TimeUnit.SECONDS);
            return ChatApplicationStatus.DIALLING;
        } catch (SocketException | UnknownHostException ex) {
            //Logger.getLogger(IdleChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex + ": IdleChatApplicationOperations.dial()");
        } catch (IOException ex) {
            //Logger.getLogger(IdleChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex + ": IdleChatApplicationOperations.dial()");
        }
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus calling(ChatApplication chatApplication, String ip) {
        try {
            ChatApplication.setPeer(InetAddress.getByName(ip));
        } catch (UnknownHostException ex) {
            //Logger.getLogger(IdleChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(ip + " calling...");
        System.out.println("accept: [y/n]");
        // start a timer to check for timeout for the receiving call. user has to either accept of reject the call
        // within that timeout. otherwise the call will be automatically disconnected.
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
            // call timeout method
            chatApplication.notAccepted();
            }
        }, 0, 20, TimeUnit.SECONDS);
        return ChatApplicationStatus.INCOMMING_CALL;
    }

    @Override
    public ChatApplicationStatus cancel(ChatApplication chatApplication) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus accepted(ChatApplication chatApplication, String ip) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus notAccepted(ChatApplication chatApplication) {
        return ChatApplicationStatus.IDLE;
    }
    
    @Override
    public ChatApplicationStatus busy(ChatApplication chatApplication) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus accept(ChatApplication chatApplication) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus reject(ChatApplication chatApplication, String ip) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus end(ChatApplication chatApplication, String ip) {
        return ChatApplicationStatus.IDLE;
    }

    @Override
    public ChatApplicationStatus callConference(ChatApplication chatApplication, String multicastIP, String[] peerArray) {
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getByName(multicastIP);
            DatagramSocket datagramSocket = new DatagramSocket();
            String header = "callConference";
            byte[] payload = (ChatApplication.findLocalHostIP() + " " + multicastIP).getBytes();
            Message msg = new Message(header, payload);
            byte[] data = Message.serialize(msg);
            
            for (String peerArray1 : peerArray) {
                ipAddress = InetAddress.getByName(peerArray1);
                DatagramPacket datagramPacket = new DatagramPacket(data, data.length, InetAddress.getByName(peerArray1), ChatApplication.getPORT());
                datagramSocket.send(datagramPacket);
            }
            
            // start the conversation
            chatApplication.setMulticastReceiveAndPlay(new MulticastReceiveAndPlay(InetAddress.getByName(multicastIP), ChatApplication.getMULTICASTPORT()));
            chatApplication.getMulticastReceiveAndPlay().start();
            
            chatApplication.setRecordAndSend(new RecordAndSend(ipAddress, ChatApplication.getMULTICASTPORT(), ChatApplication.getVOICEPACKETSIZE()));
            chatApplication.getRecordAndSend().start();
            
            System.out.println("conferencing at " + multicastIP);
        } catch (SocketException | UnknownHostException ex) {
            System.out.println(ex + ": IdleChatApplicationOperations.callConference()");
        } catch (IOException ex) {
            System.out.println(ex + ": IdleChatApplicationOperations.callConference()");
        }
        return ChatApplicationStatus.IN_CONFERENCECALL;
    }

    @Override
    public ChatApplicationStatus callConferenceReceiving(ChatApplication chatApplication, String ip, String multicastIP) {
        System.out.println("conference call from " + ip);
        System.out.println("accepted");
        // start the conversation
        try {
            chatApplication.setMulticastReceiveAndPlay(new MulticastReceiveAndPlay(InetAddress.getByName(multicastIP), ChatApplication.getMULTICASTPORT()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(IdleChatApplicationStatusOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        chatApplication.getMulticastReceiveAndPlay().start();
        try {
            chatApplication.setRecordAndSend(new RecordAndSend(InetAddress.getByName(multicastIP), ChatApplication.getMULTICASTPORT(), ChatApplication.getVOICEPACKETSIZE()));
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        }
        chatApplication.getRecordAndSend().start();
        return ChatApplicationStatus.IN_CONFERENCECALL;
    }
}
