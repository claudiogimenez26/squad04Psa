package psa.cargahoras.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.CostoMensualDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.ResumenCostoRecursoDTO;
import psa.cargahoras.dto.RolDTO;
import psa.cargahoras.entity.CargaDeHoras;

@Service
public class RecursoService {

  private final ApiExternaService apiExternaService;
  private final CargaDeHorasService cargaDeHorasService;

  private RecursoDTO obtenerRecurso(String recursoId) {
    return
        apiExternaService.getRecursos().stream()
            .filter(recurso -> recurso.getId().equals(recursoId))
            .findFirst()
            .orElseThrow(
                () -> new IllegalArgumentException("No existe el recurso con ID: " + recursoId));
  }

  private RolDTO obtenerRol(String rolId) {
    return 
        apiExternaService.getRoles().stream()
          .filter(rol -> rol.getId().equals(rolId))
          .findFirst()
          .orElse(null);
  }

  private CostoRecursoDTO obtenerCosto(List<Double> horasCargadas, RecursoDTO recurso, RolDTO rol) {
    Double costoRecurso =
        horasCargadas.stream().mapToDouble(hora -> rol.getCosto() * hora).sum();

    return new CostoRecursoDTO(
        recurso.getId(),
        rol.getId(),
        costoRecurso.intValue(),
        String.join(" ", recurso.getNombre(), recurso.getApellido()),
        String.join(" ", rol.getNombre(), rol.getExperiencia()));
  }

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

    double horasTrabajadas =
        cargasDeHoras.stream().mapToDouble(CargaDeHorasPorRecursoDTO::getCantidadHoras).sum();

    double costoTotal = horasTrabajadas * rolRecurso.getCosto();

    return new CostoRecursoDTO(
        recursoId,
        rolRecurso.getId(),
        costoTotal,
        String.join(" ", recursoBuscado.getNombre(), recursoBuscado.getApellido()),
        String.join(" ", rolRecurso.getNombre(), rolRecurso.getExperiencia()),
        horasTrabajadas,
        costoTotal);
  }

  public List<ResumenCostoRecursoDTO> obtenerCostosPorRecursoPorProyecto(
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

              Map<YearMonth, Double> horasPorMes =
                  cargasDelRecurso.stream()
                      .filter(
                          carga -> {
                            LocalDate fechaCarga = carga.getFechaCarga();
                            return ((fechaInicio == null || !fechaCarga.isBefore(fechaInicio))
                                && (fechaFin == null || !fechaCarga.isAfter(fechaFin)));
                          })
                      .collect(
                          Collectors.groupingBy(
                              carga -> YearMonth.from(carga.getFechaCarga()),
                              Collectors.summingDouble(CargaDeHoras::getCantidadHoras)));

              if (horasPorMes.isEmpty()) {
                return null;
              }

              List<CostoMensualDTO> costosMensuales =
                  horasPorMes.entrySet().stream()
                      .map(
                          mesEntry -> {
                            YearMonth yearMonth = mesEntry.getKey();
                            String mesYAnio =
                                yearMonth
                                        .getMonth()
                                        .getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                                    + " "
                                    + yearMonth.getYear();
                            return new CostoMensualDTO(mesYAnio, mesEntry.getValue());
                          })
                      .collect(Collectors.toList());

              return new ResumenCostoRecursoDTO(
                  recurso.getId(),
                  String.join(" ", recurso.getNombre(), recurso.getApellido()),
                  String.join(" ", rolRecurso.getNombre(), rolRecurso.getExperiencia()),
                  rolRecurso.getCosto(),
                  costosMensuales);
            })
        .filter(resumen -> resumen != null)
        .collect(Collectors.toList());
    return obtenerCosto(horasCargadas, recursoBuscado, rolRecurso);
  }

  public CostoRecursoDTO obtenerCostoPorRecurso(String recursoId, List<CargaDeHoras> cargasDeHoras) {
    RecursoDTO recursoBuscado = obtenerRecurso(recursoId);
    RolDTO rolRecurso = obtenerRol(recursoBuscado.getRolId());
    
    List<Double> horasCargadas =
        cargasDeHoras.stream().map(carga -> carga.getCantidadHoras()).toList();

    return obtenerCosto(horasCargadas, recursoBuscado, rolRecurso);
  }
}
