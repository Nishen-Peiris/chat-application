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
public enum ChatApplicationStatus implements ChatApplicationStatusOperations {
    IDLE(new IdleChatApplicationStatusOperations()),
    DIALLING(new DiallingChatApplicationStatusOperations()),
    INCOMMING_CALL(new Incomming_callChatApplicationStatusOperations()),
    IN_CALL(new In_callChatApplicationStatusOperations()),
    IN_CONFERENCECALL(new In_conferenceCallChatApplicationStatusOperations());
    
    private final ChatApplicationStatusOperations operations;

    ChatApplicationStatus(ChatApplicationStatusOperations operations) {
       this.operations = operations;
    }

    @Override
    public ChatApplicationStatus dial(ChatApplication chatApplication, String ip) {
        return operations.dial(chatApplication, ip);
    }

    @Override
    public ChatApplicationStatus calling(ChatApplication chatApplication, String ip) {
        return operations.calling(chatApplication, ip);
    }

    @Override
    public ChatApplicationStatus cancel(ChatApplication chatApplication) {
        return operations.cancel(chatApplication);
    }

    @Override
    public ChatApplicationStatus busy(ChatApplication chatApplication) {
        return operations.busy(chatApplication);
    }    
    
    @Override
    public ChatApplicationStatus accepted(ChatApplication chatApplication, String ip) {
        return operations.accepted(chatApplication, ip);
    }

    @Override
    public ChatApplicationStatus notAccepted(ChatApplication chatApplication) {
        return operations.notAccepted(chatApplication);
    }

    @Override
    public ChatApplicationStatus accept(ChatApplication chatApplication) {
        return operations.accept(chatApplication);
    }

    @Override
    public ChatApplicationStatus reject(ChatApplication chatApplication, String ip) {
        return operations.reject(chatApplication, ip);
    }

    @Override
    public ChatApplicationStatus end(ChatApplication chatApplication, String ip) {
        return operations.end(chatApplication, ip);
    }

    @Override
    public ChatApplicationStatus callConference(ChatApplication chatApplication, String multicastIP, String[] peerArray) {
        return operations.callConference(chatApplication, multicastIP, peerArray);
    }

    @Override
    public ChatApplicationStatus callConferenceReceiving(ChatApplication chatApplication, String ip, String multicastIP) {
        return operations.callConferenceReceiving(chatApplication, ip, multicastIP);
    }
    
    
}
