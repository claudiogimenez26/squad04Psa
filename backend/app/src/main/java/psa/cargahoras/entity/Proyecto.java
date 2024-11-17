package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "tbl_proyectos")
public class Proyecto {
  @Id
  private UUID id;

  @Column(nullable = false)
  private String nombre;

  @Column(nullable = false)
  private String descripcion;

  @OneToMany(mappedBy = "proyecto")
  private List<Tarea> tareas;

  protected Proyecto() {}

  public Proyecto(UUID id, String nombre, String descripcion) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.tareas = new ArrayList<>();
  }

  public UUID getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public List<Tarea> getTareas() {
    return tareas;
  }

  public void addTareas(Tarea... tareas) {
    for (Tarea t: tareas) {
      this.tareas.add(t);
    }
  }
}
