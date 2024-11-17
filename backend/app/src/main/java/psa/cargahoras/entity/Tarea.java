package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "tbl_tareas")
public class Tarea {
  @Id private UUID id;

  @Column(nullable = false)
  private String nombre;

  @Column(nullable = false)
  private String descripcion;

  @ManyToOne
  @JoinColumn(name = "proyectoId", nullable = false)
  private Proyecto proyecto;

  @OneToMany(mappedBy = "tarea")
  private List<CargaDeHoras> cargaDeHoras;

  public Tarea(UUID id, String nombre, String descripcion, Proyecto proyecto) {
    if (proyecto == null) {
      throw new NullPointerException("El proyecto pasado tiene que existir");
    }

    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.proyecto = proyecto;
    this.cargaDeHoras = new ArrayList<>();
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

  public Proyecto getProyecto() {
    return proyecto;
  }

  public List<CargaDeHoras> getCargaDeHoras() {
    return cargaDeHoras;
  }
}
