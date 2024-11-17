package psa.cargahoras.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalDate;
import org.junit.Test;
import psa.cargahoras.entity.Proyecto.Estado;

public class ProyectoEntityTest {
  @Test
  public void constructorProyecto() {
    String nombre = "MVP carga de horas";
    Double coeficienteRiesgo = 0.5;
    Estado estado = Estado.Activo;
    String fechaInicioStr = "11/11/2024";

    Proyecto proyecto = new Proyecto(nombre, coeficienteRiesgo, estado, fechaInicioStr);

    assertEquals(nombre, proyecto.getNombre());
    assertEquals(coeficienteRiesgo, proyecto.getCoeficienteRiesgo());
    assertEquals(estado, proyecto.getEstado());
    assertEquals(LocalDate.of(2024, 11, 11), proyecto.getFechaInicio());
  }

  @Test
  public void constructorProyectoConCoeficienteDeRiesgoNegativoTiraExcepcion() {
    String nombre = "MVP carga de horas";
    Double coeficienteRiesgo = -0.5;
    Estado estado = Estado.Activo;
    String fechaInicioStr = "11/11/2024";

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Proyecto(nombre, coeficienteRiesgo, estado, fechaInicioStr);
            });

    assertEquals(
        "Coeficiente de riesgo debe estar entre 0 y 1 (inclusive).", exception.getMessage());
  }

  @Test
  public void constructorProyectoConCoeficienteDeRiesgoMayorAUnoTiraExcepcion() {
    String nombre = "MVP carga de horas";
    Double coeficienteRiesgo = 1.5;
    Estado estado = Estado.Activo;
    String fechaInicioStr = "11/11/2024";

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Proyecto(nombre, coeficienteRiesgo, estado, fechaInicioStr);
            });

    assertEquals(
        "Coeficiente de riesgo debe estar entre 0 y 1 (inclusive).", exception.getMessage());
  }

  @Test
  public void constructorProyectoConFechaConMalFormatoTiraExcepcion() {
    String nombre = "MVP carga de horas";
    Double coeficienteRiesgo = 0.5;
    Estado estado = Estado.Activo;
    String fechaInicioStr = "fecha-inválida";

    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Proyecto(nombre, coeficienteRiesgo, estado, fechaInicioStr);
            });

    assertEquals("Formato de fecha debe ser dd/MM/yyyy", exception.getMessage());
  }
}
