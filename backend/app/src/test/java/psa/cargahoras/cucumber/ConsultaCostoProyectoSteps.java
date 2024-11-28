package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
// import java.util.Arrays;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import psa.cargahoras.dto.CostoProyectoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.ApiExternaService;
import psa.cargahoras.service.CargaDeHorasService;
import psa.cargahoras.service.ProyectoService;
import psa.cargahoras.service.RecursoService;

public class ConsultaCostoProyectoSteps {

  private final TestContext testContext;
  private final ProyectoCommonSteps proyectoCommonSteps;
  private final TareaCommonSteps tareaCommonSteps;
  private final ResultadoOperacionCommonSteps resultadoOperacionCommonSteps;

  private ProyectoService proyectoService;

  @Mock private ApiExternaService apiExternaService;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @Mock private CargaDeHorasService cargaDeHorasService;

  @Mock private RecursoService recursoService;

  private List<CargaDeHoras> cargasDeHoras;
  private CostoProyectoDTO costoProyecto;

  public ConsultaCostoProyectoSteps(
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

    cargasDeHoras = new ArrayList<>();
    cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, testContext.getApiExternaService());
    recursoService = 
        new RecursoService(testContext.getApiExternaService(), cargaDeHorasService);
    proyectoService = 
        new ProyectoService(testContext.getApiExternaService(), cargaDeHorasService, recursoService);
  }

  @Y(
      "una carga de horas con id {string}, con tarea con id {string}, cargada por el recurso con id {string} con {double} horas cargadas y fecha {string}")
  public void dadaUnaCargaDeHorasConTarea(
      String cargaDeHorasId, String tareaId, String recursoId, double cantidadHoras, String fechaHoraDeCarga) {
    String proyectoId = proyectoCommonSteps.getProyecto().getId();
    TareaDTO tarea = mock(TareaDTO.class);

    // when(tarea.getId()).thenReturn(tareaId);
    // when(tarea.getRecursoId()).thenReturn(recursoId);
    when(tarea.getProyectoId()).thenReturn(proyectoId);

    CargaDeHoras cargaDeHoras = mock(CargaDeHoras.class);

    when(cargaDeHoras.getId()).thenReturn(cargaDeHorasId);
    when(cargaDeHoras.getTareaId()).thenReturn(tareaId);
    when(cargaDeHoras.getRecursoId()).thenReturn(recursoId);
    when(cargaDeHoras.getFechaCarga()).thenReturn(LocalDate.now());
    when(cargaDeHoras.getCantidadHoras()).thenReturn(cantidadHoras);

    cargasDeHoras.add(cargaDeHoras);
    when(cargaDeHorasRepository.findAll()).thenReturn(cargasDeHoras);
    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));
}

  @Cuando("consulto el costo del proyecto")
  public void consultarCostoProyecto() {
    
    costoProyecto = 
        resultadoOperacionCommonSteps.ejecutar(
          () -> 
              proyectoService.obtenerCostoPorProyecto(
                  proyectoCommonSteps.getProyecto().getId()));
  }

  @Entonces("el costo del proyecto debe ser {int}")
  public void verificarCostoProyecto(int costoProyectoEsperado) {
      assertEquals(costoProyectoEsperado, costoProyecto.getCosto(), 0.1);
  }
}
