package psa.cargahoras.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "tbl_tareas")
public class TareaEntity {
  @Id @GeneratedValue private UUID id;

  public TareaEntity() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }
}
