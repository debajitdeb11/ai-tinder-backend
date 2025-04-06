package in.trelic.tinder_ai_backend.conversations;

import in.trelic.tinder_ai_backend.profile.Profile;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@CrossOrigin("*")
public class ConversationController {
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;
    private final ProfileRepository profileRepository;

    public ConversationController(ConversationRepository conversationRepository, ConversationService conversationService, ProfileRepository profileRepository) {
        this.conversationRepository = conversationRepository;
        this.conversationService = conversationService;
        this.profileRepository = profileRepository;

        this.conversationRepository.deleteAll();
    }

    @GetMapping("/conversations/{conversationId}")
    public Conversation getConversationById(@PathVariable String conversationId) {
        return conversationRepository
                .findById(conversationId)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find conversation for Id: " + conversationId)
                );
    }

    @PostMapping("/conversations/{conversationId}")
    public Conversation addMessageToConversation(@PathVariable String conversationId, @RequestBody ChatMessage chatMessage) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find conversation with the given Id: " + conversationId
                ));

        Profile profile = profileRepository.findById(conversation.profileId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + conversation.profileId()
                ));

        Profile user = profileRepository.findById(chatMessage.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + chatMessage.authorId()
                ));

        // TODO: Need to validate that the author of a message happens to be only the profile associated with the message user
        ChatMessage messageWithTime = new ChatMessage(
                chatMessage.messageText(),
                chatMessage.authorId(),
                LocalDateTime.now()
        );

        conversation.messages().add(messageWithTime);
        Conversation aiConversation = conversationService.generateProfileResponse(conversation, profile, user);
        conversationRepository.save(aiConversation);


        return conversation;
    }
}
