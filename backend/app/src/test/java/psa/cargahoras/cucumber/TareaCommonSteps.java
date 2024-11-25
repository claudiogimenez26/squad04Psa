package psa.cargahoras.cucumber;

import static org.mockito.Mockito.*;

import io.cucumber.java.es.Dado;
import java.util.Arrays;
import psa.cargahoras.dto.TareaDTO;

public class TareaCommonSteps {
  private final TestContext testContext;

  private TareaDTO tarea;

  public TareaCommonSteps(TestContext testContext) {
    this.testContext = testContext;
  }

  @Dado("una tarea con id {string}")
  public void dadaUnaTareaConId(String tareaId) {
    tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);

    when(testContext.getApiExternaService().getTareas()).thenReturn(Arrays.asList(tarea));
  }

  @Dado("una tarea con id inexistente {string}")
  public void dadoUnaTareaConIdInexistente(String tareaId) {
    tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);

    when(testContext.getApiExternaService().getTareas()).thenReturn(Arrays.asList());
  }

  @Dado("una tarea con id {string}, con proyecto con id {string}")
  public void dadaUnaTareaConProyecto(String tareaId, String proyectoId) {
    tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);
    when(tarea.getProyectoId()).thenReturn(proyectoId);

    when(testContext.getApiExternaService().getTareas()).thenReturn(Arrays.asList(tarea));
  }

  public TareaDTO getTarea() {
    return tarea;
  }
}
