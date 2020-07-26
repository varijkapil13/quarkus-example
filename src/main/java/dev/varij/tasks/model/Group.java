package dev.varij.tasks.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@RegisterForReflection
@Table(name = "vk_group")
public class Group extends AuditEntity {
  
  public String name;
  
  @JsonProperty(access = Access.READ_ONLY)
  @Column(name = "owner_id")
  public String ownerId;
  
  
  public static boolean isUserOwnerOfList(Long listId, String ownerId) {
    final Optional<Group> group = find("owner_id = ?1 and id = ?1", ownerId, listId).firstResultOptional();
    
    return group.isPresent();
    
  }
  
  @JsonIgnore
  public boolean isValidGroup() {
    return name != null && ownerId != null && !ownerId.isBlank();
  }
}
