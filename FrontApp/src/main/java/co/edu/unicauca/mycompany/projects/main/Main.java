package co.edu.unicauca.mycompany.projects.main;

import co.edu.unicauca.mycompany.projects.access.Factory;
import co.edu.unicauca.mycompany.projects.access.ICompanyRepository;
import co.edu.unicauca.mycompany.projects.access.IProjectRepository;
import co.edu.unicauca.mycompany.projects.access.IUserRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Company;
import co.edu.unicauca.mycompany.projects.domain.entities.Student;
import co.edu.unicauca.mycompany.projects.domain.entities.enumSector;
import co.edu.unicauca.mycompany.projects.domain.services.CompanyService;
import co.edu.unicauca.mycompany.projects.domain.services.ProjectService;
import co.edu.unicauca.mycompany.projects.domain.services.UserService;
import co.edu.unicauca.mycompany.projects.presentation.GUIDashboardEmpresa;
import co.edu.unicauca.mycompany.projects.presentation.GUIDashboardEstudiante;
import co.edu.unicauca.mycompany.projects.presentation.GUIinicioSesion;
import co.edu.unicauca.mycompany.projects.presentation.GUIregistrarEmpresa;


public class Main {
    public static void main(String[] args) {
        /*
        IUserRepository repositoryUser = Factory.getInstance().getRepositoryUser("MARIADB");
        GUIinicioSesion instance = new GUIinicioSesion(new UserService(repositoryUser));
        instance.setVisible(true);*/
        //IProjectRepository repositoryUser = Factory.getInstance().getRepositoryProject("STUDENTMICROSERVICE");
        //GUIDashboardEstudiante instance = new GUIDashboardEstudiante(new  Student("1", "paula@gmail.com", "123"), new ProjectService(repositoryUser));

        ICompanyRepository repositoryCompany = Factory.getInstance().getRepositoryCompany("COMPANY");

        GUIDashboardEmpresa instance = new GUIDashboardEmpresa(new Company("Company", "NombreContacto", "ApellidoContacto",
                "3106683378", "Gerente", enumSector.EDUCATION, "1", "sofiaarango141@gmail.com", "1234566*K"));

        instance.setVisible(true);

        /*
        // Obtener el repositorio de empresas
        ICompanyRepository repositoryCompany = Factory.getInstance().getRepositoryCompany("COMPANY");

        // Obtener el repositorio de usuarios
        IUserRepository repositoryUser = Factory.getInstance().getRepositoryUser("USER");

        // Crear los servicios necesarios
        CompanyService companyService = new CompanyService(repositoryCompany);
        UserService userService = new UserService(repositoryUser);

        // Instanciar la GUI de registro de empresa
        GUIregistrarEmpresa instance = new GUIregistrarEmpresa(companyService, userService);

        // Hacer visible la GUI
        instance.setVisible(true);
        */
    }
}


