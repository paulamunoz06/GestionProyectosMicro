package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

public class UserRepository implements IUserRepository {

    @Override
    public int iniciarSesion(String usuario, char[] pwd) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        //TODO fucking connection to microserviceLogin
        return 0;
    }

    @Override
    public boolean save(User newUser) {
        return false;
    }

    @Override
    public boolean existId(String id) {
        return false;
    }
}
