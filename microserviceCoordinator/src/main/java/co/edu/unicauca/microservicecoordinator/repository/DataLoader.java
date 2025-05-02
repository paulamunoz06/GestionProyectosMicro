package co.edu.unicauca.microservicecoordinator.repository;

import co.edu.unicauca.microservicecoordinator.entities.Project;
import co.edu.unicauca.microservicecoordinator.entities.EnumProjectState;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Componente encargado de cargar datos iniciales en la base de datos.
 *
 * <p>Esta clase implementa la funcionalidad de carga de datos de prueba
 * para el microservicio de coordinador. Se ejecuta automáticamente durante
 * la inicialización de la aplicación gracias a la anotación {@link PostConstruct}
 * en el método de carga de datos.</p>
 *
 * <p>Los datos se cargan solo si la base de datos está vacía, evitando
 * así la duplicación de registros en ejecuciones posteriores del servicio.</p>
 *
 * @author [Tu nombre aquí]
 * @version 1.0
 * @see Project
 * @see IProjectRepository
 * @see EnumProjectState
 */
@Component
public class DataLoader {

    /**
     * Repositorio de proyectos utilizado para verificar y guardar datos.
     */
    @Autowired
    private IProjectRepository projectRepository;

    /**
     * Carga datos iniciales en la base de datos.
     *
     * <p>Este método se ejecuta automáticamente después de la inicialización
     * del componente, gracias a la anotación {@link PostConstruct}. Verifica
     * si ya existen proyectos en la base de datos y, en caso negativo, crea
     * y guarda proyectos de prueba con diferentes estados.</p>
     */
    @PostConstruct
    public void loadData() {
        if (projectRepository.count() == 0) { // Para no duplicar datos si ya existen


            // Proyecto 1 con estudiante postulado
            Project project1 = new Project();
            project1.setProId("1");
            project1.setProTitle("Sistema de Gestión");
            project1.setProDescription("Sistema para gestionar recursos");
            project1.setProAbstract("Resumen del sistema");
            project1.setProGoals("Objetivos claros");
            project1.setProDeadLine(30);
            project1.setProBudget(10000.0);
            project1.setProState(EnumProjectState.ACEPTADO);
            project1.setProDate(LocalDate.now());


            Project project2 = new Project();
            project2.setProId("P002");
            project2.setProTitle("Aplicación de Seguimiento de Salud");
            project2.setProDescription("Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.");
            project2.setProAbstract("App para registro de actividad física, alimentación y signos vitales.");
            project2.setProGoals("Promover hábitos saludables mediante el seguimiento diario.");
            project2.setProDate(LocalDate.now());
            project2.setProDeadLine(8);
            project2.setProBudget(30000.0);
            project2.setProState(EnumProjectState.CERRADO);

            Project project3 = new Project();
            project3.setProId("P003");
            project3.setProTitle("Aplicación de Seguimiento de Salud");
            project3.setProDescription("Crear una app móvil que permita a los usuarios llevar el control de su salud diaria.");
            project3.setProAbstract("App para registro de actividad física, alimentación y signos vitales.");
            project3.setProGoals("Promover hábitos saludables mediante el seguimiento diario.");
            project3.setProDate(LocalDate.now());
            project3.setProDeadLine(8);
            project3.setProBudget(30000.0);
            project3.setProState(EnumProjectState.RECHAZADO);

            projectRepository.save(project1);
            projectRepository.save(project2);
            projectRepository.save(project3);

            System.out.println(">>>> Se cargaron proyectos de prueba en la base de datos <<<<");
        }
    }
}