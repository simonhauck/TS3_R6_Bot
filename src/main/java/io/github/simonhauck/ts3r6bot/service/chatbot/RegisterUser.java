package io.github.simonhauck.ts3r6bot.service.chatbot;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
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
        return "Register a user for the rainbow 6 siege ranks. Command: !register <Firstname> <Lastname> <UplayName>";
    }

    @Override
    public boolean isCommandValid(TextMessageEvent event) {
        String message = event.getMessage();

        int length = message.split(" ").length;
        return length == 4;
    }

    @Override
    public void onMessageReceived(TextMessageEvent event, TS3Service service) {
        String message = event.getMessage();
        String[] splittedMessage = message.split(" ");

        TS3User user = new TS3User(event.getInvokerUniqueId(), splittedMessage[3], splittedMessage[1], splittedMessage[2]);
        LOG.info("Storing user: " + user);
        _persistence.insertUpdateUserWithTeamspeakUID(user);
        service.getTs3AsyncApi().sendServerMessage("User data stored successfully");
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
