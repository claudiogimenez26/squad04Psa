package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.repository.CargaDeHorasRepository;

public class EliminarCargaDeHorasSteps {

  private final CargaDeHorasCommonSteps cargaDeHorasCommonSteps;
  private final ResultadoOperacionCommonSteps resultadoOperacionCommonSteps;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  public EliminarCargaDeHorasSteps(
      TestContext testContext,
      CargaDeHorasCommonSteps cargaDeHorasCommonSteps,
      ResultadoOperacionCommonSteps resultadoOperacionCommonSteps) {
    MockitoAnnotations.openMocks(this);

    this.cargaDeHorasCommonSteps = cargaDeHorasCommonSteps;
    this.resultadoOperacionCommonSteps = resultadoOperacionCommonSteps;
  }

  @Cuando("se elimina la carga de horas con id {string}")
  @Cuando("se elimina la carga de horas con id inexistente {string}")
  public void eliminarCargaDeHoras(String cargaDeHorasId) {
    resultadoOperacionCommonSteps.ejecutar(
        () ->
            cargaDeHorasCommonSteps.getCargaDeHorasService().eliminarCargaDeHoras(cargaDeHorasId));
  }

  @Entonces("la tarea no debe tener horas cargadas")
  public void verificarTareaNoTieneHorasCargadas() {
    assertEquals(
        0, cargaDeHorasCommonSteps.getCargaDeHorasService().obtenerTodasLasCargasDeHoras().size());
  }
}
