package co.edu.unicauca.microservicecoordinator.presentationTest;


import co.edu.unicauca.microservicecoordinator.application.port.in.CoordinatorServicePort;
import co.edu.unicauca.microservicecoordinator.application.port.in.ProjectServicePort;
import co.edu.unicauca.microservicecoordinator.presentation.CoordinatorController;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para la clase {@link CoordinatorController}.
 * Estas pruebas utilizan {@link WebMvcTest} para enfocar la capa MVC, simulando peticiones HTTP
 * y verificando las respuestas del controlador. Las dependencias del controlador
 * ({@link ProjectServicePort} y {@link CoordinatorServicePort}) son mockeadas con {}.
 */
@WebMvcTest(CoordinatorController.class)
class CoordinatorControllerTest {

}