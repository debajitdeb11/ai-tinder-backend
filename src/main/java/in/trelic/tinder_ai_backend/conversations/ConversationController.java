package in.trelic.tinder_ai_backend.conversations;

import in.trelic.tinder_ai_backend.profile.Profile;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RestController
public class ConversationController {
    private final ConversationRepository conversationRepository;
    private final ProfileRepository profileRepository;

    public ConversationController(ConversationRepository conversationRepository, ProfileRepository profileRepository) {
        this.conversationRepository = conversationRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversationById(@PathVariable String conversationId) {
        return conversationRepository
                .findById(conversationId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find conversation for Id: " + conversationId)
                );
    }

    @PostMapping("/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest request) {

        profileRepository.findById(request.profileId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find profile with the given Id: " + request.profileId
                ));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                new ArrayList<>()
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageToConversation(@PathVariable String conversationId, @RequestBody ChatMessage chatMessage) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find conversation with the given Id: " + conversationId
                ));

        // TODO: Need to validate that the author of a message happens to be only the profile associated with the message user
        ChatMessage messageWithTime = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.profileId(),
                LocalDateTime.now()
        );

        conversation.messages().add(messageWithTime);
        conversationRepository.save(conversation);

        return conversation;
    }

    public record CreateConversationRequest(
            String profileId
    ){}
}
