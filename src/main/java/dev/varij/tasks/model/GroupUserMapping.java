package dev.varij.tasks.model;

import static java.util.stream.Collectors.toList;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@RegisterForReflection
@Table(name = "vk_group_user_mapping")
public class GroupUserMapping extends AuditEntity {
  
  @Column(name = "user_id")
  public String userId;
  
  @Column(name = "group_id")
  public Long groupId;
  
  public static List<Group> findGroupsForUser(String userId) {
    Stream<GroupUserMapping> groupUserMappings = find("user_id", userId).stream();
    
    return Group.find("id in ?1", groupUserMappings.map(item -> item.groupId).collect(toList())).list();
  }
  
  public static List<String> findUserIdsForGroup(Long groupId) {
    Stream<GroupUserMapping> groupUserMappings = find("group_id", groupId).stream();
    
    return groupUserMappings.map(item -> item.userId).collect(toList());
  }
  
  public static void deleteGroup(Long listId) {
    delete("group_id", listId);
  }
  
  public static void deleteUserFromList(Long listId, String userId) {
    delete("group_id = ? and user_d = ?", listId, userId);
  }
  
  public static boolean isMemberOfList(Long listId, String userId) {
    final Optional<GroupUserMapping> mapping = find("group_id = ?1 and user_id = ?2", listId, userId).firstResultOptional();
    if (mapping.isPresent()) {
      final GroupUserMapping groupUserMapping = mapping.get();
      final Optional<Group> entity = Group.findByIdOptional(groupUserMapping.groupId);
      System.out.println("entity = " + entity.isPresent());
      return entity.isPresent();
    }
    return false;
  }
}
