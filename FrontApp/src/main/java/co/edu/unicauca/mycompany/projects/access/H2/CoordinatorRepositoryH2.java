package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.ICoordinatorRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Coordinator;

public class CoordinatorRepositoryH2 implements ICoordinatorRepository{
    @Override
    public Coordinator getCoordinator(String id) {
        return new Coordinator(id,"example@example.com","password");
    }
}
