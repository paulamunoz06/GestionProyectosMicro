package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IProjectService {
    @RabbitListener(queues = RabbitMQConfig.CREATEPROJECT_QUEUE)
    @Transactional
    Project createProject(ProjectDto projectDto);

    @Transactional
    Optional<Project> findById(String id);

    CompanyDto getCompanyInfo(String projectId) throws Exception;

    ProjectDto projectToDto(Project project);

    Project projectToClass(ProjectDto projectDto);
}
