package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.StudentDto;
import co.edu.unicauca.microservicestudent.repository.IProjectRepository;
import co.edu.unicauca.microservicestudent.repository.IStudentRepository;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IProjectRepository projectRepository;

    @Override
    @Transactional
    public Student studentPostulation(Student student, Project project) throws Exception {
        try {
            if(project.getProState() != EnumProjectState.ACEPTADO) throw new Exception("El proyecto no recibe postulaciones");
            if(student.getPostulated().contains(project)) throw new Exception("El estudiante ya se postuló al proyecto");
            if(student.getApproved().contains(project)) throw new Exception("El estudiante ya es parte del proyecto");

            student.getPostulated().add(project);
            project.getPostulated().add(student);

            studentRepository.save(student);
            projectRepository.save(project);

            StudentDto studentDto = studentToDto(student);

            rabbitTemplate.convertAndSend(RabbitMQConfig.STUDENTPOSTULATION_QUEUE, studentDto);

            return student;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Student> findById(Long studentId) {
        return studentRepository.findById(studentId);
    }

    @Override
    public StudentDto studentToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        studentDto.setPassword(student.getPassword());
        studentDto.setPostulatedIds(student.getPostulated().stream().map(Project::getProid).collect(Collectors.toList()));
        studentDto.setApprovedIds(student.getApproved().stream().map(Project::getProid).collect(Collectors.toList()));
        return studentDto;
    }
}
