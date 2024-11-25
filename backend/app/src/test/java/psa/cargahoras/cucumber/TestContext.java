package psa.cargahoras.cucumber;

import static org.mockito.Mockito.mock;

import psa.cargahoras.service.ApiExternaService;

public class TestContext {
  private ApiExternaService apiExternaService;

  public TestContext() {
    this.apiExternaService = mock(ApiExternaService.class);
  }

  public ApiExternaService getApiExternaService() {
    return apiExternaService;
  }
}
