package co.edu.unicauca.mycompany.projects.access.MariaDB;

import co.edu.unicauca.mycompany.projects.access.ICoordinatorRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Coordinator;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que maneja la persistencia de los coordinadores en la base de datos MariaDB.
 * Implementa la interfaz ICoordinatorRepository y extiende MariaDBConnection.
 */
public class CoordinatorRepositoryMariaDB extends MariaDBConnection implements ICoordinatorRepository {

    /**
     * Constructor por defecto.
     */
    public CoordinatorRepositoryMariaDB() {
    }
    
    /**
     * Obtiene un coordinador específico basado en su ID de usuario.
     * 
     * @param userId El ID del usuario a buscar.
     * @return Un objeto Coordinator si se encuentra, null en caso contrario.
     */
    @Override
    public Coordinator getCoordinator(String userId) {
        Coordinator coordinator = null;
        String sql = "SELECT User.userId, userEmail, userPassword FROM User "
                   + "JOIN Coordinator ON User.userId = Coordinator.userId "
                   + "WHERE User.userId = ?";
        try {
            this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                coordinator = new Coordinator(
                    rs.getString("userId"),
                    rs.getString("userEmail"),
                    rs.getString("userPassword")
                );
            }
            this.disconnect();
        } catch (SQLException ex) {
            Logger.getLogger(CoordinatorRepositoryMariaDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            disconnect();
        }
        return coordinator;
    }

    @Override
    public void setToken(String token) {
        System.out.println("This class doesn't use a token");
    }
}


