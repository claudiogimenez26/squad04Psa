package psa.cargahoras.dto;

public class CostoRecursoDTO {

  private String id;
  private String rolId;
  private double costo;
  private String nombreRecurso;
  private String nombreRol;
  private double horasTrabajadas;
  private double costoRol;

  public CostoRecursoDTO(
      String id,
      String rolId,
      double costo,
      String nombreRecurso,
      String nombreRol,
      double horasTrabajadas,
      double costoRol) {
    this.id = id;
    this.rolId = rolId;
    this.costo = costo;
    this.nombreRecurso = nombreRecurso;
    this.nombreRol = nombreRol;
    this.horasTrabajadas = horasTrabajadas;
    this.costoRol = costoRol;
  }

  public double getHorasTrabajadas() {
    return horasTrabajadas;
  }

  public double getCostoRol() {
    return costoRol;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRolId() {
    return rolId;
  }

  public void setRolId(String rolId) {
    this.rolId = rolId;
  }

  public double getCosto() {
    return costo;
  }

  public void setCosto(int costo) {
    this.costo = costo;
  }

  public String getNombreRecurso() {
    return nombreRecurso;
  }

  public void setNombreRecurso(String nombreRecurso) {
    this.nombreRecurso = nombreRecurso;
  }

  public String getNombreRol() {
    return nombreRol;
  }

  public void setNombreRol(String nombreRol) {
    this.nombreRol = nombreRol;
  }
}
