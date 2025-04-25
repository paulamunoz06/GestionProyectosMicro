package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Project;
import co.edu.unicauca.mycompany.projects.domain.entities.Student;

import java.util.Date;
import java.util.List;

public class ProjectRepository implements IProjectRepository {
    @Override
    public boolean save(Project newProject) {
        return false;
    }

    @Override
    public List<Project> listAll() {
        return List.of();
    }

    @Override
    public List<Project> listProjectsAvailable(String studentId) {
        return List.of();
    }

    @Override
    public Project getProject(String id) {
        return new Project("1","ExampleTitle","ExampleDesc","","",1,new Date(),1,"");
    }

    @Override
    public boolean apply(String studentId, String projectId) {
        return false;
    }

    @Override
    public List<Integer> countProjectsStudent(String studentId) {
        return List.of();
    }

    @Override
    public int countByStatus(String status) {
        return 0;
    }

    @Override
    public int countTotalProjects() {
        return 0;
    }

    @Override
    public boolean updateProjectStatus(String projectId, String newStatus) {
        return false;
    }

    @Override
    public boolean existProjectId(String projectId) {
        return false;
    }
}
