package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Entity(name = "tbl_proyectos")
public class ProyectoEntity {
  public enum Estado {
    Activo,
    Pausado,
    Finalizado
  }

  @Id @GeneratedValue private UUID id;

  @Column private String nombre;

  @Column private double coeficienteRiesgo;

  @Enumerated(EnumType.STRING)
  private Estado estado;

  @Column private LocalDate fechaInicio;

  public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  protected ProyectoEntity() {}

  public ProyectoEntity(
      String nombre, double coeficienteRiesgo, Estado estado, String fechaInicioStr) {
    if (coeficienteRiesgo < 0 || coeficienteRiesgo > 1) {
      throw new IllegalArgumentException(
          "Coeficiente de riesgo debe estar entre 0 y 1 (inclusive).");
    }

    try {
      this.fechaInicio = LocalDate.parse(fechaInicioStr, dateTimeFormatter);
    } catch (DateTimeParseException exception) {
      throw new IllegalArgumentException("Formato de fecha debe ser dd/MM/yyyy");
    }

    this.id = UUID.randomUUID();
    this.nombre = nombre;
    this.coeficienteRiesgo = coeficienteRiesgo;
    this.estado = estado;
  }

  public UUID getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public Double getCoeficienteRiesgo() {
    return coeficienteRiesgo;
  }

  public Estado getEstado() {
    return estado;
  }

  public LocalDate getFechaInicio() {
    return fechaInicio;
  }
}
