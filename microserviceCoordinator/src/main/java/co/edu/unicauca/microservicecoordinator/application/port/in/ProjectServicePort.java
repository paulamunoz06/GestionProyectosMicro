package co.edu.unicauca.microservicecoordinator.application.port.in;

import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para el manejo de operaciones relacionadas con proyectos.
 *
 * Esta interfaz define los casos de uso disponibles para consultar, contar y registrar proyectos.
 * Actúa como un contrato que separa la lógica del dominio de las implementaciones tecnológicas,
 * promoviendo el principio de inversión de dependencias y facilitando el desacoplamiento en la arquitectura hexagonal.
 */
public interface ProjectServicePort {

    /**
     * Busca un proyecto por su identificador único.
     *
     * @param id Identificador del proyecto.
     * @return Un Optional que contiene el ProjectDto si fue encontrado,
     *         o vacío si no existe un proyecto con el ID especificado.
     */
    Optional<ProjectDto> findById(String id);

    /**
     * Recupera todos los proyectos registrados en el sistema.
     *
     * @return Lista de todos los proyectos en forma de objetos {@link ProjectDto}.
     */
    List<ProjectDto> findAllProjects();

    /**
     * Cuenta cuántos proyectos existen en un estado específico.
     *
     * @param status Estado del proyecto como cadena de texto. Debe coincidir con un valor válido
     *               de la enumeración EnumProjectState.
     * @return Cantidad de proyectos que se encuentran en el estado especificado.
     * @throws IllegalArgumentException Si el estado proporcionado no es válido.
     */
    Long countByStatus(String status);

    /**
     * Cuenta el número total de proyectos registrados en el sistema.
     *
     * @return Número total de proyectos.
     */
    int countTotalProjects();

    /**
     * Recibe un nuevo proyecto y lo registra en el sistema.
     *
     * Este método es típicamente invocado desde un mecanismo de mensajería como RabbitMQ,
     * representando la recepción asincrónica de un evento.
     *
     * @param project Objeto de transferencia de datos que representa el proyecto a registrar.
     */
    void receiveProject(ProjectDto project);
}