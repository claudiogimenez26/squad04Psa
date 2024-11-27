package psa.cargahoras.service;

import java.util.List;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.RolDTO;

@Service
public class RecursoService {

  private final ApiExternaService apiExternaService;
  private final CargaDeHorasService cargaDeHorasService;

  public RecursoService(
      ApiExternaService apiExternaService, CargaDeHorasService cargaDeHorasService) {
    this.apiExternaService = apiExternaService;
    this.cargaDeHorasService = cargaDeHorasService;
  }

  public List<CostoRecursoDTO> obtenerCostosDeTodosLosRecursos() {
    List<RecursoDTO> recursos = apiExternaService.getRecursos();

    return recursos.stream().map(recurso -> obtenerCostoPorRecurso(recurso.getId())).toList();
  }

  public CostoRecursoDTO obtenerCostoPorRecurso(String recursoId) {
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
        cargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId, null);

    List<Double> horasCargadas =
        cargasDeHoras.stream().map(carga -> carga.getCantidadHoras()).toList();

    Double costoRecurso =
        horasCargadas.stream().mapToDouble(hora -> rolRecurso.getCosto() * hora).sum();

    return new CostoRecursoDTO(
        recursoId,
        rolRecurso.getId(),
        costoRecurso.intValue(),
        String.join(" ", recursoBuscado.getNombre(), recursoBuscado.getApellido()),
        String.join(" ", rolRecurso.getNombre(), rolRecurso.getExperiencia()));
  }
}
