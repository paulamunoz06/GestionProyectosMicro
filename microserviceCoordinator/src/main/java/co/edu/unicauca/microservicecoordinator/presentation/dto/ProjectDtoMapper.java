package co.edu.unicauca.microservicecoordinator.presentation.dto;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectDeadline;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectRegistrationDate;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectTitle;

public class ProjectDtoMapper {
    public static ProjectDto projectToDto(Project project) {
        return new ProjectDto(
                project.getProId().toString(),
                project.getProTitle().toString(),
                project.getProDescription(),
                project.getProAbstract(),
                project.getProGoals(),
                project.getProDate().getDate(),
                project.getProDeadLine().getMonths(),
                project.getProBudget(),
                project.getProState().toString(),
                project.getIdcompany(),
                project.getProCoordinator()
        );
    }

    public static Project projectToClass(ProjectDto projectDto) {
        return new Project(
                new ProjectId(projectDto.getProId()),
                new ProjectTitle(projectDto.getProTitle()),
                projectDto.getProDescription(),
                projectDto.getProAbstract(),
                projectDto.getProGoals(),
                new ProjectRegistrationDate(projectDto.getProDate()),
                new ProjectDeadline(projectDto.getProDeadLine()),
                projectDto.getProBudget(),
                EnumProjectState.valueOf(projectDto.getProState()),
                projectDto.getIdcompany(),
                projectDto.getProCoordinator()
        );
    }
}