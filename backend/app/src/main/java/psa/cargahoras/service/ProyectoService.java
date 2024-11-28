package psa.cargahoras.service;

import java.util.List;
import java.util.UUID;
import psa.cargahoras.dto.CostoProyectoDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.entity.CargaDeHoras;

public class ProyectoService {

  private final ApiExternaService apiExternaService;
  private final CargaDeHorasService cargaDeHorasService;
  private final RecursoService recursoService;

  public ProyectoService(
      ApiExternaService apiExternaService,
      CargaDeHorasService cargaDeHorasService,
      RecursoService recursoService) {
    this.apiExternaService = apiExternaService;
    this.cargaDeHorasService = cargaDeHorasService;
    this.recursoService = recursoService;
  }

  public CostoProyectoDTO obtenerCostoPorProyecto(String proyectoId) {
    ProyectoDTO proyectoBuscado =
        apiExternaService.getProyectos().stream()
            .filter(proyecto -> proyecto.getId().equals(proyectoId))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No existe el proyecto con ID: " + proyectoId));

    List<CargaDeHoras> cargasDeHoras =
        cargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId);

    List<CostoRecursoDTO> costosRecursos =
        recursoService.obtenerCostosDeTodosLosRecursos(cargasDeHoras);

    double costoProyecto =
        costosRecursos.stream().mapToDouble(costoRecurso -> costoRecurso.getCosto()).sum();

    return new CostoProyectoDTO(
        UUID.randomUUID().toString(), proyectoId, proyectoBuscado.getNombre(), costoProyecto);
  }

  public List<CostoProyectoDTO> obetenerCostosDeTodosLosProyectos() {
    List<ProyectoDTO> proyectos = apiExternaService.getProyectos();
    return proyectos.stream().map(proyecto -> obtenerCostoPorProyecto(proyecto.getId())).toList();
  }
}
