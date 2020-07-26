package dev.varij.tasks.helpers;

import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.UserRepresentation;

public final class UserJSON {
  
  final String email;
  final String firstName;
  final String lastName;
  final String id;
  final String username;
  final List<String> realmRoles;
  final Map<String, List<String>> clientRoles;
  
  public UserJSON(UserRepresentation keycloakUser) {
    this.email = keycloakUser.getEmail();
    this.firstName = keycloakUser.getFirstName();
    this.lastName = keycloakUser.getLastName();
    this.id = keycloakUser.getId();
    this.username = keycloakUser.getUsername();
    this.realmRoles = keycloakUser.getRealmRoles();
    this.clientRoles = keycloakUser.getClientRoles();
  }
  
  public String getEmail() {
    return email;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public String getId() {
    return id;
  }
  
  public String getUsername() {
    return username;
  }
  
  public List<String> getRealmRoles() {
    return realmRoles;
  }
  
  public Map<String, List<String>> getClientRoles() {
    return clientRoles;
  }
}
