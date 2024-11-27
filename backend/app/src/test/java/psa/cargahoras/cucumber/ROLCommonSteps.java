package psa.cargahoras.cucumber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.cucumber.java.es.Dado;
import java.util.Arrays;
import psa.cargahoras.dto.RolDTO;

public class ROLCommonSteps {

  private final TestContext testContext;

  private RolDTO rol;

  public ROLCommonSteps(TestContext testContext) {
    this.testContext = testContext;
  }

  @Dado("un rol con id {string}")
  public void dadoUnRolId(String rolId) {
    rol = mock(RolDTO.class);
    when(rol.getId()).thenReturn(rolId);
    when(testContext.getApiExternaService().getRoles()).thenReturn(Arrays.asList(rol));
  }

  @Dado("un rol con id {string}, con costo {double} por hora")
  public void dadoUnRolIdYUnCosto(String rolId, double costo) {
    rol = mock(RolDTO.class);
    when(rol.getId()).thenReturn(rolId);
    when(rol.getCosto()).thenReturn(costo);
    when(testContext.getApiExternaService().getRoles()).thenReturn(Arrays.asList(rol));
  }

  public RolDTO getRol() {
    return rol;
  }
}
