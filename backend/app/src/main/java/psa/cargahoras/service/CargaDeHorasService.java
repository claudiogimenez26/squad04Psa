package psa.cargahoras.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;

@Service
public class CargaDeHorasService {

    private final CargaDeHorasRepository cargaHorasRepository;
    private final ApiExternaService apiExternaService;

    public CargaDeHorasService(
        CargaDeHorasRepository cargaHorasRepository,
        ApiExternaService apiExternaService
    ) {
        this.cargaHorasRepository = cargaHorasRepository;
        this.apiExternaService = apiExternaService;
    }

    public List<CargaDeHoras> obtenerTodasLasCargasDeHoras() {
        return cargaHorasRepository.findAll();
    }

    public List<CargaDeHoras> obtenerCargasDeHorasPorProyecto(
        String proyectoId
    ) {
        boolean existeProyecto = apiExternaService
            .getProyectos()
            .stream()
            .anyMatch(proyecto -> proyecto.getId().equals(proyectoId));

        if (!existeProyecto) {
            throw new IllegalArgumentException(
                "No existe el proyecto con ID: " + proyectoId
            );
        }

        List<TareaDTO> tareasDelProyecto = apiExternaService
            .getTareas()
            .stream()
            .filter(tarea -> tarea.getProyectoId().equals(proyectoId))
            .collect(Collectors.toList());

        Set<String> tareaIdsDelProyecto = tareasDelProyecto
            .stream()
            .map(TareaDTO::getId)
            .collect(Collectors.toSet());

        List<CargaDeHoras> cargasDeHoras = cargaHorasRepository
            .findAll()
            .stream()
            .filter(carga -> tareaIdsDelProyecto.contains(carga.getTareaId()))
            .collect(Collectors.toList());

        return cargasDeHoras;
    }

    public List<CargaDeHorasPorRecursoDTO> obtenerCargasDeHorasPorRecurso(
        String recursoId,
        LocalDate fechaBusqueda
    ) {
        Set<String> recursos = apiExternaService
            .getRecursos()
            .stream()
            .map(RecursoDTO::getId)
            .collect(Collectors.toSet());

        if (!recursos.contains(recursoId)) {
            throw new IllegalArgumentException(
                "No existe el recurso con ID: " + recursoId
            );
        }

        Stream<CargaDeHoras> cargasDeRecurso = cargaHorasRepository
            .findAll()
            .stream()
            .filter(carga -> carga.getRecursoId().equals(recursoId));

        if (fechaBusqueda != null) {
            LocalDate inicioSemana = fechaBusqueda.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
            );
            LocalDate finSemana = fechaBusqueda.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)
            );

            cargasDeRecurso = cargasDeRecurso.filter(carga -> {
                return (
                    carga.getFechaCarga().isEqual(inicioSemana) ||
                    carga.getFechaCarga().isEqual(finSemana) ||
                    (carga.getFechaCarga().isAfter(inicioSemana) &&
                        carga.getFechaCarga().isBefore(finSemana))
                );
            });
        }

        List<TareaDTO> tareas = apiExternaService.getTareas();
        List<ProyectoDTO> proyectos = apiExternaService.getProyectos();

        Map<String, TareaDTO> tareaMap = tareas
            .stream()
            .collect(Collectors.toMap(TareaDTO::getId, tarea -> tarea));

        Map<String, ProyectoDTO> proyectoMap = proyectos
            .stream()
            .collect(
                Collectors.toMap(ProyectoDTO::getId, proyecto -> proyecto)
            );

        return cargasDeRecurso
            .map(carga -> {
                TareaDTO tarea = tareaMap.get(carga.getTareaId());
                String tareaNombre = tarea != null ? tarea.getNombre() : null;
                String proyectoId = tarea != null
                    ? tarea.getProyectoId()
                    : null;

                String nombreProyecto = proyectoId != null
                    ? proyectoMap
                        .getOrDefault(proyectoId, new ProyectoDTO())
                        .getNombre()
                    : "Proyecto no encontrado";

                return new CargaDeHorasPorRecursoDTO(
                    carga.getId(),
                    carga.getTareaId(),
                    tareaNombre,
                    carga.getCantidadHoras(),
                    carga.getFechaCarga(),
                    nombreProyecto
                );
            })
            .collect(Collectors.toList());
    }

    public CargaDeHoras cargarHoras(CargaDeHoras nuevaCarga) {
        List<TareaDTO> tareas = apiExternaService.getTareas();
        List<String> recursos = apiExternaService
            .getRecursos()
            .stream()
            .map(recurso -> recurso.getId())
            .collect(Collectors.toList());

        boolean existeTarea = tareas
            .stream()
            .anyMatch(tarea ->
                tarea.getId().equals(nuevaCarga.getTareaId().toString())
            );

        if (!existeTarea) {
            throw new IllegalArgumentException(
                "No existe la tarea con ID: " + nuevaCarga.getTareaId()
            );
        }

        boolean existeRecurso = recursos.contains(
            nuevaCarga.getRecursoId().toString()
        );

        if (!existeRecurso) {
            throw new IllegalArgumentException(
                "No existe el recurso con ID: " + nuevaCarga.getRecursoId()
            );
        }

        cargaHorasRepository.save(nuevaCarga);

        return nuevaCarga;
    }
}
