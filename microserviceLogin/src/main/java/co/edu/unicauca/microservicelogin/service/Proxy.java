package co.edu.unicauca.microservicelogin.service;

import co.edu.unicauca.microservicelogin.entities.User;
import co.edu.unicauca.microservicelogin.infra.dto.UserRequest;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Proxy implements IUserService {

    private UserService userService;

    public Proxy(UserService userService) {
        this.userService = userService;
    }

    @RateLimiter(name = "miServicioLimitado", fallbackMethod = "fallbackRateLimiter")
    @Override
    public User login(UserRequest user){
        //Validación básica de entrada
        if (user.getId() == null || user.getPassword() == null) {
            return null;
        }

        return userService.login(user);
    }

    public User fallbackRateLimiter(UserRequest user, RequestNotPermitted ex) {
        System.out.println("fallbackRateLimiter: Rate limit excedido para api/login/user con id: "+user.getId()+". Causa: "+ex.getMessage());
        // Devuelve una respuesta HTTP 429 Too Many Requests
        User tooManyLogins = new User();
        tooManyLogins.setRole(-3);
        return tooManyLogins;
    }

    @Override
    public void registerUser(User user) {

    }

    @Override
    public boolean checkUser(UserRequest user) {
        return false;
    }
}
