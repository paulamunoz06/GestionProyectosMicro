package co.edu.unicauca.microservicecoordinator.entities;

import co.edu.unicauca.microservicecoordinator.infra.state.ProjectState;
import co.edu.unicauca.microservicecoordinator.infra.state.ProjectStateFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Entity
@Getter
@Setter
public class Project {

    @Id
    @Column(name = "PROID",nullable = false, unique = true)
    private String proId;

    @NotBlank(message = "El título del proyecto no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(name = "PROTITLE", nullable = false)
    private String proTitle;

    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 10, max = 1000)
    @Column(name = "PRODESCRIPTION",nullable = false)
    private String proDescription;

    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(name = "PROABSTRACT",nullable = false)
    private String proAbstract;

    @NotBlank(message = " Los objetivos no pueden estar vacíos")
    @Column(name = "PROGOALS",nullable = false)
    private String proGoals;

    @Temporal(TemporalType.DATE)
    @Column(name = "PRODATE",nullable = false)
    private LocalDate proDate;

    @NotNull(message = "El tiempo maximo en meses no puede estar vacío")
    @Column(name = "PRODEADLINE",nullable = false)
    private int proDeadLine;

    @Column(name = "PROBUDGET",nullable = true)
    private Double proBudget;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROSTATE",nullable = false)
    private EnumProjectState proState;

    @Transient
    @JsonIgnore
    private ProjectState currentState;

    public ProjectState getCurrentState() {
        if (currentState == null) {
            currentState = ProjectStateFactory.getState(this);
        }
        return currentState;
    }
}