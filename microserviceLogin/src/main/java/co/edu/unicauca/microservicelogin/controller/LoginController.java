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

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity<?> login(@RequestBody UserRequest user) {
        try{
            User loginUser = userService.login(user);
            return ResponseEntity.ok(loginUser);
        }
        catch(Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al hacer login.\"}");
        }
    }

}
