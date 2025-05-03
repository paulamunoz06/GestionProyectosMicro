package co.edu.unicauca.microservicestudent.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa un proyecto.
 *
 * Esta clase se utiliza para intercambiar información de proyectos entre capas
 * de la aplicación, especialmente en operaciones de serialización/deserialización.
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
     * Resumen ejecutivo del proyecto.
     */
    private String proAbstract;

    /**
     * Objetivos que se esperan alcanzar con el desarrollo del proyecto.
     */
    private String proGoals;

    /**
     * Fecha de registro o creación del proyecto.
     */
    private LocalDate proDate;

    /**
     * Tiempo estimado para la ejecución del proyecto, en meses.
     */
    private int proDeadLine;

    /**
     * Presupuesto estimado para la realización del proyecto.
     */
    private Double proBudget;

    /**
     * Estado actual del proyecto, representado como una cadena.
     */
    private String proState;

    /**
     * Identificador de la empresa asociada al proyecto.
     */
    private String idcompany;

    /**
     * Identificador del coordinador encargado del proyecto.
     */
    private String proCoordinator;

    /**
     * Lista de identificadores de estudiantes que se han postulado al proyecto.
     *
     * Este campo se ignora en los procesos de serialización JSON.
     */
    @JsonIgnore
    private List<String> postulated = new ArrayList<>();

    /**
     * Lista de identificadores de estudiantes que han sido aprobados para el proyecto.
     *
     * Este campo se ignora en los procesos de serialización JSON.
     */
    @JsonIgnore
    private List<String> approved = new ArrayList<>();

    /**
     * Constructor personalizado utilizado para inicializar los atributos principales del proyecto.
     *
     * @param proid          Identificador del proyecto
     * @param protitle       Título del proyecto
     * @param prodescription Descripción del proyecto
     * @param proAbstract    Resumen del proyecto
     * @param proGoals       Objetivos del proyecto
     * @param proDate        Fecha de creación del proyecto
     * @param proDeadline    Tiempo límite de ejecución
     * @param proBudget      Presupuesto asignado
     * @param proState       Estado actual del proyecto
     * @param idcompany      ID de la empresa asociada
     * @param proCoordinator ID del coordinador del proyecto
     */
    public ProjectDto(String proid, String protitle, String prodescription, String proAbstract, String proGoals,
                      LocalDate proDate, int proDeadline, Double proBudget, String proState,
                      String idcompany, String proCoordinator) {
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
}