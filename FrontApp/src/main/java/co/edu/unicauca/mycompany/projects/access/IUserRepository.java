package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.User;

/**
 * Interfaz que define las operaciones para gestionar usuarios en el sistema.
 */
public interface IUserRepository {
    
    /**
     * Inicia sesión en el sistema con las credenciales proporcionadas.
     * 
     * @param usuario Nombre de usuario o identificador.
     * @param pwd Contraseña del usuario.
     * @return Un código de estado indicando el resultado del inicio de sesión.
     */
    int iniciarSesion(String usuario, char[] pwd);

    /**
     * Funcion que se encarga de obtener el el token JWT desde KEYCLOAK
     * @param username
     * @param password
     * @return
     */
    private String obtenerToken(String username, String password);

    /**
     * Se encarga de extraer el rol de un token JWT y convertirlo al entero correspondiente dependiendo del rol
     * @param token es JWT token
     * @return entero correspondiente
     */
    private int extraerRolDesdeToken(String token);
}
