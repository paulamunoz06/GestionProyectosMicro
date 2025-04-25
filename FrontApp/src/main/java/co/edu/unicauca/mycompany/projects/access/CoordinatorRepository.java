package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Coordinator;

public class CoordinatorRepository implements ICoordinatorRepository {
    @Override
    public Coordinator getCoordinator(String id) {
        return new Coordinator(id,"example@example.com","password");
    }
}
