package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;

@Entity(name = "tbl_recursos")
public class Recurso {
  @Id private UUID id;

  @Column(nullable = false)
  private String nombre;

  @Column(nullable = false)
  private String apellido;

  @Column(nullable = false, unique = true, updatable = false)
  private long dni;

  @ManyToOne
  @JoinColumn(name = "rolId")
  private Rol rol;

  protected Recurso() {}

  public Recurso(UUID id, String nombre, String apellido, long dni, Rol rol) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.dni = dni;
    this.rol = rol;
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

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Long getDni() {
    return dni;
  }

  public Rol getRol() {
    return rol;
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }
}
