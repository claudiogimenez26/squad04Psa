package psa.cargahoras.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import psa.cargahoras.dto.CargaDeHorasPorRecursoDTO;
import psa.cargahoras.dto.CostoRecursoDTO;
import psa.cargahoras.dto.RecursoDTO;
import psa.cargahoras.dto.RolDTO;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.repository.CargaDeHorasRepository;

@RunWith(MockitoJUnitRunner.class)
public class RecursoServiceTest {

  @Mock private ApiExternaService apiExternaService;

  @Mock private CargaDeHorasService mockCargaDeHorasService;

  @Mock private CargaDeHorasRepository cargaDeHorasRepository;

  @InjectMocks private RecursoService recursoService;

  @Test
  public void obtenerCostoDeUnRecurso() {
    String rolId1 = UUID.randomUUID().toString();
    String recursoId1 = UUID.randomUUID().toString();
    String cargaDeHorasId1 = UUID.randomUUID().toString();

    RolDTO rol1 = new RolDTO();
    rol1.setId(rolId1);
    rol1.setNombre("Desarrollador");
    rol1.setExperiencia("Senior");

    RecursoDTO recurso1 = new RecursoDTO();
    recurso1.setId(recursoId1);
    recurso1.setRolId(rolId1);
    recurso1.setNombre("Juan");
    recurso1.setApellido("Gómez");

    CargaDeHoras cargaDeHoras1 = new CargaDeHoras(cargaDeHorasId1, recursoId1, 8.0, "26/11/2024");

    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso1));
    when(cargaDeHorasRepository.findAll()).thenReturn(Arrays.asList(cargaDeHoras1));

    CargaDeHorasService cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, apiExternaService);

    List<CargaDeHorasPorRecursoDTO> cargaPorRecurso1 =
        cargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId1, null);

    when(apiExternaService.getRoles()).thenReturn(Arrays.asList(rol1));
    when(mockCargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId1, null))
        .thenReturn(cargaPorRecurso1);

    CostoRecursoDTO costoRecurso1 = recursoService.obtenerCostoPorRecurso(recursoId1);

    assertEquals("Juan Gómez", costoRecurso1.getNombreRecurso());
    assertEquals("Desarrollador Senior", costoRecurso1.getNombreRol());
    assertEquals(240, costoRecurso1.getCosto(), 0.1);
  }

  @Test
  public void obtenerCostosDeTodosLosRecurso() {
    String rolId1 = UUID.randomUUID().toString();
    String rolId2 = UUID.randomUUID().toString();
    String recursoId1 = UUID.randomUUID().toString();
    String recursoId2 = UUID.randomUUID().toString();
    String cargaDeHorasId1 = UUID.randomUUID().toString();
    String cargaDeHorasId2 = UUID.randomUUID().toString();

    RolDTO rol1 = new RolDTO();
    rol1.setId(rolId1);
    rol1.setNombre("Desarrollador");
    rol1.setExperiencia("Senior");

    RolDTO rol2 = new RolDTO();
    rol2.setId(rolId2);
    rol2.setNombre("Desarrollador");
    rol2.setExperiencia("Junior");

    RecursoDTO recurso1 = new RecursoDTO();
    recurso1.setId(recursoId1);
    recurso1.setRolId(rolId1);
    recurso1.setNombre("Juan");
    recurso1.setApellido("Gómez");

    RecursoDTO recurso2 = new RecursoDTO();
    recurso2.setId(recursoId2);
    recurso2.setRolId(rolId2);
    recurso2.setNombre("Pedro");
    recurso2.setApellido("Martinez");

    CargaDeHoras cargaDeHoras1 = new CargaDeHoras(cargaDeHorasId1, recursoId1, 8.0, "26/11/2024");
    CargaDeHoras cargaDeHoras2 = new CargaDeHoras(cargaDeHorasId2, recursoId2, 6.0, "27/11/2024");

    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList(recurso1, recurso2));
    when(cargaDeHorasRepository.findAll()).thenReturn(Arrays.asList(cargaDeHoras1, cargaDeHoras2));

    CargaDeHorasService cargaDeHorasService =
        new CargaDeHorasService(cargaDeHorasRepository, apiExternaService);

    List<CargaDeHorasPorRecursoDTO> cargaPorRecurso1 =
        cargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId1, LocalDate.now());
    List<CargaDeHorasPorRecursoDTO> cargaPorRecurso2 =
        cargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId2, LocalDate.now());

    when(apiExternaService.getRoles()).thenReturn(Arrays.asList(rol1, rol2));
    when(mockCargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId1, null))
        .thenReturn(cargaPorRecurso1);
    when(mockCargaDeHorasService.obtenerCargasDeHorasPorRecurso(recursoId2, null))
        .thenReturn(cargaPorRecurso2);

    List<CostoRecursoDTO> costosPorRecurso = recursoService.obtenerCostosDeTodosLosRecursos();

    assertEquals("Juan Gómez", costosPorRecurso.get(0).getNombreRecurso());
    assertEquals("Desarrollador Senior", costosPorRecurso.get(0).getNombreRol());
    assertEquals(240, costosPorRecurso.get(0).getCosto(), 0.1);

    assertEquals("Pedro Martinez", costosPorRecurso.get(1).getNombreRecurso());
    assertEquals("Desarrollador Junior", costosPorRecurso.get(1).getNombreRol());
    assertEquals(120, costosPorRecurso.get(1).getCosto(), 0.1);
  }

  @Test
  public void obtenerCostoDeUnRecursoInexistenteTiraExcepcion() {
    String recursoInexistenteId = UUID.randomUUID().toString();

    when(apiExternaService.getRecursos()).thenReturn(Arrays.asList());

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> recursoService.obtenerCostoPorRecurso(recursoInexistenteId));

    assertEquals("No existe el recurso con ID: " + recursoInexistenteId, e.getMessage());
  }
}
