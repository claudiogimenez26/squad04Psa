package psa.cargahoras.service;

import java.util.List;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.ProyectoDTO;
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
            .toList();

        List<CargaDeHoras> cargasDeHoras = cargaHorasRepository
            .findAll()
            .stream()
            .filter(carga ->
                tareasDelProyecto
                    .stream()
                    .anyMatch(tarea -> tarea.getId().equals(carga.getTareaId()))
            )
            .toList();

        return cargasDeHoras;
    }

    public List<CargaDeHorasPorRecursoDTO> obtenerCargasDeHorasPorRecurso(
        String recursoId
    ) {
        boolean existeRecurso = apiExternaService
            .getRecursos()
            .stream()
            .anyMatch(recurso -> recurso.getId().equals(recursoId));

        if (!existeRecurso) {
            throw new IllegalArgumentException(
                "No existe el recurso con ID: " + recursoId
            );
        }

        List<CargaDeHoras> cargas = cargaHorasRepository
            .findAll()
            .stream()
            .filter(carga -> carga.getRecursoId().equals(recursoId))
            .toList();

        List<TareaDTO> tareas = apiExternaService.getTareas();
        List<ProyectoDTO> proyectos = apiExternaService.getProyectos();

        return cargas
            .stream()
            .map(carga -> {
                String proyectoId = tareas
                    .stream()
                    .filter(t -> t.getId().equals(carga.getTareaId()))
                    .findFirst()
                    .map(TareaDTO::getProyectoId)
                    .orElse(null);

                String nombreProyecto = proyectos
                    .stream()
                    .filter(p -> p.getId().equals(proyectoId))
                    .findFirst()
                    .map(ProyectoDTO::getNombre)
                    .orElse("Proyecto no encontrado");

                return new CargaDeHorasPorRecursoDTO(
                    carga.getId(),
                    carga.getTareaId(),
                    carga.getCantidadHoras(),
                    carga.getFechaCarga(),
                    nombreProyecto
                );
            })
            .toList();
    }

    public CargaDeHoras cargarHoras(CargaDeHoras nuevaCarga) {
        boolean existeTarea = apiExternaService
            .getTareas()
            .stream()
            .anyMatch(tarea ->
                tarea.getId().equals(nuevaCarga.getTareaId().toString())
            );

        if (!existeTarea) {
            throw new IllegalArgumentException(
                "No existe la tarea con ID: " + nuevaCarga.getTareaId()
            );
        }

        boolean existeRecurso = apiExternaService
            .getRecursos()
            .stream()
            .anyMatch(recurso ->
                recurso.getId().equals(nuevaCarga.getRecursoId().toString())
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
