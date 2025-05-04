/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.UserRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.UserRepositoryH2;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paula
 */
public class UserRepositoryFactory implements IRepositoryFactory<IUserRepository> {

    private static UserRepositoryFactory instance;

    private final Map<RepositoryType, IUserRepository> repositoryMap;

    private UserRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new UserRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new UserRepositoryH2());
    }

    public static UserRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new UserRepositoryFactory();
        }
        return instance;
    }

    @Override
    public IUserRepository getRepository(RepositoryType type) {
        IUserRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}

