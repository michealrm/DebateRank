package net.debaterank.webrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EntityScan(basePackages = "net.debaterank.webrest")
public class WebRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebRestApplication.class, args);
	}

}
