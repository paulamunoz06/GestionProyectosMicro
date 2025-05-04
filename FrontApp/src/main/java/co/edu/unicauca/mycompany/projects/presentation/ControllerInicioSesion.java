package co.edu.unicauca.mycompany.projects.presentation;

import co.edu.unicauca.mycompany.projects.access.CompanyRepositoryFactory;
import co.edu.unicauca.mycompany.projects.access.ICompanyRepository;
import co.edu.unicauca.mycompany.projects.access.RepositoryType;
import co.edu.unicauca.mycompany.projects.domain.services.CompanyService;
import co.edu.unicauca.mycompany.projects.domain.services.UserService;
import co.edu.unicauca.mycompany.projects.infra.Messages;

/**
 * Controlador para la vista de inicio de sesión.
 * Gestiona la autenticación de usuarios y la redirección a la vista correspondiente.
 */
public class ControllerInicioSesion {
    /**
     * Servicio de usuario para manejar la autenticación.
     */
    private final UserService service;
    
    /**
     * Vista de inicio de sesión a la que este controlador da soporte.
     */
    private final GUIinicioSesion view;

    /**
     * Constructor parametrizado de Controller
     *
     * @param prmService Instancia del servicio
     * @param prmView Instancia de la vista a la que va a servir como
     * controller, en este caso GUIinicioSesion
     */
    public ControllerInicioSesion(UserService prmService, GUIinicioSesion prmView) {
        view = prmView;
        service = prmService;
    }

    /**
     * Se encarga de redirigir a la pagina correspondiente dependiendo del
     * resultado que devuelva el servicio
     *
     * @param userName Es el nombre de usuario como string
     * @param enteredPassword Es la contraseña como char[]
     */
    public void actionButtomLogin(String userName, char[] enteredPassword) {
        if(userName.isEmpty() || enteredPassword.length == 0){
            Messages.mensajeVario("Ambos campos son obligatorios");
            return;
        }
        int result = service.iniciarSesion(userName, enteredPassword);
        if (result == 0) {
            Messages.mensajeVario("Usuario o clave incorrecta");
            return;
        }

        Dashboard dashboard = DashboardFactory.getInstance().crearDashboard(result, userName);

        if (dashboard != null) {
            view.dispose();
            dashboard.mostrar();
        } else {
            Messages.mensajeVario("ERROR EN BASE DE DATOS");

        }
    }

    void actionButtomRegister() {
        view.dispose();
        ICompanyRepository repositoryCompany = CompanyRepositoryFactory.getInstance().getRepository(RepositoryType.H2);
        CompanyService companyService = new CompanyService(repositoryCompany);
        GUIregistrarEmpresa instance = new GUIregistrarEmpresa(companyService, this.service);
        instance.setVisible(true);
    }
}
