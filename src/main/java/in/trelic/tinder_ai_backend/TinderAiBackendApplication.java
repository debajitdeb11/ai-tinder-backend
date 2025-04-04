package in.trelic.tinder_ai_backend;

import in.trelic.tinder_ai_backend.conversations.ChatMessage;
import in.trelic.tinder_ai_backend.conversations.Conversation;
import in.trelic.tinder_ai_backend.conversations.ConversationRepository;
import in.trelic.tinder_ai_backend.profile.Gender;
import in.trelic.tinder_ai_backend.profile.Profile;
import in.trelic.tinder_ai_backend.profile.ProfileRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class TinderAiBackendApplication implements CommandLineRunner {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ConversationRepository conversationRepository;
	@Autowired
	private ChatClient chatClient;

	public static void main(String[] args) {
		SpringApplication.run(TinderAiBackendApplication.class, args);
	}

	public void run(String... args) {

		String res = chatClient.prompt().user("Who is Debajit Deb from Crisis24?").call().content();

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

		Conversation conversation = new Conversation(
				"1",
				profile.id(),
				List.of(
						new ChatMessage("Hello", profile.id(), LocalDateTime.now())
				)
		);

		profileRepository.save(profile);
		profileRepository.findAll().forEach(System.out::println);

		conversationRepository.save(conversation);
		conversationRepository.findAll().forEach(System.out::println);
	}

}
