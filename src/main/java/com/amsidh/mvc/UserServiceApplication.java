package com.amsidh.mvc;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.amsidh.mvc.config.SwaggerConfig;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.amsidh.mvc.repository"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class UserServiceApplication implements CommandLineRunner {

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		Class[] classes = {UserServiceApplication.class, SwaggerConfig.class};
		SpringApplication.run(classes, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info(format("ip.address.security.allow :%s", environment.getProperty("ip.address.security.allow")));
	}
}
