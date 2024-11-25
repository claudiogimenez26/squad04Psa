package psa.cargahoras.dto;

import java.time.LocalDate;
import psa.cargahoras.entity.CargaDeHoras;

public class CargaDeHorasPorRecursoDTO {

  private String id;
  private String tareaId;
  private double cantidadHoras;
  private String fechaCarga;
  private String nombreProyecto;

  public CargaDeHorasPorRecursoDTO(
      String id,
      String tareaId,
      double cantidadHoras,
      LocalDate fechaCarga,
      String nombreProyecto) {
    this.id = id;
    this.tareaId = tareaId;
    this.cantidadHoras = cantidadHoras;
    this.fechaCarga = fechaCarga.format(CargaDeHoras.formatterFecha);
    this.nombreProyecto = nombreProyecto;
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

  public double getCantidadHoras() {
    return cantidadHoras;
  }

  public void setCantidadHoras(double cantidadHoras) {
    this.cantidadHoras = cantidadHoras;
  }

  public String getFechaCarga() {
    return fechaCarga;
  }

  public void setFechaCarga(String fechaCarga) {
    this.fechaCarga = fechaCarga;
  }

  public String getNombreProyecto() {
    return nombreProyecto;
  }

  public void setNombreProyecto(String nombreProyecto) {
    this.nombreProyecto = nombreProyecto;
  }
}
