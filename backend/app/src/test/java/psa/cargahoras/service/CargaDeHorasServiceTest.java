package psa.cargahoras.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.entity.EstadoTarea;
import psa.cargahoras.entity.Recurso;
import psa.cargahoras.entity.Tarea;
import psa.cargahoras.repository.CargaDeHorasRepository;
import psa.cargahoras.repository.EstadoTareaRepository;
import psa.cargahoras.repository.RecursoRepository;
import psa.cargahoras.repository.TareaRepository;

@RunWith(MockitoJUnitRunner.class)
public class CargaDeHorasServiceTest {
  @Mock private CargaDeHorasRepository cargaDeHorasRepository;
  @Mock private TareaRepository tareaRepository;
  @Mock private RecursoRepository recursoRepository;
  @Mock private EstadoTareaRepository estadoTareaRepository;

  @InjectMocks private CargaDeHorasService cargaDeHorasService;

  @Test
  public void cargarHorasADiferentesTareasConUnMismoRecurso() {
    Recurso recurso = mock(Recurso.class);
    when(recurso.getId()).thenReturn(UUID.randomUUID());

    Tarea tarea1 = mock(Tarea.class);
    Tarea tarea2 = mock(Tarea.class);
    when(tarea1.getId()).thenReturn(UUID.randomUUID());
    when(tarea2.getId()).thenReturn(UUID.randomUUID());

    EstadoTarea estado1 = new EstadoTarea(tarea1.getId(), true);
    EstadoTarea estado2 = new EstadoTarea(tarea2.getId(), true);

    when(recursoRepository.findById(recurso.getId())).thenReturn(Optional.of(recurso));
    when(tareaRepository.findById(tarea1.getId())).thenReturn(Optional.of(tarea1));
    when(tareaRepository.findById(tarea2.getId())).thenReturn(Optional.of(tarea2));
    when(estadoTareaRepository.findById(tarea1.getId())).thenReturn(Optional.of(estado1));
    when(estadoTareaRepository.findById(tarea2.getId())).thenReturn(Optional.of(estado2));

    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    cargaDeHorasService.registrarNuevaCarga(
        tarea1.getId(), recurso.getId(), fechaCargaStr, cantidadHoras);
    cargaDeHorasService.registrarNuevaCarga(
        tarea2.getId(), recurso.getId(), fechaCargaStr, cantidadHoras);

    CargaDeHoras carga1 = new CargaDeHoras(tarea1, recurso, fechaCargaStr, cantidadHoras);
    CargaDeHoras carga2 = new CargaDeHoras(tarea2, recurso, fechaCargaStr, cantidadHoras);

    verify(cargaDeHorasRepository).save(carga1);
    verify(cargaDeHorasRepository).save(carga2);
  }

