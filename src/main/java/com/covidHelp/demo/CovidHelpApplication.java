package com.covidHelp.demo;

import com.covidHelp.demo.repository.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Import({ElasticSearchConfig.class})
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class CovidHelpApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidHelpApplication.class, args);
	}
}
