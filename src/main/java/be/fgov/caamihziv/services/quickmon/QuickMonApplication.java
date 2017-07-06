package be.fgov.caamihziv.services.quickmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@SpringBootApplication
@EnableHypermediaSupport(type = HAL)
public class QuickMonApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickMonApplication.class, args);
	}
}
