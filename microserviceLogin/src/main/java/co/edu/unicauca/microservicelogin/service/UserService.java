package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.dto.UserRequest;
import co.edu.unicauca.microservicelogin.repository.UserRepository;
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

            if(optionalUser.isPresent() && optionalUser.get().getPassword().equals(user.getPassword())){
                User tempUser = optionalUser.get();
                User returnUser = new User();
                returnUser.setEmail(tempUser.getEmail());
                returnUser.setId(user.getId());
                returnUser.setRole(tempUser.getRole());
                return returnUser;
            }
            else{
                User badUserResult = new User();
                badUserResult.setRole(-1);
                return badUserResult;
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> registerUser(UserRequest user) {
        return null;
    }

    @Override
    public boolean checkUser(UserRequest user) {
        return false;
    }




}
