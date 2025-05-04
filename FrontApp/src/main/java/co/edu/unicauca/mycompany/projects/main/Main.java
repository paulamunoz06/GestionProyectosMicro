package co.edu.unicauca.mycompany.projects.main;

import co.edu.unicauca.mycompany.projects.access.IUserRepository;
import co.edu.unicauca.mycompany.projects.access.RepositoryType;
import co.edu.unicauca.mycompany.projects.access.UserRepositoryFactory;
import co.edu.unicauca.mycompany.projects.domain.services.UserService;
import co.edu.unicauca.mycompany.projects.presentation.GUIinicioSesion;

public class Main {

    public static void main(String[] args) {
        IUserRepository repositoryUser = UserRepositoryFactory.getInstance().getRepository(RepositoryType.H2);
        GUIinicioSesion instance = new GUIinicioSesion(new UserService(repositoryUser));
        instance.setVisible(true);
    }
    
}
