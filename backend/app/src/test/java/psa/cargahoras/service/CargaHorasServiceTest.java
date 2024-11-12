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
import psa.cargahoras.entity.CargaHorasEntity;
import psa.cargahoras.entity.EmpleadoEntity;
import psa.cargahoras.entity.TareaEntity;
import psa.cargahoras.repository.CargaHorasRepository;

@RunWith(MockitoJUnitRunner.class)
public class CargaHorasServiceTest {

  @Mock private CargaHorasRepository cargaHorasRepository;

  @InjectMocks private CargaHorasService cargaHorasService;

  @Test
  public void testObtenerTodasLasCargas() {
    TareaEntity tarea = mock(TareaEntity.class);
    EmpleadoEntity empleado = mock(EmpleadoEntity.class);

    CargaHorasEntity carga1 = new CargaHorasEntity(tarea, empleado);
    CargaHorasEntity carga2 = new CargaHorasEntity(tarea, empleado);
    List<CargaHorasEntity> expectedCargas = Arrays.asList(carga1, carga2);

    when(cargaHorasRepository.findAll()).thenReturn(expectedCargas);

    List<CargaHorasEntity> actualCargas = cargaHorasService.obtenerTodasLasCargas();

    assertNotNull(actualCargas);
    assertEquals(expectedCargas.size(), actualCargas.size());
    assertEquals(expectedCargas, actualCargas);

    verify(cargaHorasRepository, times(1)).findAll();
  }
}
