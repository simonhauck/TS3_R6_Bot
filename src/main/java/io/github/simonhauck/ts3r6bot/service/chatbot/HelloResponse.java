package io.github.simonhauck.ts3r6bot.service.chatbot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloResponse implements ITextCommand {

    private static final Logger LOG = LoggerFactory.getLogger(HelloResponse.class);

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // ITextCommand methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getStartingCommand() {
        return "!hello";
    }

    @Override
    public String getExplanation() {
        return "The bot says hello: Command: !hello";
    }

    @Override
    public boolean isCommandValid(TextMessageEvent event) {
        return true;
    }

    /**
     * send a text in the server chat
     *
     * @param event can not be {@code null}
     */
    @Override
    public void onMessageReceived(TextMessageEvent event, TS3Service service) {
        service.getTs3AsyncApi().sendServerMessage("Hello, i am the bot guarding this TeamSpeak server!");
//        service.sendChannelMessageWithClientUIDAsync(event.getInvokerUniqueId(), "Hello, i am the bot guarding this TeamSpeak server!");
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
