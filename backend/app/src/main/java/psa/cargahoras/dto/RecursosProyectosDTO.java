package psa.cargahoras.dto;

import java.util.List;
import psa.cargahoras.entity.CargaDeHoras;

public class RecursosProyectosDTO {

  private String proyectoId;
  private String recursoId;
  private String nombreRecurso;
  private String dniRecurso;
  private List<CargaDeHoras> cargasDeHoras;

  public RecursosProyectosDTO(
      String proyectoId,
      String recursoId,
      String nombreRecurso,
      String dniRecurso,
      List<CargaDeHoras> cargasDeHoras) {
    this.proyectoId = proyectoId;
    this.recursoId = recursoId;
    this.nombreRecurso = nombreRecurso;
    this.dniRecurso = dniRecurso;
    this.cargasDeHoras = cargasDeHoras;
  }
}
