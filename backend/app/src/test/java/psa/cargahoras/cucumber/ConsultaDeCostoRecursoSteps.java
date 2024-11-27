package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Y;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.ApiExternaService;
import psa.cargahoras.service.CargaDeHorasService;
import psa.cargahoras.service.RecursoService;

public class ConsultaDeCostoRecursoSteps {

  private final RecursoCommonSteps recursoCommonSteps;
  // private final ROLCommonSteps rolCommonSteps;
  private final ResultadoOperacionCommonSteps resultadoOperacionCommonSteps;
  private final TestContext testContext;

  private RecursoService recursoService;

  @Mock private ApiExternaService apiExternaService;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @Mock private CargaDeHorasService cargaDeHorasService;

  private List<CargaDeHoras> cargasDeHoras;
  private CostoRecursoDTO costoRecurso;

  public ConsultaDeCostoRecursoSteps(
      ResultadoOperacionCommonSteps resultadoOperacionCommonSteps,
      RecursoCommonSteps recursoCommonSteps,
      /*ROLCommonSteps rolCommonSteps,*/
      TestContext testContext) {
    this.recursoCommonSteps = recursoCommonSteps;
    // this.rolCommonSteps = rolCommonSteps;
    this.resultadoOperacionCommonSteps = resultadoOperacionCommonSteps;
    this.testContext = testContext;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargasDeHoras = new ArrayList<>();
    cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, testContext.getApiExternaService());
    recursoService = new RecursoService(testContext.getApiExternaService(), cargaDeHorasService);
  }

  @Y(
      "una carga de horas con id {string}, con tarea con id {string}, cargada por el recurso con id {string} con {double} horas cargadas")
  public void dadaUnaCargaDeHorasConTarea(
      String cargaDeHorasId, String tareaId, String recursoId, double cantidadHoras) {
    TareaDTO tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);
    when(tarea.getRecursoId()).thenReturn(recursoId);
    when(tarea.getProyectoId()).thenReturn(UUID.randomUUID().toString());

    CargaDeHoras cargaDeHoras = mock(CargaDeHoras.class);

    when(cargaDeHoras.getId()).thenReturn(cargaDeHorasId);
    when(cargaDeHoras.getTareaId()).thenReturn(tareaId);
    when(cargaDeHoras.getRecursoId()).thenReturn(recursoId);
    when(cargaDeHoras.getFechaCarga()).thenReturn(LocalDate.now());
    when(cargaDeHoras.getCantidadHoras()).thenReturn(cantidadHoras);

    cargasDeHoras.add(cargaDeHoras);
    when(cargaDeHorasRepository.findAll()).thenReturn(cargasDeHoras);
  }

  @Cuando("consulto el costo del recurso")
  public void consultarCostoRecurso() {
    costoRecurso =
        resultadoOperacionCommonSteps.ejecutar(
            () ->
                recursoService.obtenerCostoPorRecurso(
                    recursoCommonSteps.getRecurso().getId(), null, null));
  }

  @Y("el costo del recurso debe ser {int}")
  public void verificarCostoRecurso(int costoEsperado) {
    assertEquals(costoEsperado, costoRecurso.getCosto());
  }
}
