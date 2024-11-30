package psa.cargahoras;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psa.cargahoras.dto.CargaDeHorasDTO;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.ResumenCostoRecursoDTO;
import psa.cargahoras.dto.RolDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.service.ApiExternaService;
import psa.cargahoras.service.CargaDeHorasService;
import psa.cargahoras.service.RecursoService;

@SpringBootApplication
@RestController
@EntityScan("psa.cargahoras.entity")
@EnableJpaRepositories("psa.cargahoras.repository")
@ComponentScan(basePackages = "psa.cargahoras")
public class App {

  @Autowired private CargaDeHorasService cargaDeHorasService;

  @Autowired private ApiExternaService apiExternaService;

  @Autowired private RecursoService recursoService;

  @GetMapping("/carga-de-horas")
  public ResponseEntity<?> obtenerCargasDeHoras() {
    try {
      Map<String, List<CargaDeHorasDTO>> cargas =
          cargaDeHorasService.obtenerCargasDeHorasConTarea();
      return new ResponseEntity<>(cargas, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/carga-de-horas")
  public ResponseEntity<?> cargarHoras(@RequestBody Map<String, Object> request) {
    try {
      CargaDeHoras nuevaCarga = extraerCargaDeHoras(request);
      cargaDeHorasService.cargarHoras(nuevaCarga);
      return new ResponseEntity<>(nuevaCarga, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/carga-de-horas/{recursoId}")
  public ResponseEntity<?> obtenerCargasDeHorasPorRecurso(
      @PathVariable String recursoId, @RequestParam(required = false) String fecha) {
    try {
      List<CargaDeHorasPorRecursoDTO> cargasDeRecurso =
          cargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId, extraerFecha(fecha), null);
      return new ResponseEntity<>(cargasDeRecurso, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error al obtener recursos con proyectos: " + e.getMessage(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/carga-de-horas")
  public ResponseEntity<?> modificarCargaDeHoras(@RequestBody Map<String, Object> request) {
    try {
      CargaDeHoras cargaActualizada = extraerCargaDeHoras(request);
      cargaDeHorasService.modificarCargaDeHoras(cargaActualizada);
      return new ResponseEntity<>(cargaActualizada, HttpStatus.OK);
    } catch(IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/carga-de-horas/{cargaDeHorasId}")
  public ResponseEntity<?> eliminarCargaDeHoras(@PathVariable String cargaDeHorasId) {
    try {
      cargaDeHorasService.eliminarCargaDeHoras(cargaDeHorasId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/proyectos/{proyectoId}/recursos")
  public ResponseEntity<?> obtenerCostosPorRecurso(
      @PathVariable String proyectoId,
      @RequestParam(required = true) String fechaInicio,
      @RequestParam(required = true) String fechaFin) {
    try {
      List<ResumenCostoRecursoDTO> costosPorRecursoDeProyecto =
          recursoService.obtenerCostosPorRecursoPorProyecto(
              proyectoId, extraerFecha(fechaInicio), extraerFecha(fechaFin));
      return new ResponseEntity<>(costosPorRecursoDeProyecto, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private CargaDeHoras extraerCargaDeHoras(Map<String, Object> request) {
    if (request.get("tareaId") == null) {
      throw new IllegalArgumentException("El campo tareaId es requerido");
    }
    if (request.get("recursoId") == null) {
      throw new IllegalArgumentException("El campo recursoId es requerido");
    }
    if (request.get("cantidadHoras") == null) {
      throw new IllegalArgumentException("El campo cantidadHoras es requerido");
    }
    if (request.get("fechaCarga") == null) {
      throw new IllegalArgumentException("El campo fechaCarga es requerido");
    }

    double cantidadHoras;

    try {
      cantidadHoras = Double.parseDouble(request.get("cantidadHoras").toString());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("El campo cantidadHoras debe ser un número válido");
    }

    return new CargaDeHoras(
        request.get("tareaId").toString(),
        request.get("recursoId").toString(),
        cantidadHoras,
        request.get("fechaCarga").toString());
  }

  private LocalDate extraerFecha(String fechaStr) {
    if (fechaStr != null) {
      try {
        String decodedFecha = URLDecoder.decode(fechaStr, StandardCharsets.UTF_8.toString());
        return LocalDate.from(CargaDeHoras.formatterFecha.parse(decodedFecha));
      } catch (Exception e) {
        return null;
      }
    } else {
      return null;
    }
  }

  @GetMapping("/recursos")
  public ResponseEntity<?> obtenerRecursosProxy() {
    try {
      List<RecursoDTO> recursos = apiExternaService.getRecursos();
      return new ResponseEntity<>(recursos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error al obtener recursos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tareas")
  public ResponseEntity<?> obtenerTareasProxy() {
    try {
      List<TareaDTO> tareas = apiExternaService.getTareas();
      return new ResponseEntity<>(tareas, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error al obtener tareas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/roles")
  public ResponseEntity<?> obtenerRolesProxy() {
    try {
      List<RolDTO> roles = apiExternaService.getRoles();
      return new ResponseEntity<>(roles, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error al obtener roles: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/proyectos")
  public ResponseEntity<?> obtenerProyectosProxy() {
    try {
      List<ProyectoDTO> proyectos = apiExternaService.getProyectos();
      return new ResponseEntity<>(proyectos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error al obtener proyectos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
