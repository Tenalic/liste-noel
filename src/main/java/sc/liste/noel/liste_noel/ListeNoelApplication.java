package sc.liste.noel.liste_noel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ListeNoelApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ListeNoelApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ListeNoelApplication.class);
	}

}
