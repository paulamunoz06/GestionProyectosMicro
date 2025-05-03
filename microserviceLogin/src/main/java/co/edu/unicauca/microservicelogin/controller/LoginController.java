package co.edu.unicauca.microservicelogin.controller;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.dto.UserRequest;
import co.edu.unicauca.microservicelogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que maneja las solicitudes relacionadas con el inicio de sesión de los usuarios.
 *
 * Proporciona una interfaz para que los usuarios puedan iniciar sesión en el sistema mediante sus credenciales.
 */
@RestController
@RequestMapping("api/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint para realizar el inicio de sesión de un usuario.
     *
     * Recibe las credenciales del usuario en formato JSON y verifica si las credenciales son válidas.
     * Si las credenciales son correctas, devuelve un objeto {@link User} con los datos del usuario.
     * En caso de error, retorna una respuesta con un mensaje de error.
     *
     * @param user El objeto {@link UserRequest} que contiene las credenciales del usuario (usuario y contraseña).
     * @return {@link ResponseEntity} con el estado de la operación:
     *         - Si el inicio de sesión es exitoso, devuelve el objeto {@link User}.
     *         - Si ocurre un error, devuelve un mensaje de error con el código HTTP `BAD_REQUEST`.
     */
    @PostMapping("/user")
    public ResponseEntity<?> login(@RequestBody UserRequest user) {
        try {
            // Intenta iniciar sesión con las credenciales proporcionadas
            User loginUser = userService.login(user);
            return ResponseEntity.ok(loginUser);
        } catch (Exception ex) {
            // Si ocurre un error, devuelve una respuesta con el estado BAD_REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al hacer login.\"}");
        }
    }
}