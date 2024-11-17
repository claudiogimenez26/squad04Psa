package psa.cargahoras.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import psa.cargahoras.entity.CargaDeHoras;
import psa.cargahoras.entity.Recurso;
import psa.cargahoras.entity.Tarea;
import psa.cargahoras.repository.CargaHorasRepository;

@RunWith(MockitoJUnitRunner.class)
public class CargaHorasServiceTest {

  @Mock private CargaHorasRepository cargaHorasRepository;

  @InjectMocks private CargaHorasService cargaHorasService;

  @Test
  public void testObtenerTodasLasCargas() {
    Tarea tarea = mock(Tarea.class);
    Recurso recurso = mock(Recurso.class);

    CargaDeHoras carga1 = new CargaDeHoras(tarea, recurso);
    CargaDeHoras carga2 = new CargaDeHoras(tarea, recurso);
    List<CargaDeHoras> expectedCargas = Arrays.asList(carga1, carga2);

    when(cargaHorasRepository.findAll()).thenReturn(expectedCargas);

    List<CargaDeHoras> actualCargas = cargaHorasService.obtenerTodasLasCargas();

    assertNotNull(actualCargas);
    assertEquals(expectedCargas.size(), actualCargas.size());
    assertEquals(expectedCargas, actualCargas);

    verify(cargaHorasRepository, times(1)).findAll();
  }
}
