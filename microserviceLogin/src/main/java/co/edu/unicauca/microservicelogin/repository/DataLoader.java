package co.edu.unicauca.microservicelogin.repository;

import co.edu.unicauca.microservicelogin.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setId("juanvela");
        user.setPassword("password");
        user.setEmail("juanvela@example.com");
        user.setRole(3);
        userRepository.save(user);
    }
}
