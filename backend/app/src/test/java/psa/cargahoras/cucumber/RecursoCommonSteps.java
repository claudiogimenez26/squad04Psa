package psa.cargahoras.cucumber;

import static org.mockito.Mockito.*;

import io.cucumber.java.es.Dado;
import java.util.Arrays;
import psa.cargahoras.dto.RecursoDTO;

public class RecursoCommonSteps {
  private final TestContext testContext;

  private RecursoDTO recurso;

  public RecursoCommonSteps(TestContext testContext) {
    this.testContext = testContext;
  }

  @Dado("un recurso con id {string}")
  public void dadoUnRecursoConId(String recursoId) {
    recurso = mock(RecursoDTO.class);
    when(recurso.getId()).thenReturn(recursoId);
    when(testContext.getApiExternaService().getRecursos()).thenReturn(Arrays.asList(recurso));
  }

  @Dado("un recurso con id inexistente {string}")
  public void dadoUnRecursoConIdInexistente(String recursoId) {
    recurso = mock(RecursoDTO.class);
    when(recurso.getId()).thenReturn(recursoId);
    when(testContext.getApiExternaService().getRecursos()).thenReturn(Arrays.asList());
  }

  @Dado("un recurso con id {string} que no ha cargado horas")
  public void dadoUnRecurso(String recursoId) {
    recurso = mock(RecursoDTO.class);
    when(recurso.getId()).thenReturn(recursoId);
    when(testContext.getApiExternaService().getRecursos()).thenReturn(Arrays.asList(recurso));
  }

  public RecursoDTO getRecurso() {
    return recurso;
  }
}
