package psa.cargahoras.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.entity.EstadoTarea;
import psa.cargahoras.entity.Recurso;
import psa.cargahoras.entity.Tarea;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.repository.EstadoTareaRepository;
import psa.cargahoras.repository.RecursoRepository;
import psa.cargahoras.repository.TareaRepository;

@Service
public class CargaDeHorasService {

  @Autowired private CargaDeHorasRepository cargaHorasRepository;
  @Autowired private TareaRepository tareaRepository;
  @Autowired private RecursoRepository recursoRepository;
  @Autowired private EstadoTareaRepository estadoTareaRepository;

  public CargaDeHorasService(
      CargaDeHorasRepository cargaDeHorasRepository,
      TareaRepository tareaRepository,
      RecursoRepository recursoRepository,
      EstadoTareaRepository estadoTareaRepository) {
    this.cargaHorasRepository = cargaDeHorasRepository;
    this.tareaRepository = tareaRepository;
    this.recursoRepository = recursoRepository;
    this.estadoTareaRepository = estadoTareaRepository;
  }

  public CargaDeHoras registrarNuevaCarga(
      UUID idTarea, UUID idRecurso, String fechaCargaStr, double cantidadDeHoras) {
    Tarea tarea =
        tareaRepository
            .findById(idTarea)
            .orElseThrow(
                () -> new IllegalArgumentException("No existe la tarea con ID: " + idTarea));

    EstadoTarea estado =
        estadoTareaRepository
            .findById(idTarea)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "No existe estado para la tarea con ID: " + idTarea));

    if (!estado.getActiva()) {
      throw new IllegalArgumentException("No se pueden cargar horas a una tarea pausada");
    }

    Recurso recurso =
        recursoRepository
            .findById(idRecurso)
            .orElseThrow(
                () -> new IllegalArgumentException("No existe el recurso con ID: " + idRecurso));

    List<CargaDeHoras> cargasExistentes = cargaHorasRepository.findByTareaId(idTarea);

    if (!cargasExistentes.isEmpty()
        && !cargasExistentes.get(0).getRecurso().getId().equals(idRecurso)) {
      throw new IllegalArgumentException(
          "Esta tarea ya está asignada al recurso con ID: "
              + cargasExistentes.get(0).getRecurso().getId());
    }

    CargaDeHoras nuevaCarga = new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadDeHoras);

    cargaHorasRepository.save(nuevaCarga);

    return nuevaCarga;
  }

  public List<CargaDeHoras> obtenerTodasLasCargas() {
    return cargaHorasRepository.findAll();
  }

  public CargaDeHoras obtenerCargaPorId(UUID id) {
    return cargaHorasRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No existe carga con ID: " + id));
  }
}
