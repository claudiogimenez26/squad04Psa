package psa.cargahoras.dto;

public class CostoProyectoDTO {
  private String id, proyectoId, nombreProyecto;
  private int costo;

  public CostoProyectoDTO(String id, String proyectoId, String nombreProyecto, int costo) {
    this.id = id;
    this.proyectoId = proyectoId;
    this.nombreProyecto = nombreProyecto;
    this.costo = costo;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getProyectoId() {
    return proyectoId;
  }

  public void setProyectoId(String proyectoId) {
    this.proyectoId = proyectoId;
  }

  public String getNombreProyecto() {
    return nombreProyecto;
  }

  public void setNombreProyecto(String nombreProyecto) {
    this.nombreProyecto = nombreProyecto;
  }

  public Integer getCosto() {
    return costo;
  }

  public void setCosto(int costo) {
    this.costo = costo;
  }
}
