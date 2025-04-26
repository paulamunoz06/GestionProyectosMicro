package co.edu.unicauca.microservicestudent.repository;

import co.edu.unicauca.microservicestudent.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProjectRepository extends JpaRepository<Project, String> {
    @Query(value = """
    SELECT p.* FROM Project p
    WHERE NOT EXISTS (
        SELECT 1 FROM Postulated a WHERE a.pro_Id = p.proId AND a.student_Id = :studentId
    )
    AND NOT EXISTS (
        SELECT 1 FROM Approved d WHERE d.pro_Id = p.proId AND d.student_Id = :studentId
    )
    AND p.proState = 'ACEPTADO'
    """, nativeQuery = true)
    List<Project> findAvailableProjectsForStudent(@Param("studentId") String studentId);

    @Query(value = """
    SELECT COUNT(p.proId) FROM Project p
    """,nativeQuery = true)
    int countAllProjects();

    @Query(value = """
    SELECT COUNT(p.proId) FROM Project p JOIN Postulated a ON p.proId = a.pro_Id WHERE a.student_Id = :studentId
    """,nativeQuery = true)
    int countPostulatedProjects(@Param("studentId") String studentId);

    @Query(value = """
    SELECT COUNT(p.proId) FROM Project p JOIN Approved d ON p.proId = d.pro_Id WHERE d.student_Id = :studentId
    """,nativeQuery = true)
    int countApprovedProjects(@Param("studentId") String studentId);
}
