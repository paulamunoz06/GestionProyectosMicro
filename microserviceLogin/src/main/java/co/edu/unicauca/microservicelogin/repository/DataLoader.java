package co.edu.unicauca.microservicelogin.repository;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {

        User user = new User();
        user.setId("juanvela");
        user.setPassword("password");
        user.setEmail("juanvela@example.com");
        user.setRole(3);
        userService.registerUser(user);
    }
}
