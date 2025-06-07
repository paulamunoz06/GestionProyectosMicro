package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository;

import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;

/**
 * Mapper para convertir entre la entidad JPA JpaProjectEntity y la entidad de dominio {@link Project}.
 *
 * Esta clase facilita la transformación entre las dos representaciones del proyecto en las capas de
 * infraestructura y dominio, respectivamente.
 */
public class ProjectJpaMapper {

    /**
     * Convierte una entidad de dominio Project a una entidad JPA JpaProjectEntity.
     *
     * @param project Entidad de dominio a convertir.
     * @return Entidad JPA correspondiente.
     */
    public static JpaProjectEntity toJpaEntity(Project project) {
        return new JpaProjectEntity(
                project.getProId(),
                project.getProTitle(),
                project.getProDescription(),
                project.getProAbstract(),
                project.getProGoals(),
                project.getProDate(),
                project.getProDeadLine(),
                project.getProBudget(),
                project.getProState(),
                project.getIdcompany(),
                project.getProCoordinator()
        );
    }

    /**
     * Convierte una entidad JPA JpaProjectEntity a una entidad de dominio Project.
     *
     * @param jpaProject Entidad JPA a convertir.
     * @return Entidad de dominio correspondiente.
     */
    public static Project toDomainEntity(JpaProjectEntity jpaProject) {
        return new Project(
                jpaProject.getProId(),
                jpaProject.getProTitle(),
                jpaProject.getProDescription(),
                jpaProject.getProAbstract(),
                jpaProject.getProGoals(),
                jpaProject.getProDate(),
                jpaProject.getProDeadLine(),
                jpaProject.getProBudget(),
                jpaProject.getProState(),
                jpaProject.getIdcompany(),
                jpaProject.getProCoordinator()
        );
    }
}
