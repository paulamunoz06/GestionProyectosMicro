package co.edu.unicauca.microservicestudent.repository;

import co.edu.unicauca.microservicestudent.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStudentRepository extends JpaRepository<Student, Long> {

}
