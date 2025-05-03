package co.edu.unicauca.microservicecoordinator.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String STUDENTPOSTULATION_QUEUE = "studentPostulationQueue";
    public static final String STUDENTUPDATE_QUEUE = "studentUpdateQueue";
    public static final String POSTULATION_QUEUE = "postulationQueue";
    //Cola de proyectos creados
    public static final String CREATEPROJECT_QUEUE = "createProjectQueue";
    //Cola de estados de proyectos actualizados
    public static final String UPDATEPROJECT_QUEUE = "updateProjectQueue";

    @Bean
    public Queue studentPostulationQueue() {
        return new Queue(STUDENTPOSTULATION_QUEUE, true);
    }

    @Bean
    public Queue studentUpdateQueue() {
        return new Queue(STUDENTUPDATE_QUEUE, true);
    }

    @Bean
    public Queue postulationQueue() {
        return new Queue(POSTULATION_QUEUE, true);
    }

    @Bean
    public Queue createProjectQueue() {
        return new Queue(CREATEPROJECT_QUEUE, true);
    }

    @Bean
    public Queue updateProjectQueue() {
        return new Queue(UPDATEPROJECT_QUEUE, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}