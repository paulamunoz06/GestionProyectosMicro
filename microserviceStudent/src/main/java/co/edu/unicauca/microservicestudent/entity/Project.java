package co.edu.unicauca.microservicestudent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    @ManyToMany
    @JoinTable(
            name = "postulated",
            joinColumns = @JoinColumn(name = "proId"), // clave de esta entidad
            inverseJoinColumns = @JoinColumn(name = "studentId") // clave de la otra entidad
    )
    private List<Student> postulated = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "approved",
            joinColumns = @JoinColumn(name = "proId"),
            inverseJoinColumns = @JoinColumn(name = "studentId")
    )
    private List<Student> approved = new ArrayList<>();

    public Project(String proid, String protitle, String prodescription, String proAbstract, String proGoals, int proDeadline, Double proBudget) {
        this.proId = proid;
        this.proTitle = protitle;
        this.proDescription = prodescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = LocalDate.now();
        this.proDeadLine = proDeadline;
        this.proBudget = proBudget;
        this.proState = EnumProjectState.RECIBIDO;
    }

}
