package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.StudentRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.StudentRepositoryH2;
import java.util.HashMap;
import java.util.Map;

public class StudentRepositoryFactory implements IRepositoryFactory<IStudentRepository> {

    private static StudentRepositoryFactory instance;

    private final Map<RepositoryType, IStudentRepository> repositoryMap;

    private StudentRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new StudentRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new StudentRepositoryH2());
    }

    public static StudentRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new StudentRepositoryFactory();
        }
        return instance;
    }

    @Override
    public IStudentRepository getRepository(RepositoryType type) {
        IStudentRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}
