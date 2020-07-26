package dev.varij.tasks.endpoints;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;


import io.quarkus.test.junit.QuarkusTest;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TaskResourceTest {
  
  @Test
  void getAllTasks() {
    given().when().get("/tasks").then().statusCode(200)
        .body("$.size()", is(1), "title", containsInAnyOrder("Sample Task"), "description", containsInAnyOrder("Sample description"));
  }
  
  @Test
  void addNewTask() {
    given()
        .body("{\"title\": \"New Task\", \"description\": \"New Task Description\"}")
        .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
        .post("/tasks")
        .then()
        .statusCode(200)
        .body("$.size()", is(2),
            "title", containsInAnyOrder("Sample Task", "New Task"),
            "description", containsInAnyOrder("Sample description", "New Task Description"));
    
    
  }
  
  @Test
  void deleteATask() {
    given()
        .body("{\"title\": \"New Task\", \"description\": \"New Task Description\"}")
        .header("Content-Type", MediaType.APPLICATION_JSON)
        .when()
        .delete("/tasks")
        .then()
        .statusCode(200)
        .body("$.size()", is(1),
            "title", containsInAnyOrder("Sample Task"),
            "description", containsInAnyOrder("Sample description"));
  }
}
