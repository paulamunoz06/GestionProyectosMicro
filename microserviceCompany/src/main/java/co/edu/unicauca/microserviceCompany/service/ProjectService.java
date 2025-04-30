package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private ICompanyRepository companyRepository;

    @Autowired
    private ICompanyService companyService;

    @Override
    @Transactional
    public Project createProject(ProjectDto projectDto) {
        try {
            if (projectDto.getProId() == null || projectDto.getProId().isEmpty()) {
                throw new IllegalArgumentException("Id del proyecto es nulo o vacío");
            }

            if (projectRepository.findById(projectDto.getProId()).isPresent()) {
                throw new IllegalArgumentException("El proyecto con ID " + projectDto.getProId() + " ya existe");
            }

            if (projectDto.getCompanyId() == null || projectDto.getCompanyId().isEmpty()) {
                throw new IllegalArgumentException("ID de la compañía es nulo o vacío");
            }

            // Buscar la compañía para asociarla al proyecto
            Long companyId = Long.parseLong(projectDto.getCompanyId());
            Optional<Company> companyOpt = companyRepository.findById(companyId);

            if (companyOpt.isEmpty()) {
                throw new EntityNotFoundException("La compañía con ID " + companyId + " no existe");
            }

            // Convertir DTO a entidad Project
            Project project = projectToClass(projectDto);

            // Asignar explícitamente la compañía al proyecto
            Company company = companyOpt.get();
            project.setCompany(company);

            // Guardar el proyecto
            Project projectSaved = projectRepository.save(project);

            // Actualizar la lista de proyectos de la compañía
            company.addProject(projectSaved);
            companyRepository.save(company);

            // Notificar a otros microservicios sobre el nuevo proyecto
            rabbitTemplate.convertAndSend(RabbitMQConfig.CREATEPROJECT_QUEUE, projectToDto(projectSaved));

            return projectSaved;
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Error al crear el proyecto: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id del proyecto es nulo o vacío");
        }
        return projectRepository.findById(id);
    }

    @Override
    public CompanyDto getCompanyInfo(String projectId) throws Exception {
        if (projectId == null || projectId.isEmpty()) {
            throw new IllegalArgumentException("Id del proyecto es nulo o vacío");
        }

        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isEmpty()) {
            throw new EntityNotFoundException("El proyecto con ID " + projectId + " no existe");
        }

        Project project = projectOpt.get();
        Company company = project.getCompany();

        if (company == null) {
            throw new EntityNotFoundException("El proyecto no tiene una compañía asociada");
        }

        return companyService.companyToDto(company);
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

        // Asignar el ID de la compañía si existe
        if (project.getCompany() != null) {
            projectDto.setCompanyId(project.getCompany().getId().toString());
        }

        return projectDto;
    }

    @Override
    public Project projectToClass(ProjectDto projectDto) {
        Project project = new Project(
                projectDto.getProId(),
                projectDto.getProTitle(),
                projectDto.getProDescription(),
                projectDto.getProAbstract(),
                projectDto.getProGoals(),
                projectDto.getProDeadLine(),
                projectDto.getProBudget()
        );

        // Si la fecha es nula, establecemos la fecha actual
        if (projectDto.getProDate() == null) {
            project.setProDate(LocalDate.now());
        } else {
            project.setProDate(projectDto.getProDate());
        }

        // Si el estado está definido, lo establecemos
        if (projectDto.getProState() != null && !projectDto.getProState().isEmpty()) {
            try {
                project.setProState(EnumProjectState.valueOf(projectDto.getProState()));
            } catch (IllegalArgumentException e) {
                // Si el estado no es válido, establecemos el estado por defecto
                project.setProState(EnumProjectState.RECIBIDO);
            }
        } else {
            // Si no hay estado, establecemos el estado por defecto
            project.setProState(EnumProjectState.RECIBIDO);
        }

        return project;
    }
}