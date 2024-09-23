package com.emazon.mscart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients()
public class MscartApplication {
	public static void main(String[] args) {
		SpringApplication.run(MscartApplication.class, args);
	}

}
