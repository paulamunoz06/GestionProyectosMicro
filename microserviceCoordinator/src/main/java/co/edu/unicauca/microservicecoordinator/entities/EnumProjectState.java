package co.edu.unicauca.microservicecoordinator.entities;

/**
 * Enumeración que define los posibles estados de un proyecto en el sistema.
 *
 * <p>Esta enumeración permite gestionar el ciclo de vida de un proyecto desde
 * su recepción inicial hasta su cierre.</p>
 */
public enum EnumProjectState {
    /**
     * Estado inicial cuando un proyecto es presentado pero aún no ha sido evaluado.
     */
    RECIBIDO,

    /**
     * Estado que indica que el proyecto ha sido revisado y aprobado para su implementación.
     */
    ACEPTADO,

    /**
     * Estado que indica que el proyecto ha sido evaluado y no cumple con los requisitos necesarios.
     */
    RECHAZADO,

    /**
     * Estado que indica que el proyecto está en fase de desarrollo o implementación.
     */
    EJECUCION,

    /**
     * Estado final del proyecto que indica que todas las actividades han sido completadas.
     */
    CERRADO
}