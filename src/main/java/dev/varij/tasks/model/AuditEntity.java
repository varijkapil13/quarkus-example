package dev.varij.tasks.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class AuditEntity extends PanacheEntity {
  
  @Column(name = "created_at", nullable = false)
  public LocalDateTime createdAt;
  @Column(name = "updated_at", nullable = false)
  public LocalDateTime updatedAt;
  
  @PrePersist
  public void prePersist() {
    var dateTime = LocalDateTime.now();
    this.createdAt = dateTime;
    this.updatedAt = dateTime;
  }
  
  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}
