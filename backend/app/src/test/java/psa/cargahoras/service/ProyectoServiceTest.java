package psa.cargahoras.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import psa.cargahoras.dto.CostoProyectoDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.ProyectoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.TareaDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProyectoServiceTest {

  @Mock private ApiExternaService apiExternaService;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @Mock private CargaDeHorasService mockCargaDeHorasService;

  @Mock private RecursoService recursoService;

  @InjectMocks private ProyectoService proyectoService;

  @Test
  public void obetenerCostoDeUnProyecto() {
    String proyectoId = UUID.randomUUID().toString();
    String tareaId = UUID.randomUUID().toString();
    String rolId = UUID.randomUUID().toString();
    String recursoId1 = UUID.randomUUID().toString();
    String recursoId2 = UUID.randomUUID().toString();

    ProyectoDTO proyecto = new ProyectoDTO();
    proyecto.setId(proyectoId);
    proyecto.setNombre("Sistema de Gestión de Proyectos");

    TareaDTO tarea = new TareaDTO();
    tarea.setId(tareaId);
    tarea.setProyectoId(proyectoId);

    RecursoDTO recurso1 = new RecursoDTO();
    recurso1.setId(recursoId1);
    recurso1.setRolId(rolId);
    recurso1.setNombre("José");
    recurso1.setApellido("Hernadéz");

    RecursoDTO recurso2 = new RecursoDTO();
    recurso2.setId(recursoId1);
    recurso2.setRolId(rolId);
    recurso2.setNombre("ADrián");
    recurso2.setApellido("Guerra");

    CargaDeHoras cargaDeHoras1 = new CargaDeHoras(tareaId, recursoId1, 10.0, "26/11/2024");
    CargaDeHoras cargaDeHoras2 = new CargaDeHoras(tareaId, recursoId2, 8.0, "27/11/2024");

    when(apiExternaService.getProyectos()).thenReturn(Arrays.asList(proyecto));
    when((apiExternaService.getTareas())).thenReturn(Arrays.asList(tarea));
    when(cargaDeHorasRepository.findAll()).thenReturn(Arrays.asList(cargaDeHoras1, cargaDeHoras2));

    CargaDeHorasService cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, apiExternaService);

    List<CargaDeHoras> cargasPorProyecto =
        cargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId);

    when(mockCargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId))
        .thenReturn(cargasPorProyecto);

    CostoRecursoDTO costoRecurso1 =
        new CostoRecursoDTO(
            recurso1.getId(),
            rolId,
            300,
            String.join(" ", recurso1.getNombre(), recurso1.getApellido()),
            "Desarrollador Senior");

    CostoRecursoDTO costoRecurso2 =
        new CostoRecursoDTO(
            recurso2.getId(),
            rolId,
            240,
            String.join(" ", recurso2.getNombre(), recurso2.getApellido()),
            "Desarrollador Senior");

    when(recursoService.obtenerCostosDeTodosLosRecursos(cargasPorProyecto))
        .thenReturn(Arrays.asList(costoRecurso1, costoRecurso2));

    CostoProyectoDTO costoProyecto = proyectoService.obtenerCostoPorProyecto(proyectoId);

    assertEquals("Sistema de Gestión de Proyectos", costoProyecto.getNombreProyecto());
    assertEquals(proyectoId, costoProyecto.getProyectoId());
    assertEquals(540, costoProyecto.getCosto(), 0.1);
  }

  @Test
  public void obetenerCostosDeTodosLosProyectos() {
    String proyectoId1 = UUID.randomUUID().toString();
    String proyectoId2 = UUID.randomUUID().toString();
    String tareaId1 = UUID.randomUUID().toString();
    String tareaId2 = UUID.randomUUID().toString();
    String rolId = UUID.randomUUID().toString();
    String recursoId1 = UUID.randomUUID().toString();
    String recursoId2 = UUID.randomUUID().toString();

    ProyectoDTO proyecto1 = new ProyectoDTO();
    proyecto1.setId(proyectoId1);
    proyecto1.setNombre("Sistema de Gestión de Proyectos");

    ProyectoDTO proyecto2 = new ProyectoDTO();
    proyecto2.setId(proyectoId2);
    proyecto2.setNombre("Sistema de Gestión de Inventarios");

    TareaDTO tarea1 = new TareaDTO();
    tarea1.setId(tareaId1);
    tarea1.setProyectoId(proyectoId1);

    TareaDTO tarea2 = new TareaDTO();
    tarea2.setId(tareaId2);
    tarea2.setProyectoId(proyectoId2);

    RecursoDTO recurso1 = new RecursoDTO();
    recurso1.setId(recursoId1);
    recurso1.setRolId(rolId);
    recurso1.setNombre("José");
    recurso1.setApellido("Hernadéz");

    RecursoDTO recurso2 = new RecursoDTO();
    recurso2.setId(recursoId1);
    recurso2.setRolId(rolId);
    recurso2.setNombre("ADrián");
    recurso2.setApellido("Guerra");

    CargaDeHoras cargaDeHoras1Proyecto1 =
        new CargaDeHoras(tareaId1, recursoId1, 10.0, "26/11/2024");
    CargaDeHoras cargaDeHoras1Proyecto2 = new CargaDeHoras(tareaId2, recursoId2, 8.0, "27/11/2024");
    CargaDeHoras cargaDeHoras2Proyecto1 = new CargaDeHoras(tareaId1, recursoId1, 6.0, "26/11/2024");
    CargaDeHoras cargaDeHoras2Proyecto2 = new CargaDeHoras(tareaId2, recursoId2, 8.0, "27/11/2024");

    List<CargaDeHoras> cargasdeHoras = new ArrayList<CargaDeHoras>();
    cargasdeHoras.addAll(
        Arrays.asList(
            cargaDeHoras1Proyecto1,
            cargaDeHoras2Proyecto1,
            cargaDeHoras1Proyecto2,
            cargaDeHoras2Proyecto2));

    when(apiExternaService.getProyectos()).thenReturn(Arrays.asList(proyecto1, proyecto2));
    when((apiExternaService.getTareas())).thenReturn(Arrays.asList(tarea1, tarea2));
    when(cargaDeHorasRepository.findAll()).thenReturn(cargasdeHoras);

    CargaDeHorasService cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, apiExternaService);

    List<CargaDeHoras> cargasPorProyecto1 =
        cargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId1);
    List<CargaDeHoras> cargasPorProyecto2 =
        cargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId2);

    when(mockCargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId1))
        .thenReturn(cargasPorProyecto1);
    when(mockCargaDeHorasService.obtenerCargasDeHorasPorProyecto(proyectoId2))
        .thenReturn(cargasPorProyecto2);

    CostoRecursoDTO costoRecurso1 =
        new CostoRecursoDTO(
            recurso1.getId(),
            rolId,
            480,
            String.join(" ", recurso1.getNombre(), recurso1.getApellido()),
            "Desarrollador Senior");

    CostoRecursoDTO costoRecurso2 =
        new CostoRecursoDTO(
            recurso2.getId(),
            rolId,
            4800,
            String.join(" ", recurso2.getNombre(), recurso2.getApellido()),
            "Desarrollador Senior");

    when(recursoService.obtenerCostosDeTodosLosRecursos(cargasPorProyecto1))
        .thenReturn(Arrays.asList(costoRecurso1));
    when(recursoService.obtenerCostosDeTodosLosRecursos(cargasPorProyecto2))
        .thenReturn(Arrays.asList(costoRecurso2));

    List<CostoProyectoDTO> costoProyecto = proyectoService.obetenerCostosDeTodosLosProyectos();

    assertEquals("Sistema de Gestión de Proyectos", costoProyecto.get(0).getNombreProyecto());
    assertEquals(proyectoId1, costoProyecto.get(0).getProyectoId());
    assertEquals(480, costoProyecto.get(0).getCosto(), 0.1);

    assertEquals("Sistema de Gestión de Inventarios", costoProyecto.get(1).getNombreProyecto());
    assertEquals(proyectoId2, costoProyecto.get(1).getProyectoId());
    assertEquals(4800, costoProyecto.get(1).getCosto(), 0.1);
  }

  @Test
  public void obetenerCostoDeUnProyectoInexistenteTiraExcepción() {
    String proyectoInexistenteId = UUID.randomUUID().toString();

    when(apiExternaService.getProyectos()).thenReturn(Arrays.asList());

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> proyectoService.obtenerCostoPorProyecto(proyectoInexistenteId));

    assertEquals("No existe el proyecto con ID: " + proyectoInexistenteId, e.getMessage());
  }
}
