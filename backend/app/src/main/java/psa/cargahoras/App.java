package psa.cargahoras;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.service.CargaDeHorasService;
import psa.cargahoras.service.SincronizacionService;

@SpringBootApplication
@RestController
@EntityScan("psa.cargahoras.entity")
@EnableJpaRepositories("psa.cargahoras.repository")
public class App {

    @Autowired
    private SincronizacionService sincronizacionService;

    @Autowired
    private CargaDeHorasService cargaHorasService;

    @GetMapping("/carga-horas")
    public ResponseEntity<List<CargaDeHoras>> obtenerCargas() {
        try {
            List<CargaDeHoras> cargas =
                cargaHorasService.obtenerTodasLasCargas();
            return new ResponseEntity<>(cargas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/carga-horas")
    public ResponseEntity<?> crearCargaHoras(
        @RequestBody Map<String, Object> request
    ) {
        try {
            UUID idTarea = UUID.fromString(request.get("idTarea").toString());
            UUID idRecurso = UUID.fromString(
                request.get("idRecurso").toString()
            );
            String fechaCarga = request.get("fechaCarga").toString();
            double cantidadHoras = Double.parseDouble(
                request.get("cantidadHoras").toString()
            );

            CargaDeHoras nuevaCarga = cargaHorasService.registrarNuevaCarga(
                idTarea,
                idRecurso,
                fechaCarga,
                cantidadHoras
            );

            Map<String, Object> response = new HashMap<>();
            response.put("id", nuevaCarga.getId());
            response.put("fechaCarga", nuevaCarga.getFechaCarga());
            response.put("cantidadHoras", nuevaCarga.getCantidadHoras());
            response.put("idTarea", nuevaCarga.getTarea().getId());
            response.put("idRecurso", nuevaCarga.getRecurso().getId());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                "Error interno del servidor: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/sincronizar-apis")
    public ResponseEntity<String> sincronizarAPIs() {
        try {
            sincronizacionService.sincronizarDatos();
            return new ResponseEntity<>(
                "Sincronización completada",
                HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                "Error en la sincronización: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public static void main(String[] args) {
        System.out.println("DATABASE_HOST: " + System.getenv("DATABASE_HOST"));
        System.out.println("DATABASE_PORT: " + System.getenv("DATABASE_PORT"));
        System.out.println("DATABASE_DB: " + System.getenv("DATABASE_DB"));

        SpringApplication.run(App.class, args);
    }
}
