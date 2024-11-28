package psa.cargahoras.dto;

public class RolDTO {

  private String id;
  private String nombre;
  private String experiencia;

  private double definirCosto() {
    return switch (String.join(" ", nombre, experiencia)) {
      case "Desarrollador Senior" -> 30;
      case "Desarrollador Semi-Senior" -> 25;
      case "Desarrollador Junior" -> 20;
      case "Analista nivel I" -> 20;
      case "Analista nivel II" -> 25;
      default -> 30;
    };
  }

  public RolDTO() {}

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

  public String getExperiencia() {
    return experiencia;
  }

  public void setExperiencia(String experiencia) {
    this.experiencia = experiencia;
  }

  public double getCosto() {
    return definirCosto();
  }
}
