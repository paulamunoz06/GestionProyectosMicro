package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import co.edu.unicauca.microservicestudent.infra.dto.StudentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface IStudentService {

    @Transactional
    public Student studentPostulation(Student student, Project project) throws Exception;

    @Transactional
    public Optional<Student> findById(Long studentId);

    public StudentDto studentToDto(Student student);
}


