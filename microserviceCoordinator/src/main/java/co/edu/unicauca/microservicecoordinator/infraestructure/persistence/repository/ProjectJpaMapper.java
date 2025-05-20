package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository;

import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;

public class ProjectJpaMapper {
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
