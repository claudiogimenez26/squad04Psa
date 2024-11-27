package psa.cargahoras.cucumber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.Before;
import io.cucumber.java.es.Dada;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.CargaDeHorasService;

public class CargaDeHorasCommonSteps {

  private final TestContext testContext;
  private CargaDeHoras cargaDeHoras;
  private CargaDeHorasService cargaDeHorasService;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  public CargaDeHorasCommonSteps(TestContext testContext) {
    this.testContext = testContext;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, testContext.getApiExternaService());
  }

  @Dada("una carga de horas con id {string}")
  public void dadaUnaCargaDeHoras(String cargaDeHorasId) {
    RecursoDTO recurso = mock(RecursoDTO.class);
    String recursoId = UUID.randomUUID().toString();

    when(recurso.getId()).thenReturn(recursoId);

    TareaDTO tarea = mock(TareaDTO.class);
    String tareaId = UUID.randomUUID().toString();

    when(tarea.getId()).thenReturn(tareaId);

    cargaDeHoras = mock(CargaDeHoras.class);

    when(cargaDeHoras.getId()).thenReturn(cargaDeHorasId);
    when(cargaDeHoras.getRecursoId()).thenReturn(recursoId);
    when(cargaDeHoras.getTareaId()).thenReturn(tareaId);

    when(testContext.getApiExternaService().getRecursos()).thenReturn(Arrays.asList(recurso));
    when(testContext.getApiExternaService().getTareas()).thenReturn(Arrays.asList(tarea));

    cargaDeHorasRepository.save(cargaDeHoras);
    when(cargaDeHorasRepository.findById(cargaDeHoras.getId()))
        .thenReturn(Optional.of(cargaDeHoras));
  }

  public CargaDeHoras getCargaDeHoras() {
    return cargaDeHoras;
  }

  public CargaDeHorasService getCargaDeHorasService() {
    return cargaDeHorasService;
  }
}
