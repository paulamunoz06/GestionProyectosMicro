package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.ProjectDto;
import co.edu.unicauca.microservicestudent.repository.IProjectRepository;
import co.edu.unicauca.microservicestudent.repository.IStudentRepository;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los proyectos.
 *
 * Implementa los métodos definidos en la interfaz {@link IProjectService} para manejar la creación, actualización,
 * obtención y conversión de proyectos, así como la integración con RabbitMQ para la actualización de proyectos.
 */
@Service
public class ProjectService implements IProjectService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IStudentRepository studentRepository;

    /**
     * Actualiza un proyecto a partir de los datos recibidos desde RabbitMQ.
     *
     * @param projectDto El objeto DTO que contiene los datos actualizados del proyecto.
     * @throws AmqpRejectAndDontRequeueException Si el mensaje recibido es inválido y no debe ser reenviado.
     */
    @Override
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.UPDATEPROJECT_QUEUE)
    public void updateProject(ProjectDto projectDto) {
        try {
            // Verifica que el ID del proyecto no sea nulo.
            if (projectDto.getProId() == null) {
                throw new EntityNotFoundException("Id del proyecto es nulo");
            }

            // Convierte el DTO a entidad y guarda el proyecto actualizado.
            Project project = projectToClass(projectDto);
            projectRepository.save(project);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Si ocurre un error, no reenviar el mensaje a la cola.
            throw new AmqpRejectAndDontRequeueException("Mensaje inválido, no reenviar");
        }
    }

    /**
     * Busca un proyecto por su ID.
     *
     * @param id El ID del proyecto a buscar.
     * @return Un {@link Optional} que contiene el proyecto si existe, o está vacío si no se encuentra.
     */
    @Override
    @Transactional
    public Optional<Project> findById(String id) {
        return projectRepository.findById(id);
    }

    /**
     * Obtiene los proyectos disponibles para un estudiante, es decir, aquellos que están aceptados
     * y no han sido postulados ni aprobados por el estudiante.
     *
     * @param studentId El ID del estudiante que solicita los proyectos disponibles.
     * @return Una lista de objetos {@link ProjectDto} que representan los proyectos disponibles.
     * @throws EntityNotFoundException Si el estudiante no existe.
     */
    @Override
    public List<ProjectDto> getAvailableProjectsForStudent(String studentId) {
        // Verifica que el ID del estudiante no sea nulo.
        if (studentId == null) {
            throw new EntityNotFoundException("Id del estudiante es nulo");
        }
        // Verifica que el estudiante exista.
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new EntityNotFoundException("El estudiante con ID " + studentId + " no existe");
        }

        // Obtiene los proyectos disponibles para el estudiante.
        List<Project> projects = projectRepository.findAvailableProjectsForStudent(studentId);
        return projects.stream().map(this::projectToDto).collect(Collectors.toList());
    }

    /**
     * Obtiene la cantidad total de proyectos en el sistema.
     *
     * @return El número total de proyectos en el sistema.
     */
    @Override
    public int getAllProjects() {
        return projectRepository.countAllProjects();
    }

    /**
     * Obtiene la cantidad de proyectos a los cuales un estudiante se ha postulado.
     *
     * @param studentId El ID del estudiante.
     * @return El número de proyectos a los cuales el estudiante se ha postulado.
     * @throws EntityNotFoundException Si el estudiante no existe.
     */
    @Override
    public int getPostulatedProjects(String studentId) {
        // Verifica que el ID del estudiante no sea nulo.
        if (studentId == null) {
            throw new EntityNotFoundException("Id del estudiante es nulo");
        }
        // Verifica que el estudiante exista.
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new EntityNotFoundException("El estudiante con ID " + studentId + " no existe");
        }
        return projectRepository.countPostulatedProjects(studentId);
    }

    /**
     * Obtiene la cantidad de proyectos aprobados para un estudiante.
     *
     * @param studentId El ID del estudiante.
     * @return El número de proyectos aprobados para el estudiante.
     * @throws EntityNotFoundException Si el estudiante no existe.
     */
    @Override
    public int getApprovedProjects(String studentId) {
        // Verifica que el ID del estudiante no sea nulo.
        if (studentId == null) {
            throw new EntityNotFoundException("Id del estudiante es nulo");
        }
        // Verifica que el estudiante exista.
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new EntityNotFoundException("El estudiante con ID " + studentId + " no existe");
        }
        return projectRepository.countApprovedProjects(studentId);
    }

    /**
     * Convierte un objeto {@link Project} en un objeto {@link ProjectDto}.
     *
     * @param project El proyecto que se desea convertir.
     * @return El objeto {@link ProjectDto} que representa el proyecto.
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
        projectDto.setPostulated(project.getPostulated().stream().map(Student::getId).collect(Collectors.toList()));
        projectDto.setApproved(project.getApproved().stream().map(Student::getId).collect(Collectors.toList()));
        projectDto.setIdcompany(project.getIdcompany());
        projectDto.setProCoordinator(project.getProCoordinator());
        return projectDto;
    }

    /**
     * Convierte un objeto {@link ProjectDto} en un objeto {@link Project}.
     *
     * @param projectDto El DTO del proyecto que se desea convertir.
     * @return El objeto {@link Project} correspondiente al DTO.
     */
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
        List<Student> postulatedStudents = studentRepository.findAllById(projectDto.getPostulated());
        projectClass.setPostulated(postulatedStudents);
        List<Student> approvedStudents = studentRepository.findAllById(projectDto.getApproved());
        projectClass.setApproved(approvedStudents);
        projectClass.setIdcompany(projectDto.getIdcompany());
        projectClass.setProCoordinator(projectDto.getProCoordinator());

        return projectClass;
    }
}