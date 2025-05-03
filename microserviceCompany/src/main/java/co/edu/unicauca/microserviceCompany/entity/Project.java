package co.edu.unicauca.microserviceCompany.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Clase que representa un proyecto en el sistema.
 *
 * La clase {@link Project} define los atributos y comportamientos relacionados con un proyecto,
 * incluyendo detalles como su título, descripción, metas, presupuesto, fecha de inicio, fecha de vencimiento,
 * y el estado del proyecto. Esta entidad está vinculada a la base de datos y se utiliza para representar los proyectos
 * gestionados por las empresas en el sistema.
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Project {

    /**
     * Identificador único del proyecto.
     * Este atributo es utilizado para distinguir un proyecto de otro.
     */
    @Id
    @Column(name = "PROID", nullable = false, unique = true)
    private String proId;

    /**
     * Título del proyecto.
     * Este atributo no puede estar vacío y debe tener entre 2 y 100 caracteres.
     */
    @NotBlank(message = "El título del proyecto no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(name = "PROTITLE", nullable = false)
    private String proTitle;

    /**
     * Descripción detallada del proyecto.
     * Este atributo no puede estar vacío y debe tener entre 10 y 1000 caracteres.
     */
    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 10, max = 1000)
    @Column(name = "PRODESCRIPTION", nullable = false)
    private String proDescription;

    /**
     * Resumen del proyecto.
     * Este atributo no puede estar vacío.
     */
    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(name = "PROABSTRACT", nullable = false)
    private String proAbstract;

    /**
     * Objetivos del proyecto.
     * Este atributo no puede estar vacío.
     */
    @NotBlank(message = "Los objetivos no pueden estar vacíos")
    @Column(name = "PROGOALS", nullable = false)
    private String proGoals;

    /**
     * Fecha en que se crea el proyecto.
     * Se utiliza la fecha actual como valor predeterminado.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "PRODATE", nullable = false)
    private LocalDate proDate;

    /**
     * Tiempo máximo en meses para completar el proyecto.
     * Este valor no puede ser nulo.
     */
    @NotNull(message = "El tiempo máximo en meses no puede estar vacío")
    @Column(name = "PRODEADLINE", nullable = false)
    private int proDeadLine;

    /**
     * Presupuesto estimado para el proyecto.
     * Este valor es opcional.
     */
    @Column(name = "PROBUDGET", nullable = true)
    private Double proBudget;

    /**
     * Estado actual del proyecto.
     * El estado está representado por la enumeración {@link EnumProjectState}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PROSTATE", nullable = false)
    private EnumProjectState proState;

    /**
     * Identificador de la empresa que gestiona el proyecto.
     * Este valor es opcional y puede estar vacío.
     */
    @Column(name = "IDCOMPANY", nullable = true)
    private String idcompany;

    /**
     * Identificador del coordinador del proyecto.
     * Este valor es opcional y puede estar vacío.
     */
    @Column(name = "IDCOORDINADOR", nullable = true)
    private String proCoordinator;

    /**
     * Constructor para crear un nuevo proyecto con los parámetros proporcionados.
     *
     * @param proid          Identificador del proyecto.
     * @param protitle       Título del proyecto.
     * @param prodescription Descripción del proyecto.
     * @param proAbstract    Resumen del proyecto.
     * @param proGoals       Objetivos del proyecto.
     * @param proDeadline    Tiempo máximo en meses para completar el proyecto.
     * @param proBudget      Presupuesto estimado para el proyecto.
     * @param idcompany      Identificador de la empresa que gestiona el proyecto.
     * @param proCoordinator Identificador del coordinador del proyecto.
     */
    public Project(String proid, String protitle, String prodescription, String proAbstract, String proGoals, int proDeadline, Double proBudget, String idcompany, String proCoordinator) {
        this.proId = proid;
        this.proTitle = protitle;
        this.proDescription = prodescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = LocalDate.now();
        this.proDeadLine = proDeadline;
        this.proBudget = proBudget;
        this.proState = EnumProjectState.RECIBIDO;
        this.idcompany = idcompany;
        this.proCoordinator = proCoordinator;
    }
}