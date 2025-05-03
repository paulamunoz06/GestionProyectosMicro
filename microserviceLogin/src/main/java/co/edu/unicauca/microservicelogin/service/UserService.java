package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.dto.UserRequest;
import co.edu.unicauca.microservicelogin.repository.UserRepository;
import com.password4j.Hash;
import com.password4j.Password;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con los usuarios en el sistema.
 *
 * Esta clase es responsable de realizar operaciones de autenticación, registro y verificación
 * de usuarios. Utiliza el repositorio {@link UserRepository} para interactuar con la base de datos
 * y el servicio de RabbitMQ para la comunicación asincrónica con otros microservicios o componentes.
 *
 */
@Service
public class UserService implements IUserService {

    /**
     * Plantilla de RabbitTemplate para la comunicación con RabbitMQ.
     * Permite enviar mensajes asincrónicos, en este caso relacionados con la información de los usuarios.
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Repositorio que maneja las operaciones CRUD sobre los usuarios.
     * Permite consultar y guardar usuarios en la base de datos.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Método para autenticar a un usuario con el sistema.
     *
     * Este método toma un objeto {@link UserRequest} que contiene las credenciales del usuario
     * (ID y contraseña), verifica si las credenciales coinciden con los datos almacenados
     * en la base de datos y devuelve un objeto {@link User} con la información del usuario si es válido.
     * En caso contrario, devuelve un usuario con el rol configurado a 0, indicando que el inicio de sesión
     * falló.
     *
     * @param user Objeto que contiene las credenciales de un usuario ({@link UserRequest}).
     * @return Un objeto {@link User} con la información del usuario si las credenciales son correctas,
     *         o un usuario con rol 0 si las credenciales son incorrectas.
     */
    @Override
    public User login(UserRequest user) {
        try {
            Optional<User> optionalUser = userRepository.findById(user.getId());

            // Verifica si el usuario existe
            if (optionalUser.isPresent()) {
                String hashedPassword = optionalUser.get().getPassword();

                // Verifica si la contraseña en texto plano coincide con el hash almacenado en la base de datos
                boolean verified = Password.check(user.getPassword(), hashedPassword).withArgon2();

                // Si las contraseñas coinciden, retorna el usuario con los datos disponibles
                if (verified) {
                    User tempUser = optionalUser.get();
                    User returnUser = new User();
                    returnUser.setEmail(tempUser.getEmail());
                    returnUser.setId(user.getId());
                    returnUser.setRole(tempUser.getRole());
                    return returnUser;
                }
            }

            // Si el usuario no existe o la contraseña es incorrecta, retorna un usuario con rol 0
            User badUserResult = new User();
            badUserResult.setRole(0);
            return badUserResult;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para registrar un nuevo usuario en el sistema.
     *
     * Si el usuario no existe en la base de datos, el método procederá a hashear su contraseña
     * utilizando el algoritmo Argon2id, y luego guardará el usuario en la base de datos.
     * En caso de que el usuario ya exista, imprime un mensaje indicando que el usuario ya existe.
     *
     * @param user Objeto {@link User} con la información del nuevo usuario a registrar.
     */
    @Override
    public void registerUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());

        // Si el usuario no existe, se procede a registrar el nuevo usuario
        if (optionalUser.isEmpty()) {
            try {
                // Hashea la contraseña usando Argon2id para garantizar su seguridad
                Hash hash = Password.hash(user.getPassword())
                        .addRandomSalt(16)
                        .withArgon2();

                // Establece la contraseña hasheada en el objeto usuario
                user.setPassword(hash.getResult());

                // Guarda el usuario en la base de datos
                userRepository.save(user);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Si el usuario ya existe, muestra un mensaje de advertencia
            System.out.println("Usuario existente");
        }
    }

    /**
     * Método para verificar la existencia de un usuario en el sistema.
     */
    @Override
    public boolean checkUser(UserRequest user) {
        return false;
    }
}