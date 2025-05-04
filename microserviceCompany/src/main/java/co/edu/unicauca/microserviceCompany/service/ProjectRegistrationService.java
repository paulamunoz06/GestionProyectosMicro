package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.ICompanyRepository;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
/**
 * Implementación concreta del servicio de registro para proyectos.
 */
public class ProjectRegistrationService extends AbstractEntityRegistrationService<Project, ProjectDto> {

    private final IProjectRepository projectRepository;
    private final ICompanyRepository companyRepository;
    private final RabbitTemplate rabbitTemplate;

    public ProjectRegistrationService(IProjectRepository projectRepository,
                                      ICompanyRepository companyRepository,
                                      RabbitTemplate rabbitTemplate) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    protected void validateData(ProjectDto projectDto) throws IllegalArgumentException {
        if (projectDto == null) {
            throw new IllegalArgumentException("La información del proyecto no puede ser nula");
        }

        if (projectDto.getProId() == null || projectDto.getProId().isEmpty()) {
            throw new IllegalArgumentException("Id del proyecto es nulo o vacío");
        }

        if (projectDto.getCompanyId() == null || projectDto.getCompanyId().isEmpty()) {
            throw new IllegalArgumentException("ID de la compañía es nulo o vacío");
        }
    }

    @Override
    protected void checkEntityExists(ProjectDto projectDto) throws IllegalArgumentException {
        if (projectRepository.findById(projectDto.getProId()).isPresent()) {
            throw new IllegalArgumentException("El proyecto con ID " + projectDto.getProId() + " ya existe");
        }
    }

    @Override
    protected void preRegisterOperations(ProjectDto projectDto) throws Exception {
        // Verificar que la empresa existe antes de asociar el proyecto
        String companyId = projectDto.getCompanyId();
        Optional<Company> companyOpt = companyRepository.findById(companyId);

        if (companyOpt.isEmpty()) {
            throw new EntityNotFoundException("La compañía con ID " + companyId + " no existe");
        }
    }

    @Override
    protected Project dtoToEntity(ProjectDto projectDto) {
        return new Project(
                projectDto.getProId(),
                projectDto.getProTitle(),
                projectDto.getProDescription(),
                projectDto.getProAbstract(),
                projectDto.getProGoals(),
                projectDto.getProDeadLine(),
                projectDto.getProBudget(),
                projectDto.getCompanyId(),
                projectDto.getProCoordinator()
        );
    }

    @Override
    protected Project saveEntity(Project project) {
        Project savedProject = projectRepository.save(project);

        // Actualizar la lista de proyectos de la compañía
        Optional<Company> companyOpt = companyRepository.findById(project.getIdcompany());
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            company.addProject(savedProject);
            companyRepository.save(company);
        }

        return savedProject;
    }

    @Override
    protected void postRegisterOperations(Project project) throws Exception {
        // Notificar a otros microservicios sobre el nuevo proyecto
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
        projectDto.setCompanyId(project.getIdcompany());
        projectDto.setIdcompany(project.getIdcompany());
        projectDto.setProCoordinator(project.getProCoordinator());

        System.out.print(projectDto);

        rabbitTemplate.convertAndSend(RabbitMQConfig.CREATEPROJECT_QUEUE, projectDto);
    }
}