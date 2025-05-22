package co.edu.unicauca.microservicecoordinator.application.port.in;

import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;

public interface CoordinatorServicePort {
    ProjectDto evaluateProject(String proId, String proStatusStr);
}
