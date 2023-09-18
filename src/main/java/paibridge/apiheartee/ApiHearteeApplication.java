package paibridge.apiheartee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiHearteeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiHearteeApplication.class, args);
	}

}
