package dev.varij.tasks.helpers;

import dev.varij.tasks.model.Task;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public final class GroupJSON {
  
  private final String name;
  private final String ownerId;
  private final List<Task> tasks;
  
  public GroupJSON(String listName, String listOwnerId, List<Task> tasks) {
    this.name = listName;
    this.ownerId = listOwnerId;
    this.tasks = tasks;
  }
  
  public String getName() {
    return name;
  }
  
  public String getOwnerId() {
    return ownerId;
  }
  
  public List<Task> getTasks() {
    return tasks;
  }
}
