package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "tbl_carga_de_horas")
public class CargaDeHoras {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "tareaId", nullable = false)
  private Tarea tarea;

  @ManyToOne
  @JoinColumn(name = "recursoId", nullable = false)
  private Recurso recurso;

  @Column(nullable = false)
  private LocalDate fechaCarga;

  @Column(nullable = false)
  private double cantidadHoras;

  public static DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  protected CargaDeHoras() {}

  public CargaDeHoras(Tarea tarea, Recurso recurso, String fechaCargaStr, double cantidadHoras) {
    if (cantidadHoras < 0) {
      throw new IllegalArgumentException("La cantidad de horas no puede ser negativa");
    }
    if (tarea == null) {
      throw new IllegalArgumentException("La tarea no puede ser nula");
    }
    if (recurso == null) {
      throw new IllegalArgumentException("El recurso no puede ser nulo");
    }
    if (fechaCargaStr == null) {
      throw new IllegalArgumentException("La fecha de carga no puede ser nula");
    }

    try {
      this.fechaCarga = LocalDate.parse(fechaCargaStr, formatterFecha);
    } catch (Exception e) {
      throw new IllegalArgumentException("El formato de la fecha debe ser dd/MM/yyyy", e);
    }

    this.tarea = tarea;
    this.recurso = recurso;
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

  public LocalDate getFechaCarga() {
    return fechaCarga;
  }

  public double getCantidadHoras() {
    return cantidadHoras;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CargaDeHoras that = (CargaDeHoras) o;
    return Double.compare(that.cantidadHoras, cantidadHoras) == 0
        && Objects.equals(tarea, that.tarea)
        && Objects.equals(recurso, that.recurso)
        && Objects.equals(fechaCarga, that.fechaCarga);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tarea, recurso, fechaCarga, cantidadHoras);
  }
}
