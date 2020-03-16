package io.github.simonhauck.ts3r6bot.service.chatbot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import io.github.simonhauck.ts3r6bot.model.ts3.TS3User;
import io.github.simonhauck.ts3r6bot.persistence.UserServicePersistence;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterUser implements ITextCommand {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUser.class);

    private UserServicePersistence _persistence;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * initialize the user service persistence
     *
     * @param persistence can not be {@code null}
     */
    public RegisterUser(UserServicePersistence persistence) {
        assert persistence != null;
        _persistence = persistence;
    }

    //------------------------------------------------------------------------------------------------------------------
    // ITextCommand methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String getStartingCommand() {
        return "!register";
    }

    @Override
    public String getExplanation() {
        return "Register a user for the rainbow 6 siege ranks. Command: !register <Firstname> <Lastname> <UplayName>\n" +
                "Register a other user for the rainbow6 siege ranks: !register <Firstname> <Lastname> <UplayName> <tsName>";
    }

    @Override
    public boolean isCommandValid(TextMessageEvent event) {
        String message = event.getMessage();

        int length = message.split(" ").length;
        return length == 4 || length == 5;
    }

    @Override
    public void onMessageReceived(TextMessageEvent event, TS3Service service) {
        String message = event.getMessage();
        String[] splittedMessage = message.split(" ");

        String uniqueIdentifierOfNewUser = event.getInvokerUniqueId();
        //Name of ts3User was passed, update uniqueIdentifierOfNewUser
        if (splittedMessage.length == 5) {
            Client loadedClient = service.getTs3Api().getClientByNameExact(splittedMessage[4], true);
            //Check if client was found
            if (loadedClient == null) {
                LOG.warn("Provided User not found, Register command failed! Calling User: " +
                        event.getInvokerName() + ", Provided String: " + splittedMessage[4]);
                service.getTs3AsyncApi().sendServerMessage("User with name " + splittedMessage[4] + " not found");
                return;
            } else {
                uniqueIdentifierOfNewUser = loadedClient.getUniqueIdentifier();
            }
        }

        TS3User user = new TS3User(uniqueIdentifierOfNewUser, splittedMessage[3], splittedMessage[1], splittedMessage[2]);
        LOG.info("Storing user: " + user);
        _persistence.insertUpdateUserWithTeamspeakUID(user);
        service.getTs3AsyncApi().sendServerMessage("User data stored successfully for " + user);
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
