package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class JpaProjectEntity {
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "proId"))
    private ProjectId proId;

    @Embedded
    @Column(nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "proTitle"))
    private ProjectTitle proTitle;

    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Column(nullable = false)
    private String proDescription;

    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(nullable = false)
    private String proAbstract;

    @NotBlank(message = " Los objetivos no pueden estar vacíos")
    @Column(nullable = false)
    private String proGoals;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "proDate"))
    @Column(nullable = false)
    private ProjectRegistrationDate proDate;

    @Embedded
    @AttributeOverride(name = "months", column = @Column(name = "proDeadLine"))
    @Column(nullable = false)
    private ProjectDeadline proDeadLine;

    @Column(nullable = true)
    private Double proBudget;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumProjectState proState;

    @Column(nullable = true)
    private String idcompany;

    @Column(nullable = true)
    private String proCoordinator;

    public JpaProjectEntity() {

    }
}
