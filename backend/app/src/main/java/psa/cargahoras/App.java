package psa.cargahoras;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.service.CargaDeHorasService;

@SpringBootApplication
@RestController
@EntityScan("psa.cargahoras.entity")
@EnableJpaRepositories("psa.cargahoras.repository")
@ComponentScan(basePackages = "psa.cargahoras")
public class App {

  @Autowired private CargaDeHorasService cargaHorasService;

  @GetMapping("/carga-horas")
  public ResponseEntity<List<CargaDeHoras>> obtenerCargas() {
    try {
      List<CargaDeHoras> cargas = cargaHorasService.obtenerTodasLasCargasDeHoras();
      return new ResponseEntity<>(cargas, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/carga-horas")
  public ResponseEntity<?> crearCargaHoras(@RequestBody Map<String, Object> request) {
    try {
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

      double cantidadHoras = Double.parseDouble(request.get("cantidadHoras").toString());
      String fechaCarga = request.get("fechaCarga").toString();

      CargaDeHoras nuevaCarga =
          cargaHorasService.cargarHoras(
              request.get("tareaId").toString(),
              request.get("recursoId").toString(),
              cantidadHoras,
              fechaCarga);

      Map<String, Object> response = new HashMap<>();
      response.put("id", nuevaCarga.getId());
      response.put("tareaId", nuevaCarga.getTareaId());
      response.put("recursoId", nuevaCarga.getRecursoId());
      response.put("cantidadHoras", nuevaCarga.getCantidadHoras());
      response.put("fechaCarga", nuevaCarga.getFechaCarga());

      return new ResponseEntity<>(response, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error interno del servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
