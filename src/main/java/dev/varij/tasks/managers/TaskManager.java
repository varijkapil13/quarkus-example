package dev.varij.tasks.managers;

import dev.varij.tasks.model.Group;
import dev.varij.tasks.model.Task;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@RequestScoped
@Transactional
public class TaskManager {
  
  public Response createNewTask(Task task, String userId) {
    System.out.println("userId = " + userId);
    if (task != null) {
      System.out.println("task.title = " + task.title);
      task.userId = userId;
      System.out.println("task.valid = " + task.isValidTask());
      if (task.listId != null) {
        final Optional<Group> list = Group.findByIdOptional(task.listId);
        if (list.isEmpty()) {
          task.listId = null;
        }
      }
      if (task.isValidTask()) {
        Task.persist(task);
        return Response.ok().entity(task).build();
      }
    }
    return Response.status(Status.BAD_REQUEST).build();
  }
  
  
  public Response deleteTask(Long taskId, String userId) {
    Optional<Task> taskById = Task.findByIdOptional(taskId);
    if (taskById.isPresent()) {
      final Task taskEntity = taskById.get();
      if (!taskEntity.userId.equalsIgnoreCase(userId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
      final boolean delete = Task.deleteById(taskId);
      if (delete) {
        return Response.ok().build();
      }
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  public Response addTaskToList(Long taskId, Long listId, String userId) {
    final Optional<Group> list = Group.findByIdOptional(listId);
    final Optional<Task> task = Task.findByIdOptional(taskId);
    if (list.isPresent() && task.isPresent()) {
      final Task taskEntity = task.get();
      if (!taskEntity.userId.equalsIgnoreCase(userId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
      taskEntity.listId = listId;
      Task.persist(taskEntity);
      return Response.ok().build();
    }
    
    return Response.status(Status.NOT_FOUND).build();
  }
  
  public Response getTaskDetails(Long taskId, String userId) {
    final Optional<Task> task = Task.findByIdOptional(taskId);
    if (task.isPresent()) {
      final Task taskEntity = task.get();
      if (!taskEntity.userId.equalsIgnoreCase(userId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
      return Response.ok().entity(taskEntity).build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  public Response competeTask(Long taskId, String userId) {
    final Optional<Task> task = Task.findByIdOptional(taskId);
    if (task.isPresent()) {
      final Task taskEntity = task.get();
      if (!taskEntity.userId.equalsIgnoreCase(userId)) {
        return Response.status(Status.FORBIDDEN).build();
      }
      taskEntity.complete = true;
      Task.persist(taskEntity);
      return Response.ok().build();
    }
    return null;
  }
}
