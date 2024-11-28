package psa.cargahoras.dto;

public class CostoMensualDTO {

  private String mes;
  private double cantidadHoras;

  public CostoMensualDTO(String mes, double cantidadHoras) {
    this.mes = mes;
    this.cantidadHoras = cantidadHoras;
  }

  public String getMes() {
    return mes;
  }

  public void setMes(String mes) {
    this.mes = mes;
  }

  public double getCantidadHoras() {
    return cantidadHoras;
  }

  public void setCantidadHoras(double cantidadHoras) {
    this.cantidadHoras = cantidadHoras;
  }
}
