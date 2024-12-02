package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity(name = "tbl_carga_de_horas")
public class CargaDeHoras {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false)
  private String tareaId;

  @Column(nullable = false)
  private String recursoId;

  @Column(nullable = false)
  private double cantidadHoras;

  @Column(nullable = false)
  private LocalDate fechaCarga;

  public static DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  protected CargaDeHoras() {}

  public CargaDeHoras(
      String tareaId, String recursoId, double cantidadHoras, String fechaCargaStr) {
    if (cantidadHoras < 0) {
      throw new IllegalArgumentException("La cantidad de horas no puede ser negativa");
    }
    if (tareaId == null) {
      throw new IllegalArgumentException("El id de la tarea no puede ser nulo");
    }
    if (recursoId == null) {
      throw new IllegalArgumentException("El id del recurso no puede ser nulo");
    }
    if (fechaCargaStr == null) {
      throw new IllegalArgumentException("La fecha de carga no puede ser nula");
    }

    try {
      this.fechaCarga = LocalDate.parse(fechaCargaStr, formatterFecha);
    } catch (Exception e) {
      throw new IllegalArgumentException("El formato de la fecha debe ser dd/MM/yyyy", e);
    }

    this.tareaId = tareaId;
    this.recursoId = recursoId;
    this.cantidadHoras = cantidadHoras;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTareaId() {
    return tareaId;
  }

  public void setTareaId(String tareaId) {
    this.tareaId = tareaId;
  }

  public String getRecursoId() {
    return recursoId;
  }

  public void setRecursoId(String recursoId) {
    this.recursoId = recursoId;
  }

  public double getCantidadHoras() {
    return cantidadHoras;
  }

  public void setCantidadHoras(double cantidadHoras) {
    this.cantidadHoras = cantidadHoras;
  }

  public LocalDate getFechaCarga() {
    return fechaCarga;
  }

  public void setFechaCarga(String fechaCargaStr) {
    try {
      this.fechaCarga = LocalDate.parse(fechaCargaStr, formatterFecha);
    } catch (Exception e) {
      throw new IllegalArgumentException("El formato de la fecha debe ser dd/MM/yyyy", e);
    }
  }
}
