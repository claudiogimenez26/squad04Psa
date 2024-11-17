package psa.cargahoras;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.service.CargaHorasService;

@SpringBootApplication
@RestController
@EntityScan("psa.cargahoras.entity")
@EnableJpaRepositories("psa.cargahoras.repository")
public class App {
  @Autowired private CargaHorasService cargaHorasService;

  @GetMapping("/carga-horas")
  public ResponseEntity<List<CargaDeHoras>> obtenerCargas() {
    try {
      List<CargaDeHoras> cargas = cargaHorasService.obtenerTodasLasCargas();
      return new ResponseEntity<>(cargas, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
