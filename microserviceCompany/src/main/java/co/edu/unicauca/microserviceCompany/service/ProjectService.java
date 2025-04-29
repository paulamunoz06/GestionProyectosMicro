package co.edu.unicauca.microserviceCompany.service;

import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.infra.config.RabbitMQConfig;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.infra.dto.ProjectDto;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProjectService implements IProjectService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IProjectRepository projectRepository;

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

        return projectClass;
    }
}
