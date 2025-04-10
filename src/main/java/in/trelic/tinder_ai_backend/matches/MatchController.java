package in.trelic.tinder_ai_backend.matches;

import in.trelic.tinder_ai_backend.conversations.Conversation;
import in.trelic.tinder_ai_backend.conversations.ConversationRepository;
import in.trelic.tinder_ai_backend.profile.Profile;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/matches")
@CrossOrigin("*")
public class MatchController {
    private final ProfileRepository profileRepository;
    private final ConversationRepository conversationRepository;
    private final MatchRepository matchRepository;

    public MatchController(ProfileRepository profileRepository, ConversationRepository conversationRepository, MatchRepository matchRepository) {
        this.profileRepository = profileRepository;
        this.conversationRepository = conversationRepository;
        this.matchRepository = matchRepository;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return new ArrayList<>(matchRepository.findAll());
    }

    @PostMapping
    public Match createNewMatch(@RequestBody CreateMatchRequest request) {
         Profile profile = profileRepository.findById(request.profileId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + request.profileId()
                ));

         // TODO: Make sure there are no existing conversation with this profile already

        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                profile.id(),
                new ArrayList<>()
        );

        conversationRepository.save(conversation);

        Match match = new Match(UUID.randomUUID().toString(), profile, conversation.id());

        matchRepository.save(match);
        return match;
    }

    public record CreateMatchRequest(String profileId) {
    }
}
