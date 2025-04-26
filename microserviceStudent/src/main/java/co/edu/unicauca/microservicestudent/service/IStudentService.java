package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.StudentDto;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface IStudentService {

    @Transactional
    Student studentPostulation(Student student, Project project) throws Exception;

    @Transactional
    Optional<Student> findById(String studentId);

    StudentDto studentToDto(Student student);
}


