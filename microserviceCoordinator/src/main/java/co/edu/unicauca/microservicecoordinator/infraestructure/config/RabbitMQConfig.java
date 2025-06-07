package co.edu.unicauca.microservicecoordinator.infraestructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de las colas y beans relacionados con RabbitMQ para la comunicación
 * entre microservicios.
 *
 * Define las colas para postulaciones de estudiantes, creación y actualización de proyectos,
 * además de configurar el template y el convertidor de mensajes para manejar JSON.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Nombre de la cola para postulaciones de estudiantes.
     */
    public static final String STUDENTPOSTULATION_QUEUE = "studentPostulationQueue";

    /**
     * Nombre de la cola para actualización de proyectos.
     */
    public static final String UPDATEPROJECT_QUEUE = "updateProjectQueue";

    /**
     * Nombre de la cola para creación de proyectos.
     */
    public static final String CREATEPROJECT_QUEUE = "createProjectQueue";

    /**
     * Declara la cola para postulaciones de estudiantes.
     *
     * @return Instancia de Queue con el nombre y configuración de durabilidad.
     */
    @Bean
    public Queue studentPostulationQueue() {
        return new Queue(STUDENTPOSTULATION_QUEUE, true);
    }

    /**
     * Declara la cola para creación de proyectos.
     *
     * @return Instancia de Queue con el nombre y configuración de durabilidad.
     */
    @Bean
    public Queue createProjectQueue() {
        return new Queue(CREATEPROJECT_QUEUE, true);
    }

    /**
     * Declara la cola para actualización de proyectos.
     *
     * @return Instancia de Queue con el nombre y configuración de durabilidad.
     */
    @Bean
    public Queue updateProjectQueue() {
        return new Queue(UPDATEPROJECT_QUEUE, true);
    }

    /**
     * Configura el RabbitTemplate que se usará para enviar y recibir mensajes
     * a través de RabbitMQ, usando un convertidor JSON.
     *
     * @param connectionFactory Fábrica de conexiones para RabbitMQ.
     * @return Instancia configurada de RabbitTemplate.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Configura el convertidor de mensajes para serializar y deserializar mensajes
     * en formato JSON usando Jackson.
     *
     * @return Instancia de Jackson2JsonMessageConverter.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}