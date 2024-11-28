package psa.cargahoras.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        ApiExternaService apiExternaService,
        CargaDeHorasService cargaDeHorasService
    ) {
        this.apiExternaService = apiExternaService;
        this.cargaDeHorasService = cargaDeHorasService;
    }

    public List<CostoRecursoDTO> obtenerCostosDeRecursos() {
        List<RecursoDTO> recursos = apiExternaService.getRecursos();

        return recursos
            .stream()
            .map(recurso -> obtenerCostoPorRecurso(recurso.getId(), null, null))
            .toList();
    }

    public CostoRecursoDTO obtenerCostoPorRecurso(
        String recursoId,
        LocalDate fechaInicio,
        LocalDate fechaFin
    ) {
        RecursoDTO recursoBuscado = apiExternaService
            .getRecursos()
            .stream()
            .filter(recurso -> recurso.getId().equals(recursoId))
            .findFirst()
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "No existe el recurso con ID: " + recursoId
                )
            );

        RolDTO rolRecurso = apiExternaService
            .getRoles()
            .stream()
            .filter(rol -> rol.getId().equals(recursoBuscado.getRolId()))
            .findFirst()
            .orElse(null);

        List<CargaDeHorasPorRecursoDTO> cargasDeHoras =
            cargaDeHorasService.obtenerCargasDeHorasPorRecurso(
                recursoId,
                null,
                null
            );

        double horasTrabajadas = cargasDeHoras
            .stream()
            .mapToDouble(CargaDeHorasPorRecursoDTO::getCantidadHoras)
            .sum();

        double costoTotal = horasTrabajadas * rolRecurso.getCosto();

        return new CostoRecursoDTO(
            recursoId,
            rolRecurso.getId(),
            costoTotal,
            String.join(
                " ",
                recursoBuscado.getNombre(),
                recursoBuscado.getApellido()
            ),
            String.join(
                " ",
                rolRecurso.getNombre(),
                rolRecurso.getExperiencia()
            ),
            horasTrabajadas,
            costoTotal
        );
    }

    public Map<
        String,
        List<CostoRecursoDTO>
    > obtenerCostosPorRecursoPorProyecto(
        String proyectoId,
        LocalDate fechaInicio,
        LocalDate fechaFin
    ) {
        List<CargaDeHoras> cargasDelProyecto =
            cargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId);

        List<RecursoDTO> recursos = apiExternaService.getRecursos();
        List<RolDTO> roles = apiExternaService.getRoles();

        Map<String, RecursoDTO> recursosMap = recursos
            .stream()
            .collect(Collectors.toMap(RecursoDTO::getId, r -> r));

        Map<String, RolDTO> rolesMap = roles
            .stream()
            .collect(Collectors.toMap(RolDTO::getId, r -> r));

        Map<String, List<CargaDeHoras>> cargasPorRecurso = cargasDelProyecto
            .stream()
            .collect(Collectors.groupingBy(CargaDeHoras::getRecursoId));

        return cargasPorRecurso
            .entrySet()
            .stream()
            .collect(
                Collectors.groupingBy(
                    Map.Entry<String, List<CargaDeHoras>>::getKey,
                    Collectors.flatMapping(
                        entry -> {
                            String recursoId = entry.getKey();
                            List<CargaDeHoras> cargasDelRecurso =
                                entry.getValue();
                            RecursoDTO recurso = recursosMap.get(recursoId);
                            RolDTO rolRecurso = rolesMap.get(
                                recurso.getRolId()
                            );

                            Map<YearMonth, Double> horasPorMes =
                                cargasDelRecurso
                                    .stream()
                                    .filter(carga -> {
                                        LocalDate fechaCarga =
                                            carga.getFechaCarga();
                                        return (
                                            (fechaInicio == null ||
                                                !fechaCarga.isBefore(
                                                    fechaInicio
                                                )) &&
                                            (fechaFin == null ||
                                                !fechaCarga.isAfter(fechaFin))
                                        );
                                    })
                                    .collect(
                                        Collectors.groupingBy(
                                            carga ->
                                                YearMonth.from(
                                                    carga.getFechaCarga()
                                                ),
                                            Collectors.summingDouble(
                                                CargaDeHoras::getCantidadHoras
                                            )
                                        )
                                    );

                            return horasPorMes
                                .entrySet()
                                .stream()
                                .map(mesEntry -> {
                                    YearMonth mes = mesEntry.getKey();
                                    double horasTrabajadas =
                                        mesEntry.getValue();
                                    double costoTotal =
                                        horasTrabajadas * rolRecurso.getCosto();

                                    return new CostoRecursoDTO(
                                        recurso.getId(),
                                        recurso.getRolId(),
                                        costoTotal,
                                        String.join(
                                            " ",
                                            recurso.getNombre(),
                                            recurso.getApellido()
                                        ),
                                        rolRecurso.getNombre(),
                                        horasTrabajadas,
                                        rolRecurso.getCosto(),
                                        mes
                                    );
                                });
                        },
                        Collectors.toList()
                    )
                )
            );
    }
}
