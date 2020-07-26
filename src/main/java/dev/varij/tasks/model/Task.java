package dev.varij.tasks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@RegisterForReflection
@Table(name = "vk_task")
public class Task extends AuditEntity {
  
  public String title;
  public String description;
  @Column(name = "due_date")
  public LocalDateTime dueDate;
  public boolean priority;
  @JsonProperty(access = Access.READ_ONLY)
  public boolean complete;
  
  @JsonProperty(access = Access.READ_ONLY)
  @Column(name = "user_id")
  public String userId;
  public Long listId;
  
  public Task() {
  }
  
  public static List<Task> findByUser(String userId) {
    System.out.println("userId = " + userId);
    return find("user_id", Sort.ascending("due_date"), userId).list();
    
  }
  
  public static List<Task> findByTitleForUser(String title, String userId) {
    return find("title = ?1 and userId = ?2", title, userId).list();
  }
  
  public static List<Task> findCompletedForUser(String userId) {
    return find("complete = ?1 and userId = ?2", true, userId).list();
  }
  
  public static List<Task> findAllByListId(Long listId) {
    return find("listId", listId).list();
  }
  
  @JsonIgnore
  public boolean isValidTask() {
    if (dueDate != null && !dueDate.isAfter(LocalDateTime.now())) {
      return false;
    }
    return title != null && description != null && userId != null && !title.isBlank() && !description.isBlank();
  }
  
  
}
