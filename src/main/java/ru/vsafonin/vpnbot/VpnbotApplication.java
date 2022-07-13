package ru.vsafonin.vpnbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VpnbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(VpnbotApplication.class, args);
		System.out.println("i'm alive");
	}

}
