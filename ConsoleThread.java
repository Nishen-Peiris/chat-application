/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

import java.util.Scanner;

/**
 *
 * @author nishen
 */
class ConsoleThread extends Thread {
    private final ChatApplication chatApplication;
    Scanner scanner;

    ConsoleThread(ChatApplication chatApplication) {
        this.chatApplication = chatApplication;
    }
    
    @Override
    public void run() {
        // print initial instructions to the user to continue
        printInstructions();
        
        this.scanner = new Scanner(System.in);
        
        while(true) {
            String str = scanner.nextLine();
            String[] arr = str.split(" ");
            
            switch (arr[0]) {
                case "call":
                    if(arr.length == 2) {
                        chatApplication.dial(arr[1]);
                    } else if(arr.length > 2) {
                        System.out.println("too many arguments for call.");
                    } else if(arr.length < 2) {
                        System.out.println("too few arguments for call.");
                    }
                    break;
                case "call_conference":
                    if(arr.length < 3) {
                        System.out.println("too few arguments for call.");
                    } else {
                        int noOfPeers = arr.length - 2;
                        String[] peerArray = new String[noOfPeers];
                        System.arraycopy(arr, 2, peerArray, 0, noOfPeers);
                        chatApplication.callConference(arr[1], peerArray);
                    }
                    break;
                case "y":
                    chatApplication.accept();
                    break;
                case "n":
                    
                    break;
                case "cancel":
                    break;
            }
        }
    }
    
    // print initial instructions to the user to continue
    public void printInstructions() {
        System.out.println("by default, application is configured to use wlan0 interface. If you are using any other interface, please configure it at line 146 of ChatApplication.java");
        System.out.println("chat application started.");
        System.out.println("to call: call <ip>");
        System.out.println("to call conference: call_conference <udp multicast ip> <ip> <ip>...");
    }
}
