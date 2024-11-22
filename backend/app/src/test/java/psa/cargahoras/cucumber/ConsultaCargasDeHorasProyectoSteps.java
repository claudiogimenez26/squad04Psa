package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Y;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.ApiExternaService;
import psa.cargahoras.service.CargaDeHorasService;

public class ConsultaCargasDeHorasProyectoSteps {

  private final ResultadoOperacionCommonSteps resultadoOperacion;

  private ProyectoDTO proyecto;
  private TareaDTO tarea;
  private List<CargaDeHoras> cargasDeHorasActuales;
  private List<CargaDeHoras> cargasDeHorasFinales;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @Mock private ApiExternaService apiExternaService;

  private CargaDeHorasService cargaDeHorasService;

  public ConsultaCargasDeHorasProyectoSteps(ResultadoOperacionCommonSteps resultadoOperacion) {
    this.resultadoOperacion = resultadoOperacion;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargaDeHorasService = new CargaDeHorasService(cargaDeHorasRepository, apiExternaService);

    cargasDeHorasActuales = new ArrayList<>();
  }

  @Dado("un proyecto con id {string}")
  @Dado("un proyecto sin tareas con id {string}")
  public void dadoUnProyecto(String proyectoId) {
    proyecto = mock(ProyectoDTO.class);

    when(proyecto.getId()).thenReturn(proyectoId);

    when(apiExternaService.getProyectos()).thenReturn(Arrays.asList(proyecto));
  }

  @Y("una tarea con id {string}, con proyecto con id {string}")
  public void dadaUnaTareaConProyecto(String tareaId, String proyectoId) {
    tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);
    when(tarea.getProyectoId()).thenReturn(proyectoId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));
  }

  @Y("una carga de horas con id {string}, con tarea con id {string}")
  public void dadaUnaCargaDeHorasConTarea(String cargaDeHorasId, String tareaId) {
    CargaDeHoras cargaDeHoras = mock(CargaDeHoras.class);

    when(cargaDeHoras.getId()).thenReturn(cargaDeHorasId);
    when(cargaDeHoras.getTareaId()).thenReturn(tareaId);

    cargasDeHorasActuales.add(cargaDeHoras);
    when(cargaDeHorasRepository.findAll()).thenReturn(cargasDeHorasActuales);
  }

  @Cuando("consulto las cargas de horas del proyecto")
  public void consultarCargasDeHorasDelProyecto() {
    cargasDeHorasFinales =
        resultadoOperacion.ejecutar(
            () -> cargaDeHorasService.obtenerTodasLasCargasDeHorasPorProyecto(proyecto.getId()));
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

  @Dado("un proyecto con id inexistente {string}")
  public void dadoUnProyectoConIdInexistente(String proyectoId) {
    proyecto = mock(ProyectoDTO.class);

    when(proyecto.getId()).thenReturn(proyectoId);

    when(apiExternaService.getProyectos()).thenReturn(Arrays.asList());
  }
}
