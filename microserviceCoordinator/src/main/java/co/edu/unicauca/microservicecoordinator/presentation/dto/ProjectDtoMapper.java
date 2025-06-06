package co.edu.unicauca.microservicecoordinator.presentation.dto;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectDeadline;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectRegistrationDate;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectTitle;

/**
 * Mapper para convertir entre la clase de dominio Project y el Data Transfer Object {@link ProjectDto}.
 *
 * Esta clase facilita la transformación entre el modelo interno de negocio y el DTO utilizado para la comunicación
 * con capas externas, como la API REST.
 */
public class ProjectDtoMapper {

    /**
     * Convierte una entidad de dominio Project a un DTO ProjectDto.
     *
     * @param project Entidad de dominio a convertir.
     * @return DTO correspondiente con los datos del proyecto.
     */
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

    /**
     * Convierte un DTO ProjectDto a una entidad de dominio Project.
     *
     * @param projectDto DTO a convertir.
     * @return Entidad de dominio correspondiente con los datos del DTO.
     */
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