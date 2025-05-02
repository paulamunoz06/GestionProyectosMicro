package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ICoordinatorService{

    @Transactional
    Project evaluateProject(@PathVariable String proId,@RequestBody String proStatus);



}
