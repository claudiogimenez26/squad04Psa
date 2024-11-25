package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.CargaDeHorasService;

public class CargaDeHorasSteps {

  private final TestContext testContext;
  private final RecursoCommonSteps recursoCommonSteps;
  private final TareaCommonSteps tareaCommonSteps;
  private final ResultadoOperacionCommonSteps resultadoOperacionCommonSteps;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  private CargaDeHoras cargaDeHoras;
  private CargaDeHorasService cargaDeHorasService;

  public CargaDeHorasSteps(
      TestContext testContext,
      RecursoCommonSteps cargaDeHorasSteps,
      TareaCommonSteps tareaCommonSteps,
      ResultadoOperacionCommonSteps resultadoOperacion) {
    this.testContext = testContext;
    this.recursoCommonSteps = cargaDeHorasSteps;
    this.tareaCommonSteps = tareaCommonSteps;
    this.resultadoOperacionCommonSteps = resultadoOperacion;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargaDeHoras = null;
    cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, testContext.getApiExternaService());
  }

  @Cuando("el recurso realiza una carga de {double} horas a la tarea en la fecha {string}")
  public void cuandoElRecursoCargaHorasATareaEnFecha(double cantidadHoras, String fechaDateStr) {
    cargaDeHoras =
        resultadoOperacionCommonSteps.ejecutar(
            () ->
                cargaDeHorasService.cargarHoras(
                    new CargaDeHoras(
                        tareaCommonSteps.getTarea().getId(),
                        recursoCommonSteps.getRecurso().getId(),
                        cantidadHoras,
                        fechaDateStr)));
  }

  @Y("la carga de horas debe ser registrada")
  public void verificarCargaDeHorasRegistrada() {
    assertNotNull(cargaDeHoras);
    verify(cargaDeHorasRepository).save(any(CargaDeHoras.class));
  }

  @Y("la carga de horas debe estar asociada el recurso con id {string}")
  public void verificarRecursoCargaDeHoras(String recursoId) {
    assertEquals(recursoId, cargaDeHoras.getRecursoId().toString());
  }

  @Y("la carga de horas debe estar asociada a la tarea con id {string}")
  public void verificarTareaCargaDeHoras(String tareaId) {
    assertEquals(tareaId, cargaDeHoras.getTareaId().toString());
  }

  @Y("la cantidad de horas de la carga de horas debe ser {double} horas")
  public void verificarCantidadHorasCargaDeHoras(double cantidadHoras) {
    assertEquals(cantidadHoras, cargaDeHoras.getCantidadHoras(), 0.0);
  }

  @Y("la fecha de carga de horas debe ser {string}")
  public void verificarFechaCargaDeHoras(String fechaDateStr) {
    assertEquals(fechaDateStr, cargaDeHoras.getFechaCarga().format(CargaDeHoras.formatterFecha));
  }

  @Y(
      "una carga de {double} horas registradas para la tarea con id {string}, y recurso con id {string}, en la fecha {string}")
  public void dadaUnaCargaDeHorasParaTareaDeRecurso(
      double cantidadHoras, String tareaId, String recursoId, String fechaDateStr) {
    cargaDeHoras = new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaDateStr);
  }

  @Entonces("la carga de horas no debe ser registrada")
  public void verificarCargaDeHorasNoRegistrada() {
    verify(cargaDeHorasRepository, never()).save(any(CargaDeHoras.class));
  }

  @Entonces("el mensaje de error debe ser {string}")
  public void verificarMensajeDeError(String mensajeEsperado) {
    assertEquals(mensajeEsperado, resultadoOperacionCommonSteps.getExcepcion().getMessage());
  }
}
