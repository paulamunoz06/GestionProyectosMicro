package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Student;

public class StudentRepository implements IStudentRepository {
    @Override
    public Student getStudent(String id) {
        return new Student("1","example@example.com","password123");
    }
}
