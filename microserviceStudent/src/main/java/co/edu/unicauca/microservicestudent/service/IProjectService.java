package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import co.edu.unicauca.microservicestudent.infra.dto.CompanyDto;
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
    Optional<Project> findById(String id);

    CompanyDto getCompanyInfo(String projectId) throws Exception;

    List<ProjectDto> getAvailableProjectsForStudent(String studentId) throws Exception;

    int getAllProjects();

    int getPostulatedProjects(String studentId);

    int getApprovedProjects(String studentId);

    ProjectDto projectToDto(Project project);

    Project projectToClass(ProjectDto projectDto);
}
