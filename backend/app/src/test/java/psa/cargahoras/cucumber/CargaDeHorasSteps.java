package psa.cargahoras.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.entity.EstadoTarea;
import psa.cargahoras.entity.Proyecto;
import psa.cargahoras.entity.Recurso;
import psa.cargahoras.entity.Rol;
import psa.cargahoras.entity.Tarea;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.repository.EstadoTareaRepository;
import psa.cargahoras.repository.RecursoRepository;
import psa.cargahoras.repository.TareaRepository;
import psa.cargahoras.service.CargaDeHorasService;

public class CargaDeHorasSteps {
  private final ResultadoOperacionCommonSteps resultadoOperacion;

  private Recurso recurso;
  private Tarea tarea;
  private CargaDeHoras cargaDeHoras;

  @Mock private RecursoRepository recursoRepository;
  @Mock private TareaRepository tareaRepository;
  @Mock private CargaDeHorasRepository cargaDeHorasRepository;
  @Mock private EstadoTareaRepository estadoTareaRepository;

  @Autowired private CargaDeHorasService cargaDeHorasService;

  public CargaDeHorasSteps(ResultadoOperacionCommonSteps resultadoOperacion) {
    this.resultadoOperacion = resultadoOperacion;
  }

  @Before
  public void resetear() {
    MockitoAnnotations.openMocks(this);

    cargaDeHorasService =
        new CargaDeHorasService(
            cargaDeHorasRepository, tareaRepository, recursoRepository, estadoTareaRepository);

    recurso = null;
    tarea = null;
    cargaDeHoras = null;
  }

  @Dado("un recurso con id {string}")
  public void dadoUnRecursoConId(String id) {
    recurso = new Recurso(UUID.fromString(id), "Carlos", "Castillo", 96113425, mock(Rol.class));

    when(recursoRepository.findById(recurso.getId())).thenReturn(Optional.of(recurso));
  }

  @Y("una tarea activa con id {string}")
  public void dadaUnaTareaConId(String id) {
    tarea =
        new Tarea(
            UUID.fromString(id),
            "Terminar el backend",
            "Se debe finalizar la creación de los endpoints y las entidades correspondientes",
            mock(Proyecto.class));

    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.of(tarea));
    when(estadoTareaRepository.findById(tarea.getId()))
        .thenReturn(Optional.of(new EstadoTarea(tarea.getId(), true)));
  }

  @Cuando("el recurso realiza una carga de {double} horas a la tarea en la fecha {string}")
  public void cuandoElRecursoCargaHorasATareaEnFecha(double cantidadHoras, String fechaDateStr) {
    cargaDeHoras =
        resultadoOperacion.ejecutar(
            () ->
                cargaDeHorasService.registrarNuevaCarga(
                    tarea.getId(), recurso.getId(), fechaDateStr, cantidadHoras));
  }

  @Y("la carga de horas debe ser registrada")
  public void verificarCargaDeHorasRegistrada() {
    assertNotNull(cargaDeHoras);
    verify(cargaDeHorasRepository).save(any(CargaDeHoras.class));
  }

  @Y("la carga de horas debe estar asociada el recurso con id {string}")
  public void verificarRecursoCargaDeHoras(String recursoId) {
    assertEquals(UUID.fromString(recursoId), cargaDeHoras.getRecurso().getId());
  }

  @Y("la carga de horas debe estar asociada a la tarea con id {string}")
  public void verificarTareaCargaDeHoras(String tareaId) {
    assertEquals(UUID.fromString(tareaId), cargaDeHoras.getTarea().getId());
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
      "una carga de horas registradas para la tarea con id {string}, y recurso con id {string}, en la fecha {string}")
  public void dadaUnaCargaDeHorasParaTareaDeRecurso(
      String tareaId, String recursoId, String fechaDateStr) {
    UUID recursoIdUUID = UUID.fromString(recursoId);

    Recurso recursoPrevio;

    if (recurso.getId() != recursoIdUUID) {
      recursoPrevio = new Recurso(recursoIdUUID, "Flavio", "Castillo", 97116471, mock(Rol.class));
    } else {
      recursoPrevio = recurso;
    }

    assertEquals(UUID.fromString(tareaId), tarea.getId());

    CargaDeHoras cargaPrevia = new CargaDeHoras(tarea, recursoPrevio, fechaDateStr, 8.0);

    when(cargaDeHorasRepository.findByTareaId(tarea.getId())).thenReturn(List.of(cargaPrevia));
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
    recurso =
        new Recurso(UUID.fromString(recursoId), "Carlos", "Castillo", 96113425, mock(Rol.class));

    when(recursoRepository.findById(recurso.getId())).thenReturn(Optional.empty());
    when(recursoRepository.existsById(recurso.getId())).thenReturn(false);
  }

  @Dado("una tarea con id inexistente {string}")
  public void dadoUnaTareaConIdInexistente(String tareaId) {
    tarea =
        new Tarea(
            UUID.fromString(tareaId),
            "Terminar el backend",
            "Se debe finalizar la creación de los endpoints y las entidades correspondientes",
            mock(Proyecto.class));

    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.empty());
    when(tareaRepository.existsById(tarea.getId())).thenReturn(false);
  }
}
