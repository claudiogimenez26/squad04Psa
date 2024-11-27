package psa.cargahoras.cucumber;

import static org.mockito.Mockito.*;

import io.cucumber.java.es.Dado;
import java.util.Arrays;
import psa.cargahoras.dto.ProyectoDTO;

public class ProyectoCommonSteps {

  private final TestContext testContext;

  private ProyectoDTO proyecto;

  public ProyectoCommonSteps(TestContext testContext) {
    this.testContext = testContext;
  }

  @Dado("un proyecto con id {string}")
  @Dado("un proyecto sin tareas con id {string}")
  public void dadoUnProyecto(String proyectoId) {
    proyecto = mock(ProyectoDTO.class);

    when(proyecto.getId()).thenReturn(proyectoId);

    when(testContext.getApiExternaService().getProyectos()).thenReturn(Arrays.asList(proyecto));
  }

  @Dado("un proyecto con id inexistente {string}")
  public void dadoUnProyectoConIdInexistente(String proyectoId) {
    proyecto = mock(ProyectoDTO.class);

    when(proyecto.getId()).thenReturn(proyectoId);

    when(testContext.getApiExternaService().getProyectos()).thenReturn(Arrays.asList());
  }

  public ProyectoDTO getProyecto() {
    return proyecto;
  }
}
