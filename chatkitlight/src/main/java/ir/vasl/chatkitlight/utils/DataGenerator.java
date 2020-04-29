package ir.vasl.chatkitlight.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationStatus;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;

public class DataGenerator {

    private static String[] title = {
            "Lorem ipsum",
            "placeholder",
            "commonly",
            "graphic",
            "print",
            "publishing",
            "industries",
            "previewing",
            "visual",
            "mockups",
            "mollit",
            "labore"
    };

    private static String[] messages = {
            "Lorem ipsum, or lipsum as it is sometimes known",
            "is dummy text used in laying out print, graphic or web designs",
            "The passage is attributed to an unknown typesetter in the th century ",
            "who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book",
            "The purpose of lorem ipsum is to create a natural looking block of text",
            "laying out pages with meaningless filler text can be very useful",
            "when the focus is meant to be on design, not content.",
            "The passage experienced a surge in popularity during the world ",
            "Letraset used it on their dry-transfer sheets",
            "be very useful when the focus is meant to be on design",
            "The passage experienced a surge in popularity during the world",
            "Letraset used it on their dry-transfer sheets",
    };

    public static List<ConversationModel> getConversationList() {

        final int randomBound = 10;

        List<ConversationModel> conversationModels = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            ConversationModel conversationModel = new ConversationModel();
            conversationModel.setId(UUID.randomUUID().toString());
            conversationModel.setTitle(title[new Random().nextInt(randomBound)]);
            conversationModel.setMessage(messages[new Random().nextInt(randomBound)]);
            conversationModel.setTime("07:48");
            conversationModel.setConversationType((i % 2 == 0) ? ConversationType.CLIENT : ConversationType.SERVER);
            conversationModel.setConversationStatus(ConversationStatus.SENT);

            conversationModels.add(conversationModel);
        }

        return conversationModels;
    }

    public static List<ConversationModel> getClientConversationList() {

        final int randomBound = 10;

        List<ConversationModel> conversationModels = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            ConversationModel conversationModel = new ConversationModel();
            conversationModel.setId(UUID.randomUUID().toString());
            conversationModel.setTitle(title[new Random().nextInt(randomBound)]);
            conversationModel.setMessage(messages[new Random().nextInt(randomBound)]);
            conversationModel.setTime("02:18");
            conversationModel.setConversationType(ConversationType.CLIENT);
            conversationModel.setConversationStatus(ConversationStatus.SENDING);

            conversationModels.add(conversationModel);
        }

        return conversationModels;
    }

}
