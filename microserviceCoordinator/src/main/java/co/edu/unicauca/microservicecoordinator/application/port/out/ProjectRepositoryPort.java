package co.edu.unicauca.microservicecoordinator.application.port.out;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el acceso a la capa de persistencia de proyectos.
 *
 * Esta interfaz define las operaciones necesarias para almacenar y recuperar proyectos desde una fuente de datos externa,
 * como una base de datos. Forma parte de la arquitectura hexagonal actuando como un contrato que debe ser implementado
 * por un adaptador de infraestructura (por ejemplo, un repositorio JPA).
 *
 * Permite desacoplar el dominio de las tecnologías de almacenamiento, facilitando la escalabilidad, mantenibilidad
 * y la capacidad de hacer pruebas con mocks o stubs.
 */
public interface ProjectRepositoryPort {

    /**
     * Guarda un proyecto en la fuente de datos persistente.
     *
     * @param project Proyecto de dominio que se desea almacenar.
     * @return Proyecto almacenado, con sus posibles valores actualizados (por ejemplo, el ID generado).
     */
    Project save(Project project);

    /**
     * Busca un proyecto por su identificador único.
     *
     * @param id Identificador del proyecto.
     * @return Un Optional que contiene el proyecto como ProjectDto si se encuentra;
     *         de lo contrario, retorna un Optional vacío.
     */
    Optional<ProjectDto> findById(String id);

    /**
     * Recupera todos los proyectos almacenados.
     *
     * @return Lista de proyectos en forma de ProjectDto.
     */
    List<ProjectDto> findAll();

    /**
     * Cuenta cuántos proyectos se encuentran en un estado determinado.
     *
     * @param enumProjectState Estado del proyecto representado por la enumeración del dominio.
     * @return Número de proyectos con el estado especificado.
     */
    Long countByProState(EnumProjectState enumProjectState);

    /**
     * Cuenta el total de proyectos registrados en la fuente de datos.
     *
     * @return Número total de proyectos.
     */
    int count();
}
