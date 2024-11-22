package psa.cargahoras.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;

@RunWith(MockitoJUnitRunner.class)
public class CargaDeHorasServiceTest {

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @Mock private ApiExternaService apiExternaService;

  @InjectMocks private CargaDeHorasService cargaDeHorasService;

  @Test
  public void cargarHorasAUnaMismaTareaConUnMismoRecurso() {
    String recursoId = UUID.randomUUID().toString();
    String tareaId = UUID.randomUUID().toString();

    double cantidadHoras = 8.0;
    String fechaCargaStr = "19/11/2024";

    TareaDTO tarea = new TareaDTO();
    tarea.setId(tareaId);

    RecursoDTO recurso = new RecursoDTO();
    recurso.setId(recursoId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));
    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso));

    cargaDeHorasService.cargarHoras(
        new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));
    cargaDeHorasService.cargarHoras(
        new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    verify(cargaDeHorasRepository, times(2)).save(any(CargaDeHoras.class));
  }

  public void cargarHorasAUnaMismaTareaConDistintosRecursos() {
    String recurso1Id = UUID.randomUUID().toString();
    String recurso2Id = UUID.randomUUID().toString();
    String tareaId = UUID.randomUUID().toString();

    double cantidadHoras = 8.0;
    String fechaCargaStr = "19/11/2024";

    TareaDTO tarea = new TareaDTO();
    tarea.setId(tareaId);

    RecursoDTO recurso1 = new RecursoDTO();
    recurso1.setId(recurso1Id);

    RecursoDTO recurso2 = new RecursoDTO();
    recurso2.setId(recurso2Id);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));
    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso1, recurso2));

    cargaDeHorasService.cargarHoras(
        new CargaDeHoras(tareaId, recurso1Id, cantidadHoras, fechaCargaStr));

    cargaDeHorasService.cargarHoras(
        new CargaDeHoras(tareaId, recurso2Id, cantidadHoras, fechaCargaStr));

    verify(cargaDeHorasRepository, times(2)).save(any(CargaDeHoras.class));
  }

  @Test
  public void cargarHorasADistintasTareasConUnMismoRecurso() {
    double cantidadHoras = 8.0;
    String fechaCargaStr = "19/11/2024";

    String tarea1Id = UUID.randomUUID().toString();
    String tarea2Id = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();

    TareaDTO tarea1 = new TareaDTO();
    tarea1.setId(tarea1Id);

    TareaDTO tarea2 = new TareaDTO();
    tarea2.setId(tarea2Id);

    RecursoDTO recurso = new RecursoDTO();
    recurso.setId(recursoId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea1, tarea2));
    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso));

    cargaDeHorasService.cargarHoras(
        new CargaDeHoras(tarea1Id, recursoId, cantidadHoras, fechaCargaStr));

    cargaDeHorasService.cargarHoras(
        new CargaDeHoras(tarea2Id, recursoId, cantidadHoras, fechaCargaStr));

    verify(cargaDeHorasRepository, times(2)).save(any(CargaDeHoras.class));
  }

  public void obtenerTodasLasCargasDeHoras() {
    double cantidadHoras = 8.0;
    String fechaCargaStr = "19/11/2024";

    String recursoId = UUID.randomUUID().toString();
    String tareaId = UUID.randomUUID().toString();

    TareaDTO tarea = new TareaDTO();
    tarea.setId(tareaId);

    RecursoDTO recurso = new RecursoDTO();
    recurso.setId(recursoId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));
    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso));

    CargaDeHoras carga1 =
        cargaDeHorasService.cargarHoras(
            new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));
    CargaDeHoras carga2 =
        cargaDeHorasService.cargarHoras(
            new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    List<CargaDeHoras> cargasGuardadas = cargaDeHorasService.obtenerTodasLasCargasDeHoras();

    assertNotNull(cargasGuardadas);
    assertEquals(2, cargasGuardadas.size());
    assertEquals(cargasGuardadas.get(0).getId(), carga1.getId());
    assertEquals(cargasGuardadas.get(1).getId(), carga2.getId());
  }

  @Test
  public void cargarHorasATareaInexistenteTiraExcepcion() {
    String tareaId = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();

    RecursoDTO recurso = new RecursoDTO();
    recurso.setId(recursoId);

    when(apiExternaService.getTareas()).thenReturn(Arrays.asList());

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                cargaDeHorasService.cargarHoras(
                    new CargaDeHoras(tareaId, recursoId, 8.0, "19/11/2024")));

    assertEquals("No existe la tarea con ID: " + tareaId, e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
  }

  @Test
  public void cargarHorasDeRecursoInexistenteTiraExcepcion() {
    String recursoId = UUID.randomUUID().toString();
    String tareaId = UUID.randomUUID().toString();

    TareaDTO tarea = new TareaDTO();
    tarea.setId(tareaId);

    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList());
    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea));

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                cargaDeHorasService.cargarHoras(
                    new CargaDeHoras(tareaId, recursoId, 8.0, "19/11/2024")));

    assertEquals("No existe el recurso con ID: " + recursoId, e.getMessage());

    verify(cargaDeHorasRepository, never()).save(any());
  }

  @Test
  public void obtenerCargasDeHorasPorProyecto() {
    String proyecto1Id = UUID.randomUUID().toString();
    String proyecto2Id = UUID.randomUUID().toString();

    ProyectoDTO proyecto1 = new ProyectoDTO();
    proyecto1.setId(proyecto1Id);

    ProyectoDTO proyecto2 = new ProyectoDTO();
    proyecto2.setId(proyecto2Id);

    String tarea1Id = UUID.randomUUID().toString();
    String tarea2Id = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();

    TareaDTO tarea1 = new TareaDTO();
    tarea1.setId(tarea1Id);
    tarea1.setProyectoId(proyecto1Id);

    TareaDTO tarea2 = new TareaDTO();
    tarea2.setId(tarea2Id);
    tarea2.setProyectoId(proyecto2Id);

    CargaDeHoras carga1 = new CargaDeHoras(tarea1Id, recursoId, 8.0, "19/11/2024");
    CargaDeHoras carga2 = new CargaDeHoras(tarea2Id, recursoId, 4.0, "19/11/2024");

    when(apiExternaService.getProyectos()).thenReturn(Arrays.asList(proyecto1, proyecto2));
    when(apiExternaService.getTareas()).thenReturn(Arrays.asList(tarea1, tarea2));
    when(cargaDeHorasRepository.findAll()).thenReturn(Arrays.asList(carga1, carga2));

    List<CargaDeHoras> cargasProyecto1 =
        cargaDeHorasService.obtenerTodasLasCargasDeHorasPorProyecto(proyecto1Id);

    assertEquals(1, cargasProyecto1.size());
    assertEquals(tarea1Id, cargasProyecto1.get(0).getTareaId());
  }

  @Test
  public void obtenerCargasDeHorasPorProyectoInexistenteTiraExcepcion() {
    String proyectoInexistenteId = UUID.randomUUID().toString();

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                cargaDeHorasService.obtenerTodasLasCargasDeHorasPorProyecto(proyectoInexistenteId));

    assertEquals("No existe el proyecto con ID: " + proyectoInexistenteId, e.getMessage());
  }
}
