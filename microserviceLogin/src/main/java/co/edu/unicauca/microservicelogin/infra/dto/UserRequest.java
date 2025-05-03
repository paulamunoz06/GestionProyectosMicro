package co.edu.unicauca.microservicelogin.infra.dto;

/**
 * Clase que representa una solicitud de usuario para login.
 *
 * Esta clase contiene los campos necesarios para realizar un proceso de autenticación de usuario,
 * incluyendo el ID del usuario, su contraseña, correo electrónico y rol. Es utilizada principalmente
 * para recibir la información de login del usuario a través de una petición HTTP.
 */
public class UserRequest {

    /**
     * El identificador único del usuario.
     * Este campo se utiliza para identificar al usuario en el sistema.
     *
     * @see #getId()
     * @see #setId(String)
     */
    private String id;

    /**
     * La contraseña del usuario.
     * Este campo contiene la clave secreta asociada al usuario para su autenticación.
     *
     * @see #getPassword()
     * @see #setPassword(String)
     */
    private String password;

    /**
     * El correo electrónico del usuario.
     * Este campo almacena la dirección de correo electrónico asociada al usuario.
     *
     * @see #getEmail()
     * @see #setEmail(String)
     */
    private String email;

    /**
     * El rol del usuario en el sistema.
     * Este campo indica el rol del usuario (por ejemplo, 'admin', 'user', etc.),
     * lo que puede determinar sus permisos dentro de la aplicación.
     *
     * @see #getRole()
     * @see #setRole(String)
     */
    private String role;

    /**
     * Obtiene el ID del usuario.
     *
     * @return {@link String} El ID del usuario.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return {@link String} La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return {@link String} El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return {@link String} El rol del usuario.
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id El ID del usuario.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email El correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param role El rol del usuario.
     */
    public void setRole(String role) {
        this.role = role;
    }
}