package in.trelic.tinder_ai_backend;

import in.trelic.tinder_ai_backend.profile.Gender;
import in.trelic.tinder_ai_backend.profile.Profile;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinderAiBackendApplication implements CommandLineRunner {

	@Autowired
	private ProfileRepository profileRepository;

	public static void main(String[] args) {
		SpringApplication.run(TinderAiBackendApplication.class, args);
	}

	public void run(String... args) {
		Profile profile = new Profile(
				"1",
				"Debajit",
				"Deb",
				27,
				"Software Engineer",
				"foo.jpg",
				"Indian",
				Gender.MALE,
				"INFP"
		);

		profileRepository.save(profile);

		profileRepository.findAll().forEach(p -> {
			System.out.println(p.toString());
		});
	}

}
