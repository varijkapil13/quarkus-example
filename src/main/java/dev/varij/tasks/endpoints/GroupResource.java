package dev.varij.tasks.endpoints;

import dev.varij.tasks.helpers.TaskAppRoles;
import dev.varij.tasks.managers.GroupManager;
import dev.varij.tasks.model.Group;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/lists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource {
  
  @Inject GroupManager manager;
  @Inject JsonWebToken jsonWebToken;
  
  
  @GET
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public List<Group> getListForUser() {
    return manager.getListsForUser(getCurrentUserIdFromToken());
  }
  
  private String getCurrentUserIdFromToken() {
    return jsonWebToken.getSubject();
  }
  
  @GET
  @Path("/{listId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response getListDetails(@PathParam("listId") Long listId) {
    return manager.getListDetails(listId, getCurrentUserIdFromToken());
  }
  
  @POST
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response addNewList(Group group) {
    return manager.createNewGroupForUser(group, getCurrentUserIdFromToken());
  }
  
  @DELETE
  @Path("/{groupId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response deleteAList(@PathParam("groupId") Long groupId) {
    return manager.deleteGroup(groupId);
  }
  
  @GET
  @Path("/{groupId}/user")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response findUsersInList(@PathParam("groupId") Long groupId) {
    return manager.findUsersRelatedToList(groupId);
  }
  
  @DELETE
  @Path("/{groupId}/user/{userId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response deleteUserFromList(@PathParam("groupId") Long groupId, @PathParam("userId") @NotBlank String userId) {
    return manager.deleteUserFromGroup(groupId, getCurrentUserIdFromToken(), userId);
  }
  
  @PUT
  @Path("/{groupId}/user/{userId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response addUserToList(@PathParam("groupId") Long groupId, @PathParam("userId") @NotBlank String userId) {
    return manager.addUserToList(groupId, getCurrentUserIdFromToken(), userId);
  }
}
