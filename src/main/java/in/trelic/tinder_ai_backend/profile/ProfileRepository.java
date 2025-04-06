package in.trelic.tinder_ai_backend.profile;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    // { $sample: { size: 1 } }
    @Aggregation(pipeline = {"{ $sample: { size: 1 } }"})
    Profile getRandomProfile();
}
