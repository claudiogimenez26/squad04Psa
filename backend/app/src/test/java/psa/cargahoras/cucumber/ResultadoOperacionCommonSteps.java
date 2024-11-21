package psa.cargahoras.cucumber;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import io.cucumber.java.Before;
import io.cucumber.java.es.Entonces;
import java.util.function.Supplier;

public class ResultadoOperacionCommonSteps {
  private Exception excepcion;

  public Exception getExcepcion() {
    return excepcion;
  }

  public <T> T ejecutar(Supplier<T> operacion) {
    try {
      return operacion.get();
    } catch (Exception e) {
      this.excepcion = e;
      return null;
    }
  }

  public void ejecutar(Runnable operacion) {
    try {
      operacion.run();
    } catch (Exception e) {
      this.excepcion = e;
    }
  }

  @Before
  public void resetear() {
    excepcion = null;
  }

  @Entonces("la operación debe ser exitosa")
  public void verificarOperacionExitosa() {
    assertNull(excepcion);
  }

  @Entonces("la operación debe ser declinada")
  public void verificarOperacionDeclinada() {
    assertNotNull(excepcion);
  }
}
