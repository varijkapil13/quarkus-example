package dev.varij.tasks.endpoints;

import dev.varij.tasks.helpers.TaskAppRoles;
import dev.varij.tasks.managers.TaskManager;
import dev.varij.tasks.model.Task;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/tasks")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
  
  @Inject TaskManager taskManager;
  @Inject JsonWebToken jsonWebToken;
  
  @GET
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public List<Task> getAllTasks() {
    return Task.findByUser(getCurrentUserIdFromToken());
  }
  
  private String getCurrentUserIdFromToken() {
    return jsonWebToken.getSubject();
  }
  
  @POST
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response addNewTask(Task task) {
    return taskManager.createNewTask(task, getCurrentUserIdFromToken());
  }
  
  @DELETE
  @Path("/{taskId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response deleteATask(@PathParam("taskId") Long taskId) {
    return taskManager.deleteTask(taskId, getCurrentUserIdFromToken());
  }
  
  @GET
  @Path("/{taskId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response getTask(@PathParam("taskId") Long taskId) {
    return taskManager.getTaskDetails(taskId, getCurrentUserIdFromToken());
  }
  
  @PUT
  @Path("/{taskId}/complete")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response addTaskToList(@PathParam("taskId") Long taskId) {
    return taskManager.competeTask(taskId, getCurrentUserIdFromToken());
  }
  
  @PUT
  @Path("/{taskId}")
  @RolesAllowed({TaskAppRoles.TASK_APP_ADMIN, TaskAppRoles.USER})
  public Response addTaskToList(@PathParam("taskId") Long taskId, @QueryParam("listId") Long listId) {
    return taskManager.addTaskToList(taskId, listId, getCurrentUserIdFromToken());
  }
}
