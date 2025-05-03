package co.edu.unicauca.microservicelogin.repository;

import co.edu.unicauca.microservicelogin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de usuarios que extiende JpaRepository.
 */
public interface UserRepository extends JpaRepository<User, String> {
}