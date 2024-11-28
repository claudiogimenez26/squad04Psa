package psa.cargahoras.dto;

public class CargaDeHorasDTO {

    private String idCarga;
    private String nombreTarea;
    private double cantidadHoras;
    private String fechaCarga;

    public CargaDeHorasDTO(
        String idCarga,
        String nombreTarea,
        double cantidadHoras,
        String fechaCarga
    ) {
        this.idCarga = idCarga;
        this.nombreTarea = nombreTarea;
        this.cantidadHoras = cantidadHoras;
        this.fechaCarga = fechaCarga;
    }

    public String getIdCarga() {
        return idCarga;
    }

    public String getNombreTarea() {
        return nombreTarea;
    }

    public double getCantidadHoras() {
        return cantidadHoras;
    }

    public String getFechaCarga() {
        return fechaCarga;
    }
}
