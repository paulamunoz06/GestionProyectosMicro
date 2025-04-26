package co.edu.unicauca.microservicecoordinator.service;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.ProjectStatus;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ICoordinatorService{

    @Transactional
    Project evaluateProject(@PathVariable Long proId,@RequestBody ProjectStatus proStatus);



}
