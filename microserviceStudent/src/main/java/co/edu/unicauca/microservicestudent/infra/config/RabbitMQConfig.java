package co.edu.unicauca.microservicestudent.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

@Configuration
public class RabbitMQConfig {
    public static final String STUDENTPOSTULATION_QUEUE = "studentPostulationQueue";

    public static final String CREATEPROJECT_QUEUE = "createProjectQueue";
    public static final String UPDATEPROJECT_QUEUE = "updateProjectQueue";

    public static final String PROJECTCOMPANYINFO_QUEUE = "projectCompanyQueue";

    @Bean
    public Queue studentPostulationQueue() {
        return new Queue(STUDENTPOSTULATION_QUEUE, true);
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
    public Queue projectCompanyQueue() {
        return new Queue(PROJECTCOMPANYINFO_QUEUE, true);
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