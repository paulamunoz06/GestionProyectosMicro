package co.edu.unicauca.microservicecoordinator.entities;

import co.edu.unicauca.microservicecoordinator.infra.state.ProjectState;
import co.edu.unicauca.microservicecoordinator.infra.state.ProjectStateFactory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private String proId;

    @NotBlank(message = "El título del proyecto no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(name = "pro_title", nullable = false)
    private String proTitle;

    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 10, max = 1000)
    @Column(name = "pro_description", nullable = false)
    private String proDescription;

    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(name = "pro_abstract")
    private String proAbstract;

    @NotBlank(message = " Los objetivos no pueden estar vacíos")
    @Column(name = "pro_goals")
    private String proGoals;

    @Temporal(TemporalType.DATE)
    @Column(name = "pro_date")
    private Date proDate;

    @NotNull(message = "El tiempo maximo en meses no puede estar vacío")
    @Column(name = "pro_deadline")
    private String proDeadline;

    @Column(name = "pro_budget")
    private Double proBudget;

    @Enumerated(EnumType.STRING)
    @Column(name = "pro_status")
    private ProjectStatus proStatus;

    @Transient
    private ProjectState currentState;

    public ProjectState getCurrentState() {
        if (currentState == null) {
            currentState = ProjectStateFactory.getState(this);
        }
        return currentState;
    }
}