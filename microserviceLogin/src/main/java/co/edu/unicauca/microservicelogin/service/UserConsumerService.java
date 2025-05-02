package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.config.RabbitMQConfig;
import co.edu.unicauca.microservicelogin.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {
    @Autowired
    private UserService userService;

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receiveMessage(User user) {
        // Guardar el usuario en el repositorio local a traves del servicio
        try {
            userService.registerUser(user);
            System.out.println("Recibido usuario: " + user.getId() + " Role:" + user.getRole());
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());

        }

    }
}
