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

/**
 * Servicio que implementa la interfaz {@link IProjectService} para gestionar las operaciones relacionadas con los proyectos.
 * Este servicio maneja la creación de proyectos, la consulta de información sobre proyectos y empresas asociadas,
 * y la conversión entre entidades {@link Project} y DTOs {@link ProjectDto}.
 */
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

    // Servicio que implementa el patrón Template Method para el registro de proyectos
    private final ProjectRegistrationService projectRegistrationService;

    @Autowired
    public ProjectService(RabbitTemplate rabbitTemplate, IProjectRepository projectRepository,
                          ICompanyRepository companyRepository, ICompanyService companyService) {
        this.rabbitTemplate = rabbitTemplate;
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.companyService = companyService;
        this.projectRegistrationService = new ProjectRegistrationService(projectRepository, companyRepository, rabbitTemplate);
    }

    /**
     * Crea un nuevo proyecto usando el patrón Template Method.
     *
     * @param projectDto DTO con la información del proyecto a crear.
     * @return El proyecto creado.
     * @throws AmqpRejectAndDontRequeueException Si ocurre un error al intentar crear el proyecto.
     */
    // En ProjectService.java, modificar el método createProject para validar mejor el companyId

    @Override
    @Transactional
    public Project createProject(ProjectDto projectDto) {
        try {
            // Validar que el projectDto no sea nulo
            if (projectDto == null) {
                throw new IllegalArgumentException("La información del proyecto no puede ser nula");
            }

            // Validar que el ID de la compañía esté presente
            String companyIdOrEmail = projectDto.getCompanyId();
            if (companyIdOrEmail == null || companyIdOrEmail.trim().isEmpty()) {
                throw new IllegalArgumentException("El ID de la compañía no puede estar vacío");
            }

            // Comprobar si el companyId parece ser un email (contiene @)
            if (companyIdOrEmail.contains("@")) {
                // Intentar encontrar la compañía por email primero
                Optional<Company> companyByEmail = companyRepository.findByEmail(companyIdOrEmail);
                if (companyByEmail.isPresent()) {
                    // Si encontramos la compañía por email, usamos su ID real
                    String realCompanyId = companyByEmail.get().getId();
                    projectDto.setCompanyId(realCompanyId);
                    System.out.println("Converted company email to ID: " + realCompanyId);
                } else {
                    // Si no encontramos la compañía con ese email
                    throw new EntityNotFoundException("No existe una compañía con el email: " + companyIdOrEmail);
                }
            } else {
                // Verificar que la compañía con ese ID existe
                Optional<Company> companyById = companyRepository.findById(companyIdOrEmail);
                if (companyById.isEmpty()) {
                    throw new EntityNotFoundException("La compañía con ID " + companyIdOrEmail + " no existe");
                }
            }

            // Ajustar también el campo idcompany si existe en el DTO
            if (projectDto.getIdcompany() != null) {
                projectDto.setIdcompany(projectDto.getCompanyId());
            }

            // Continuar con el registro usando el servicio de registro
            return projectRegistrationService.registerEntity(projectDto);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Error al crear el proyecto: " + e.getMessage());
        }
    }

    /**
     * Busca un proyecto por su ID.
     *
     * @param id Identificador único del proyecto.
     * @return Un objeto Optional con el proyecto encontrado.
     * @throws IllegalArgumentException Si el ID del proyecto es nulo o vacío.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id del proyecto es nulo o vacío");
        }
        return projectRepository.findById(id);
    }

    /**
     * Obtiene la información de la empresa asociada a un proyecto.
     *
     * @param projectId Identificador del proyecto.
     * @return Un DTO con la información de la empresa asociada.
     * @throws Exception Si ocurre un error al obtener la información de la empresa.
     */
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
        Company company = companyRepository.findCompanyById(project.getIdcompany()).get();

        if (company == null) {
            throw new EntityNotFoundException("El proyecto no tiene una compañía asociada");
        }

        return companyService.companyToDto(company);
    }

    /**
     * Convierte una entidad {@link Project} a su correspondiente DTO {@link ProjectDto}.
     *
     * @param project Entidad {@link Project} a convertir.
     * @return El DTO correspondiente al proyecto.
     */
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
        if (project.getIdcompany() != null) {
            projectDto.setCompanyId(project.getIdcompany());
        }

        return projectDto;
    }

    /**
     * Convierte un DTO {@link ProjectDto} en su entidad correspondiente {@link Project}.
     *
     * @param projectDto DTO con los datos del proyecto.
     * @return La entidad {@link Project} correspondiente.
     */
    @Override
    public Project projectToClass(ProjectDto projectDto) {
        Project project = new Project(
                projectDto.getProId(),
                projectDto.getProTitle(),
                projectDto.getProDescription(),
                projectDto.getProAbstract(),
                projectDto.getProGoals(),
                projectDto.getProDeadLine(),
                projectDto.getProBudget(),
                projectDto.getIdcompany(),
                projectDto.getProCoordinator()
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

    /**
     * Actualiza un proyecto existente con la información proporcionada en el DTO.
     *
     * @param projectDto DTO con los datos actualizados del proyecto.
     * @return El proyecto actualizado.
     * @throws IllegalArgumentException Si hay datos inválidos en el DTO.
     * @throws EntityNotFoundException Si el proyecto a actualizar no existe.
     */
    @Override
    @Transactional
    public Project updateProject(ProjectDto projectDto) throws IllegalArgumentException, EntityNotFoundException {
        if (projectDto == null) {
            throw new IllegalArgumentException("La información del proyecto no puede ser nula");
        }

        if (projectDto.getProId() == null || projectDto.getProId().isEmpty()) {
            throw new IllegalArgumentException("ID del proyecto es nulo o vacío");
        }

        Optional<Project> projectOpt = projectRepository.findById(projectDto.getProId());
        if (projectOpt.isEmpty()) {
            throw new EntityNotFoundException("El proyecto con ID " + projectDto.getProId() + " no existe");
        }

        Project project = projectOpt.get();

        // Actualizar solo los campos no nulos del DTO
        if (projectDto.getProTitle() != null && !projectDto.getProTitle().isEmpty()) {
            project.setProTitle(projectDto.getProTitle());
        }

        if (projectDto.getProDescription() != null) {
            project.setProDescription(projectDto.getProDescription());
        }

        if (projectDto.getProAbstract() != null) {
            project.setProAbstract(projectDto.getProAbstract());
        }

        if (projectDto.getProGoals() != null) {
            project.setProGoals(projectDto.getProGoals());
        }

        if (projectDto.getProDeadLine() != 0) {
            project.setProDeadLine(projectDto.getProDeadLine());
        }

        if (projectDto.getProBudget() != null) {
            project.setProBudget(projectDto.getProBudget());
        }

        // Actualizar estado si viene especificado en el DTO
        if (projectDto.getProState() != null && !projectDto.getProState().isEmpty()) {
            try {
                project.setProState(EnumProjectState.valueOf(projectDto.getProState()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de proyecto inválido: " + projectDto.getProState());
            }
        }

        // Guardar el proyecto actualizado
        Project updatedProject = projectRepository.save(project);

        // Notificar a otros microservicios sobre la actualización del proyecto
        rabbitTemplate.convertAndSend(RabbitMQConfig.PROJECTCOMPANYINFO_QUEUE, projectToDto(updatedProject));

        return updatedProject;
    }
}