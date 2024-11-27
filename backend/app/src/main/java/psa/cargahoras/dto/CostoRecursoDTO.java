package psa.cargahoras.dto;

public class CostoRecursoDTO {

  private String id;
  private String rolId;
  private int costo;
  private String nombreRecurso;
  private String nombreRol;

  public CostoRecursoDTO(
      String id, String rolId, int costo, String nombreRecurso, String nombreRol) {
    this.id = id;
    this.rolId = rolId;
    this.costo = costo;
    this.nombreRecurso = nombreRecurso;
    this.nombreRol = nombreRol;
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

  public int getCosto() {
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
