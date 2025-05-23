package co.edu.unicauca.microserviceCompany.service;

import jakarta.ws.rs.NotFoundException; // Importar para manejar rol no encontrado
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
import java.util.List;
// Elimina import java.util.List; si no se usa directamente aquí

@Service
public class KeycloakAdminService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminService.class);
    private static final String APP_CLIENT_ID_CONTAINING_ROLES = "sistema-desktop";

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String serviceAccountAuthRealm;

    @Value("${keycloak.resource}")
    private String serviceAccountClientId;

    @Value("${keycloak.credentials.secret}")
    private String serviceAccountClientSecret;

    @Value("${app.keycloak.target-realm}")
    private String targetAppRealm;

    private Keycloak keycloakAdminClientInstance;

    private synchronized Keycloak getKeycloakAdminClient() {
        if (keycloakAdminClientInstance == null) {
            logger.info("Inicializando instancia de Keycloak Admin Client...");
            logger.info("Server URL: {}", keycloakServerUrl);
            logger.info("Service Account Auth Realm: {}", serviceAccountAuthRealm);
            logger.info("Service Account Client ID: {}", serviceAccountClientId);
            try {
                keycloakAdminClientInstance = KeycloakBuilder.builder()
                        .serverUrl(keycloakServerUrl)
                        .realm(serviceAccountAuthRealm)
                        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                        .clientId(serviceAccountClientId)
                        .clientSecret(serviceAccountClientSecret)
                        .build();
                logger.info("Instancia de Keycloak Admin Client creada exitosamente.");
            } catch (Exception e) {
                logger.error("FALLO AL CREAR LA INSTANCIA DE KEYCLOAK ADMIN CLIENT: {}", e.getMessage(), e);
                throw new RuntimeException("No se pudo inicializar el cliente de administración de Keycloak", e);
            }
        }
        return keycloakAdminClientInstance;
    }

    public String   createUserInKeycloak(String username, String email, String password, String roleNameToAssign, String name, String lastName) {
        Keycloak keycloak = getKeycloakAdminClient();
        RealmResource appRealmResource = keycloak.realm(targetAppRealm); // targetAppRealm es "sistema"
        UsersResource usersResource = appRealmResource.users();

        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setFirstName(name);
        newUser.setLastName(lastName);
        newUser.setEmailVerified(true);
        newUser.setEnabled(true);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        passwordCred.setTemporary(false);
        newUser.setCredentials(Collections.singletonList(passwordCred));

        Response response = null;
        String createdUserId = null;

        try {
            logger.info("Intentando crear usuario '{}' en realm '{}' de Keycloak.", username, targetAppRealm);
            response = usersResource.create(newUser);

            if (response.getStatus() == 201) {
                String locationHeader = response.getLocation().toString();
                createdUserId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);
                logger.info("Usuario '{}' creado en Keycloak con ID: {}. Intentando asignar Client Role...", username, createdUserId);

                if (roleNameToAssign != null && !roleNameToAssign.isEmpty()) {
                    UserResource createdUserResource = usersResource.get(createdUserId);

                    // --- LÓGICA PARA ASIGNAR CLIENT ROLE ---
                    // Primero, obtener el ID interno del cliente "sistema-desktop"
                    List<org.keycloak.representations.idm.ClientRepresentation> clients = appRealmResource.clients()
                            .findByClientId(APP_CLIENT_ID_CONTAINING_ROLES);

                    if (clients == null || clients.isEmpty()) {
                        logger.error("Error Crítico: El cliente '{}' (donde se definen los roles de aplicación) no fue encontrado en el realm '{}'. No se puede asignar el rol.",
                                APP_CLIENT_ID_CONTAINING_ROLES, targetAppRealm);
                        throw new RuntimeException("Configuración: Cliente '" + APP_CLIENT_ID_CONTAINING_ROLES + "' no encontrado.");
                    }
                    String internalAppClientId = clients.get(0).getId(); // ID interno del cliente "sistema-desktop"

                    RoleRepresentation clientRoleToAssign = null;
                    try {
                        logger.info("Buscando Client Role '{}' dentro del cliente '{}' (ID interno: {}) en realm '{}'",
                                roleNameToAssign, APP_CLIENT_ID_CONTAINING_ROLES, internalAppClientId, targetAppRealm);
                        // Obtener el rol del cliente específico
                        clientRoleToAssign = appRealmResource.clients().get(internalAppClientId).roles().get(roleNameToAssign).toRepresentation();
                        logger.info("Client Role '{}' encontrado para el cliente '{}'.", roleNameToAssign, APP_CLIENT_ID_CONTAINING_ROLES);
                    } catch (NotFoundException nfe) {
                        logger.warn("El Client Role '{}' NO FUE ENCONTRADO para el cliente '{}' en el realm '{}'. Verifique que el rol exista y el nombre sea exacto.",
                                roleNameToAssign, APP_CLIENT_ID_CONTAINING_ROLES, targetAppRealm);
                        throw new RuntimeException("Configuración: El rol '" + roleNameToAssign + "' requerido no existe como Client Role de '" + APP_CLIENT_ID_CONTAINING_ROLES + "'.");
                    } catch (Exception e) {
                        logger.error("Error inesperado al intentar obtener el Client Role '{}' del cliente '{}': {}",
                                roleNameToAssign, APP_CLIENT_ID_CONTAINING_ROLES, e.getMessage(), e);
                        throw new RuntimeException("Error obteniendo Client Role: " + e.getMessage(), e);
                    }

                    if (clientRoleToAssign != null) {
                        try {
                            // Asignar el Client Role al nivel del cliente
                            createdUserResource.roles().clientLevel(internalAppClientId).add(Collections.singletonList(clientRoleToAssign));
                            logger.info("Client Role '{}' (del cliente '{}') asignado exitosamente al usuario '{}' (ID: {}).",
                                    roleNameToAssign, APP_CLIENT_ID_CONTAINING_ROLES, username, createdUserId);
                        } catch (Exception e) {
                            logger.error("FALLO al asignar el Client Role '{}' al usuario '{}' (ID: {}): {}",
                                    roleNameToAssign, username, createdUserId, e.getMessage(), e);
                            throw new RuntimeException("Usuario creado en Keycloak, pero falló la asignación del Client Role: " + e.getMessage(), e);
                        }
                    } else {
                        // Esto no debería ocurrir si se lanzó la excepción NotFoundException antes
                        logger.warn("No se asignó el Client Role '{}' al usuario '{}' porque no se encontró.", roleNameToAssign, username);
                    }
                } else {
                    logger.warn("No se especificó un nombre de rol para asignar al usuario '{}'.", username);
                }
            } else {
                String errorDetails = response.hasEntity() ? response.readEntity(String.class) : "No hay detalles adicionales del error.";
                logger.error("Error al crear usuario '{}' en Keycloak. Status: {}, Info: {}, Detalles: {}",
                        username, response.getStatus(), response.getStatusInfo(), errorDetails);
                throw new RuntimeException("Fallo al crear usuario en Keycloak. Status: " + response.getStatus() + ". Detalles: " + errorDetails);
            }
        } catch (Exception e) {
            logger.error("Excepción general durante la creación del usuario '{}' o asignación de rol en Keycloak: {}", username, e.getMessage(), e);
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException("Excepción durante el proceso de Keycloak: " + e.getMessage(), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return createdUserId;
    }
}