package com.covidHelp.demo;

import com.covidHelp.demo.repository.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Import({ElasticSearchConfig.class})
// @EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})     
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class CovidHelpApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidHelpApplication.class, args);
	}
}
