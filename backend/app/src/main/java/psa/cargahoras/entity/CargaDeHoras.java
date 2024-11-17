package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "tbl_carga_de_horas")
public class CargaDeHoras {
  @Id @GeneratedValue private UUID id;

  @ManyToOne
  @JoinColumn(name = "tareaId", nullable = false)
  private Tarea tarea;

  @ManyToOne
  @JoinColumn(name = "recursoId", nullable = false)
  private Recurso recurso;

  @Column(nullable = false)
  private LocalDateTime fechaCarga;

  @Column(nullable = false)
  private double cantidadHoras;

  protected CargaDeHoras() {}

  public CargaDeHoras(UUID id, Tarea tarea, Recurso recurso, LocalDateTime fechaCarga, double cantidadHoras) {
    this.id = id;
    this.tarea = tarea;
    this.recurso = recurso;
    this.fechaCarga = fechaCarga;
    this.cantidadHoras = cantidadHoras;
  }

  public UUID getId() {
    return id;
  }

  public Tarea getTarea() {
    return tarea;
  }

  public Recurso getRecurso() {
    return recurso;
  }

  public LocalDateTime getFechaCarga() {
    return fechaCarga;
  }

  public double getCantidadHoras() {
    return cantidadHoras;
  }
}
