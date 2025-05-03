package co.edu.unicauca.microservicestudent.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Clase de configuración para la integración con RabbitMQ.
 *
 * Define las colas utilizadas para la comunicación entre microservicios,
 * así como los componentes necesarios para el envío y la recepción de mensajes
 * en formato JSON.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Nombre de la cola utilizada para recibir postulaciones de estudiantes a proyectos.
     */
    public static final String STUDENTPOSTULATION_QUEUE = "studentPostulationQueue";

    /**
     * Nombre de la cola utilizada para actualizar el estado de los proyectos.
     */
    public static final String UPDATEPROJECT_QUEUE = "updateProjectQueue";

    /**
     * Crea y registra una cola persistente para las postulaciones de estudiantes.
     *
     * @return instancia de la cola {@code studentPostulationQueue}
     */
    @Bean
    public Queue studentPostulationQueue() {
        return new Queue(STUDENTPOSTULATION_QUEUE, true);
    }

    /**
     * Crea y registra una cola persistente para la actualización de proyectos.
     *
     * @return instancia de la cola {@code updateProjectQueue}
     */
    @Bean
    public Queue updateProjectQueue() {
        return new Queue(UPDATEPROJECT_QUEUE, true);
    }

    /**
     * Configura y devuelve un {@link RabbitTemplate} que utiliza un conversor de mensajes JSON.
     *
     * Este template permite enviar mensajes serializados automáticamente a formato JSON
     * al utilizar el conversor Jackson.
     *
     * @param connectionFactory fábrica de conexiones proporcionada por Spring AMQP
     * @return una instancia configurada de {@link RabbitTemplate}
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Crea el conversor de mensajes basado en Jackson para serialización y deserialización JSON.
     *
     * Este conversor es necesario para transformar objetos Java en mensajes JSON y viceversa.
     *
     * @return instancia de {@link Jackson2JsonMessageConverter}
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}