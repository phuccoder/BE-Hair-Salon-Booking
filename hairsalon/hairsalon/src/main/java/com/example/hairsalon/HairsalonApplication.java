package com.example.hairsalon;

import com.example.hairsalon.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class) // Register AppProperties here
public class HairsalonApplication {

	public static void main(String[] args) {
		SpringApplication.run(HairsalonApplication.class, args);
	}

}
