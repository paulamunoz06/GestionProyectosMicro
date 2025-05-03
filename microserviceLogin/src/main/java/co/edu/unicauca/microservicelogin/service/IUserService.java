package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Interfaz para la lógica de negocio relacionada con los usuarios.
 *
 * Esta interfaz define los métodos necesarios para el manejo de usuarios, incluyendo
 * el inicio de sesión, el registro de nuevos usuarios y la verificación de las credenciales
 * de un usuario. Los métodos de esta interfaz se implementan en una clase de servicio
 * para manejar la lógica detrás de cada acción.
 */
public interface IUserService {

    /**
     * Método para iniciar sesión de un usuario.
     *
     * Este método verifica las credenciales proporcionadas por el usuario
     * para determinar si son válidas. Si las credenciales son correctas,
     * el usuario se autentica y se devuelve el objeto {@link User}.
     *
     * @param user Objeto de tipo {@link UserRequest} que contiene las credenciales del usuario.
     * @return {@link User} El usuario autenticado si las credenciales son correctas.
     */
    public abstract User login(@RequestBody UserRequest user);

    /**
     * Método para registrar un nuevo usuario en el sistema.
     *
     * Este método toma un objeto {@link User} y lo persiste en la base de datos
     * como un nuevo usuario en el sistema. Es usado para crear una nueva cuenta.
     *
     * @param user Objeto de tipo {@link User} que contiene los datos del nuevo usuario.
     */
    public abstract void registerUser(User user);

    /**
     * Método para verificar si un usuario existe en el sistema con las credenciales proporcionadas.
     *
     * Este método recibe las credenciales de un usuario y verifica si existen en el sistema
     * antes de permitir el inicio de sesión. Devuelve un valor booleano indicando si las credenciales
     * son correctas o no.
     *
     * @param user Objeto de tipo {@link UserRequest} que contiene las credenciales del usuario a verificar.
     * @return {@code true} si las credenciales son válidas, {@code false} si no lo son.
     */
    public abstract boolean checkUser(@RequestBody UserRequest user);
}
