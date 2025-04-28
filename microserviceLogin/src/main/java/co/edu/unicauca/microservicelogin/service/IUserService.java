package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.dto.UserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUserService {
    public abstract User login(@RequestBody UserRequest user);
    public abstract ResponseEntity<?> registerUser(@RequestBody UserRequest user);
    public abstract boolean checkUser(@RequestBody UserRequest user);
}
