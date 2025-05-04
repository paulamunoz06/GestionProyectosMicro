package co.edu.unicauca.microserviceCompany.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Configura la integración de la aplicación con RabbitMQ para el manejo de colas y la
 * comunicación asíncrona entre los diferentes servicios de la aplicación.
 */
@Configuration
public class RabbitMQConfig {

    /**
     * Nombre de la cola para la creación de proyectos.
     */
    public static final String CREATEPROJECT_QUEUE = "createProjectQueue";

    /**
     * Nombre de la cola para la información relacionada con las empresas de los proyectos.
     */
    public static final String PROJECTCOMPANYINFO_QUEUE = "projectCompanyQueue";

    /**
     * Nombre de la cola para la comunicación relacionada con los usuarios.
     */
    public static final String USER_QUEUE = "userQueue";

    /**
     * Nombre de la cola para las actualizaciones de proyectos provenientes del coordinador.
     */
    public static final String UPDATEPROJECT_QUEUE = "updateProjectQueue";

    /**
     * Define la cola de RabbitMQ utilizada para operaciones relacionadas con usuarios.
     * Esta cola está configurada para ser persistente, es decir, los mensajes no se perderán
     * incluso si el servidor de RabbitMQ se reinicia.
     *
     * @return una nueva instancia de {@link Queue} configurada para la cola de usuarios.
     */
    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    /**
     * Define la cola de RabbitMQ utilizada para la creación de proyectos.
     * Esta cola está configurada para ser persistente, es decir, los mensajes no se perderán
     * incluso si el servidor de RabbitMQ se reinicia.
     *
     * @return una nueva instancia de {@link Queue} configurada para la cola de creación de proyectos.
     */
    @Bean
    public Queue createProjectQueue() {
        return new Queue(CREATEPROJECT_QUEUE, true);
    }

    /**
     * Define la cola de RabbitMQ utilizada para la información relacionada con las empresas
     * de los proyectos.
     * Esta cola está configurada para ser persistente, es decir, los mensajes no se perderán
     * incluso si el servidor de RabbitMQ se reinicia.
     *
     * @return una nueva instancia de {@link Queue} configurada para la cola de información de empresa de proyectos.
     */
    @Bean
    public Queue projectCompanyQueue() {
        return new Queue(PROJECTCOMPANYINFO_QUEUE, true);
    }

    /**
     * Define la cola de RabbitMQ utilizada para recibir actualizaciones de proyectos
     * desde el microservicio Coordinador.
     * Esta cola está configurada para ser persistente, es decir, los mensajes no se perderán
     * incluso si el servidor de RabbitMQ se reinicia.
     *
     * @return una nueva instancia de {@link Queue} configurada para la cola de actualización de proyectos.
     */
    @Bean
    public Queue updateProjectQueue() {
        return new Queue(UPDATEPROJECT_QUEUE, true);
    }

    /**
     * Configura el {@link RabbitTemplate} que es el principal objeto utilizado para enviar
     * y recibir mensajes de RabbitMQ. Este template utiliza un {@link Jackson2JsonMessageConverter}
     * para convertir los objetos Java a formato JSON y viceversa.
     *
     * @param connectionFactory el objeto {@link ConnectionFactory} que gestiona las conexiones
     *                          a RabbitMQ.
     * @return una instancia de {@link RabbitTemplate} configurada para enviar y recibir mensajes en formato JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Configura un {@link Jackson2JsonMessageConverter} que convierte los mensajes de y hacia
     * el formato JSON. Este conversor es utilizado por el {@link RabbitTemplate} para garantizar
     * que los mensajes enviados y recibidos sean en formato JSON.
     *
     * @return una nueva instancia de {@link Jackson2JsonMessageConverter}.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}