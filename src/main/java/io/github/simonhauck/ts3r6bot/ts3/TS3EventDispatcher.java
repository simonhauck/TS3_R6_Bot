package io.github.simonhauck.ts3r6bot.ts3;

import com.github.theholywaffle.teamspeak3.api.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

class TS3EventDispatcher implements ITS3EventDispatcher, TS3Listener {

    private static final Logger LOG = LoggerFactory.getLogger(TS3EventDispatcher.class);

    private Set<TS3EventListener> _eventListeners;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * create a {@link TS3EventDispatcher} with an empty list of {@link TS3EventListener} objects.
     */
    public TS3EventDispatcher() {
        _eventListeners = new HashSet<>();
    }

    //------------------------------------------------------------------------------------------------------------------
    // ITS3EventDispatcher methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * register the listener for events
     *
     * @param listener can not be {@code null}
     */
    @Override
    public void registerEventListener(TS3EventListener listener) {
        assert listener != null;
        _eventListeners.add(listener);
    }

    /**
     * unregister the lister from the events
     *
     * @param listener can not be {@code null}
     */
    @Override
    public void unregisterEventListener(TS3EventListener listener) {
        assert listener != null;
        _eventListeners.remove(listener);
    }

    //------------------------------------------------------------------------------------------------------------------
    // TS3Listener methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onTextMessage(TextMessageEvent textMessageEvent) {
        sendAction(x -> x.onTextMessageReceived(textMessageEvent));
    }

    @Override
    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        sendAction(x -> x.onClientJoin(clientJoinEvent));
    }

    @Override
    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

    }

    @Override
    public void onServerEdit(ServerEditedEvent serverEditedEvent) {

    }

    @Override
    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

    }

    @Override
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

    }

    @Override
    public void onClientMoved(ClientMovedEvent clientMovedEvent) {

    }

    @Override
    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

    }

    @Override
    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

    }

    @Override
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

    }

    @Override
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * send a method to all clients
     *
     * @param action that should be executed. Can not be {@code null}
     */
    private void sendAction(Action action) {
        assert action != null;
        for (TS3EventListener listener : _eventListeners) {
            action.action(listener);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------

    private interface Action {

        void action(TS3EventListener lister);
    }
}
