package in.trelic.tinder_ai_backend.conversations;

import org.springframework.data.annotation.Id;

import java.util.List;

public record Conversation(
        @Id String id,
        String profileId,
        List<ChatMessage> messages
) {
}
