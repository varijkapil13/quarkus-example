package dev.varij.tasks.managers;

import dev.varij.tasks.helpers.GroupJSON;
import dev.varij.tasks.model.Group;
import dev.varij.tasks.model.GroupUserMapping;
import dev.varij.tasks.model.Task;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@RequestScoped
@Transactional
public class GroupManager {
  
  
  public List<Group> getAllGroups() {
    return Group.findAll().list();
  }
  
  public List<Group> getListsForUser(String userId) {
    
    return GroupUserMapping.findGroupsForUser(userId);
  }
  
  public Response createNewGroupForUser(Group group, String userId) {
    if (group != null) {
      group.ownerId = userId;
      if (group.isValidGroup()) {
        Group.persist(group);
        var groupUserMapping = new GroupUserMapping();
        groupUserMapping.groupId = group.id;
        groupUserMapping.userId = userId;
        GroupUserMapping.persist(groupUserMapping);
        
        return Response.created(URI.create("/task-list/" + group.id)).build();
      }
    }
    return Response.status(Status.BAD_REQUEST).build();
  }
  
  public Response addUserToList(Long listId, String ownerId, String userToBeAddedId) {
    if (Group.isUserOwnerOfList(listId, ownerId)) {
      var groupUserMapping = new GroupUserMapping();
      groupUserMapping.groupId = listId;
      groupUserMapping.userId = userToBeAddedId;
      GroupUserMapping.persist(groupUserMapping);
      return Response.ok().build();
    }
    return Response.status(Status.FORBIDDEN).build();
  }
  
  public Response findUsersRelatedToList(Long listId) {
    final Optional<Group> group = Group.findByIdOptional(listId);
    if (group.isPresent()) {
      final List<String> userIdsForGroup = GroupUserMapping.findUserIdsForGroup(group.get().id);
      return Response.ok().entity(userIdsForGroup).build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  public Response deleteGroup(Long listId) {
    final Optional<Group> group = Group.findByIdOptional(listId);
    if (group.isPresent()) {
      final Group list = group.get();
      Group.deleteById(list.id);
      GroupUserMapping.deleteGroup(list.id);
      return Response.ok().build();
    }
    return Response.status(Status.NOT_FOUND).build();
  }
  
  public Response deleteUserFromGroup(Long listId, String ownerId, String userId) {
    if (Group.isUserOwnerOfList(listId, ownerId)) {
      GroupUserMapping.deleteUserFromList(listId, userId);
      return Response.ok().build();
    }
    return Response.status(Status.FORBIDDEN).build();
  }
  
  
  public Response getListDetails(Long listId, String userId) {
    if (GroupUserMapping.isMemberOfList(listId, userId)) {
      final Optional<Group> list = Group.findByIdOptional(listId);
      if (list.isPresent()) {
        final List<Task> tasks = Task.findAllByListId(listId);
        final Group listEntity = list.get();
        final GroupJSON groupJSON = new GroupJSON(listEntity.name, listEntity.ownerId, tasks);
        return Response.ok().entity(groupJSON).build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      return Response.status(Status.FORBIDDEN).build();
    }
  }
}
