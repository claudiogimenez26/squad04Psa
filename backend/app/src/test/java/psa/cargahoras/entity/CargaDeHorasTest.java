package psa.cargahoras.entity;

import static org.junit.Assert.*;

import java.util.UUID;
import org.junit.Test;

public class CargaDeHorasTest {

  @Test
  public void constructorAsignaCamposCorrectamente() {
    String tareaId = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();
    double cantidadHoras = 8.0;
    String fechaCargaStr = "19/11/2024";

    CargaDeHoras cargaDeHoras = new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr);

    assertEquals(tareaId, cargaDeHoras.getTareaId());
    assertEquals(recursoId, cargaDeHoras.getRecursoId());
    assertEquals(fechaCargaStr, cargaDeHoras.getFechaCarga().format(CargaDeHoras.formatterFecha));
    assertEquals(cantidadHoras, cargaDeHoras.getCantidadHoras(), 0.0);
  }

  @Test
  public void crearUnaCargaDeHorasConHorasNegativasTiraExcepcion() {
    String tareaId = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();
    String fechaCargaStr = "19/11/2024";

    double cantidadHoras = -8.0;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    assertEquals("La cantidad de horas no puede ser negativa", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConTareaNulaTiraExcepcion() {
    String recursoId = UUID.randomUUID().toString();
    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    String tareaId = null;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    assertEquals("El id de la tarea no puede ser nulo", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConRecursoNuloTiraExcepcion() {
    String tareaId = UUID.randomUUID().toString();
    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    String recursoId = null;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    assertEquals("El id del recurso no puede ser nulo", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConFechaNulaTiraExcepcion() {
    String tareaId = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();
    double cantidadHoras = 8.0;

    String fechaCargaStr = null;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    assertEquals("La fecha de carga no puede ser nula", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConFechaEnFormatoIncorrectoTiraExcepcion() {
    String tareaId = UUID.randomUUID().toString();
    String recursoId = UUID.randomUUID().toString();
    double cantidadHoras = 8.0;

    String fechaCargaStr = "2024/31/12";

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tareaId, recursoId, cantidadHoras, fechaCargaStr));

    assertEquals("El formato de la fecha debe ser dd/MM/yyyy", e.getMessage());
  }
}
