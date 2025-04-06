package in.trelic.tinder_ai_backend.matches;

import in.trelic.tinder_ai_backend.profile.Profile;
import org.springframework.data.annotation.Id;

public record Match (
        @Id String id,
        Profile profile,
        String conversationId
) {
}
