/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author nishen
 */
public class ChatApplication {
    private static final int PORT = 2000; //port to control message exchange
    private static final int CALLPORT = 2001; //port for single peer calls
    private static final int MULTICASTPORT = 8888; // prot for multicast calls
    private static final int VOICEPACKETSIZE = 200;
    private static InetAddress peer = null;
    private static InetAddress multicastIP;

    public static int getMULTICASTPORT() {
        return MULTICASTPORT;
    }

    public static void setMulticastIP(InetAddress aMulticastIP) {
        multicastIP = aMulticastIP;
    }

    private ChatApplicationStatus status = ChatApplicationStatus.IDLE;
    private ConsoleThread consoleThread;
    private NetworkThread networkThread;
    private RecordAndSend recordAndSend;
    private ReceiveAndPlay receiveAndPlay;
    private MulticastReceiveAndPlay multicastReceiveAndPlay;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        ChatApplication chatApplication = new ChatApplication();

        chatApplication.consoleThread = new ConsoleThread(chatApplication);
        chatApplication.consoleThread.start();
        
        chatApplication.networkThread = new NetworkThread(chatApplication);
        chatApplication.networkThread.start();
    }

    public MulticastReceiveAndPlay getMulticastReceiveAndPlay() {
        return multicastReceiveAndPlay;
    }

    public static int getPORT() {
        return PORT;
    }
    
    public static int getCALLPORT() {
        return CALLPORT;
    }
    
    public static int getVOICEPACKETSIZE() {
        return VOICEPACKETSIZE;
    }
    
    public static InetAddress getPeer() {
        return peer;
    }

    
    public static void setPeer(InetAddress peer) {
        ChatApplication.peer = peer;
    }
    
    public void setRecordAndSend(RecordAndSend recordAndSend) {
        this.recordAndSend = recordAndSend;
    }

    public void setReceiveAndPlay(ReceiveAndPlay receiveAndPlay) {
        this.receiveAndPlay = receiveAndPlay;
    }
    
    public RecordAndSend getRecordAndSend() {
        return this.recordAndSend;
    }

    public ReceiveAndPlay getReceiveAndPlay() {
        return this.receiveAndPlay;
    }
    
    public void dial(String ip) {
        setStatus(status.dial(this, ip));
    }
    
    void callConference(String multicastIP, String[] peerArray) {
        setStatus(status.callConference(this, multicastIP, peerArray));
    }
    
    void callConferenceReceiving(String ip, String multicastIP) {
        setStatus(status.callConferenceReceiving(this, ip, multicastIP));
    }
    
    public void calling(String ip) {
        setStatus(status.calling(this, ip));
    }

    public void cancel() {
        setStatus(status.cancel(this));
    }

    public void busy() {
        setStatus(status.busy(this));
    }    
    
    public void accepted(String ip) {
        setStatus(status.accepted(this, ip));
    }    
    
    public void notAccepted() {
        setStatus(status.notAccepted(this));
    }
        
    public void accept() {
        setStatus(status.accept(this));
    }
    
    public void reject(String ip) {
        setStatus(status.reject(this, ip));
    }    
    
    public void end(String ip) {
        setStatus(status.end(this, ip));
    }
    
    public void setStatus(ChatApplicationStatus status) {
        if (status != null && status != this.status) {
        this.status = status;
        }
    }    
    
    public static String findLocalHostIP() {
        try {
            NetworkInterface ni = NetworkInterface.getByName("wlan0");
            Enumeration<InetAddress> inetAddresses =  ni.getInetAddresses();
            
            while(inetAddresses.hasMoreElements()) {
                InetAddress ia = inetAddresses.nextElement();
                if(!ia.isLinkLocalAddress()) {
                    return ia.getHostAddress();
                }         
            }
            return null;
        } catch (SocketException ex) {
            System.out.println(ex + ": ChatApplication.main()");
            System.out.println("by default, application is configured to use wlan0 interface. If you are using any other interface, please configure it at line 146 of ChatApplication.java");
            return null;
        }
    }
    
    public static InetAddress getMulticastIP() {
        return ChatApplication.multicastIP;
    }

    public void setMulticastReceiveAndPlay(MulticastReceiveAndPlay multicastReceiveAndPlay) {
        this.multicastReceiveAndPlay = multicastReceiveAndPlay;
    }
}