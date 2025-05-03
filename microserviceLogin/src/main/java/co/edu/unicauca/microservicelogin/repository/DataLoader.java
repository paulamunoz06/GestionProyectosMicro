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
        //empresa
        User user1 = new User();
        user1.setId("juanvela");
        user1.setPassword("passwordVela");
        user1.setEmail("juanvela@example.com");
        user1.setRole(3);
        userService.registerUser(user1);

        //coordinaro
        User user2 = new User();
        user2.setId("paulamunoz");
        user2.setPassword("passwordPaula");
        user2.setEmail("paulamunoz@example.com");
        user2.setRole(2);
        userService.registerUser(user2);

        //estudiante
        User user3 = new User();
        user3.setId("cunas");
        user3.setPassword("passwordCris");
        user3.setEmail("cunas@example.com");
        user3.setRole(1);
        userService.registerUser(user3);
    }
}
