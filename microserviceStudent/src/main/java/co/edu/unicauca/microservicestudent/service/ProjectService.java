package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.CompanyDto;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService implements IProjectService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATEPROJECT_QUEUE)
    @Transactional
    public Project createProject(ProjectDto projectDto) {
        try {
            if (projectDto.getProId() == null) {
                throw new EntityNotFoundException("Id del proyecto es nulo");
            }
            if (projectRepository.findById(projectDto.getProId()).isPresent()) {
                throw new IllegalAccessException("El proyecto con ID " + projectDto.getProId() + " ya existe");
            }

            Project project = projectToClass(projectDto);
            Project projectSaved = projectRepository.save(project);

            rabbitTemplate.convertAndSend(RabbitMQConfig.CREATEPROJECT_QUEUE, projectSaved);

            return projectSaved;
        }catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Mensaje inválido, no reenviar");
        }

    }

    @Override
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.UPDATEPROJECT_QUEUE)
    public Project updateProject(ProjectDto projectDto){
        try{
            if (projectDto.getProId() == null) {
                throw new EntityNotFoundException("Id del proyecto es nulo");
            }
            if (projectRepository.findById(projectDto.getProId()).isPresent()) {
                throw new IllegalAccessException("El proyecto con ID " + projectDto.getProId() + " ya existe");
            }

            Project project = projectToClass(projectDto);
            Project projectSaved = projectRepository.save(project);

            rabbitTemplate.convertAndSend(RabbitMQConfig.UPDATEPROJECT_QUEUE, projectSaved);

            return projectSaved;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Mensaje inválido, no reenviar");
        }
    }

    @Override
    @Transactional
    public Optional<Project> findById(String id) {
        return projectRepository.findById(id);
    }

    @Override
    public CompanyDto getCompanyInfo(String projectId){
        if (projectId == null) {
            throw new EntityNotFoundException("Id del proyecto es nulo");
        }
        if (projectRepository.findById(projectId).isEmpty()) {
            throw new EntityNotFoundException("El proyecto con ID " + projectId + " no existe");
        }

        //return (CompanyDto) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.PROJECTCOMPANYINFO_QUEUE, projectId);
        return new CompanyDto(
                "COMP001",                            // id
                "empresa@ficticia.com",              // email
                "encryptedPassword123",              // password
                "Empresa Ficticia S.A.",             // companyName
                "Laura",                              // contactName
                "Gómez",                              // contactLastName
                "3001234567",                         // contactPhone
                "Gerente de TI",                      // contactPosition
                "TECHNOLOGY"                          // companySector
        );
    }

    @Override
    public List<ProjectDto> getAvailableProjectsForStudent(String studentId){
        if (studentId == null) {
            throw new EntityNotFoundException("Id del estudiante es nulo");
        }
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new EntityNotFoundException("El estudiante con ID " + studentId + " no existe");
        }

        List<Project> projects = projectRepository.findAvailableProjectsForStudent(studentId);

        return projects.stream().map(this::projectToDto).collect(Collectors.toList());
    }

    @Override
    public int getAllProjects() {
        return projectRepository.countAllProjects();
    }

    @Override
    public int getPostulatedProjects(String studentId){
        if (studentId == null) {
            throw new EntityNotFoundException("Id del estudiante es nulo");
        }
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new EntityNotFoundException("El estudiante con ID " + studentId + " no existe");
        }
        return projectRepository.countPostulatedProjects(studentId);
    }

    @Override
    public int getApprovedProjects(String studentId) {
        if (studentId == null) {
            throw new EntityNotFoundException("Id del estudiante es nulo");
        }
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new EntityNotFoundException("El estudiante con ID " + studentId + " no existe");
        }
        return projectRepository.countApprovedProjects(studentId);
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
        projectDto.setPostulated(project.getPostulated().stream().map(Student::getId).collect(Collectors.toList()));
        projectDto.setApproved(project.getApproved().stream().map(Student::getId).collect(Collectors.toList()));

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
        List<Student> postulatedStudents = studentRepository.findAllById(projectDto.getPostulated());
        projectClass.setPostulated(postulatedStudents);
        List<Student> approvedStudents = studentRepository.findAllById(projectDto.getApproved());
        projectClass.setApproved(approvedStudents);

        return projectClass;
    }
}
