package in.trelic.tinder_ai_backend;

import in.trelic.tinder_ai_backend.conversations.ConversationRepository;
import in.trelic.tinder_ai_backend.matches.MatchRepository;
import in.trelic.tinder_ai_backend.profile.ProfileCreationService;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinderAiBackendApplication implements CommandLineRunner {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ConversationRepository conversationRepository;
	@Autowired
	private OllamaChatModel chatModel;
	@Autowired
	private ProfileCreationService profileCreationService;
	@Autowired
	private MatchRepository matchRepository;

	public static void main(String[] args) {
		SpringApplication.run(TinderAiBackendApplication.class, args);
	}

	public void run(String... args) {
		clearDatabaseEntries();
		profileCreationService.saveProfilesToDB();
	}

	private void clearDatabaseEntries() {
		profileRepository.deleteAll();
		conversationRepository.deleteAll();
		matchRepository.deleteAll();
	}

}
