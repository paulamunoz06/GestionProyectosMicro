package co.edu.unicauca.microservicecoordinator.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Objeto de Transferencia de Datos (DTO) para proyectos.
 *
 * Esta clase se utiliza para transferir datos de proyectos entre diferentes
 * capas de la aplicación o entre sistemas, sin exponer los detalles de implementación
 * de la entidad de dominio.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {

    /**
     * Identificador único del proyecto.
     */
    private String proId;

    /**
     * Título del proyecto.
     */
    private String proTitle;

    /**
     * Descripción detallada del proyecto.
     */
    private String proDescription;

    /**
     * Resumen del proyecto.
     */
    private String proAbstract;

    /**
     * Objetivos del proyecto.
     */
    private String proGoals;

    /**
     * Fecha de registro del proyecto.
     */
    private LocalDate proDate;

    /**
     * Tiempo máximo de ejecución del proyecto en meses.
     */
    private int proDeadLine;

    /**
     * Presupuesto asignado al proyecto.
     */
    private Double proBudget;

    /**
     * Estado actual del proyecto como cadena de texto.
     */
    private String proState;

    /**
     * Identificador único de la compañia.
     */
    private String idcompany;

    /**
     * Identificador único del coordinador.
     */
    private String proCoordinator;

    /**
     * Constructor con todos los campos para crear un objeto ProjectDto.
     *
     * @param proid Identificador único del proyecto
     * @param protitle Título del proyecto
     * @param prodescription Descripción detallada del proyecto
     * @param proAbstract Resumen del proyecto
     * @param proGoals Objetivos del proyecto
     * @param proDate Fecha de registro del proyecto
     * @param proDeadline Tiempo máximo de ejecución del proyecto en meses
     * @param proBudget Presupuesto asignado al proyecto
     * @param proState Estado actual del proyecto como cadena de texto
     */
    public ProjectDto(String proid, String protitle, String prodescription, String proAbstract, String proGoals, LocalDate proDate, int proDeadline, Double proBudget, String proState, String idcompany, String proCoordinator) {
        this.proId = proid;
        this.proTitle = protitle;
        this.proDescription = prodescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = proDate;
        this.proDeadLine = proDeadline;
        this.proBudget = proBudget;
        this.proState = proState;
        this.idcompany = idcompany;
        this.proCoordinator = proCoordinator;
    }

    public ProjectDto(String proid, String proState) {
        this.proId = proid;
        this.proState = proState;
    }
}