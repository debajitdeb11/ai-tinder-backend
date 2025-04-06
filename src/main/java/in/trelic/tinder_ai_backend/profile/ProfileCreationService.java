package in.trelic.tinder_ai_backend.profile;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProfileCreationService {

    private final ProfileRepository profileRepository;

    @Value("#{${tinderai.character.user}}")
    private Map<String, String> userProfileProperties;

    public ProfileCreationService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }


    public void saveProfilesToDB() {
        Gson gson = new Gson();

        try {
            List<Profile> existingProfiles = gson.fromJson(
                    new FileReader("profiles.json"),
                    new TypeToken<ArrayList<Profile>>() {}.getType()
            );
            profileRepository.saveAll(existingProfiles);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Profile profile = new Profile(
                userProfileProperties.get("id"),
                userProfileProperties.get("firstName"),
                userProfileProperties.get("lastName"),
                Integer.parseInt(userProfileProperties.get("age")),
                userProfileProperties.get("bio"),
                userProfileProperties.get("imageUrl"),
                userProfileProperties.get("ethnicity"),
                Gender.valueOf(userProfileProperties.get("gender")),
                userProfileProperties.get("myersBriggsPersonalityType")
        );

        System.out.println(userProfileProperties);
        profileRepository.save(profile);
    }
}
