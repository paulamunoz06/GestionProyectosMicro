package co.edu.unicauca.mycompany.projects.main;

import co.edu.unicauca.mycompany.projects.access.Factory;
import co.edu.unicauca.mycompany.projects.access.IProjectRepository;
import co.edu.unicauca.mycompany.projects.access.IUserRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Student;
import co.edu.unicauca.mycompany.projects.domain.services.ProjectService;
import co.edu.unicauca.mycompany.projects.domain.services.UserService;
import co.edu.unicauca.mycompany.projects.presentation.GUIDashboardEstudiante;
import co.edu.unicauca.mycompany.projects.presentation.GUIinicioSesion;

public class Main {

    public static void main(String[] args) {
        /*
        IUserRepository repositoryUser = Factory.getInstance().getRepositoryUser("MARIADB");
        GUIinicioSesion instance = new GUIinicioSesion(new UserService(repositoryUser));
        instance.setVisible(true);*/
        IProjectRepository repositoryUser = Factory.getInstance().getRepositoryProject("STUDENTMICROSERVICE");
        GUIDashboardEstudiante instance = new GUIDashboardEstudiante(new  Student("1", "paula@gmail.com", "123"), new ProjectService(repositoryUser));
        instance.setVisible(true);
    }
    
}
