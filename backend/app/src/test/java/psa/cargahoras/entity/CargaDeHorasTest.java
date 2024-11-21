package psa.cargahoras.entity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class CargaDeHorasTest {
  @Test
  public void constructorAsignaCamposCorrectamente() {
    Tarea tarea = mock(Tarea.class);
    Recurso recurso = mock(Recurso.class);
    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    CargaDeHoras cargaDeHoras = new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras);

    assertEquals(tarea, cargaDeHoras.getTarea());
    assertEquals(recurso, cargaDeHoras.getRecurso());
    assertEquals(fechaCargaStr, cargaDeHoras.getFechaCarga().format(CargaDeHoras.formatterFecha));
    assertEquals(cantidadHoras, cargaDeHoras.getCantidadHoras(), 0.0);
  }

  @Test
  public void crearUnaCargaDeHorasConHorasNegativasTiraExcepcion() {
    Tarea tarea = mock(Tarea.class);
    Recurso recurso = mock(Recurso.class);
    String fechaCargaStr = "19/11/2024";

    double cantidadHoras = -8.0;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras));

    assertEquals("La cantidad de horas no puede ser negativa", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConTareaNulaTiraExcepcion() {
    Recurso recurso = mock(Recurso.class);
    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    Tarea tarea = null;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras));

    assertEquals("La tarea no puede ser nula", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConRecursoNuloTiraExcepcion() {
    Tarea tarea = mock(Tarea.class);
    String fechaCargaStr = "19/11/2024";
    double cantidadHoras = 8.0;

    Recurso recurso = null;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras));

    assertEquals("El recurso no puede ser nulo", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConFechaNulaTiraExcepcion() {
    Tarea tarea = mock(Tarea.class);
    Recurso recurso = mock(Recurso.class);
    double cantidadHoras = 8.0;

    String fechaCargaStr = null;

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras));

    assertEquals("La fecha de carga no puede ser nula", e.getMessage());
  }

  @Test
  public void crearUnaCargaDeHorasConFechaEnFormatoIncorrectoTiraExcepcion() {
    Tarea tarea = mock(Tarea.class);
    Recurso recurso = mock(Recurso.class);
    double cantidadHoras = 8.0;

    String fechaCargaStr = "2024/31/12";

    Exception e =
        assertThrows(
            IllegalArgumentException.class,
            () -> new CargaDeHoras(tarea, recurso, fechaCargaStr, cantidadHoras));

    assertEquals("El formato de la fecha debe ser dd/MM/yyyy", e.getMessage());
  }
}
