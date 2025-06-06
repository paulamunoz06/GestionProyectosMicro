package co.edu.unicauca.microservicecoordinator.application.port.in;

import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

/**
 * Puerto de entrada para la coordinación de evaluaciones de proyectos.
 *
 * Esta interfaz define la operación que permite evaluar un proyecto a través de una transición de estado,
 * siguiendo el patrón de arquitectura hexagonal. Las implementaciones de esta interfaz orquestan la lógica
 * de negocio relacionada con el cambio de estado de los proyectos.
 *
 * El uso de este puerto permite desacoplar la lógica de aplicación del resto del sistema,
 * facilitando la prueba, extensión y mantenimiento del dominio.
 */
public interface CoordinatorServicePort {

    /**
     * Evalúa un proyecto según su identificador y un nuevo estado propuesto.
     *
     * Esta operación aplica una transición de estado al proyecto, utilizando el patrón State,
     * y devuelve el estado actualizado como un objeto de transferencia de datos (DTO).
     *
     * @param proId Identificador único del proyecto a evaluar.
     * @param proStatusStr Estado al que se desea transicionar, expresado como cadena de texto.
     *                     Debe coincidir con un valor válido de EnumProjectState.
     * @return El proyecto actualizado como ProjectDto.
     * @throws InvalidStateTransitionException Si el estado proporcionado es inválido o
     *         si no se encuentra el proyecto con el identificador dado.
     */
    ProjectDto evaluateProject(String proId, String proStatusStr);
}

