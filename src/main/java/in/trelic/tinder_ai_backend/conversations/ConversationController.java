package in.trelic.tinder_ai_backend.conversations;

import in.trelic.tinder_ai_backend.profile.Profile;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest request) {

        profileRepository.findById(request.profileId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                new ArrayList<>()
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    public record CreateConversationRequest(
            String profileId
    ){}
}
