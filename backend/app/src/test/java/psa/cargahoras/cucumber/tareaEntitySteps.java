package psa.cargahoras.cucumber;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import psa.cargahoras.entity.ProyectoEntity;
import psa.cargahoras.entity.TareaEntity;

import static org.junit.Assert.*;

public class tareaEntitySteps {

  private ProyectoEntity proyecto;
  private TareaEntity tarea;
  private Exception exception;

  @Dado("un proyecto")
  public void un_proyecto() {
    proyecto = new ProyectoEntity("Proyecto de prueba", 0.5, ProyectoEntity.Estado.Activo, "11/11/2024");
  }

  @Dado("un proyecto inexistente")
  public void un_proyecto_inexistente() {
    proyecto = null;
  }

  @Cuando("creo una tarea asociada con ese proyecto")
  public void creo_una_tarea_asociada_con_ese_proyecto() {
    tarea = new TareaEntity("Tarea de prueba", proyecto);
  }

  @Cuando("intento crear una tarea asociada con ese proyecto inexistente")
  public void intento_crear_una_tarea_asociada_con_ese_proyecto_inexistente() {
    try {
      tarea = new TareaEntity("Tarea de prueba", proyecto);
    } catch (Exception e) {
      exception = e;
    }
  }

  @Entonces("la tarea debería tener asociado a ese proyecto como proyecto asociado")
  public void la_tarea_debería_tener_asociado_a_ese_proyecto_como_proyecto_asociado() {
    assertEquals(proyecto, tarea.getProyecto());
  }

  @Entonces("la operación debería fallar")
  public void la_operación_debería_fallar() {
    assertNotNull(exception);
  }

  @Entonces("la tarea no debería existir")
  public void la_tarea_no_debería_existir() {
    assertNull(tarea);
  }
}
