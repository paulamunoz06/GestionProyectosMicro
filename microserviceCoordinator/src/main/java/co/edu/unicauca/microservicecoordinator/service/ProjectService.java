package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.infra.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.repository.IProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    IProjectRepository projectRepository;

    @Override
    public Optional<Project> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public ProjectDto projectToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();

        projectDto.setProId(project.getProId());
        projectDto.setProTitle(project.getProTitle());
        projectDto.setProDescription(project.getProDescription());
        projectDto.setProAbstract(project.getProAbstract());
        projectDto.setProGoals(project.getProGoals());
        projectDto.setProDate(project.getProDate());
        projectDto.setProDeadLine(project.getProDeadLine());
        projectDto.setProBudget(project.getProBudget());
        projectDto.setProState(project.getProState().toString());

        return projectDto;
    }

    @Override
    public Project projectToClass(ProjectDto projectDto) {
        Project projectClass = new Project();

        projectClass.setProId(projectDto.getProId());
        projectClass.setProTitle(projectDto.getProTitle());
        projectClass.setProDescription(projectDto.getProDescription());
        projectClass.setProAbstract(projectDto.getProAbstract());
        projectClass.setProGoals(projectDto.getProGoals());
        projectClass.setProDate(projectDto.getProDate());
        projectClass.setProDeadLine(projectDto.getProDeadLine());
        projectClass.setProBudget(projectDto.getProBudget());
        projectClass.setProState(EnumProjectState.valueOf(projectDto.getProState()));

        return projectClass;
    }

    @Override
    public Long countByStatus(String status) {
        // Convierte el String a EnumProjectState
        EnumProjectState enumStatus = EnumProjectState.valueOf(status.toUpperCase());
        return projectRepository.countByProState(enumStatus);
    }

    // Método para contar el total de proyectos
    @Override
    public int countTotalProjects() {
        return (int) projectRepository.count(); // Devuelve el número total de proyectos
    }



}
