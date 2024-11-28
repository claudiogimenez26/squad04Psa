package psa.cargahoras.dto;

import java.util.List;

public class ResumenCostoRecursoDTO {

  private String id;
  private String nombre;
  private String rol;
  private double costoRol;
  private List<CostoMensualDTO> costos;

  public ResumenCostoRecursoDTO(
      String id, String nombre, String rol, double costoRol, List<CostoMensualDTO> costos) {
    this.id = id;
    this.nombre = nombre;
    this.rol = rol;
    this.costoRol = costoRol;
    this.costos = costos;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public double getCostoRol() {
    return costoRol;
  }

  public void setCostoRol(double costoRol) {
    this.costoRol = costoRol;
  }

  public List<CostoMensualDTO> getCostos() {
    return costos;
  }

  public void setCostos(List<CostoMensualDTO> costos) {
    this.costos = costos;
  }
}
