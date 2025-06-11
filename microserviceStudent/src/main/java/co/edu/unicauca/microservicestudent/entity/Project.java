package co.edu.unicauca.microservicestudent.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un proyecto académico que puede ser postulado por estudiantes y gestionado por un coordinador.
 *
 * <p>Esta clase contiene la información esencial de un proyecto como su título, descripción, resumen,
 * objetivos, fecha de creación, duración estimada, presupuesto, estado actual, empresa responsable
 * y el coordinador a cargo. Además, mantiene la relación con los estudiantes postulados y aprobados.</p>
 *
 * <p>Esta entidad está mapeada a una tabla en la base de datos mediante JPA.</p>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project {

    /**
     * Identificador único del proyecto.
     */
    @Id
    @Column(name = "proid", nullable = false, unique = true)
    private String proId;

    /**
     * Título del proyecto.
     *
     * <p>Debe contener entre 2 y 100 caracteres.</p>
     */
    @NotBlank(message = "El título del proyecto no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(name = "protitle", nullable = false)
    private String proTitle;

    /**
     * Descripción detallada del proyecto.
     *
     * <p>Debe contener entre 10 y 1000 caracteres.</p>
     */
    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 10, max = 1000)
    @Column(name = "prodescription", nullable = false)
    private String proDescription;

    /**
     * Resumen o sinopsis breve del proyecto.
     */
    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(name = "proabstract", nullable = false)
    private String proAbstract;

    /**
     * Objetivos principales que el proyecto busca alcanzar.
     */
    @NotBlank(message = "Los objetivos no pueden estar vacíos")
    @Column(name = "progoals", nullable = false)
    private String proGoals;

    /**
     * Fecha de creación del proyecto.
     *
     * Se almacena únicamente la fecha sin información de hora.
     */
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "prodate", nullable = false)
    private LocalDate proDate;

    /**
     * Plazo máximo estimado para la ejecución del proyecto (en meses).
     */
    @NotNull(message = "El tiempo máximo en meses no puede estar vacío")
    @Column(name = "prodeadline", nullable = false)
    private int proDeadLine;

    /**
     * Presupuesto estimado del proyecto.
     */
    @Column(name = "probudget", nullable = true)
    private Double proBudget;

    /**
     * Estado actual del proyecto.
     *
     * Define en qué fase se encuentra dentro del ciclo de vida.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "prostate", nullable = false)
    private EnumProjectState proState;

    /**
     * Identificador de la empresa que propone o financia el proyecto.
     */
    @Column(name = "idcompany", nullable = true)
    private String idcompany;

    /**
     * Identificador del coordinador académico asignado al proyecto.
     */
    @Column(name = "idcoordinator", nullable = true)
    private String proCoordinator;

    /**
     * Lista de estudiantes que han postulado al proyecto.
     *
     * Representa una relación muchos-a-muchos con la entidad {@link Student}.
     */
    @ManyToMany
    @JoinTable(
            name = "postulated",
            joinColumns = @JoinColumn(name = "pro_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> postulated = new ArrayList<>();

    /**
     * Lista de estudiantes aprobados para participar en el proyecto.
     *
     * También representa una relación muchos-a-muchos con la entidad {@link Student}.
     */
    @ManyToMany
    @JoinTable(
            name = "approved",
            joinColumns = @JoinColumn(name = "pro_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> approved = new ArrayList<>();

    /**
     * Constructor personalizado para inicializar un proyecto con los atributos más importantes.
     *
     * @param proid          Identificador del proyecto.
     * @param protitle       Título del proyecto.
     * @param prodescription Descripción del proyecto.
     * @param proAbstract    Resumen del proyecto.
     * @param proGoals       Objetivos del proyecto.
     * @param proDeadline    Plazo máximo en meses.
     * @param proBudget      Presupuesto estimado.
     * @param idcompany      Identificador de la empresa asociada.
     * @param proCoordinator Identificador del coordinador asignado.
     */
    public Project(String proid, String protitle, String prodescription, String proAbstract, String proGoals,
                   int proDeadline, Double proBudget, String idcompany, String proCoordinator) {
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
