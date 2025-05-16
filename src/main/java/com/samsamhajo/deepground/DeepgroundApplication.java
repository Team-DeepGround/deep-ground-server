package com.samsamhajo.deepground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
		exclude = {
				SecurityAutoConfiguration.class,
				ManagementWebSecurityAutoConfiguration.class
		}
)
public class DeepgroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeepgroundApplication.class, args);
	}

}
