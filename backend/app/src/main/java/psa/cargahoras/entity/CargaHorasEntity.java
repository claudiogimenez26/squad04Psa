package psa.cargahoras.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

@Entity(name = "tbl_carga_de_horas")
public class CargaHorasEntity {
  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "tarea_id")
  private TareaEntity tarea;

  @ManyToOne
  @JoinColumn(name = "empleado_id")
  private EmpleadoEntity empleado;

  protected CargaHorasEntity() {}

  public CargaHorasEntity(TareaEntity tarea, EmpleadoEntity empleado) {
    this.id = UUID.randomUUID();
    this.tarea = tarea;
    this.empleado = empleado;
  }

  public UUID getId() {
    return id;
  }
}
