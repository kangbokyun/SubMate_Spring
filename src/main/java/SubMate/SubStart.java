package SubMate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileReader;
import java.io.Reader;

@EnableJpaAuditing
@SpringBootApplication
public class SubStart {
	public static void main(String[] args) {
		SpringApplication.run(SubStart.class, args);
	}
}
