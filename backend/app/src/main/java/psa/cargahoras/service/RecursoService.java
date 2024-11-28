package psa.cargahoras.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.RolDTO;
import psa.cargahoras.entity.CargaDeHoras;

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
    List<CargaDeHoras> cargasDelProyecto =
        cargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId);

    List<RecursoDTO> recursos = apiExternaService.getRecursos();
    List<RolDTO> roles = apiExternaService.getRoles();

    Map<String, RecursoDTO> recursosMap =
        recursos.stream().collect(Collectors.toMap(RecursoDTO::getId, r -> r));

    Map<String, RolDTO> rolesMap = roles.stream().collect(Collectors.toMap(RolDTO::getId, r -> r));

    Map<String, List<CargaDeHoras>> cargasPorRecurso =
        cargasDelProyecto.stream().collect(Collectors.groupingBy(CargaDeHoras::getRecursoId));

    return cargasPorRecurso.entrySet().stream()
        .map(
            entry -> {
              String recursoId = entry.getKey();
              List<CargaDeHoras> cargasDelRecurso = entry.getValue();

              RecursoDTO recurso = recursosMap.get(recursoId);
              RolDTO rolRecurso = rolesMap.get(recurso.getRolId());

              double costoTotal =
                  cargasDelRecurso.stream()
                          .filter(
                              carga -> {
                                LocalDate fechaCarga = carga.getFechaCarga();
                                return ((fechaInicio == null || !fechaCarga.isBefore(fechaInicio))
                                    && (fechaFin == null || !fechaCarga.isAfter(fechaFin)));
                              })
                          .mapToDouble(CargaDeHoras::getCantidadHoras)
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
