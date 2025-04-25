package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import co.edu.unicauca.microservicestudent.infra.dto.ProjectCompanyDto;
import co.edu.unicauca.microservicestudent.infra.dto.ProjectDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IProjectService {
    @RabbitListener(queues = RabbitMQConfig.CREATEPROJECT_QUEUE)
    @Transactional
    Project createProject(ProjectDto projectDto);

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.UPDATEPROJECT_QUEUE)
    Project updateProject(ProjectDto projectDto);

    @Transactional
    Optional<Project> findById(Long id);

    ProjectCompanyDto getProjectCompanyInfo(Long projectId) throws Exception;

    List<ProjectDto> getAvailableProjectsForStudent(Long studentId) throws Exception;

    int getAllProjects();

    int getPostulatedProjects(Long studentId);

    int getApprovedProjects(Long studentId);

    ProjectDto projectToDto(Project project);

    Project projectToClass(ProjectDto projectDto);
}