  @Test
  public void cargarHorasAUnaMismaTareaConDiferentesRecursosTiraExcepcion() {
    Tarea tarea = mock(Tarea.class);
    when(tarea.getId()).thenReturn(UUID.randomUUID());

    Recurso recurso1 = mock(Recurso.class);
    Recurso recurso2 = mock(Recurso.class);
    when(recurso1.getId()).thenReturn(UUID.randomUUID());
    when(recurso2.getId()).thenReturn(UUID.randomUUID());

    EstadoTarea estadoActivo = new EstadoTarea(tarea.getId(), true);
    when(estadoTareaRepository.findById(tarea.getId())).thenReturn(Optional.of(estadoActivo));

    CargaDeHoras cargaPrevia = new CargaDeHoras(tarea, recurso1, "10/11/2024", 8.0);

    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.of(tarea));
    when(recursoRepository.findById(recurso2.getId())).thenReturn(Optional.of(recurso2));
    when(cargaDeHorasRepository.findByTareaId(tarea.getId()))
        .thenReturn(Arrays.asList(cargaPrevia));

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                cargaDeHorasService.registrarNuevaCarga(
                    tarea.getId(), recurso2.getId(), "19/11/2024", 8.0));

    assertEquals(
        "Esta tarea ya está asignada al recurso con ID: " + recurso1.getId(), e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
  }

  @Test
  public void obtenerTodasLasCargas() {
    Tarea tarea = mock(Tarea.class);
    Recurso recurso = mock(Recurso.class);

    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    CargaDeHoras carga1 = new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras);
    CargaDeHoras carga2 = new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras);
    List<CargaDeHoras> cargasEsperadas = Arrays.asList(carga1, carga2);

    when(cargaDeHorasRepository.findAll()).thenReturn(cargasEsperadas);

    List<CargaDeHoras> cargasGuardadas = cargaDeHorasService.obtenerTodasLasCargas();

    assertNotNull(cargasGuardadas);
    assertEquals(cargasEsperadas.size(), cargasGuardadas.size());
    assertEquals(cargasEsperadas, cargasGuardadas);

    verify(cargaDeHorasRepository, times(1)).findAll();
  }

  @Test
  public void cargarHorasATareaInexistenteTiraExcepcion() {
    UUID tareaId = UUID.randomUUID();
    UUID recursoId = UUID.randomUUID();

    when(tareaRepository.findById(tareaId)).thenReturn(Optional.empty());

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> cargaDeHorasService.registrarNuevaCarga(tareaId, recursoId, "19/11/2024", 8.0));

    assertEquals("No existe la tarea con ID: " + tareaId, e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
  }

  @Test
  public void cargarHorasDeRecursoInexistenteTiraExcepcion() {
    Recurso recurso = mock(Recurso.class);
    when(recurso.getId()).thenReturn(UUID.randomUUID());

    Tarea tarea = mock(Tarea.class);
    when(tarea.getId()).thenReturn(UUID.randomUUID());

    EstadoTarea estadoActivo = new EstadoTarea(tarea.getId(), true);
    when(estadoTareaRepository.findById(tarea.getId())).thenReturn(Optional.of(estadoActivo));

    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.of(tarea));
    when(recursoRepository.findById(recurso.getId())).thenReturn(Optional.empty());

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                cargaDeHorasService.registrarNuevaCarga(
                    tarea.getId(), recurso.getId(), "19/11/2024", 8.0));

    assertEquals("No existe el recurso con ID: " + recurso.getId(), e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
  }

  @Test
  public void cargarHorasATareaActiva() {
    UUID tareaId = UUID.randomUUID();
    UUID recursoId = UUID.randomUUID();

    Tarea tarea = mock(Tarea.class);
    when(tarea.getId()).thenReturn(tareaId);

    Recurso recurso = mock(Recurso.class);
    when(recurso.getId()).thenReturn(recursoId);

    EstadoTarea estadoActivo = new EstadoTarea(tarea.getId(), true);

    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.of(tarea));
    when(recursoRepository.findById(recursoId)).thenReturn(Optional.of(recurso));
    when(estadoTareaRepository.findById(tarea.getId())).thenReturn(Optional.of(estadoActivo));
    when(cargaDeHorasRepository.findByTareaId(tarea.getId())).thenReturn(Arrays.asList());

    CargaDeHoras resultado =
        cargaDeHorasService.registrarNuevaCarga(tarea.getId(), recurso.getId(), "19/11/2024", 8.0);

    assertNotNull(resultado);

    verify(cargaDeHorasRepository).save(any(CargaDeHoras.class));
  }

  @Test
  public void cargarHorasATareaPausadaTiraExcepcion() {
    UUID tareaId = UUID.randomUUID();
    UUID recursoId = UUID.randomUUID();

    Tarea tarea = mock(Tarea.class);
    when(tarea.getId()).thenReturn(tareaId);

    EstadoTarea estadoPausado = new EstadoTarea(tarea.getId(), false);

    when(tareaRepository.findById(tareaId)).thenReturn(Optional.of(tarea));
    when(estadoTareaRepository.findById(tareaId)).thenReturn(Optional.of(estadoPausado));

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> cargaDeHorasService.registrarNuevaCarga(tareaId, recursoId, "19/11/2024", 8.0));

    assertEquals("No se pueden cargar horas a una tarea pausada", e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
  }

  @Test
  public void cargarHorasATareaSinEstadoTiraExcepcion() {
    UUID tareaId = UUID.randomUUID();
    UUID recursoId = UUID.randomUUID();

    Tarea tarea = mock(Tarea.class);
    when(tarea.getId()).thenReturn(tareaId);

    when(tareaRepository.findById(tarea.getId())).thenReturn(Optional.of(tarea));
    when(estadoTareaRepository.findById(tarea.getId())).thenReturn(Optional.empty());

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                cargaDeHorasService.registrarNuevaCarga(
                    tarea.getId(), recursoId, "19/11/2024", 8.0));

    assertEquals("No existe estado para la tarea con ID: " + tarea.getId(), e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
    verify(recursoRepository, never()).findById(any());
  }
}
