package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.util.Arrays;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.service.ApiExternaService;
import psa.cargahoras.service.CargaDeHorasService;

public class CargaDeHorasSteps {

  private final ResultadoOperacionCommonSteps resultadoOperacion;

  private CargaDeHoras cargaDeHoras;
  private TareaDTO tarea;
  private RecursoDTO recurso;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @Mock private ApiExternaService apiExternaService;

  private CargaDeHorasService cargaDeHorasService;

  public CargaDeHorasSteps(ResultadoOperacionCommonSteps resultadoOperacion) {
    this.resultadoOperacion = resultadoOperacion;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargaDeHorasService = new CargaDeHorasService(cargaDeHorasRepository, apiExternaService);

    cargaDeHoras = null;
    tarea = null;
    recurso = null;
  }

  @Dado("un recurso con id {string}")
  public void dadoUnRecursoConId(String recursoId) {
    recurso = mock(RecursoDTO.class);

    when(recurso.getId()).thenReturn(recursoId);

    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso));
  }

  @Y("una tarea con id {string}")
  public void dadaUnaTareaConId(String tareaId) {
    tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));
  }

  @Cuando("el recurso realiza una carga de {double} horas a la tarea en la fecha {string}")
  public void cuandoElRecursoCargaHorasATareaEnFecha(double cantidadHoras, String fechaDateStr) {
    cargaDeHoras =
        resultadoOperacion.ejecutar(
            () ->
                cargaDeHorasService.cargarHoras(
                    tarea.getId(), recurso.getId(), cantidadHoras, fechaDateStr));
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
    assertEquals(mensajeEsperado, resultadoOperacion.getExcepcion().getMessage());
  }

  @Dado("un recurso con id inexistente {string}")
  public void dadoUnRecursoConIdInexistente(String recursoId) {
    recurso = mock(RecursoDTO.class);

    when(recurso.getId()).thenReturn(recursoId);

    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList());
  }

  @Y("una tarea con id inexistente {string}")
  public void dadoUnaTareaConIdInexistente(String tareaId) {
    tarea = mock(TareaDTO.class);

    when(tarea.getId()).thenReturn(tareaId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList());
  }
}
