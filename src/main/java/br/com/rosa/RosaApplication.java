package br.com.rosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RosaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RosaApplication.class, args);

	}

}
