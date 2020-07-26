package dev.varij.tasks.endpoints;

import static java.util.stream.Collectors.toSet;


import dev.varij.tasks.helpers.TaskAppRoles;
import dev.varij.tasks.helpers.UserJSON;
import java.util.List;
import java.util.Set;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
  
  private static final String SERVER_URL = "http://localhost:9991/auth";
  private static final String REALM_NAME = "vk_task_app";
  private static final String CLIENT_ID = "vk_task_app";
  private static final String CLIENT_SECRET = "369e2612-23d5-4519-b95a-8e2cc69e0bda";
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "admin";
  
  
  @GET
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN})
  public Set<UserJSON> getAllUsers() {
    return users();
  }
  
  private Set<UserJSON> users() {
    
    final List<UserRepresentation> list = getUserRepresentations();
    System.out.println("list = " + list);
    return list.stream().map(UserJSON::new).collect(toSet());
  }
  
  private List<UserRepresentation> getUserRepresentations() {
    var keycloak =
        KeycloakBuilder.builder().username(USERNAME).password(PASSWORD).serverUrl(SERVER_URL).realm(REALM_NAME).clientId(CLIENT_ID).clientSecret(CLIENT_SECRET)
            .build();
    var realmResource = keycloak.realm(REALM_NAME);
    var userResource = realmResource.users();
    return userResource.list();
  }
  
  @GET
  @Path("/1")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN})
  public List<UserRepresentation> getAllUsersNNew() {
    return getUserRepresentations();
  }
}
