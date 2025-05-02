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

@Service
public class UserService implements IUserService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(UserRequest user) {
        try{
            Optional<User> optionalUser = userRepository.findById(user.getId());

            if (optionalUser.isPresent()) {
                String hashedPassword = optionalUser.get().getPassword();

                // Verifica si la contraseña en texto plano coincide con el hash guardado
                boolean verified = Password.check(user.getPassword(), hashedPassword).withArgon2();

                if (verified) {
                    User tempUser = optionalUser.get();
                    User returnUser = new User();
                    returnUser.setEmail(tempUser.getEmail());
                    returnUser.setId(user.getId());
                    returnUser.setRole(tempUser.getRole());
                    return returnUser;
                }
            }

            User badUserResult = new User();
            badUserResult.setRole(0);
            return badUserResult;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if(optionalUser.isEmpty()){
            try{
                // Hashea la contraseña usando Argon2id
                Hash hash = Password.hash(user.getPassword())
                        .addRandomSalt(16)
                        .withArgon2();

                // Guarda el hash resultante en el campo de contraseña
                user.setPassword(hash.getResult());

                userRepository.save(user);
            }
            catch(RuntimeException e){
                throw new RuntimeException(e);
            }
        }
        else{
            System.out.println("Usuario existente");
        }
    }


    @Override
    public boolean checkUser(UserRequest user) {
        return false;
    }




}
