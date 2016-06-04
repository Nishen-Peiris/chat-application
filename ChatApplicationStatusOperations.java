/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication;

/**
 *
 * @author nishen
 */
public interface ChatApplicationStatusOperations {
    ChatApplicationStatus dial(ChatApplication chatApplication, String ip);
    ChatApplicationStatus callConference(ChatApplication chatApplication, String multicastIP, String[] peerArray);
    ChatApplicationStatus callConferenceReceiving(ChatApplication chatApplication, String ip, String multicastIP);
    ChatApplicationStatus calling(ChatApplication chatApplication, String ip);
    ChatApplicationStatus cancel(ChatApplication chatApplication);    
    ChatApplicationStatus accepted(ChatApplication chatApplication, String ip);
    ChatApplicationStatus notAccepted(ChatApplication chatApplication);
    ChatApplicationStatus busy(ChatApplication chatApplication);
    ChatApplicationStatus accept(ChatApplication chatApplication);
    ChatApplicationStatus reject(ChatApplication chatApplication, String ip);
    ChatApplicationStatus end(ChatApplication chatApplication, String ip);
}
