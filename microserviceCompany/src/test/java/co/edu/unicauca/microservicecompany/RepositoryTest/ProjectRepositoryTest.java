package co.edu.unicauca.microservicecompany.RepositoryTest;

import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import co.edu.unicauca.microserviceCompany.repository.IProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IProjectRepository projectRepository;

    @Test
    public void shouldSaveProject() {
        // Given
        Project project = new Project(
                "P001",
                "Test Project",
                "This is a test project description",
                "Test Abstract",
                "Test Goals",
                12,
                10000.0,
                "C001",
                "COORD001"
        );

        // When
        Project savedProject = projectRepository.save(project);

        // Then
        assertThat(savedProject).isNotNull();
        assertThat(savedProject.getProId()).isEqualTo("P001");
        assertThat(savedProject.getProTitle()).isEqualTo("Test Project");
    }

    @Test
    public void shouldFindProjectById() {
        // Given
        Project project = new Project(
                "P002",
                "Another Test Project",
                "This is another test project description",
                "Another Test Abstract",
                "Another Test Goals",
                6,
                5000.0,
                "C002",
                "COORD002"
        );
        entityManager.persist(project);
        entityManager.flush();

        // When
        Optional<Project> found = projectRepository.findById("P002");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getProTitle()).isEqualTo("Another Test Project");
    }

    @Test
    public void shouldNotFindProjectById() {
        // When
        Optional<Project> found = projectRepository.findById("NonExistentId");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    public void shouldFindAvailableProjectsForStudent() {
        // Given
        // Create a project with ACEPTADO state
        Project acceptedProject = new Project(
                "P003",
                "Accepted Project",
                "This is an accepted project description",
                "Accepted Abstract",
                "Accepted Goals",
                12,
                10000.0,
                "C003",
                "COORD003"
        );
        acceptedProject.setProState(EnumProjectState.ACEPTADO);
        entityManager.persist(acceptedProject);

        // Create project tables and add test data directly with SQL
        entityManager.getEntityManager().createNativeQuery(
                "CREATE TABLE IF NOT EXISTS Postulated (" +
                        "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        "pro_Id VARCHAR(255), " +
                        "student_Id VARCHAR(255)" +
                        ")"
        ).executeUpdate();

        entityManager.getEntityManager().createNativeQuery(
                "CREATE TABLE IF NOT EXISTS Approved (" +
                        "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        "pro_Id VARCHAR(255), " +
                        "student_Id VARCHAR(255)" +
                        ")"
        ).executeUpdate();

        // Add data for a different student to verify our query works
        entityManager.getEntityManager().createNativeQuery(
                "INSERT INTO Postulated (pro_Id, student_Id) VALUES ('P003', 'S002')"
        ).executeUpdate();

        entityManager.flush();

        // When
        String studentId = "S001"; // This student has no postulated or approved projects
        List<Project> availableProjects = projectRepository.findAvailableProjectsForStudent(studentId);

        // Then
        assertThat(availableProjects).isNotNull();
        // The project should be available for student S001 since they haven't postulated
        assertThat(availableProjects).extracting(Project::getProId).contains("P003");
    }

    @Test
    public void shouldCountAllProjects() {
        // Given
        Project project1 = new Project(
                "P004",
                "Project 1",
                "Project 1 description",
                "Abstract 1",
                "Goals 1",
                12,
                10000.0,
                "C004",
                "COORD004"
        );

        Project project2 = new Project(
                "P005",
                "Project 2",
                "Project 2 description",
                "Abstract 2",
                "Goals 2",
                6,
                5000.0,
                "C004",
                "COORD004"
        );

        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.flush();

        // When
        int count = projectRepository.countAllProjects();

        // Then
        // The count should include the two projects we just created
        // plus any existing projects in the test database
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void shouldCountPostulatedProjects() {
        // Given
        // Create project and student data for test
        Project project = new Project(
                "P006",
                "Project 6",
                "Project 6 description",
                "Abstract 6",
                "Goals 6",
                12,
                10000.0,
                "C004",
                "COORD004"
        );
        entityManager.persist(project);

        // Create Postulated table if it doesn't exist
        entityManager.getEntityManager().createNativeQuery(
                "CREATE TABLE IF NOT EXISTS Postulated (" +
                        "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        "pro_Id VARCHAR(255), " +
                        "student_Id VARCHAR(255)" +
                        ")"
        ).executeUpdate();

        // Insert test data
        String studentId = "S001";
        entityManager.getEntityManager().createNativeQuery(
                        "INSERT INTO Postulated (pro_Id, student_Id) VALUES ('P006', :studentId)"
                ).setParameter("studentId", studentId)
                .executeUpdate();

        entityManager.flush();

        // When
        int count = projectRepository.countPostulatedProjects(studentId);

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void shouldCountApprovedProjects() {
        // Given
        // Create project and student data for test
        Project project = new Project(
                "P007",
                "Project 7",
                "Project 7 description",
                "Abstract 7",
                "Goals 7",
                12,
                10000.0,
                "C004",
                "COORD004"
        );
        entityManager.persist(project);

        // Create Approved table if it doesn't exist
        entityManager.getEntityManager().createNativeQuery(
                "CREATE TABLE IF NOT EXISTS Approved (" +
                        "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                        "pro_Id VARCHAR(255), " +
                        "student_Id VARCHAR(255)" +
                        ")"
        ).executeUpdate();

        // Insert test data
        String studentId = "S001";
        entityManager.getEntityManager().createNativeQuery(
                        "INSERT INTO Approved (pro_Id, student_Id) VALUES ('P007', :studentId)"
                ).setParameter("studentId", studentId)
                .executeUpdate();

        entityManager.flush();

        // When
        int count = projectRepository.countApprovedProjects(studentId);

        // Then
        assertThat(count).isEqualTo(1);
    }
}