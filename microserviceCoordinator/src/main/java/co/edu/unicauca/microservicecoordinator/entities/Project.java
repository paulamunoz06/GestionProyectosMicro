package co.edu.unicauca.microservicecoordinator.entities;

import co.edu.unicauca.microservicecoordinator.infra.state.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * Entidad que representa un proyecto en el sistema.
 *
 * <p>Esta clase define la estructura de datos para los proyectos gestionados
 * por el microservicio coordinador, incluyendo toda la información relevante
 * y su estado actual en el ciclo de vida.</p>
 *
 */
@Entity
@Getter
@Setter
public class Project {

    /**
     * Identificador único del proyecto.
     */
    @Id
    @Column(name = "PROID", nullable = false, unique = true)
    private String proId;

    /**
     * Título del proyecto.
     * No puede estar vacío y debe tener entre 2 y 100 caracteres.
     */
    @NotBlank(message = "El título del proyecto no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(name = "PROTITLE", nullable = false)
    private String proTitle;

    /**
     * Descripción detallada del proyecto.
     * No puede estar vacía y debe tener entre 10 y 1000 caracteres.
     */
    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 10, max = 1000)
    @Column(name = "PRODESCRIPTION", nullable = false)
    private String proDescription;

    /**
     * Resumen del proyecto.
     * No puede estar vacío.
     */
    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(name = "PROABSTRACT", nullable = false)
    private String proAbstract;

    /**
     * Objetivos del proyecto.
     * No pueden estar vacíos.
     */
    @NotBlank(message = " Los objetivos no pueden estar vacíos")
    @Column(name = "PROGOALS", nullable = false)
    private String proGoals;

    /**
     * Fecha de registro del proyecto.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "PRODATE", nullable = false)
    private LocalDate proDate;

    /**
     * Tiempo máximo de ejecución del proyecto en meses.
     * No puede estar vacío.
     */
    @NotNull(message = "El tiempo maximo en meses no puede estar vacío")
    @Column(name = "PRODEADLINE", nullable = false)
    private int proDeadLine;

    /**
     * Presupuesto asignado al proyecto.
     * Este campo puede ser nulo.
     */
    @Column(name = "PROBUDGET", nullable = true)
    private Double proBudget;

    /**
     * Estado actual del proyecto dentro del ciclo de vida.
     * @see EnumProjectState
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PROSTATE", nullable = false)
    private EnumProjectState proState;

    /**
     * Estado del proyecto como objeto State para implementar el patrón State.
     * Este campo no se persiste en la base de datos y se excluye de la serialización JSON.
     */
    @Transient
    @JsonIgnore
    private ProjectState currentState;

    /**
     * Sincroniza el estado del proyecto como objeto (currentState) con el valor
     * del enumerado (proState).
     *
     * <p>Este método implementa el patrón State, creando el objeto de estado apropiado
     * basado en el valor del enumerado actual.</p>
     */
    public void syncState() {
        switch (this.proState) {
            case ACEPTADO -> this.currentState = new AcceptedState();
            case RECHAZADO -> this.currentState = new RejectState();
            case EJECUCION -> this.currentState = new ExecutedState();
            case CERRADO -> this.currentState = new ClosedState();
            case RECIBIDO -> this.currentState = new ReceivedState();
        }
    }
}