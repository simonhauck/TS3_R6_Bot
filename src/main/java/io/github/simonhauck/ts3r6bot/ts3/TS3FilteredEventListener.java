package io.github.simonhauck.ts3r6bot.ts3;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TS3FilteredEventListener implements TS3EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(TS3FilteredEventListener.class);

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // TS3EventListener methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * default implementation
     *
     * @param clientJoinEvent can not be {@code null}
     */
    @Override
    public void onClientJoin(ClientJoinEvent clientJoinEvent) {

    }

    /**
     * default implementation
     *
     * @param textMessageEvent can not be {@code null}
     */
    @Override
    public void onTextMessageReceived(TextMessageEvent textMessageEvent) {

    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
