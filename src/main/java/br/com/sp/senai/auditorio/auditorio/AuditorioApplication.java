package br.com.sp.senai.auditorio.auditorio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class AuditorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditorioApplication.class, args);
	}

}
