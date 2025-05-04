package co.edu.unicauca.microservicecoordinator;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class MicroserviceCoordinatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceCoordinatorApplication.class, args);
	}

}
