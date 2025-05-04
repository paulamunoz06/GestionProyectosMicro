package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.StudentRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.StudentRepositoryH2;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica concreta para la creación de objetos que implementan la interfaz IStudentRepository.
 * 
 * Aplica el patrón Singleton para garantizar una única instancia durante la ejecución.
 * 
 * Utiliza un mapa para asociar cada tipo de repositorio con su implementación correspondiente.
 */
public class StudentRepositoryFactory implements IRepositoryFactory<IStudentRepository> {

    /**
     * Instancia única de StudentRepositoryFactory.
     */
    private static StudentRepositoryFactory instance;

    /**
     * Mapa que contiene las implementaciones concretas de IStudentRepository asociadas a cada tipo de repositorio.
     */
    private final Map<RepositoryType, IStudentRepository> repositoryMap;

    /**
     * Constructor privado que inicializa el mapa con las implementaciones disponibles:
     * MariaDB y H2.
     */
    private StudentRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new StudentRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new StudentRepositoryH2());
    }

    /**
     * Retorna la instancia única de StudentRepositoryFactory.
     * Si aún no existe, se crea una nueva instancia.
     *
     * @return instancia única de StudentRepositoryFactory
     */
    public static StudentRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new StudentRepositoryFactory();
        }
        return instance;
    }

    /**
     * Devuelve una implementación concreta de IStudentRepository correspondiente al tipo de repositorio indicado.
     *
     * @param type tipo de repositorio solicitado (por ejemplo, MARIADB o H2)
     * @return instancia correspondiente de IStudentRepository
     * @throws IllegalArgumentException si el tipo de repositorio no está soportado
     */
    @Override
    public IStudentRepository getRepository(RepositoryType type) {
        IStudentRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}