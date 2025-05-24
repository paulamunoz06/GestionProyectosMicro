
package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.IStudentRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Implementación del repositorio de estudiantes que interactúa con un servicio REST
 * para realizar operaciones relacionadas con los estudiantes.
 */
public class StudentRepositoryH2 extends Token implements IStudentRepository{

     /**
     * Obtiene un estudiante por su ID desde el servicio REST.
     *
     * @param id el ID del estudiante que se desea obtener.
     * @return un objeto {@link Student} que representa al estudiante solicitado.
     *         Si ocurre un error al obtener el estudiante o si el estudiante no existe, se retorna {@code null}.
     */
    @Override
    public Student getStudent(String id) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String apiUrl = IUrl.ApiGatewayUrl + "/student/"+id ;
        Student student = null;
        try {
            // Crear una solicitud GET
            HttpGet request = new HttpGet(apiUrl);
            if (this.token == null || this.token.isEmpty()){
                System.out.println("Token is null, is not valid ");
                return null;
            }
            request.setHeader("Authorization","Bearer "+this.token);
            
            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a objetos Product
                student = mapper.readValue(jsonResponse, Student.class);
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(StudentRepositoryH2.class.getName()).log(Level.SEVERE, null, "Error al obtener un proyecto. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(StudentRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return student;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
    
}
