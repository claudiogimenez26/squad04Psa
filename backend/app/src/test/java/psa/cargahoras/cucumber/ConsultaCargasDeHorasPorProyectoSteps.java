package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Y;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.CargaDeHorasService;

public class ConsultaCargasDeHorasPorProyectoSteps {

  private final TestContext testContext;
  private final ProyectoCommonSteps proyectoCommonSteps;
  private final TareaCommonSteps tareaCommonSteps;
  private final ResultadoOperacionCommonSteps resultadoOperacionCommonSteps;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  private List<CargaDeHoras> cargasDeHorasIniciales;
  private List<CargaDeHoras> cargasDeHorasFinales;

  private CargaDeHorasService cargaDeHorasService;

  public ConsultaCargasDeHorasPorProyectoSteps(
      TestContext testContext,
      ProyectoCommonSteps proyectoCommonSteps,
      TareaCommonSteps tareaCommonSteps,
      ResultadoOperacionCommonSteps resultadoOperacionCommonSteps) {
    this.testContext = testContext;
    this.proyectoCommonSteps = proyectoCommonSteps;
    this.tareaCommonSteps = tareaCommonSteps;
    this.resultadoOperacionCommonSteps = resultadoOperacionCommonSteps;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargasDeHorasIniciales = new ArrayList<>();
    cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, testContext.getApiExternaService());
  }

  @Y("una carga de horas con id {string}, con tarea con id {string}")
  public void dadaUnaCargaDeHorasConTarea(String cargaDeHorasId, String tareaId) {
    CargaDeHoras cargaDeHoras = mock(CargaDeHoras.class);

    when(cargaDeHoras.getId()).thenReturn(cargaDeHorasId);
    when(cargaDeHoras.getTareaId()).thenReturn(tareaId);

    cargasDeHorasIniciales.add(cargaDeHoras);
    when(cargaDeHorasRepository.findAll()).thenReturn(cargasDeHorasIniciales);
  }

  @Cuando("consulto las cargas de horas del proyecto")
  public void consultarCargasDeHorasDelProyecto() {
    cargasDeHorasFinales =
        resultadoOperacionCommonSteps.ejecutar(
            () ->
                cargaDeHorasService.obtenerCargasDeHorasPorProyecto(
                    proyectoCommonSteps.getProyecto().getId()));
  }

  @Y("la cantidad de cargas de horas del proyecto debe ser {int}")
  public void verificarCantidadCargasDeHoras(int cantidadEsperada) {
    assertEquals(cantidadEsperada, cargasDeHorasFinales.size());
  }

  @Y("una de las cargas de horas del proyecto debe tener id {string}")
  public void verificarIdAlgunaCargaDeHoras(String cargaId) {
    boolean existeCarga =
        cargasDeHorasFinales.stream().anyMatch(carga -> carga.getId().equals(cargaId));

    assertEquals(true, existeCarga);
  }
}
