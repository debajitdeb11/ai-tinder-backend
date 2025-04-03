package in.trelic.tinder_ai_backend.profile;

import org.springframework.data.annotation.Id;

public record Profile(
        @Id String id,
        String firstName,
        String lastName,
        int age,
        String bio,
        String imageUrl,
        String ethnicity,
        Gender gender,
        String myersBriggsPersonalityType
) {
}
