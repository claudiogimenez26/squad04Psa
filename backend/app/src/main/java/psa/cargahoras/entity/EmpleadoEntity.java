package psa.cargahoras.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity(name = "tbl_empleados")
public class EmpleadoEntity {
  @Id @GeneratedValue private UUID id;

  @Column private String nombre;

  @Column private String apellido;

  @Column private String cargo;

  @Column private Double salario;

  protected EmpleadoEntity() {}

  public EmpleadoEntity(String nombre, String apellido, String cargo, Double salario) {
    this.id = UUID.randomUUID();
    this.nombre = nombre;
    this.apellido = apellido;
    this.cargo = cargo;
    this.salario = salario;
  }
}
