package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class JpaCoordinatorEntity {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String coordinatorId;

    @Column(nullable = false, unique = true)
    private String coordinatorEmail;

    @Column(nullable = false)
    private String coordinatorPassword;

    public JpaCoordinatorEntity() {

    }
}
