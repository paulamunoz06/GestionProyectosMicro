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
    public Project createProject(ProjectDto projectDto);

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.UPDATEPROJECT_QUEUE)
    public Project updateProject(ProjectDto projectDto);

    @Transactional
    public Optional<Project> findById(Long id);

    public ProjectCompanyDto getProjectCompanyInfo(Long projectId) throws Exception;

    public List<ProjectDto> getAvailableProjectsForStudent(Long studentId) throws Exception;

    public int getAllProjects();

    public int getPostulatedProjects(Long studentId) throws Exception;

    public int getApprovedProjects(Long studentId) throws Exception;

    public ProjectDto projectToDto(Project project);

    public Project projectToClass(ProjectDto projectDto);
}
