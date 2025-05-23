package co.edu.unicauca.microserviceCompany.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeycloakAdminService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminService.class);

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}") // Realm donde se autentica este cliente de servicio (ej. "sistema")
    private String serviceAccountAuthRealm;

    @Value("${keycloak.resource}") // Client ID del cliente de servicio (ej. "company-service-registrar")
    private String serviceAccountClientId;

    @Value("${keycloak.credentials.secret}") // Client secret del cliente de servicio
    private String serviceAccountClientSecret;

    @Value("${app.keycloak.target-realm}") // Realm donde se crearán los usuarios de la app (ej. "sistema")
    private String targetAppRealm;

    private Keycloak keycloakAdminClientInstance; // Para reutilizar la instancia si es posible

    // Método para obtener una instancia del cliente Keycloak
    // Sincronizado para evitar problemas de concurrencia en la primera inicialización
    private synchronized Keycloak getKeycloakAdminClient() {
        if (keycloakAdminClientInstance == null) {
            logger.info("Inicializando instancia de Keycloak Admin Client...");
            logger.info("Server URL: {}", keycloakServerUrl);
            logger.info("Service Account Auth Realm: {}", serviceAccountAuthRealm);
            logger.info("Service Account Client ID: {}", serviceAccountClientId);
            // No loguear el secret
            keycloakAdminClientInstance = KeycloakBuilder.builder()
                    .serverUrl(keycloakServerUrl)
                    .realm(serviceAccountAuthRealm) // Realm para autenticar el cliente de servicio
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS) // Usar client credentials grant
                    .clientId(serviceAccountClientId)
                    .clientSecret(serviceAccountClientSecret)
                    .build();
            logger.info("Instancia de Keycloak Admin Client creada.");
        }
        return keycloakAdminClientInstance;
    }

    /**
     * Crea un nuevo usuario en el realm de la aplicación en Keycloak y le asigna un rol.
     *
     * @param username El username deseado para el nuevo usuario.
     * @param email El email del nuevo usuario.
     * @param password La contraseña en texto plano para el nuevo usuario.
     * @param roleName El nombre del rol (del realm) a asignar al nuevo usuario (ej. "company").
     * @return El ID del usuario (sub) creado en Keycloak.
     * @throws RuntimeException Si ocurre un error durante la creación del usuario o asignación del rol.
     */
    public String createUserInKeycloak(String username, String email, String password, String roleName) {
        Keycloak keycloak = getKeycloakAdminClient();
        RealmResource appRealmResource = keycloak.realm(targetAppRealm); // Apunta al realm de tu aplicación
        UsersResource usersResource = appRealmResource.users();

        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setEmailVerified(true); // Puedes cambiar esto si tienes un flujo de verificación de email
        newUser.setEnabled(true);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        passwordCred.setTemporary(false); // La contraseña no es temporal
        newUser.setCredentials(Collections.singletonList(passwordCred));

        Response response = null;
        String createdUserId = null;

        try {
            logger.info("Intentando crear usuario '{}' en realm '{}'", username, targetAppRealm);
            response = usersResource.create(newUser); // Intenta crear el usuario

            if (response.getStatus() == 201) { // HTTP 201 Created
                // Extraer el ID del usuario creado desde la cabecera Location
                String locationHeader = response.getLocation().toString();
                createdUserId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                logger.info("Usuario '{}' creado exitosamente en Keycloak con ID: {}", username, createdUserId);

                // Asignar el rol especificado al usuario recién creado
                if (roleName != null && !roleName.isEmpty()) {
                    UserResource createdUserResource = usersResource.get(createdUserId);
                    RoleRepresentation roleToAssign = null;
                    try {
                        // Intenta obtener el rol del realm. Asegúrate que el rol exista.
                        roleToAssign = appRealmResource.roles().get(roleName).toRepresentation();
                    } catch (Exception e) {
                        logger.warn("No se pudo obtener el rol '{}' del realm '{}'. ¿Existe el rol?", roleName, targetAppRealm, e);
                        // Decide si esto es un error fatal. Por ahora, continuamos sin asignar el rol si no se encuentra.
                    }

                    if (roleToAssign != null) {
                        createdUserResource.roles().realmLevel().add(Collections.singletonList(roleToAssign));
                        logger.info("Rol '{}' asignado exitosamente al usuario '{}' (ID: {}).", roleName, username, createdUserId);
                    } else {
                        logger.warn("El rol '{}' no fue encontrado o no se pudo asignar al usuario '{}'.", roleName, username);
                        // Puedes optar por lanzar una excepción aquí si la asignación de rol es mandatoria.
                        // throw new RuntimeException("Rol '" + roleName + "' no encontrado en el realm '" + targetAppRealm + "'.");
                    }
                }
            } else {
                // Error al crear el usuario
                String errorDetails = response.hasEntity() ? response.readEntity(String.class) : "No hay detalles adicionales del error.";
                logger.error("Error al crear usuario '{}' en Keycloak. Status: {}, Info: {}, Detalles: {}",
                        username, response.getStatus(), response.getStatusInfo(), errorDetails);
                throw new RuntimeException("Fallo al crear usuario en Keycloak. Status: " + response.getStatus() + ". Detalles: " + errorDetails);
            }
        } catch (Exception e) {
            logger.error("Excepción durante la creación del usuario '{}' en Keycloak: {}", username, e.getMessage(), e);
            throw new RuntimeException("Excepción durante la creación del usuario en Keycloak: " + e.getMessage(), e);
        } finally {
            if (response != null) {
                response.close(); // ¡Muy importante cerrar el Response!
            }
        }
        return createdUserId;
    }
}