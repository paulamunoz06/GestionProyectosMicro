package co.edu.unicauca.mycompany.projects.presentation;

import co.edu.unicauca.mycompany.projects.access.ICompanyRepository;
import co.edu.unicauca.mycompany.projects.access.ICoordinatorRepository;
import co.edu.unicauca.mycompany.projects.access.IProjectRepository;
import co.edu.unicauca.mycompany.projects.access.IStudentRepository;
import co.edu.unicauca.mycompany.projects.access.RepositoryType;
import co.edu.unicauca.mycompany.projects.domain.services.CompanyService;
import co.edu.unicauca.mycompany.projects.domain.services.CoordinatorService;
import co.edu.unicauca.mycompany.projects.domain.services.ProjectService;
import co.edu.unicauca.mycompany.projects.domain.services.StudentService;
import co.edu.unicauca.mycompany.projects.access.StudentRepositoryFactory;
import co.edu.unicauca.mycompany.projects.access.CoordinatorRepositoryFactory;
import co.edu.unicauca.mycompany.projects.access.CompanyRepositoryFactory;
import co.edu.unicauca.mycompany.projects.access.ProjectRepositoryFactory;

/**
 *
 * @author spart
 */
public class DashboardFactory {
    private static DashboardFactory instance;
    private StudentService studentService;
    CoordinatorService coordinatorService;
    CompanyService companyService;
    ProjectService projectService;
    public DashboardFactory(String token){
        // Obtener instancias de los repositorios a través de la fábrica
        IStudentRepository studentRepository = StudentRepositoryFactory.getInstance().getRepository(RepositoryType.H2);
        studentRepository.setToken(token);
        studentService = new StudentService(studentRepository);
        
        ICoordinatorRepository coordinatorRepository = CoordinatorRepositoryFactory.getInstance().getRepository(RepositoryType.H2);
        coordinatorRepository.setToken(token);
        coordinatorService = new CoordinatorService(coordinatorRepository);

        ICompanyRepository companyRepository = CompanyRepositoryFactory.getInstance().getRepository(RepositoryType.H2);
        companyRepository.setToken(token);
        companyService = new CompanyService(companyRepository);
        
        IProjectRepository projectRepository = ProjectRepositoryFactory.getInstance().getRepository(RepositoryType.H2);
        projectRepository.setToken(token);
        projectService = new ProjectService(projectRepository);
    }
    public Dashboard crearDashboard(int result, String idEntity){
        switch(result){
            case 1:
                return new GUIDashboardEstudiante(studentService.getStudent(idEntity),projectService);
            case 2:
                return new GUIDashboardCoordinadorInicio(coordinatorService.getCoordinator(idEntity), projectService);
            case 3:
                return new GUIDashboardEmpresa(companyService.getCompany(idEntity));
            default:
                return null;
        }
    }
    /**
     * Obtiene la instancia única de la fábrica (patrón Singleton).
     * 
     * @return Instancia de Factory.
     */
    public static DashboardFactory getInstance(String token) {
        if (instance == null) {
            instance = new DashboardFactory(token);
        }
        return instance;
    }
}
