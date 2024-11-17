package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "tbl_roles")
public class Rol {
  @Id private UUID id;

  @Column(nullable = false, unique = true)
  private String nombre;

  @Column(nullable = false)
  private String experiencia;

  @Column(nullable = false)
  private double salario;

  protected Rol() {}

  public Rol(UUID id, String nombre, String experiencia, double salario) {
    this.id = id;
    this.nombre = nombre;
    this.experiencia = experiencia;
    this.salario = salario;
  }

  public UUID getId() {
    return id;
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

  public double getSalario() {
    return salario;
  }

  public void setSalario(double salario) {
    this.salario = salario;
  }
}
