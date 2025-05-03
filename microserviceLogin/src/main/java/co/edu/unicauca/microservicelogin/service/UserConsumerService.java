package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.config.RabbitMQConfig;
import co.edu.unicauca.microservicelogin.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para consumir mensajes de la cola RabbitMQ relacionados con usuarios.
 */
@Service
public class UserConsumerService {

    /**
     * Servicio de usuario para manejar la lógica de negocio de usuarios.
     * Inyectado a través de la anotación {@link Autowired}.
     */
    @Autowired
    private UserService userService;

    /**
     * Método que recibe los mensajes de la cola RabbitMQ.
     *
     * Este método es invocado automáticamente cada vez que se recibe un mensaje en
     * la cola configurada `USER_QUEUE`. El mensaje recibido es un objeto `User`,
     * el cual será procesado y registrado en el sistema a través del servicio `userService`.
     *
     * @param user Objeto de tipo {@link User} que contiene los datos del usuario
     *             recibidos desde la cola RabbitMQ.
     */
    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receiveMessage(User user) {
        try {
            // Guardar el usuario en el repositorio local a través del servicio
            userService.registerUser(user);
            System.out.println("Recibido usuario: " + user.getId() + " Role:" + user.getRole());
        } catch (Exception e) {
            // Manejo de excepciones en caso de error al registrar el usuario
            System.out.println("Error: " + e.getMessage());
        }
    }
}