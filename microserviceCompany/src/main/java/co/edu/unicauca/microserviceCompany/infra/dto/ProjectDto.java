package co.edu.unicauca.microserviceCompany.infra.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para la transferencia de datos de un proyecto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
     * Fecha de inicio del proyecto.
     */
    private LocalDate proDate;

    /**
     * Plazo máximo para la finalización del proyecto en meses.
     */
    private int proDeadLine;

    /**
     * Presupuesto estimado del proyecto.
     */
    private Double proBudget;

    /**
     * Estado actual del proyecto.
     */
    private String proState;

    /**
     * Identificador de la empresa asociada al proyecto.
     */
    private String companyId;

    /**
     * Identificador de la empresa (duplicado con 'companyId').
     */
    private String idcompany;

    /**
     * Identificador del coordinador del proyecto.
     */
    private String proCoordinator;
}