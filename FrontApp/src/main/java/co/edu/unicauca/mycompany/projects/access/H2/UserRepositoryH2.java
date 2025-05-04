/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.IUserRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author paula
 */
public class UserRepositoryH2 implements IUserRepository{

    @Override
    public int iniciarSesion(String usuario, char[] pwd) {
        // 1. Cliente HTTP y mapper Jackson
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String apiUrl = "http://localhost:8082/api/login/user";

            // 2. Creamos la petición POST
            HttpPost request = new HttpPost(apiUrl);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Accept", "application/json");

            // 3. Preparamos el JSON con usuario y contraseña
            Map<String, String> creds = new HashMap<>();
            creds.put("id", usuario);
            creds.put("password", new String(pwd));
            String jsonCreds = mapper.writeValueAsString(creds);

            StringEntity entity = new StringEntity(jsonCreds, ContentType.APPLICATION_JSON);
            request.setEntity(entity);

            // 4. Ejecutamos la petición
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                // 5. Leemos la respuesta JSON y la mapeamos a User
                String jsonResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                User user = mapper.readValue(jsonResponse, User.class);
                return user.getRole();
            }
            else {
                throw new RuntimeException("Error en login. Código HTTP: " + statusCode);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error de E/S al invocar el servicio de login", ex);
        }
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
