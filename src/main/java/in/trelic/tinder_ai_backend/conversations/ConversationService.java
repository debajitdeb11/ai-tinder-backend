package in.trelic.tinder_ai_backend.conversations;

import in.trelic.tinder_ai_backend.profile.Profile;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class ConversationService {

    private final OllamaChatModel ollamaChatModel;

    public ConversationService(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    public Conversation generateProfileResponse(Conversation conversation, Profile profile, Profile user) {

        List<Message> messages = new ArrayList<>();

        String systemMessageStr = "You're a " + profile.age() + " year old " + profile.ethnicity() + " " + profile.gender() + " called " + profile.firstName() +
                " " + profile.lastName() + " matched with " + user.firstName() + " " + user.lastName() + " on Tinder." + "\n" + "This is a an in-app text conversation between you too" + "\n" +
                "your bio is " + profile.bio() + " and " + profile.myersBriggsPersonalityType() + " is your personality";

        // System Message
        Message systemMessage = new SystemMessage("Pretend to be a Tinder user");
        messages.add(systemMessage);
        messages.add(new SystemMessage(systemMessageStr));
        // User message
//        String userMessageText = conversation.messages().getFirst().messageText();

        conversation.messages().forEach(message -> {
            if (message.authorId().equals(profile.id())) {
                messages.add(new AssistantMessage(message.messageText()));
            } else {
                messages.add(new UserMessage(message.messageText()));
            }
        });

        Prompt prompt = new Prompt(messages);
        ChatResponse response = ollamaChatModel.call(prompt);

        conversation.messages().add(new ChatMessage(
                response.getResult().getOutput().getText(),
                profile.id(),
                LocalDateTime.now()
        ));

        return conversation;
    }
}
