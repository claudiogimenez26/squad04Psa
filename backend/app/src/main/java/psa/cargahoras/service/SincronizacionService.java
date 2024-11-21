package psa.cargahoras.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.RolDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.EstadoTarea;
import psa.cargahoras.entity.Proyecto;
import psa.cargahoras.entity.Recurso;
import psa.cargahoras.entity.Rol;
import psa.cargahoras.entity.Tarea;
import psa.cargahoras.repository.EstadoTareaRepository;
import psa.cargahoras.repository.ProyectoRepository;
import psa.cargahoras.repository.RecursoRepository;
import psa.cargahoras.repository.RolRepository;
import psa.cargahoras.repository.TareaRepository;

@Service
public class SincronizacionService {
  @Autowired private APIService apiService;

  @Autowired private RolRepository rolRepository;
  @Autowired private RecursoRepository recursoRepository;
  @Autowired private ProyectoRepository proyectoRepository;
  @Autowired private TareaRepository tareaRepository;
  @Autowired private EstadoTareaRepository estadoTareaRepository;

  public void sincronizarDatos() {
    System.out.println("Iniciando sincronización de datos...");
    System.out.println("Sincronizando roles...");
    sincronizarRoles();
    System.out.println("Sincronizando proyectos...");
    sincronizarProyectos();
    System.out.println("Sincronizando recursos...");
    sincronizarRecursos();
    System.out.println("Sincronizando tareas...");
    sincronizarTareas();
    System.out.println("Sincronización completada");
  }

  private void sincronizarRoles() {
    List<RolDTO> rolesDTO = apiService.obtenerRoles();

    if (rolesDTO == null || rolesDTO.isEmpty()) {
      throw new RuntimeException("No se obtuvieron roles de la API");
    }

    for (RolDTO dto : rolesDTO) {
      UUID id = UUID.fromString(dto.getId());

      if (!rolRepository.existsById(id)) {
        Rol rol = new Rol(id, dto.getNombre(), dto.getExperiencia(), 0.0);
        try {
          rolRepository.save(rol);
        } catch (Exception e) {
          throw new RuntimeException(
              "Error al guardar el rol en la base de datos - ID: "
                  + rol.getId()
                  + ", Nombre: "
                  + rol.getNombre()
                  + " - Error: "
                  + e.getMessage());
        }
      } else {
        System.out.println("Saltando rol existente - ID: " + id + ", Nombre: " + dto.getNombre());
      }
    }
  }

  private void sincronizarRecursos() {
    List<RecursoDTO> recursosDTO = apiService.obtenerRecursos();

    if (recursosDTO == null || recursosDTO.isEmpty()) {
      throw new RuntimeException("No se obtuvieron recursos de la API");
    }

    for (RecursoDTO dto : recursosDTO) {
      UUID id = UUID.fromString(dto.getId());
      if (!recursoRepository.existsById(id)) {
        Rol rol =
            rolRepository
                .findById(UUID.fromString(dto.getRolId()))
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Recurso recurso =
            new Recurso(id, dto.getNombre(), dto.getApellido(), Long.parseLong(dto.getDni()), rol);
        try {
          recursoRepository.save(recurso);
        } catch (Exception e) {
          throw new RuntimeException(
              "Error al guardar el recurso en la base de datos - ID: "
                  + recurso.getId()
                  + ", Nombre: "
                  + recurso.getNombre()
                  + " - Error: "
                  + e.getMessage());
        }
      } else {
        System.out.println(
            "Saltando recurso existente - ID: " + id + ", Nombre: " + dto.getNombre());
      }
    }
  }

  private void sincronizarProyectos() {
    List<ProyectoDTO> proyectosDTO = apiService.obtenerProyectos();

    if (proyectosDTO == null || proyectosDTO.isEmpty()) {
      throw new RuntimeException("No se obtuvieron proyectos de la API");
    }

    for (ProyectoDTO dto : proyectosDTO) {
      UUID id = UUID.fromString(dto.getId());
      if (!proyectoRepository.existsById(id)) {
        Proyecto proyecto = new Proyecto(id, dto.getNombre(), dto.getDescripcion());
        try {
          proyectoRepository.save(proyecto);
        } catch (Exception e) {
          throw new RuntimeException(
              "Error al guardar el proyecto en la base de datos - ID: "
                  + proyecto.getId()
                  + ", Nombre: "
                  + proyecto.getNombre()
                  + " - Error: "
                  + e.getMessage());
        }
      } else {
        System.out.println(
            "Saltando proyecto existente - ID: " + id + ", Nombre: " + dto.getNombre());
      }
    }
  }

  private void sincronizarTareas() {
    List<TareaDTO> tareasDTO = apiService.obtenerTareas();

    if (tareasDTO == null || tareasDTO.isEmpty()) {
      throw new RuntimeException("No se obtuvieron tareas de la API");
    }

    Map<String, Boolean> estadosInicialesTareas = new HashMap<>();

    estadosInicialesTareas.put("f635b4ca-c091-472c-8b5a-cb3086d1973", true);
    estadosInicialesTareas.put("1635b4ca-c091-472c-8b5a-cb3086d1973", true);
    estadosInicialesTareas.put("d635b4ca-c091-472c-8b5a-cb3086d1973", false);
    estadosInicialesTareas.put("b635b4ca-c091-472c-8b5a-cb3086d1973", false);
    estadosInicialesTareas.put("a635b4ca-c091-472c-8b5a-cb3086d1973", true);

    for (TareaDTO dto : tareasDTO) {
      UUID id = UUID.fromString(dto.getId());
      if (!tareaRepository.existsById(id)) {
        Proyecto proyecto =
            proyectoRepository
                .findById(UUID.fromString(dto.getProyectoId()))
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        Tarea tarea = new Tarea(id, dto.getNombre(), dto.getDescripcion(), proyecto);
        try {
          tareaRepository.save(tarea);

          boolean estadoInicial = estadosInicialesTareas.getOrDefault(dto.getId(), true);
          EstadoTarea estadoTarea = new EstadoTarea(id, estadoInicial);
          estadoTareaRepository.save(estadoTarea);

        } catch (Exception e) {
          throw new RuntimeException(
              "Error al guardar la tarea en la base de datos - ID: "
                  + tarea.getId()
                  + ", Nombre: "
                  + tarea.getNombre()
                  + " - Error: "
                  + e.getMessage());
        }
      } else {
        System.out.println("Saltando tarea existente - ID: " + id + ", Nombre: " + dto.getNombre());
      }
    }
  }
}
