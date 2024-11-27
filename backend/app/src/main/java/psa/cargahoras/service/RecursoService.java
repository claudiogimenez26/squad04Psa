package psa.cargahoras.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.RolDTO;
import psa.cargahoras.dto.TareaDTO;

@Service
public class RecursoService {

  private final ApiExternaService apiExternaService;
  private final CargaDeHorasService cargaDeHorasService;

  public RecursoService(
      ApiExternaService apiExternaService, CargaDeHorasService cargaDeHorasService) {
    this.apiExternaService = apiExternaService;
    this.cargaDeHorasService = cargaDeHorasService;
  }

  public List<CostoRecursoDTO> obtenerCostosDeRecursos() {
    List<RecursoDTO> recursos = apiExternaService.getRecursos();

    return recursos.stream()
        .map(recurso -> obtenerCostoPorRecurso(recurso.getId(), null, null))
        .toList();
  }

  public CostoRecursoDTO obtenerCostoPorRecurso(
      String recursoId, LocalDate fechaInicio, LocalDate fechaFin) {
    RecursoDTO recursoBuscado =
        apiExternaService.getRecursos().stream()
            .filter(recurso -> recurso.getId().equals(recursoId))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No existe el recurso con ID: " + recursoId));

    RolDTO rolRecurso =
        apiExternaService.getRoles().stream()
            .filter(rol -> rol.getId().equals(recursoBuscado.getRolId()))
            .findFirst()
            .orElse(null);

    List<CargaDeHorasPorRecursoDTO> cargasDeHoras =
        cargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId, null, null);

    List<Double> horasCargadas =
        cargasDeHoras.stream().map(carga -> carga.getCantidadHoras()).toList();

    Double costoRecurso =
        horasCargadas.stream().mapToDouble(hora -> rolRecurso.getCosto() * hora).sum();

    return new CostoRecursoDTO(
        recursoId,
        rolRecurso.getId(),
        costoRecurso,
        String.join(" ", recursoBuscado.getNombre(), recursoBuscado.getApellido()),
        String.join(" ", rolRecurso.getNombre(), rolRecurso.getExperiencia()));
  }

  public List<CostoRecursoDTO> obtenerCostosPorRecursoPorProyecto(
      String proyectoId, LocalDate fechaInicio, LocalDate fechaFin) {
    List<TareaDTO> tareas = apiExternaService.getTareas();
    List<TareaDTO> tareasDelProyecto =
        tareas.stream().filter(t -> t.getProyectoId().equals(proyectoId)).toList();

    List<RecursoDTO> recursos = apiExternaService.getRecursos();

    return tareasDelProyecto.stream()
        .map(
            t -> {
              RecursoDTO recurso =
                  recursos.stream()
                      .filter(r -> r.getId().equals(t.getRecursoId()))
                      .findFirst()
                      .orElseThrow();

              RolDTO rolRecurso =
                  apiExternaService.getRoles().stream()
                      .filter(rol -> rol.getId().equals(recurso.getRolId()))
                      .findFirst()
                      .orElseThrow();

              List<CargaDeHorasPorRecursoDTO> cargasDelRecurso =
                  cargaDeHorasService.obtenerCargasDeHorasPorRecurso(
                      recurso.getId(), fechaInicio, fechaFin);

              double costoTotal =
                  cargasDelRecurso.stream()
                          .mapToDouble(CargaDeHorasPorRecursoDTO::getCantidadHoras)
                          .sum()
                      * rolRecurso.getCosto();

              return new CostoRecursoDTO(
                  recurso.getId(),
                  recurso.getRolId(),
                  costoTotal,
                  String.join(" ", recurso.getNombre(), recurso.getApellido()),
                  rolRecurso.getNombre());
            })
        .toList();
  }
}
