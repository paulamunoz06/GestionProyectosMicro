package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.IUserRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.User;
import java.net.URI;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.net.http.HttpResponse;
import org.apache.http.HttpStatus;
import java.net.http.HttpClient;
import org.json.JSONObject;

/**
 * Implementación del repositorio de usuarios que interactúa con un servicio REST
 * para realizar operaciones relacionadas con los usuarios, como el inicio de sesión.
 */
public class UserRepositoryH2 implements IUserRepository{

     /**
     * Inicia sesión en el sistema con las credenciales del usuario.
     * 
     * Este método envía una solicitud POST con el nombre de usuario y la contraseña al 
     * servicio REST, y luego interpreta la respuesta para obtener el rol del usuario.
     *
     * @param usuario el nombre de usuario utilizado para iniciar sesión.
     * @param pwd la contraseña del usuario, representada como un arreglo de caracteres.
     * @return el rol del usuario como un entero, el cual es recuperado de la respuesta del servicio REST.
     *         Si la autenticación es exitosa, se devuelve el rol del usuario. Si ocurre algún error,
     *         se lanza una excepción {@link RuntimeException}.
     * @throws RuntimeException si ocurre un error al invocar el servicio de inicio de sesión o si la respuesta
     *                          del servicio no es exitosa (código HTTP distinto de 200).
     */
    @Override
    public int iniciarSesion(String usuario, char[] pwd) {
        try{
            String token = obtenerToken(usuario, new String(pwd));
            if (token != null) {
                return extraerRolDesdeToken(token);
            }
            else {
                throw new RuntimeException("Error en login. El codigo al intentar obtener el token es distinto de 200 ");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return -1;
        }

    }

    public int extraerRolDesdeToken(String token) {
        String[] partes = token.split("\\.");
        if (partes.length < 2) {
            throw new IllegalArgumentException("Token JWT inválido.");
        }

        String payload = new String(Base64.getUrlDecoder().decode(partes[1]));
        JSONObject json = new JSONObject(payload);

        if (json.has("resource_access")) {
            JSONObject resourceAccess = json.getJSONObject("resource_access");

            // Verificamos si contiene el cliente "sistema-desktop"
            if (resourceAccess.has("sistema-desktop")) {
                JSONObject sistemaDesktop = resourceAccess.getJSONObject("sistema-desktop");

                if (sistemaDesktop.has("roles")) {
                    for (Object rol : sistemaDesktop.getJSONArray("roles")) {
                        String rolStr = rol.toString();
                        if ("coordinator".equals(rolStr)) {
                            return 2;
                        } else if ("company".equals(rolStr)) {
                            return 3;
                        } else if ("student".equals(rolStr)) {
                            return 1;
                        }
                    }
                }
            }
        }

        return -1;
    }

    
    @Override
    public String obtenerToken(String username, String password) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = "client_id=sistema-desktop&grant_type=password&username=" + username + "&password=" + password;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/realms/sistema/protocol/openid-connect/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Código de respuesta: " + response.statusCode());
        System.out.println("Cuerpo de respuesta: " + response.body());
        if (response.statusCode() != 200) {
            return null;
        }
        JSONObject json = new JSONObject(response.body());

        // Verificamos que existe el campo
        if (!json.has("access_token")) {
            throw new RuntimeException("La respuesta no contiene access_token.");
        }

        return json.getString("access_token");
    }

    @Override
    public boolean save(User newUser) {
        return false;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existId(String id) {
        return false;
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
