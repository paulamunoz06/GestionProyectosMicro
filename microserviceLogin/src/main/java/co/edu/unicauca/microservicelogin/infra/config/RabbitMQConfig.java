package co.edu.unicauca.microservicelogin.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de RabbitMQ para la aplicación.
 *
 * Esta clase configura los componentes necesarios para la interacción con RabbitMQ, incluyendo la cola
 * de mensajes para el usuario y la configuración del convertidor de mensajes JSON.
 */
@Configuration
public class RabbitMQConfig {

    // Nombre de la cola de mensajes para usuarios
    public static final String USER_QUEUE = "userQueue";

    /**
     * Configura una cola de mensajes en RabbitMQ.
     *
     * Esta cola se utiliza para manejar mensajes relacionados con usuarios.
     * La cola se configura para ser persistente.
     *
     * @return {@link Queue} La cola configurada para usuarios.
     */
    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    /**
     * Configura el template de RabbitMQ para el envío de mensajes.
     *
     * Se utiliza para enviar y recibir mensajes a través de RabbitMQ. El mensaje será convertido
     * al formato JSON utilizando el convertidor especificado.
     *
     * @param connectionFactory La fábrica de conexiones que gestiona las conexiones a RabbitMQ.
     * @return {@link RabbitTemplate} El template configurado para RabbitMQ.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Configura el convertidor de mensajes para RabbitMQ que convierte los mensajes a formato JSON.
     *
     * Se utiliza Jackson para convertir los mensajes a y desde el formato JSON.
     *
     * @return {@link Jackson2JsonMessageConverter} El convertidor de mensajes JSON.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}