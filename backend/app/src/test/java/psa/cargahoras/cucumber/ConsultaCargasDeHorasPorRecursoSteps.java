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
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.CargaDeHorasService;

public class ConsultaCargasDeHorasPorRecursoSteps {

  private final TestContext testContext;
  private final RecursoCommonSteps recursoCommonSteps;
  private final ResultadoOperacionCommonSteps resultadoOperacionCommonSteps;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  private List<CargaDeHoras> cargasDeHoras;
  private List<CargaDeHorasPorRecursoDTO> cargasDeHorasIniciales;
  private List<CargaDeHorasPorRecursoDTO> cargasDeHorasFinales;

  private CargaDeHorasService cargaDeHorasService;

  public ConsultaCargasDeHorasPorRecursoSteps(
      TestContext testContext,
      RecursoCommonSteps recursoCommonSteps,
      ResultadoOperacionCommonSteps resultadoOperacionCommonSteps) {
    this.testContext = testContext;
    this.recursoCommonSteps = recursoCommonSteps;
    this.resultadoOperacionCommonSteps = resultadoOperacionCommonSteps;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargasDeHoras = new ArrayList<>();
    cargasDeHorasIniciales = new ArrayList<>();
    cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, testContext.getApiExternaService());
  }

  @Y(
      "una carga de horas con id {string}, con tarea con id {string}, cargada por el recurso con id {string}")
  public void dadaUnaCargaDeHorasConTarea(String cargaDeHorasId, String tareaId, String recursoId) {
    TareaDTO tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);
    when(tarea.getRecursoId()).thenReturn(recursoId);
    when(tarea.getProyectoId()).thenReturn(UUID.randomUUID().toString());

    CargaDeHoras cargaDeHoras = mock(CargaDeHoras.class);

    when(cargaDeHoras.getId()).thenReturn(cargaDeHorasId);
    when(cargaDeHoras.getTareaId()).thenReturn(tareaId);
    when(cargaDeHoras.getRecursoId()).thenReturn(recursoId);
    when(cargaDeHoras.getFechaCarga()).thenReturn(LocalDate.now());

    cargasDeHoras.add(cargaDeHoras);
    when(cargaDeHorasRepository.findAll()).thenReturn(cargasDeHoras);
  }

  @Cuando("consulto las cargas de horas del recurso")
  public void consultarCargasDeHorasDelRecurso() {
    cargasDeHorasFinales =
        resultadoOperacionCommonSteps.ejecutar(
            () ->
                cargaDeHorasService.obtenerCargasDeHorasPorRecurso(
                    recursoCommonSteps.getRecurso().getId(), LocalDate.now()));
  }

  @Y("la cantidad de cargas de horas del recurso debe ser {int}")
  public void verificarCantidadCargasDeHoras(int cantidadEsperada) {
    assertEquals(cantidadEsperada, cargasDeHorasFinales.size());
  }

  @Y("una de las cargas de horas del recurso debe tener id {string}")
  public void verificarIdAlgunaCargaDeHoras(String cargaId) {
    boolean existeCarga =
        cargasDeHorasFinales.stream().anyMatch(carga -> carga.getId().equals(cargaId));

    assertEquals(true, existeCarga);
  }
}
