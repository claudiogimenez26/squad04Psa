package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "tbl_estados_tarea")
public class EstadoTarea {
  @Id private UUID tareaId;

  @Column(nullable = false)
  private boolean activa;

  protected EstadoTarea() {}

  public EstadoTarea(UUID tareaId, boolean activa) {
    this.tareaId = tareaId;
    this.activa = activa;
  }

  public UUID getTareaId() {
    return tareaId;
  }

  public boolean getActiva() {
    return activa;
  }
}
